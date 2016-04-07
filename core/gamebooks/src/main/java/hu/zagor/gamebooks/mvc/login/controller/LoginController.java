package hu.zagor.gamebooks.mvc.login.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.filters.LocaleSwitchingFilter;
import hu.zagor.gamebooks.mvc.login.domain.LoginData;
import hu.zagor.gamebooks.mvc.login.service.LoginAttemptOverviewService;
import hu.zagor.gamebooks.mvc.login.service.VersionNumberProvider;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.util.Enumeration;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the login page.
 * @author Tamas_Szekeres
 */
@Controller
public class LoginController {

    private static final int GUEST_SESSION_TTL = 5 * 60;
    private static final int USER_SESSION_TTL = 4 * 60 * 60;

    @LogInject private Logger logger;
    @Autowired private LoginAttemptOverviewService loginAttemptService;
    @Autowired private VersionNumberProvider version;

    /**
     * Shows the user the login page.
     * @param model the data model for the page
     * @param request the http request
     * @param response the http response
     * @return the login page
     */
    @RequestMapping(value = PageAddresses.LOGIN)
    public String loginGet(final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        logHeaders(request);
        final HttpSession session = request.getSession();
        session.setMaxInactiveInterval(GUEST_SESSION_TTL);
        String result;
        if (missingLanguageCookie(request)) {
            result = setLanguageCookie(request, response);
        } else {
            model.addAttribute("pageTitle", "page.title");
            result = chekcUserLogin(model, session);
        }
        model.addAttribute("loginBlocked", loginAttemptService.isBlocked(request.getRemoteAddr()));
        model.addAttribute("version", version.getVersion());
        return result;
    }

    private String setLanguageCookie(final HttpServletRequest request, final HttpServletResponse response) {
        final String locale = request.getLocale().getLanguage();
        response.addCookie(getCookie(LocaleSwitchingFilter.LANG_KEY, locale));
        response.addCookie(getCookie(LocaleSwitchingFilter.LOGIN_LANG_KEY, "true"));
        return "redirect:" + PageAddresses.LOGIN;
    }

    private Cookie getCookie(final String key, final String value) {
        final Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private boolean missingLanguageCookie(final HttpServletRequest request) {
        return getCookie(LocaleSwitchingFilter.LANG_KEY, request) == null;
    }

    private String chekcUserLogin(final Model model, final HttpSession session) {
        String result;
        if (session.getAttribute(ControllerAddresses.USER_STORE_KEY) == null) {
            logger.info("Displaying login screen.");
            model.addAttribute("loginData", new LoginData());
            result = PageAddresses.LOGIN;
        } else {
            logger.debug("User already logged in with name '{}'; redirecting.", session.getAttribute(ControllerAddresses.USER_STORE_KEY));
            session.setMaxInactiveInterval(USER_SESSION_TTL);
            result = "redirect:" + PageAddresses.BOOK_LIST;
        }
        return result;
    }

    private String getCookie(final String cookieName, final HttpServletRequest request) {
        String value = null;
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (final Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    value = cookie.getValue();
                }
            }
        }
        return value;
    }

    private void logHeaders(final HttpServletRequest request) {
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement();
            final String value = request.getHeader(name);
            logger.info("'{}': '{}'", name, value);
        }
    }

}
