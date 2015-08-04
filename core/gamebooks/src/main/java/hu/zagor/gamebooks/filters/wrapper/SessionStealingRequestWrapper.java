package hu.zagor.gamebooks.filters.wrapper;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * {@link HttpServletRequestWrapper} to avoid session stealing.
 * @author Tamas_Szekeres
 *
 */
public class SessionStealingRequestWrapper extends HttpServletRequestWrapper {

    /**
     * Basic constructor that wraps the request.
     * @param request the request to wrap
     */
    public SessionStealingRequestWrapper(final HttpServletRequest request) {
        super(request);
    }

    @Override
    public HttpSession getSession(final boolean create) {
        boolean shouldCreate = false;
        HttpSession currentSession = super.getSession(true);

        if (currentSession != null) {
            final ServletRequest request = getRequest();
            final String remoteIp = request.getRemoteAddr();

            final String storedRemoteIp = (String) currentSession.getAttribute("remoteIp");
            if (!remoteIp.equals(storedRemoteIp)) {
                shouldCreate = true;
            }

            if (shouldCreate) {
                currentSession.invalidate();
            }

            currentSession = super.getSession(true);
            if (currentSession != null) {
                currentSession.setAttribute("remoteIp", remoteIp);
            }
        }

        return currentSession;
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

}
