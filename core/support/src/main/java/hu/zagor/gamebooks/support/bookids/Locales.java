package hu.zagor.gamebooks.support.bookids;

import java.util.Locale;

/**
 * List of available locales, cached for simplicity's sake.
 * @author Tamas_Szekeres
 */
public final class Locales {

    public static final Locale ENGLISH = Locale.ENGLISH;
    public static final Locale HUNGARIAN = new Locale("hu");
    public static final Locale PORTUGESE = new Locale("pt", "pt");
    public static final Locale PORTUGESE_BRAZILIAN = new Locale("pt", "br");

    private Locales() {
    }
}
