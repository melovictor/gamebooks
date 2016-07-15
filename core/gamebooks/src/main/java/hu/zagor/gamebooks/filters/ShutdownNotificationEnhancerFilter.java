package hu.zagor.gamebooks.filters;

import hu.zagor.gamebooks.mvc.update.domain.UpdateStatusContainer;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 * Filter for adding information to the response about the next anticipated shutdown.
 * @author Tamas_Szekeres
 */
public class ShutdownNotificationEnhancerFilter extends AbstractHttpFilter {
    private static UpdateStatusContainer updateStatusContainer;

    @Override
    protected void doFilterHttp(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final DateTime updateTime = updateStatusContainer.getUpdateTime();
        if (updateTime != null) {
            request.setAttribute("nextUpdateTime", new Period(DateTime.now(), updateTime).toStandardMinutes().getMinutes());
        }
        chain.doFilter(request, response);
    }

    public static void setUpdateStatusContainer(final UpdateStatusContainer updateStatusContainer) {
        ShutdownNotificationEnhancerFilter.updateStatusContainer = updateStatusContainer;
    }

}
