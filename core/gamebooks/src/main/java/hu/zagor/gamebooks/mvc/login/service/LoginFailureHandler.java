package hu.zagor.gamebooks.mvc.login.service;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * Handler for {@link AuthenticationFailureBadCredentialsEvent} events.
 * @author Tamas_Szekeres
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Autowired private LoginAttemptOverviewService loginAttemptService;

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception)
        throws IOException {
        loginAttemptService.loginFailed(request.getRemoteAddr());
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
        response.sendRedirect("login");
    }
}
