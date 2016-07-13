package hu.zagor.gamebooks.mvc.login.facade;

import hu.zagor.gamebooks.connectivity.ServerCommunicator;
import hu.zagor.gamebooks.mvc.login.domain.Login;
import hu.zagor.gamebooks.mvc.login.domain.LoginStatus;
import hu.zagor.gamebooks.mvc.login.domain.User;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Production implementation of the {@link AbstractLoginFacade} class that goes to the Zagor.hu server to authenticate the user that's trying to log in. Usable both for development
 * and production use.
 * @author Tamas_Szekeres
 */
public class ZagorLoginFacade extends AbstractLoginFacade {

    @LogInject private Logger logger;
    @Autowired private ServerCommunicator communcator;
    private String url = "http://zagor.hu/remoteloginxml.php";
    @Resource(name = "authorizationCodeContainer") private Map<String, String> authorizationCodeContainer;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        try {
            final String postData = assemblePostData(authentication);
            final URLConnection connection = communcator.connect(url);
            communcator.sendRequest(connection, postData);
            final Login loginResult = parseResponse(connection);
            return analizeResponse(authentication, loginResult);
        } catch (final SocketTimeoutException e) {
            throw new AuthenticationServiceException("page.login.zagor.server.slow");
        } catch (final FileNotFoundException e) {
            throw new AuthenticationServiceException("page.login.zagor.server.nofile");
        } catch (final UnknownHostException e) {
            throw new AuthenticationServiceException("page.login.zagor.server.nohost");
        } catch (final IOException e) {
            throw new AuthenticationServiceException("page.login.zagor.server.unknown");
        } catch (final JAXBException e) {
            throw new AuthenticationServiceException("page.login.zagor.server.wrongResponse");
        }
    }

    private Login parseResponse(final URLConnection connection) throws JAXBException, IOException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(Login.class);
        final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (Login) jaxbUnmarshaller.unmarshal(connection.getInputStream());
    }

    private Authentication analizeResponse(final Authentication authentication, final Login loginResult) {
        if (loginResult.getStatus() == LoginStatus.failure) {
            throw new BadCredentialsException("page.login.invalid.username.password");
        }

        final User user = loginResult.getUser();
        final List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles());
        return new PlayerUser(user.getId(), authentication.getPrincipal(), authorities, user.getReward());
    }

    private String assemblePostData(final Authentication authentication) throws IOException {
        String part = communcator.compilePostData("username", authentication.getPrincipal(), null);
        part = communcator.compilePostData("password", authentication.getCredentials(), part);
        part = communcator.compilePostData("authCode", authorizationCodeContainer.get("login"), part);
        return part;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

}
