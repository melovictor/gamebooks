package hu.zagor.gamebooks.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract filter for {@link HttpServletRequest} and {@link HttpServletResponse} that checks that the request and response is appropriate.
 * @author Tamas_Szekeres
 */
public abstract class AbstractHttpFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            throw new IllegalArgumentException("this filter only supports HttpServletRequests");
        }
        if (!(response instanceof HttpServletResponse)) {
            throw new IllegalArgumentException("this filter only supports HttpServletResponses");
        }
        doFilterHttp((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    /**
     * Does the actual filtering.
     * @param request the request
     * @param response the response
     * @param chain the chain of filters to go through
     * @throws IOException occurs if there is an I/O problem
     * @throws ServletException occurs if there is a problem with the servlet
     */
    protected abstract void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;

    Logger getLogger() {
        return logger;
    }

}
