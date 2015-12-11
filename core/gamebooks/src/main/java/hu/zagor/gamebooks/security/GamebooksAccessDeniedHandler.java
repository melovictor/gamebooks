package hu.zagor.gamebooks.security;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
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
public class GamebooksAccessDeniedHandler extends AccessDeniedHandlerImpl {

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response, final AccessDeniedException accessDeniedException)
        throws IOException, ServletException {

        if (isAuthenticated(request)) {
            response.sendRedirect(PageAddresses.BOOK_LIST);
        } else {
            final String messageKey = "page.login.invalid.token";
            request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, new Exception(messageKey));
            response.sendRedirect(PageAddresses.LOGIN);
        }
    }

    private boolean isAuthenticated(final HttpServletRequest request) {
        return request.getSession().getAttribute(ControllerAddresses.USER_STORE_KEY) != null;
    }

}
