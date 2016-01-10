package hu.zagor.gamebooks.filters;

import hu.zagor.gamebooks.filters.wrapper.ErrorClosedownPreventingResponseWrapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple filter that wraps the request into our own wrapper to avoid stealing the session.
 * @author Tamas_Szekeres
 */
public class SessionFilter extends AbstractHttpFilter {

    @Override
    protected void doFilterHttp(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final ErrorClosedownPreventingResponseWrapper responseWrapper = new ErrorClosedownPreventingResponseWrapper(response);
        chain.doFilter(request, responseWrapper);
        if (responseWrapper.isStatusCode(HttpServletResponse.SC_NOT_FOUND)) {
            responseWrapper.sendRedirect("../booklist");
        } else {
            responseWrapper.approveSendError();
        }
    }

}
