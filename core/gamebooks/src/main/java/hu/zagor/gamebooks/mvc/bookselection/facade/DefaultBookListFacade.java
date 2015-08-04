package hu.zagor.gamebooks.mvc.bookselection.facade;

import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.bookselection.domain.SeriesCollection;
import hu.zagor.gamebooks.mvc.bookselection.domain.transformer.SeriesCollectionTransformer;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link BookListFacade} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultBookListFacade implements BookListFacade {

    @Autowired
    private List<BookInformations> availableBooks;
    @Autowired
    private SeriesCollectionTransformer collectionTransformer;
    @LogInject
    private Logger logger;
    private final Map<String, SeriesCollection> seriesByLanguage = new HashMap<>();

    @Override
    public SeriesCollection getAvailableBooks(final Locale locale, final PlayerUser player) {
        SeriesCollection collection;
        if (player.isAdmin()) {
            collection = loadSeriesData(player, locale);
        } else {
            if (!seriesByLanguage.containsKey(locale.getLanguage())) {
                final SeriesCollection list = loadSeriesData(player, locale);
                seriesByLanguage.put(locale.getLanguage(), list);
            }
            collection = seriesByLanguage.get(locale.getLanguage());
        }
        return collection;
    }

    private SeriesCollection loadSeriesData(final PlayerUser player, final Locale simplifiedLocale) {
        return collectionTransformer.createSeriesCollection(availableBooks, simplifiedLocale, player);
    }

    public void setAvailableBooks(final List<BookInformations> availableBooks) {
        this.availableBooks = availableBooks;
    }

    public void setLogger(final Logger logger) {
        this.logger = logger;
    }

}
