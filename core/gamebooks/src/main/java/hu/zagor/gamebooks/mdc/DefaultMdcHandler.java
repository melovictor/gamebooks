package hu.zagor.gamebooks.mdc;

import javax.servlet.http.HttpSession;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link MdcHandler} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultMdcHandler implements MdcHandler {

    private static final String MDC_KEY = "userid";
    private static final String SESSION_KEY = "mdcUserId";

    @Override
    public void setUserId(final String value, final HttpSession session) {
        session.setAttribute(SESSION_KEY, value);
        MDC.put(MDC_KEY, value);
    }

    @Override
    public void provideUserId(final HttpSession session) {
        final String userId = (String) session.getAttribute(SESSION_KEY);
        if (userId == null) {
            MDC.remove(MDC_KEY);
        } else {
            MDC.put(MDC_KEY, userId);
        }
    }

    @Override
    public void cleanUserId(final HttpSession session) {
        session.setAttribute(SESSION_KEY, null);
        MDC.remove(MDC_KEY);
    }

}
