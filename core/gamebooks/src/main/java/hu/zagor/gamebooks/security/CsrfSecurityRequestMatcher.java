package hu.zagor.gamebooks.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

/**
 * A custom {@link RequestMatcher} for the {@link CsrfFilter} to only protect the login and logout screens.
 * @author Tamas_Szekeres
 */
@Component("csrfSecurityRequestMatcher")
public class CsrfSecurityRequestMatcher implements RequestMatcher {
    private final RegexRequestMatcher loginLogoutPathMatcher = new RegexRequestMatcher("\\/(?:login|logout).*", null);

    @Override
    public boolean matches(final HttpServletRequest request) {
        return !"GET".equals(request.getMethod()) && loginLogoutPathMatcher.matches(request);
    }

}
