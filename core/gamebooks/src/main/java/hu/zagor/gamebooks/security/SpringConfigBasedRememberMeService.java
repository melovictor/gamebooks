package hu.zagor.gamebooks.security;

import static hu.zagor.gamebooks.player.PlayerUser.ADMIN;
import static hu.zagor.gamebooks.player.PlayerUser.TEST;
import static hu.zagor.gamebooks.player.PlayerUser.USER;
import hu.zagor.gamebooks.mvc.login.domain.LoginResult;
import hu.zagor.gamebooks.player.PlayerUser;
import java.util.Arrays;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.util.StringUtils;

/**
 * Service to log in the user automatically the first time the spring context initializes itself using spring configuration.
 * @author Tamas_Szekeres
 */
public class SpringConfigBasedRememberMeService implements RememberMeServices {
    private final boolean firstLogin = true;
    @Value("${login.username}") private String username;
    @Resource(name = "dummyUserMap") private Map<String, LoginResult> dummyUsers;

    @Override
    public Authentication autoLogin(final HttpServletRequest request, final HttpServletResponse response) {
        Authentication authResult = null;

        if (!StringUtils.isEmpty(username) && firstLogin) {
            final LoginResult loginResult = dummyUsers.get(username);
            if (loginResult != null) {
                authResult = new PlayerUser(loginResult.getId(), username, Arrays.asList(USER, TEST, ADMIN));
            }
        }
        return authResult;
    }

    @Override
    public void loginFail(final HttpServletRequest request, final HttpServletResponse response) {
    }

    @Override
    public void loginSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication successfulAuthentication) {
    }

}
