package hu.zagor.gamebooks.mvc.login.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Handler for successful and failed login events.
 * @author Tamas_Szekeres
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler, AuthenticationSuccessHandler {

    @Autowired private LoginAttemptOverviewService loginAttemptService;

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception)
        throws IOException {
        loginAttemptService.loginFailed(request.getRemoteAddr());
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
        response.sendRedirect("login");
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication)
        throws IOException, ServletException {
        response.sendRedirect("loginSuccessful");
    }
}
