package hu.zagor.gamebooks.filters;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.player.settings.UserSettingsHandler;
import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract class for filters that are operating with cookies.
 * @author Tamas_Szekeres
 */
public abstract class CookieHandlingAbstractFilter extends AbstractHttpFilter {

    public static final String LANG_KEY = "lang";
    public static final String LOGIN_LANG_KEY = "login_lang";

    private static UserSettingsHandler settingsHandler;

    /**
     * Gets the contents of a specific cookie from the request.
     * @param cookieName the name of the cookie to fetch the value for
     * @param request the request from which to extract the cookie
     * @return the value of the cookie
     */
    protected String getCookie(final String cookieName, final HttpServletRequest request) {
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

    /**
     * Sets a new language both for the generic and the login language and triggers a reload.
     * @param nextLanguage the language to be set
     * @param request the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @throws IOException occurs if something untoward occurs during the operation
     */
    protected void setNewLanguage(final String nextLanguage, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final PlayerUser player = getPlayer(request);
        if (player != null) {
            final PlayerSettings settings = player.getSettings();
            settings.setDefaultLanguage(nextLanguage);
            settingsHandler.saveSettings(player);
        }

        response.addCookie(getCookie(LOGIN_LANG_KEY, nextLanguage));
        response.addCookie(getCookie(LANG_KEY, nextLanguage));
        response.sendRedirect(request.getRequestURI());
    }

    private Cookie getCookie(final String key, final String value) {
        final Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        return cookie;
    }

    /**
     * Gets the currently used {@link PlayerUser} object.
     * @param request the {@link HttpServletRequest} object
     * @return the currently logged-in player or null if the player hasn't logged in yet in the current session
     */
    protected PlayerUser getPlayer(final HttpServletRequest request) {
        return new HttpSessionWrapper(request).getPlayer();
    }

    public static void setSettingsHandler(final UserSettingsHandler settingsHandler) {
        CookieHandlingAbstractFilter.settingsHandler = settingsHandler;
    }
}
