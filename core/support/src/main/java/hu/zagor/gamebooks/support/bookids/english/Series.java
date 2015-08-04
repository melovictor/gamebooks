package hu.zagor.gamebooks.support.bookids.english;

import hu.zagor.gamebooks.support.bookids.Language;

public final class Series {

    private static final long BASE = Language.ENGLISH;
    public static final long SERIES_MULTIPLIER = 1000;

    public static final long A_GIRL = BASE + 421 * SERIES_MULTIPLIER;

    public static final long CHOOSE_YOUR_OWN_ADVENTURE = BASE + 762 * SERIES_MULTIPLIER;
    public static final long CHOOSE_YOUR_OWN_ADVENTURE_REISSUE = BASE + 591 * SERIES_MULTIPLIER;
    public static final long CHOOSE_YOUR_OWN_ADVENTURE_WALT_DISNEY = BASE + 596 * SERIES_MULTIPLIER;

    public static final long FIGHTING_FANTASY = BASE + 828 * SERIES_MULTIPLIER;
    public static final long FIGHTING_FANTAZINE = BASE + 339 * SERIES_MULTIPLIER;

    public static final long PROTEUS = BASE + 552 * SERIES_MULTIPLIER;

    public static final long STAR_CHALLENGE = BASE + 358 * SERIES_MULTIPLIER;

    public static final long STORYTRAILS = BASE + 711 * SERIES_MULTIPLIER;

    public static final long TIME_MACHINE = BASE + 966 * SERIES_MULTIPLIER;

    public static final long WARLOCK = BASE + 477 * SERIES_MULTIPLIER;

    private Series() {
    }
}
