package hu.zagor.gamebooks.mvc.login.facade;

import static hu.zagor.gamebooks.player.PlayerUser.ADMIN;
import static hu.zagor.gamebooks.player.PlayerUser.TEST;
import static hu.zagor.gamebooks.player.PlayerUser.USER;
import hu.zagor.gamebooks.mvc.login.domain.LoginResult;
import hu.zagor.gamebooks.player.PlayerUser;
import java.util.Arrays;
import java.util.Map;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Default implementation of the {@link LoginFacade} interface.
 * @author Tamas_Szekeres
 */
public class DummyLoginFacade extends AbstractLoginFacade {

    private Map<String, LoginResult> dummyUsers;

    public void setDummyUsers(final Map<String, LoginResult> dummyUsers) {
        this.dummyUsers = dummyUsers;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final LoginResult loginResult = dummyUsers.get(authentication.getPrincipal());
        if (loginResult == null) {
            throw new BadCredentialsException("page.login.invalid.username.password");
        }
        return new PlayerUser(loginResult.getId(), authentication.getPrincipal(), Arrays.asList(USER, TEST, ADMIN));
    }

}
