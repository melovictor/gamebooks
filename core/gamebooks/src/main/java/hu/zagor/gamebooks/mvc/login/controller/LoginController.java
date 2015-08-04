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
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for handling the login page.
 * @author Tamas_Szekeres
 */
@Controller
public class LoginController {

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
    private EnvironmentDetector environmentDetector;

    /**
     * Shows the user the login page.
     * @param model the data model for the page
     * @param request the http request
     * @param response the http response
     * @return the login page
     */
    @RequestMapping(value = PageAddresses.LOGIN, method = RequestMethod.GET)
    public String loginGet(final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        return doLogin(model, request, response, false);
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
        return doLogin(model, request, response, true);
    }

    private String doLogin(final Model model, final HttpServletRequest request, final HttpServletResponse response, final boolean isTesting) {
        String result;
        final HttpSession session = request.getSession();
        session.setMaxInactiveInterval(GUEST_SESSION_TTL);
        environmentDetector.setSeleniumTesting(isTesting);
        if (missingLanguageCookie(request)) {
            result = setLanguageCookie(request, response, isTesting);
        } else {
            model.addAttribute("pageTitle", "page.title");
            result = chekcUserLogin(model, session);
        }
        return result;
    }

    private String setLanguageCookie(final HttpServletRequest request, final HttpServletResponse response, final boolean isTesting) {
        final String locale = request.getLocale().getLanguage();
        response.addCookie(new Cookie(LocaleSwitchingFilter.LANG_KEY, locale));
        response.addCookie(new Cookie(LocaleSwitchingFilter.LOGIN_LANG_KEY, "true"));
        return "redirect:" + (isTesting ? PageAddresses.TEST_LOGIN : PageAddresses.LOGIN);
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

    /**
     * Shows the user the login page.
     * @return the login page
     */
    @RequestMapping(value = "/**/" + PageAddresses.LOGIN, method = RequestMethod.GET)
    public String loginRedirectGet() {
        return "redirect:../" + PageAddresses.LOGIN;
    }

    /**
     * Shows the user the login page.
     * @param model the data model for the page
     * @param request the http request
     * @param guid the session ID received from the caller server to be used for user identification
     * @return the login page
     */
    @RequestMapping(value = PageAddresses.LOGIN + "/{guid}", method = RequestMethod.GET)
    public String loginWithGui(final Model model, final HttpServletRequest request, @PathVariable("guid") final String guid) {
        final LoginData data = new LoginData();
        data.setGuid(guid);
        model.addAttribute("loginData", data);
        final String loginResult = loginPost(model, data, request);
        final String tile;
        if (loginResult.contains(":")) {
            tile = "redirect:../" + PageAddresses.BOOK_LIST;
        } else {
            tile = "redirect:../" + PageAddresses.LOGIN;
        }
        model.addAttribute("loginError", "");
        return tile;
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
        final LoginResult loginResult = login.doLogin(data);
        final HttpSession session = request.getSession();

        String nextPage;
        if (loginResult.isSuccessful()) {
            final String mdcUserId = loginResult.getId() + "-" + new Date().getTime();
            mdcHandler.setUserId(mdcUserId, request.getSession());
            logger.info("User '{}' logged in successfully.", data.getUsername());
            final PlayerUser playerUser = new PlayerUser(loginResult.getId(), data.getUsername(), loginResult.isAdmin());
            playerUser.getSettings().putAll(defaultSettingsHandler.getDefaultSettings());
            userSettingsHandler.loadSettings(playerUser);
            session.setAttribute(ControllerAddresses.USER_STORE_KEY, playerUser);
            session.setMaxInactiveInterval(USER_SESSION_TTL);
            nextPage = "redirect:" + PageAddresses.BOOK_LIST;
        } else {
            logger.warn("User '{}' tried to log in with invalid password!", data.getUsername());
            session.setAttribute(ControllerAddresses.USER_STORE_KEY, null);
            model.addAttribute("loginError", loginResult.getMessage());
            data.setPassword("");
            nextPage = PageAddresses.LOGIN;
        }

        model.addAttribute("pageTitle", "page.title");
        return nextPage;
    }

    public void setLogin(final LoginFacade login) {
        this.login = login;
    }

}
