package hu.zagor.gamebooks.mvc.ping.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.ping.service.PingService;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for displaying statistics about the Zagor.hu server's uptime.
 * @author Tamas_Szekeres
 */
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class PingController {
    private static final DateTimeFormatter LONG_FORMAT = DateTimeFormat.forPattern("YYYY. MMMM d. HH:mm:ss");

    @Autowired private PingService pingService;

    /**
     * Shows the ping statistics page.
     * @param model the {@link Model} object
     * @return the name of the view
     */
    @RequestMapping(value = PageAddresses.PING)
    public String showStatistics(final Model model) {
        final List<Long> failureTimestamps = pingService.getFailureTimestamps();
        final List<String> failureTimes = new ArrayList<>();
        for (final Long timestamp : failureTimestamps) {
            failureTimes.add(LONG_FORMAT.withZone(DateTimeZone.forID("Europe/Budapest")).print(timestamp));
        }
        model.addAttribute("failureTimes", failureTimes);
        return "pingView";
    }

}
