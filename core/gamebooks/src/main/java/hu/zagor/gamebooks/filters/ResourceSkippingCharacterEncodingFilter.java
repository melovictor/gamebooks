package hu.zagor.gamebooks.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Variant of the Spring {@link CharacterEncodingFilter} which skips setting the encoding for resources (images, css, js, etc.).
 * @author Tamas_Szekeres
 */
public class ResourceSkippingCharacterEncodingFilter extends CharacterEncodingFilter {
    /**
     * Default constructor which enforces the usage of UTF-8 encoding.
     */
    public ResourceSkippingCharacterEncodingFilter() {
        super("UTF-8", true);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
        throws ServletException, IOException {
        if (request.getRequestURI().contains("/resources/")) {
            filterChain.doFilter(request, response);
        } else {
            super.doFilterInternal(request, response, filterChain);
        }
    }
}
