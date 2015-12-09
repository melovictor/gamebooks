package hu.zagor.gamebooks.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * Null service for production with logout handling capabilities.
 * @author Tamas_Szekeres
 */
public class NullRememberMeService extends NullRememberMeServices implements LogoutHandler {

    @Override
    public void logout(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) {
    }

}
