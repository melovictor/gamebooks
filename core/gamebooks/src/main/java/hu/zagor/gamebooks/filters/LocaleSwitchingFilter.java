package hu.zagor.gamebooks.filters;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.player.settings.UserSettingsHandler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

/**
 * Filter for switching the filter in the cookies if it's necessary.
 * @author Tamas_Szekeres
 */
public class LocaleSwitchingFilter extends AbstractHttpFilter {

    public static final String LANG_KEY = "lang";
    public static final String LOGIN_LANG_KEY = "login_lang";

    private static final Pattern QUERY_LANG_PATTERN = Pattern.compile(LANG_KEY + "=([^&]+)");

    private static UserSettingsHandler settingsHandler;

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
        return !StringUtils.isEmpty(loginLangCookie) && defaultLanguage != null;
    }

    private void setNewLanguage(final String nextLanguage, final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        final PlayerUser player = getPlayer(request);
        if (player != null) {
            final PlayerSettings settings = player.getSettings();
            settings.setDefaultLanguage(nextLanguage);
            settingsHandler.saveSettings(player);
        }

        response.addCookie(new Cookie(LOGIN_LANG_KEY, null));
        response.addCookie(new Cookie(LANG_KEY, nextLanguage));
        response.sendRedirect(request.getRequestURI());
    }

    private PlayerUser getPlayer(final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = new HttpSessionWrapper(request.getSession());
        final PlayerUser player = wrapper.getPlayer();
        return player;
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

    private String getCookie(final String cookieName, final HttpServletRequest request) {
        String value = null;
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (final Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    value = cookie.getValue();
                }
            }
        }
        return value;
    }

    public static void setSettingsHandler(final UserSettingsHandler settingsHandler) {
        LocaleSwitchingFilter.settingsHandler = settingsHandler;
    }

}
