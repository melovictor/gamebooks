package hu.zagor.gamebooks.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

/**
 * Custom extension of the {@link AccessDeniedHandlerImpl} for handling the missing or incorrect CSRF token differently.
 * @author Tamas_Szekeres
 */
public class CsrfAccessDeniedHandler extends AccessDeniedHandlerImpl {

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response, final AccessDeniedException accessDeniedException)
        throws IOException, ServletException {
        final String messageKey = "page.login.invalid.token";
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new Exception(messageKey));
        response.sendRedirect("login");
    }

}
