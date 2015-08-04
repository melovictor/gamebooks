package hu.zagor.gamebooks.mvc.login.facade;

import hu.zagor.gamebooks.mvc.login.domain.LoginData;
import hu.zagor.gamebooks.mvc.login.domain.LoginResult;

/**
 * Interface for handling the login.
 * @author Tamas_Szekeres
 *
 */
public interface LoginFacade {

    /**
     * Tries to log in the user using the provided login name and password.
     * @param data the bean containing the user's login name and password
     * @return a bean containing the result of the login
     */
    LoginResult doLogin(LoginData data);

}
