package hu.zagor.gamebooks.filters;

import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

/**
 * Filter for switching the filter in the cookies if it's necessary.
 * @author Tamas_Szekeres
 */
public class LocaleSwitchingFilter extends CookieHandlingAbstractFilter {

    private static final Pattern QUERY_LANG_PATTERN = Pattern.compile(LANG_KEY + "=([^&]+)");

    @Override
    protected void doFilterHttp(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final String currentLanguage = getCookie(LANG_KEY, request);
        String defaultLanguage = null;
        final PlayerUser player = getPlayer(request);
        if (player != null) {
            final PlayerSettings settings = player.getSettings();
            defaultLanguage = settings.getDefaultLanguage();
        }
        final String nextLanguage = getRequestedLanguage(request);
        if (mustSetInitialLanguage(defaultLanguage, getCookie(LOGIN_LANG_KEY, request))) {
            getLogger().debug("Setting initial language {}.", defaultLanguage);
            setNewLanguage(defaultLanguage, request, response);
        } else if (languageChangeWasRequested(currentLanguage, nextLanguage)) {
            getLogger().debug("Setting new language {}.", nextLanguage);
            setNewLanguage(nextLanguage, request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean languageChangeWasRequested(final String currentLanguage, final String nextLanguage) {
        return (currentLanguage == null || !currentLanguage.equals(nextLanguage)) && nextLanguage != null;
    }

    private boolean mustSetInitialLanguage(final String defaultLanguage, final String loginLangCookie) {
        return !StringUtils.isEmpty(loginLangCookie) && defaultLanguage != null && !loginLangCookie.equals(defaultLanguage);
    }

    private String getRequestedLanguage(final HttpServletRequest request) {
        String value = null;
        final String queryString = request.getQueryString();
        if (queryString != null) {
            final Matcher matcher = QUERY_LANG_PATTERN.matcher(queryString);
            if (matcher.find()) {
                value = matcher.group(1);
            }
        }
        return value;
    }

}
