package hu.zagor.gamebooks.support.locale;

import java.util.Locale;

/**
 * Interface for providing the current locale for the user.
 * @author Tamas_Szekeres
 */
public interface LocaleProvider {

    /**
     * Gets the locale from whatever source it can dig out.
     * @return the currently used locale
     */
    Locale getLocale();
}
