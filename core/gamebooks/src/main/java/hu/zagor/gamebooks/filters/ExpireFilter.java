package hu.zagor.gamebooks.filters;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter for switching the filter in the cookies if it's necessary.
 * @author Tamas_Szekeres
 */
public class ExpireFilter extends AbstractHttpFilter {

    private static final Long MILLISEC = 1000L;
    private static final Long HOUR = MILLISEC * 60 * 60;
    private static final Long DAY = HOUR * 24;
    private static final Long MONTH = DAY * 30;
    private static final Long YEAR = DAY * 365;

    private final Map<String, Long> extensionBasedExpiration;
    private final Map<String, Long> nameBasedExpiration;

    /**
     * Sets up the maps governing the expiration times.
     */
    public ExpireFilter() {
        extensionBasedExpiration = new HashMap<>();
        extensionBasedExpiration.put("css", MONTH);
        extensionBasedExpiration.put("js", MONTH);

        extensionBasedExpiration.put("jpg", YEAR);
        extensionBasedExpiration.put("png", YEAR);
        extensionBasedExpiration.put("jpeg", YEAR);

        extensionBasedExpiration.put("small", 0L);

        nameBasedExpiration = new HashMap<>();
        nameBasedExpiration.put("bze.js", HOUR);
        nameBasedExpiration.put("bze.css", HOUR);
        nameBasedExpiration.put("ff.js", HOUR);
        nameBasedExpiration.put("ff.css", HOUR);
        nameBasedExpiration.put("raw.js", HOUR);
        nameBasedExpiration.put("raw.css", HOUR);
    }

    @Override
    protected void doFilterHttp(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        setUpExpireDate(request, response);
        chain.doFilter(request, response);
    }

    private void setUpExpireDate(final HttpServletRequest request, final HttpServletResponse response) {
        if (!resolveByName(request, response)) {
            resolveByExtension(request, response);
        }
    }

    private boolean resolveByName(final HttpServletRequest request, final HttpServletResponse response) {
        final String uri = request.getRequestURI();
        final String name = uri.substring(uri.lastIndexOf("/") + 1);
        final Long expiration = nameBasedExpiration.get(name);
        setUpHeader(response, expiration);
        return expiration != null;
    }

    private void resolveByExtension(final HttpServletRequest request, final HttpServletResponse response) {
        final String uri = request.getRequestURI();
        final String extension = uri.substring(uri.lastIndexOf(".") + 1);
        final Long expiration = extensionBasedExpiration.get(extension);
        setUpHeader(response, expiration);
    }

    private void setUpHeader(final HttpServletResponse response, final Long expiration) {
        if (expiration != null) {
            final long expiry = new Date().getTime() + expiration;
            response.setDateHeader("Expires", expiry);
            response.setHeader("Cache-Control", "max-age=" + (expiration / MILLISEC));
            response.setHeader("resource", "resource");
        }
    }
}
