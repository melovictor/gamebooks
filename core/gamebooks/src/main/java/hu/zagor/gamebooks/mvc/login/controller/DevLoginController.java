package hu.zagor.gamebooks.mvc.login.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.login.domain.LoginData;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for handling the login page on the development environment.
 * @author Tamas_Szekeres
 */
public class DevLoginController extends LoginController {

    private static final CsrfToken NULL_TOKEN = new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "null");
    private static final int GUEST_SESSION_TTL = 5 * 60;

    @Autowired
    private EnvironmentDetector environmentDetector;
    @Value("${login.username:}")
    private String defaultUsername;

    /**
     * Shows the user the login page.
     * @param model the data model for the page
     * @param request the http request
     * @param response the {@link HttpServletResponse}
     * @return the login page
     */
    @Override
    @RequestMapping(value = PageAddresses.LOGIN, method = RequestMethod.GET)
    public String loginGet(final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        String pageTarget;
        if (defaultUsername.isEmpty()) {
            pageTarget = super.loginGet(model, request, response);
        } else {
            final LoginData data = new LoginData();
            data.setUsername(defaultUsername);
            data.setCsrfToken("null");
            getCsrfTokenRepository().saveToken(NULL_TOKEN, request, response);
            model.addAttribute("loginData", data);
            pageTarget = loginPost(model, data, request);
        }
        return pageTarget;
    }

    /**
     * Shows the user the login page for testing.
     * @param model the data model for the page
     * @param request the http request
     * @param response the http response
     * @return the login page
     */
    @RequestMapping(value = PageAddresses.TEST_LOGIN, method = RequestMethod.GET)
    public String testLoginGet(final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        return doPrepare(model, request, response, true);
    }

    private String doPrepare(final Model model, final HttpServletRequest request, final HttpServletResponse response, final boolean isTesting) {
        String result;
        logHeaders(request);
        final HttpSession session = request.getSession();
        session.setMaxInactiveInterval(GUEST_SESSION_TTL);
        environmentDetector.setSeleniumTesting(isTesting);
        if (missingLanguageCookie(request)) {
            result = setLanguageCookie(request, response, isTesting);
        } else {
            model.addAttribute("pageTitle", "page.title");
            model.addAttribute("csrfToken", "null");
            result = chekcUserLogin(model, session);
        }
        setTiming(model, session);
        return result;
    }

    private String setLanguageCookie(final HttpServletRequest request, final HttpServletResponse response, final boolean isTesting) {
        super.setLanguageCookie(request, response);
        return "redirect:" + (isTesting ? PageAddresses.TEST_LOGIN : PageAddresses.LOGIN);
    }

}
