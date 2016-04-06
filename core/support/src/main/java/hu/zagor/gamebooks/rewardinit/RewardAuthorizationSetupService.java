package hu.zagor.gamebooks.rewardinit;

import hu.zagor.gamebooks.connectivity.ServerCommunicator;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.io.IOException;
import java.net.URLConnection;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Profile("!dev")
public class RewardAuthorizationSetupService implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired private ServerCommunicator communicator;
    @LogInject private Logger logger;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        try {
            final URLConnection connection = communicator.connect("http://zagor.hu/gamebooks/rewardAuth.php");
            communicator.sendRequest(connection, "");
            communicator.receiveResponse(connection);
        } catch (final IOException ex) {
            logger.error("Failed to trigger reward authorization setup.", ex);
        }
    }

}
