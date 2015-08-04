package hu.zagor.gamebooks.mvc.logout.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mdc.MdcHandler;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the logout page.
 * @author Tamas_Szekeres
 */
@Controller
public class LogoutController {

    @Autowired
    private MdcHandler mdcHandler;
    @Autowired
    private EnvironmentDetector environmentDetector;

    /**
     * Logs the user out, then redirects him to the login page.
     * @param session the session
     * @return redirection to the login page
     */
    @RequestMapping(value = PageAddresses.LOGOUT)
    public String loginGet(final HttpSession session) {
        session.setAttribute(ControllerAddresses.USER_STORE_KEY, null);
        mdcHandler.cleanUserId(session);
        environmentDetector.setSeleniumTesting(false);
        return "redirect:" + PageAddresses.LOGIN;
    }
}
