package hu.zagor.gamebooks.filters;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter for redirecting users not logged in to the login page.
 * @author Tamas_Szekeres
 */
public class RequestLoggingFilter extends AbstractHttpFilter {

    private final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterHttp(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try {
            if (response.getHeader("resource") == null) {
                logRequestInfo(request);
            }
            chain.doFilter(request, response);
        } catch (final Throwable ex) {
            logger.error("Exception bubbled up:", ex);
            throw ex;
        }
    }

    private void logRequestInfo(final HttpServletRequest request) {
        final Logger logger = getLogger();
        logger.info("Requested url: '{}'", request.getRequestURI());

        final Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            final String name = parameterNames.nextElement();
            final Object value = "password".equals(name) ? "******************" : request.getParameter(name);
            logger.info("Parameter '{}': '{}'.", name, value);
        }
    }
}
