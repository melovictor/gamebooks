package hu.zagor.gamebooks.mvc.update.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.update.domain.UpdateStatusContainer;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for receiving notification about an upcoming application update.
 * @author Tamas_Szekeres
 */
@Controller
public class ServerUpdateNoticeReceivingController {
    private static final int UPDATE_TIME_MINUTES = 14;
    @Autowired private UpdateStatusContainer updateStatusContainer;

    /**
     * Method for receiving a notification about an upcoming event and setting up the update time in the {@link UpdateStatusContainer}.
     * @param response the {@link HttpServletResponse} to do the redirection to the login page
     * @throws IOException occurs when the redirection to the login page fails for some reason
     */
    @RequestMapping("updateImminent")
    public void acknowledgeNotification(final HttpServletResponse response) throws IOException {
        final DateTime now = DateTime.now();
        DateTime updateTime = now.withMinuteOfHour(UPDATE_TIME_MINUTES).withSecondOfMinute(0);
        if (updateTime.isBefore(now)) {
            updateTime = updateTime.plusHours(1);
        }

        updateStatusContainer.setUpdateTime(updateTime);
        response.sendRedirect(PageAddresses.LOGIN);
    }
}
