package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.hunt;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.support.locale.LocaleProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;

/**
 * Abstract superclass for {@link PieceMover} with some commonly used elements.
 * @author Tamas_Szekeres
 */
public abstract class AbstractPieceMover implements PieceMover {

    static final String DOG_POSITION = "dogPosition";
    static final String TIGER_POSITION = "tigerPosition";

    static final int ROLL_1 = 1;
    static final int ROLL_2 = 2;
    static final int ROLL_3 = 3;
    static final int ROLL_4 = 4;
    static final int ROLL_5 = 5;
    static final int ROLL_6 = 6;

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private HierarchicalMessageSource messageSource;
    @Autowired
    private LocaleProvider localeProvider;

    int getRandom() {
        return generator.getRandomNumber(1)[0];
    }

    String resolveTextKey(final String key) {
        return messageSource.getMessage(key, null, localeProvider.getLocale()) + "<br />";
    }

}
