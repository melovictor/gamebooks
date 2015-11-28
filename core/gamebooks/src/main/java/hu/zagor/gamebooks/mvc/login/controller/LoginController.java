package hu.zagor.gamebooks.mvc.login.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.filters.LocaleSwitchingFilter;
import hu.zagor.gamebooks.mdc.MdcHandler;
import hu.zagor.gamebooks.mvc.login.domain.LoginData;
import hu.zagor.gamebooks.mvc.login.domain.LoginResult;
import hu.zagor.gamebooks.mvc.login.facade.LoginFacade;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.player.settings.DefaultSettingsHandler;
import hu.zagor.gamebooks.player.settings.UserSettingsHandler;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for handling the login page.
 * @author Tamas_Szekeres
 */
public class LoginController {

    private static final int FURTHER_TRY_LOCKOUT = 3;
    private static final int MIN_TRY_LOCKOUT = 5;
    private static final int GUEST_SESSION_TTL = 5 * 60;
    private static final int USER_SESSION_TTL = 4 * 60 * 60;

    @LogInject
    private Logger logger;
    @Autowired
    @Qualifier("activeLoginFacade")
    private LoginFacade login;
    @Autowired
    private UserSettingsHandler userSettingsHandler;
    @Autowired
    private DefaultSettingsHandler defaultSettingsHandler;
    @Autowired
    private MdcHandler mdcHandler;
    @Autowired
    private CsrfTokenRepository csrfTokenRepository;

    /**
     * Shows the user the login page.
     * @param model the data model for the page
     * @param request the http request
     * @param response the http response
     * @return the login page
     */
    @RequestMapping(value = PageAddresses.LOGIN, method = RequestMethod.GET)
    public String loginGet(final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        logHeaders(request);
        final HttpSession session = request.getSession();
        session.setMaxInactiveInterval(GUEST_SESSION_TTL);
        String result;
        if (missingLanguageCookie(request)) {
            result = setLanguageCookie(request, response);
        } else {
            model.addAttribute("pageTitle", "page.title");
            newToken(model, request);
            result = chekcUserLogin(model, session);
        }
        setTiming(model, session);
        return result;
    }

    private void newToken(final Model model, final HttpServletRequest request) {
        final CsrfToken generateToken = csrfTokenRepository.generateToken(request);
        csrfTokenRepository.saveToken(generateToken, request, null);
        logger.info("Generated token for user to log in with: '{}'.", generateToken.getToken());
        model.addAttribute("csrfToken", generateToken.getToken());
    }

    String setLanguageCookie(final HttpServletRequest request, final HttpServletResponse response) {
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

    boolean missingLanguageCookie(final HttpServletRequest request) {
        return getCookie(LocaleSwitchingFilter.LANG_KEY, request) == null;
    }

    String chekcUserLogin(final Model model, final HttpSession session) {
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

    /**
     * Shows the user the login page.
     * @return the login page
     */
    @RequestMapping(value = "/**/" + PageAddresses.LOGIN, method = RequestMethod.GET)
    public String loginRedirectGet() {
        return "redirect:../" + PageAddresses.LOGIN;
    }

    /**
     * If the login name and password are valid, logs the user in, otherwise sends him back to the main page.
     * @param data the bean containing the field's contents
     * @param model the page's model
     * @param request the request object
     * @return the name of the next page to go
     */
    @RequestMapping(value = PageAddresses.LOGIN, method = RequestMethod.POST)
    public String loginPost(final Model model, final LoginData data, final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        String nextPage;
        final CsrfToken expectedToken = csrfTokenRepository.loadToken(request);
        if (expectedToken != null && expectedToken.getToken().equals(data.getCsrfToken())) {
            final LoginResult loginResult = login.doLogin(data);
            if (loginResult.isSuccessful()) {
                nextPage = executeLogin(data, request, session, loginResult);
            } else {
                nextPage = reportWrongLogin(model, data, session, loginResult);
            }
        } else {
            nextPage = reportWrongToken(model, data, session, expectedToken);
        }
        newToken(model, request);
        model.addAttribute("pageTitle", "page.title");
        setTiming(model, session);
        return nextPage;
    }

    private String reportWrongLogin(final Model model, final LoginData data, final HttpSession session, final LoginResult loginResult) {
        logger.warn("User '{}' tried to log in with invalid password!", data.getUsername());
        session.setAttribute(ControllerAddresses.USER_STORE_KEY, null);
        model.addAttribute("loginError", loginResult.getMessage());
        data.setPassword("");
        guessPrevention(session);
        return PageAddresses.LOGIN;
    }

    private void guessPrevention(final HttpSession session) {
        final int totalTries = getTotalTries(session);
        logger.warn("Total failed login attempts: {}.", totalTries);
        if (totalTries >= MIN_TRY_LOCKOUT) {
            if (totalTries == MIN_TRY_LOCKOUT) {
                final int timeoutInMinutes = 1;
                setTimeout(session, timeoutInMinutes);
            } else {
                if ((totalTries - MIN_TRY_LOCKOUT) % FURTHER_TRY_LOCKOUT == 0) {
                    final int lastTimeout = (int) session.getAttribute("timeout");
                    final int nextTimeout = lastTimeout * 5;
                    setTimeout(session, nextTimeout);
                }
            }
        }
    }

    private void setTimeout(final HttpSession session, final int nextTimeout) {
        session.setAttribute("timeout", nextTimeout);
        session.setAttribute("loginPrevention", DateTime.now().plusMinutes(nextTimeout).getMillis());
    }

    private int getTotalTries(final HttpSession session) {
        Integer totalTries = (Integer) session.getAttribute("totalTries");
        if (totalTries == null) {
            totalTries = 1;
        } else {
            totalTries++;
        }
        session.setAttribute("totalTries", totalTries);
        return totalTries;
    }

    private String executeLogin(final LoginData data, final HttpServletRequest request, final HttpSession session, final LoginResult loginResult) {
        String nextPage;
        final String mdcUserId = loginResult.getId() + "-" + new Date().getTime();
        mdcHandler.setUserId(mdcUserId, request.getSession());
        logger.info("User '{}' logged in successfully.", data.getUsername());
        logHeaders(request);
        final PlayerUser playerUser = new PlayerUser(loginResult.getId(), data.getUsername(), loginResult.isAdmin());
        playerUser.getSettings().putAll(defaultSettingsHandler.getDefaultSettings());
        userSettingsHandler.loadSettings(playerUser);
        session.setAttribute(ControllerAddresses.USER_STORE_KEY, playerUser);
        session.setMaxInactiveInterval(USER_SESSION_TTL);
        nextPage = "redirect:" + PageAddresses.BOOK_LIST;
        return nextPage;
    }

    void logHeaders(final HttpServletRequest request) {
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement();
            final String value = request.getHeader(name);
            logger.info("'{}': '{}'", name, value);
        }
    }

    private String reportWrongToken(final Model model, final LoginData data, final HttpSession session, final CsrfToken expectedToken) {
        logger.warn("User '{}' tried to log in with invalid token! Actual: '{}', expected: '{}'.", data.getUsername(), data.getCsrfToken(), expectedToken == null ? null
            : expectedToken.getToken());
        session.setAttribute(ControllerAddresses.USER_STORE_KEY, null);
        model.addAttribute("loginError", "page.login.invalid.token");
        data.setPassword("");
        return PageAddresses.LOGIN;
    }

    public void setLogin(final LoginFacade login) {
        this.login = login;
    }

    void setTiming(final Model model, final HttpSession session) {
        model.addAttribute("currentTime", DateTime.now().getMillis());
        model.addAttribute("loginPrevention", session.getAttribute("loginPrevention"));
    }

    CsrfTokenRepository getCsrfTokenRepository() {
        return csrfTokenRepository;
    }

}
