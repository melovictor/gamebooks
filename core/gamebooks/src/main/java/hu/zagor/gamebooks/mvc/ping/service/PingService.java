package hu.zagor.gamebooks.mvc.ping.service;

import hu.zagor.gamebooks.connectivity.ServerCommunicator;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Class for executing ping tests against the Zagor.hu server.
 * @author Tamas_Szekeres
 */
@Component
public class PingService {

    private final List<Long> failureTimestamps = new ArrayList<>();
    @Autowired private ServerCommunicator communicator;

    public List<Long> getFailureTimestamps() {
        return Collections.unmodifiableList(failureTimestamps);
    }

    /**
     * Periodically checks the status of the server every minute.
     */
    @Scheduled(cron = "0 * * * * *")
    public void executeAvailabilityCheck() {
        try {
            final URLConnection connection = communicator.connect("http://zagor.hu/index.php");
            communicator.sendRequest(connection, "{}");
            communicator.receiveResponse(connection);
        } catch (final IOException exception) {
            failureTimestamps.add(System.currentTimeMillis());
        }
    }

}
