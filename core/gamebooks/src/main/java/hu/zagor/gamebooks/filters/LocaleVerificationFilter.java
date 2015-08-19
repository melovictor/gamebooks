package hu.zagor.gamebooks.filters;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter for verifying the validity of the contents of the locale cookie, and if it's not valid, replace it with a hopefully acceptible default value.
 * @author Tamas_Szekeres
 */
public class LocaleVerificationFilter extends CookieHandlingAbstractFilter {

    @Override
    protected void doFilterHttp(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        boolean valid = verifyLangCookie(request, LANG_KEY);
        valid &= verifyLangCookie(request, LOGIN_LANG_KEY);

        if (valid) {
            chain.doFilter(request, response);
        } else {
            setNewLanguage("en", request, response);
        }
    }

    private boolean verifyLangCookie(final HttpServletRequest request, final String key) {
        boolean valid = true;
        final String currentLanguage = getCookie(key, request);

        try {
            final Locale locale = new Locale.Builder().setLanguageTag(currentLanguage.replace("_", "-")).build();
            if (!isValidLocale(locale)) {
                valid = false;
            }
        } catch (final Exception ex) {
            valid = false;
        }
        return valid;
    }

    private boolean isValidLocale(final Locale locale) {
        return locale.getISO3Language() != null;
    }
}
