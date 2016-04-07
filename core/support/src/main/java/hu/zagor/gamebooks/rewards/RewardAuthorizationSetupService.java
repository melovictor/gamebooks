package hu.zagor.gamebooks.rewards;

import hu.zagor.gamebooks.connectivity.ServerCommunicator;
import hu.zagor.gamebooks.support.logging.LogInject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Service for pinging to the home server, requesting an authentication notification for the rewards.
 * @author Tamas_Szekeres
 */
@Component
@Profile("!dev")
public class RewardAuthorizationSetupService implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired private ServerCommunicator communicator;
    @LogInject private Logger logger;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        logger.info("Initializing reward authorization code.");
        communicator.connectAsync("http://zagor.hu/gamebooks/rewardAuth.php");
    }
}
