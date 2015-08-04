package hu.zagor.gamebooks.mvc.bookselection.domain.transformer;

import hu.zagor.gamebooks.books.BookIdentifier;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.bookselection.domain.SeriesCollection;
import hu.zagor.gamebooks.player.PlayerUser;

import java.util.List;
import java.util.Locale;

/**
 * Interface for creating {@link SeriesCollection} beans.
 * @author Tamas_Szekeres
 */
public interface SeriesCollectionTransformer {

    /**
     * Creates a {@link SeriesCollection} bean from a list of {@link BookIdentifier} objects using a {@link Locale} to filter them by language.
     * @param bookList the list of beans to convert, cannot be null
     * @param locale the locale against which to filter the list, cannot be null
     * @param player the {@link PlayerUser} object
     * @return the converted {@link SeriesCollection} bean
     */
    SeriesCollection createSeriesCollection(List<BookInformations> bookList, Locale locale, PlayerUser player);

}
