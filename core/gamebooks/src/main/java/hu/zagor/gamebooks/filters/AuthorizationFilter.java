package hu.zagor.gamebooks.filters;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mdc.DefaultMdcHandler;
import hu.zagor.gamebooks.mdc.MdcHandler;
import hu.zagor.gamebooks.player.PlayerUser;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter for redirecting users not logged in to the login page.
 * @author Tamas_Szekeres
 */
public class AuthorizationFilter extends AbstractHttpFilter {

    private static final int REQUEST_URL_PREFIX_LENGTH = (PageAddresses.ROOT_CONTEXT + PageAddresses.BOOK_PAGE).length() + 1;
    private static final String LAST_PAGE_BEFORE_REDIRECT = "lastPageBeforeRedirect";

    private final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);
    private List<String> alloweds;
    private String resourceDir;
    private String to;
    private final MdcHandler mdcHandler = new DefaultMdcHandler();

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        to = filterConfig.getInitParameter("redirectTo");
        resourceDir = filterConfig.getInitParameter("resourceDirectory");
        alloweds = new ArrayList<>();
        final String fromsString = filterConfig.getInitParameter("redirectAlloweds");
        if (fromsString == null || to == null || resourceDir == null) {
            throw new IllegalStateException(MessageFormat.format("please set the redirectFroms, redirectTo and resourceDirectory init parameters for the {0} filter",
                filterConfig.getFilterName()));
        }
        final String[] splittedFromsString = fromsString.split(",\\s*");
        for (final String fromString : splittedFromsString) {
            alloweds.add(filterConfig.getServletContext().getContextPath() + fromString);
        }
    }

    @Override
    protected void doFilterHttp(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpSessionWrapper httpSessionWrapper = new HttpSessionWrapper(request.getSession());
        final PlayerUser player = httpSessionWrapper.getPlayer();
        if (shouldRedirect(request, player)) {
            mdcHandler.cleanUserId(request.getSession());
            String lastPageUrl = request.getRequestURI();
            getLogger().error("Unknown user tried to access page {}; redirecting him to login page.", lastPageUrl);
            final String queryString = request.getQueryString();
            if (queryString != null) {
                lastPageUrl += '?' + queryString;
            }
            if (lastPageUrl != null) {
                request.getSession().setAttribute(LAST_PAGE_BEFORE_REDIRECT, lastPageUrl);
                response.sendRedirect(PageAddresses.ROOT_CONTEXT + PageAddresses.LOGIN);
            } else {
                response.sendRedirect(PageAddresses.LOGIN);
            }
        } else {
            try {
                mdcHandler.provideUserId(request.getSession(), fetchBookId(request.getRequestURI()));
                if (response.getHeader("resource") == null) {
                    logRequestInfo(request);
                }
                chain.doFilter(request, response);
            } catch (final Throwable ex) {
                logger.error("Exception bubbled up:", ex);
                throw ex;
            }
        }

    }

    private String fetchBookId(final String requestUri) {
        String bookId = null;
        if (requestUri.startsWith(PageAddresses.ROOT_CONTEXT + PageAddresses.BOOK_PAGE + "/")) {
            bookId = requestUri.substring(REQUEST_URL_PREFIX_LENGTH, requestUri.indexOf("/", REQUEST_URL_PREFIX_LENGTH));
            if (!bookId.matches("[0-9]+")) {
                bookId = null;
            }
        }
        return bookId;
    }

    private boolean shouldRedirect(final HttpServletRequest request, final PlayerUser player) {
        return (notAllowedUri(request) && notResourceUri(request)) && player == null;
    }

    private boolean notResourceUri(final HttpServletRequest request) {
        return !request.getRequestURI().contains(resourceDir);
    }

    private boolean notAllowedUri(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        boolean allowedPath = false;
        for (final String allowedUrl : alloweds) {
            allowedPath |= requestUri.startsWith(allowedUrl);
        }
        return !allowedPath;
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

        if (request.getCookies() != null) {
            for (final Cookie cookie : request.getCookies()) {
                logger.info("Cookie '{}': '{}'.", cookie.getName(), cookie.getValue());
            }
        }
    }
}
