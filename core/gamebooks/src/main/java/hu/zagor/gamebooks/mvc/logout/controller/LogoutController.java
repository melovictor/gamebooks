package hu.zagor.gamebooks.mvc.logout.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;
import hu.zagor.gamebooks.support.logging.LogInject;

/**
 * Controller for handling the logout page.
 * @author Tamas_Szekeres
 */
@Controller
public class LogoutController {

    @LogInject
    private Logger logger;
    @Autowired
    private EnvironmentDetector environmentDetector;

    /**
     * Logs the user out, then redirects him to the login page.
     * @param session the session
     * @return redirection to the login page
     */
    @RequestMapping(value = PageAddresses.LOGOUT)
    public String loginGet(final HttpSession session) {
        final PlayerUser playerUser = (PlayerUser) session.getAttribute(ControllerAddresses.USER_STORE_KEY);
        if (playerUser == null) {
            logger.info("Logging out when no user is available.");
        } else {
            logger.info("Logging out user '{}'.", playerUser.getUsername());
        }
        session.invalidate();
        environmentDetector.setSeleniumTesting(false);
        return "redirect:" + PageAddresses.LOGIN;
    }
}
