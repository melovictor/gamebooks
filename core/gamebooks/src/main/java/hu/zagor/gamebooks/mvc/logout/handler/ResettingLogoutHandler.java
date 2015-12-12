package hu.zagor.gamebooks.mvc.logout.handler;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;
import hu.zagor.gamebooks.support.logging.LogInject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Controller;

/**
 * Controller for handling the logout page.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
public class ResettingLogoutHandler implements LogoutHandler {
    @LogInject private Logger logger;
    @Autowired private EnvironmentDetector environmentDetector;

    @Override
    public void logout(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) {
        final PlayerUser playerUser = (PlayerUser) request.getSession().getAttribute(ControllerAddresses.USER_STORE_KEY);
        if (playerUser == null) {
            logger.info("Logging out when no user is available.");
        } else {
            logger.info("Logging out user '{}'.", playerUser.getPrincipal());
        }
        environmentDetector.setSeleniumTesting(false);
    }

}
