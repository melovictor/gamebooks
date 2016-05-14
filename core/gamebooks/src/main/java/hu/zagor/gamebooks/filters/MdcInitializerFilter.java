package hu.zagor.gamebooks.filters;

import hu.zagor.gamebooks.mdc.DefaultMdcHandler;
import hu.zagor.gamebooks.mdc.MdcHandler;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter for redirecting users not logged in to the login page.
 * @author Tamas_Szekeres
 */
public class MdcInitializerFilter extends AbstractHttpFilter {

    private static final Pattern BOOK_ID_PATTERN = Pattern.compile("book\\/([0-9]{8,10})\\/(?!resources)");

    private final MdcHandler mdcHandler = new DefaultMdcHandler();

    @Override
    protected void doFilterHttp(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        mdcHandler.provideUserId(request.getSession(), fetchBookId(request.getRequestURI()));
        chain.doFilter(request, response);
    }

    private String fetchBookId(final String requestUri) {
        String bookId = null;
        final Matcher matcher = BOOK_ID_PATTERN.matcher(requestUri);
        if (matcher.find()) {
            bookId = matcher.group(1);
        }
        return bookId;
    }

}
