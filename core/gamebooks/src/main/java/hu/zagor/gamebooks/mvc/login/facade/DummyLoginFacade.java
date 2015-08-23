package hu.zagor.gamebooks.mvc.login.facade;

import hu.zagor.gamebooks.mvc.login.domain.LoginData;
import hu.zagor.gamebooks.mvc.login.domain.LoginResult;

import java.util.Map;

/**
 * Default implementation of the {@link LoginFacade} interface.
 * @author Tamas_Szekeres
 */
public class DummyLoginFacade implements LoginFacade {

    private Map<String, LoginResult> dummyUsers;

    @Override
    public LoginResult doLogin(final LoginData data) {
        LoginResult loginResult = dummyUsers.get(data.getUsername());
        if (loginResult == null) {
            loginResult = new LoginResult();
            loginResult.setSuccessful(false);
            loginResult.setMessage("page.login.invalid.username.password");
        }
        return loginResult;
    }

    public void setDummyUsers(final Map<String, LoginResult> dummyUsers) {
        this.dummyUsers = dummyUsers;
    }
}
