package hu.zagor.gamebooks.mvc.login.facade;

import org.springframework.security.authentication.AuthenticationProvider;

/**
 * Abstract implementation of the {@link AuthenticationProvider}.
 * @author Tamas_Szekeres
 */
public abstract class AbstractLoginFacade implements AuthenticationProvider {

    @Override
    public boolean supports(final Class<?> authentication) {
        return true;
    }
}
