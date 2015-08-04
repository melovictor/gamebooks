package hu.zagor.gamebooks.support.bookids;

/**
 * List of available languages and their identification numbers.
 * @author Tamas_Szekeres
 */
public final class Language {

    public static final long LANGUAGE_MULTIPLIER = 10000000;

    public static final long HUNGARIAN = 99 * LANGUAGE_MULTIPLIER;
    public static final long ENGLISH = 760 * LANGUAGE_MULTIPLIER;

    public static final long PORTUGESE = 852 * LANGUAGE_MULTIPLIER;
    public static final long BRAZILIAN = 577 * LANGUAGE_MULTIPLIER;

    private Language() {
    }
}
