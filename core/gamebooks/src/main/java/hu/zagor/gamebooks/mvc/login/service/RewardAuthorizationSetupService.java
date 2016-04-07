package hu.zagor.gamebooks.mvc.login.service;

import hu.zagor.gamebooks.connectivity.ServerCommunicator;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.io.IOException;
import java.net.URLConnection;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Service for pinging to the home server, requesting an authentication notification for the rewards.
 * @author Tamas_Szekeres
 */
@Component
@Profile("!dev")
public class RewardAuthorizationSetupService {
    private static boolean alreadySetUp;

    @Autowired private ServerCommunicator communicator;
    @LogInject private Logger logger;

    /**
     * Executes the ping that will result in the reward authentication code to be received.
     */
    public synchronized void setUpRewardAuthorizations() {
        if (!alreadySetUp) {
            alreadySetUp = true;
            logger.info("Initializing reward authorization code.");
            try {
                final URLConnection connection = communicator.connect("http://zagor.hu/gamebooks/rewardAuth.php");
                communicator.sendRequest(connection, "");
                communicator.receiveResponse(connection);
            } catch (final IOException ex) {
                logger.error("Failed to trigger reward authorization setup.", ex);
            }
        }
    }
}
