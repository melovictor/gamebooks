package hu.zagor.gamebooks.support.spring;

import hu.zagor.gamebooks.connectivity.ServerCommunicator;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import java.io.IOException;
import java.net.URLConnection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class for doing whatever needs to be done when the webapplication starts.
 * @author Tamas_Szekeres
 */
@Component
public class UpdateInitializer {

    @Autowired
    private ServerCommunicator communicator;
    @Autowired
    private EnvironmentDetector detector;

    @PostConstruct
    public void initMethod() throws IOException {
        removeUpdates();
    }

    private void removeUpdates() throws IOException {
        if (!detector.isDevelopment()) {
            final URLConnection connection = communicator.connect("http://zagor.hu/gamebooks/remove.php");
            communicator.sendRequest(connection, "");
            communicator.receiveResponse(connection);
        }
    }
}
