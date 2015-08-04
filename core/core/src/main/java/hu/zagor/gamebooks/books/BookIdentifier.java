package hu.zagor.gamebooks.books;

import hu.zagor.gamebooks.domain.BookInformations;

/**
 * Interface for getting informations about the different books found in the provided files.
 * @author Tamas_Szekeres
 *
 */
public interface BookIdentifier {

    /**
     * Method for getting informations about a single book.
     * @return the bean containing the requested information
     */
    BookInformations getInformations();

}
