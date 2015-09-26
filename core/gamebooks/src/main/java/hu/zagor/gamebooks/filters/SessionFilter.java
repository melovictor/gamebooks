package hu.zagor.gamebooks.filters;

import hu.zagor.gamebooks.filters.wrapper.ErrorClosedownPreventingResponseWrapper;
import hu.zagor.gamebooks.filters.wrapper.SessionStealingRequestWrapper;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

/**
 * A simple filter that wraps the request into our own wrapper to avoid stealing the session.
 * @author Tamas_Szekeres
 */
public class SessionFilter extends AbstractHttpFilter {

    @Override
    protected void doFilterHttp(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (response.getHeader("resource") == null) {
            logRequestInfo(request);
        }
        final HttpServletRequestWrapper requestWrapper = new SessionStealingRequestWrapper(request);
        final ErrorClosedownPreventingResponseWrapper responseWrapper = new ErrorClosedownPreventingResponseWrapper(response);
        response.setHeader("X-Frame-Options", "DENY");
        chain.doFilter(requestWrapper, responseWrapper);
        if (responseWrapper.isStatusCode(HttpServletResponse.SC_NOT_FOUND)) {
            responseWrapper.sendRedirect("../booklist");
        } else {
            responseWrapper.approveSendError();
        }
    }

    private void logRequestInfo(final HttpServletRequest request) {
        final Logger logger = getLogger();
        logger.info("Requested url: '{}'", request.getRequestURL().toString());

        final Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            final String name = attributeNames.nextElement();
            final Object value = request.getAttribute(name);
            logger.info("Attribute '{}': '{}'.", name, value);
        }

        final Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            final String name = parameterNames.nextElement();
            final Object value = "password".equals(name) ? "******************" : request.getParameter(name);
            logger.info("Parameter '{}': '{}'.", name, value);
        }

        for (final Cookie cookie : request.getCookies()) {
            logger.info("Cookie '{}': '{}'.", cookie.getName(), cookie.getValue());
        }
    }
}
