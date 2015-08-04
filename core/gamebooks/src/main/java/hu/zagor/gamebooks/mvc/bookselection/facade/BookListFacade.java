package hu.zagor.gamebooks.mvc.bookselection.facade;

import hu.zagor.gamebooks.mvc.bookselection.domain.SeriesCollection;
import hu.zagor.gamebooks.player.PlayerUser;

import java.util.Locale;

/**
 * Interface for acquiring the list of available books in an ordered fashion.
 * @author Tamas_Szekeres
 */
public interface BookListFacade {

    /**
     * Gets the list of books available in the selected locale ordered by the name of the series, then the position of the book.
     * @param locale the locale of the series to return
     * @param player the {@link PlayerUser} object
     * @return an object containing the books in the series
     */
    SeriesCollection getAvailableBooks(Locale locale, PlayerUser player);

}
