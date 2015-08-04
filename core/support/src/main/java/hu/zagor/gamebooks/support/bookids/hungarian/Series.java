package hu.zagor.gamebooks.support.bookids.hungarian;

import static hu.zagor.gamebooks.support.bookids.english.Series.SERIES_MULTIPLIER;
import hu.zagor.gamebooks.support.bookids.Language;

public final class Series {

    private static final long BASE = Language.HUNGARIAN;

    public static final long CSILLAGPROBA = BASE + 327 * SERIES_MULTIPLIER;

    public static final long EGY_LANY = BASE + 744 * SERIES_MULTIPLIER;

    public static final long FANTAZIA_HARCOS = BASE + 133 * SERIES_MULTIPLIER;

    public static final long IDOGEP_REGENY = BASE + 934 * SERIES_MULTIPLIER;

    public static final long KALAND_JATEK_KOCKAZAT = BASE + 162 * SERIES_MULTIPLIER;
    public static final long KALAND_JATEK_KOCKAZAT_ZAGOR = BASE + 231 * SERIES_MULTIPLIER;
    public static final long KALAND_JATEK_KOCKAZAT_ZAGOR_FUZET = BASE + 665 * SERIES_MULTIPLIER;

    public static final long NYOMKERESO = BASE + 812 * SERIES_MULTIPLIER;

    public static final long VALASSZ_KALANDOT_MAGADNAK = BASE + 952 * SERIES_MULTIPLIER;
    public static final long VALASSZ_KALANDOT_MAGADNAK_UJRAKIADAS = BASE + 697 * SERIES_MULTIPLIER;
    public static final long VALASSZ_KALANDOT_MAGADNAK_WALT_DISNEY = BASE + 927 * SERIES_MULTIPLIER;

    public static final long SOLO = BASE + 262 * SERIES_MULTIPLIER;

    private Series() {
    }
}
