package hu.zagor.gamebooks.mvc.login.facade;

import static hu.zagor.gamebooks.player.PlayerUser.ADMIN;
import static hu.zagor.gamebooks.player.PlayerUser.USER;
import hu.zagor.gamebooks.connectivity.ServerCommunicator;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

/**
 * Default implementation of the {@link LoginFacade} interface.
 * @author Tamas_Szekeres
 */
public class ZagorLoginFacade extends AbstractLoginFacade {

    @LogInject private Logger logger;
    @Autowired private ServerCommunicator communcator;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

        try {
            final String postData = assemblePostData(authentication);
            final URLConnection connection = communcator.connect("http://zagor.hu/remotelogin.php");
            communcator.sendRequest(connection, postData);
            final String response = communcator.receiveResponse(connection);
            analizeResponse(authentication, response);
            final Authentication result = null;
            return result;
        } catch (final SocketTimeoutException e) {
            throw new AuthenticationServiceException("page.login.zagor.server.slow");
        } catch (final FileNotFoundException e) {
            throw new AuthenticationServiceException("page.login.zagor.server.nofile");
        } catch (final UnknownHostException e) {
            throw new AuthenticationServiceException("page.login.zagor.server.nohost");
        } catch (final IOException e) {
            throw new AuthenticationServiceException("page.login.zagor.server.unknown");
        }
    }

    private Authentication analizeResponse(final Authentication authentication, final String response) {
        if (response == null) {
            throw new BadCredentialsException("page.login.invalid.username.password");
        }
        final boolean isAdmin = Integer.parseInt(response.substring(0, 1)) == 1;
        final int id = Integer.parseInt(response.substring(1));

        Collection<? extends GrantedAuthority> authorities;
        if (isAdmin) {
            authorities = Arrays.asList(USER, ADMIN);
        } else {
            authorities = Arrays.asList(USER);
        }

        return new PlayerUser(id, authentication.getPrincipal(), authorities);
    }

    private String assemblePostData(final Authentication authentication) throws IOException {
        String part = communcator.compilePostData("username", authentication.getPrincipal(), null);
        part = communcator.compilePostData("password", authentication.getCredentials(), part);
        return part;
    }

}
