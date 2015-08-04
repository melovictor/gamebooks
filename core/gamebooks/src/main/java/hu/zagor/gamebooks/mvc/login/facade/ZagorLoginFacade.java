package hu.zagor.gamebooks.mvc.login.facade;

import hu.zagor.gamebooks.connectivity.ServerCommunicator;
import hu.zagor.gamebooks.mvc.login.domain.LoginData;
import hu.zagor.gamebooks.mvc.login.domain.LoginResult;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default implementation of the {@link LoginFacade} interface.
 * @author Tamas_Szekeres
 */
public class ZagorLoginFacade implements LoginFacade {

    @LogInject
    private Logger logger;
    @Autowired
    private ServerCommunicator communcator;

    @Override
    public LoginResult doLogin(final LoginData data) {
        final LoginResult result = new LoginResult();
        String response = null;

        try {
            final String postData = assemblePostData(data);
            final URLConnection connection = communcator.connect("http://zagor.hu/remotelogin.php");
            communcator.sendRequest(connection, postData);
            response = communcator.receiveResponse(connection);
            analizeResponse(data, result, response);
        } catch (final SocketTimeoutException e) {
            result.setMessage("page.login.zagor.server.slow");
        } catch (final FileNotFoundException e) {
            result.setMessage("page.login.zagor.server.nofile");
        } catch (final UnknownHostException e) {
            result.setMessage("page.login.zagor.server.nohost");
        } catch (final IOException e) {
            result.setSuccessful(false);
            result.setMessage("page.login.zagor.server.unknown");
        }

        return result;
    }

    private void analizeResponse(final LoginData data, final LoginResult result, final String response) {
        if (response == null) {
            result.setSuccessful(false);
            result.setMessage("page.login.invalid.username.password");
        } else {
            result.setSuccessful(true);

            if (data.getUsername() == null) {
                final String[] pieces = response.split("\\|");
                result.setAdmin(Integer.parseInt(pieces[0]) == 1);
                result.setId(Integer.parseInt(pieces[1]));
                data.setUsername(pieces[2]);
            } else {
                result.setAdmin(Integer.parseInt(response.substring(0, 1)) == 1);
                result.setId(Integer.parseInt(response.substring(1)));
            }
        }
    }

    private String assemblePostData(final LoginData data) throws IOException {
        String part;
        if (data.getUsername() != null) {
            part = communcator.compilePostData("username", data.getUsername(), null);
            part = communcator.compilePostData("password", data.getPassword(), part);
        } else {
            part = communcator.compilePostData("guid", data.getGuid(), null);
        }
        return part;
    }
}
