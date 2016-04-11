package hu.zagor.gamebooks.mvc.login.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mdc.MdcHandler;
import hu.zagor.gamebooks.mvc.login.service.LoginAttemptOverviewService;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.player.settings.DefaultSettingsHandler;
import hu.zagor.gamebooks.player.settings.UserSettingsHandler;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the login page.
 * @author Tamas_Szekeres
 */
@Controller
public final class LoginSuccessController {

    private static final int USER_SESSION_TTL = 4 * 60 * 60;

    @LogInject private Logger logger;
    @Autowired private MdcHandler mdcHandler;
    @Autowired private DefaultSettingsHandler defaultSettingsHandler;
    @Autowired private UserSettingsHandler userSettingsHandler;
    @Autowired private LoginAttemptOverviewService loginAttemptService;

    /**
     * Handles the successful login.
     * @param request the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @throws IOException if an input or output exception occurs
     * @throws ServletException if logout fails
     */
    @RequestMapping("/loginSuccessful")
    public void handleSuccessfulLogin(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        logHeaders(request);

        final String ipAddress = request.getRemoteAddr();
        if (loginAttemptService.isBlocked(ipAddress)) {
            logUserOut(request, response);
        } else {
            loginAttemptService.loginSucceeded(ipAddress);
            initializeUserSession(request, response);
        }
    }

    private void logUserOut(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        request.logout();
        response.sendRedirect(PageAddresses.LOGIN);
    }

    private void initializeUserSession(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final PlayerUser authentication = (PlayerUser) SecurityContextHolder.getContext().getAuthentication();
        final String mdcUserId = authentication.getId() + "-" + new Date().getTime();
        mdcHandler.setUserId(mdcUserId, request.getSession());
        logger.info("User '{}' logged in successfully.", authentication.getPrincipal());
        authentication.getSettings().putAll(defaultSettingsHandler.getDefaultSettings());
        userSettingsHandler.loadSettings(authentication);
        final HttpSession session = request.getSession();
        session.setAttribute(ControllerAddresses.USER_STORE_KEY, authentication);
        session.setMaxInactiveInterval(USER_SESSION_TTL);
        response.sendRedirect("booklist");
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
