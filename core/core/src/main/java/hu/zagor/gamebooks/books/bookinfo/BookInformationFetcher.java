package hu.zagor.gamebooks.books.bookinfo;

import hu.zagor.gamebooks.domain.BookInformations;

/**
 * Interface for fetching a {@link BookInformations} object.
 * @author Tamas_Szekeres
 */
public interface BookInformationFetcher {

    /**
     * Fetches a {@link BookInformations} object by the book's ID.
     * @param bookId the ID of the book
     * @return the {@link BookInformations} object
     */
    BookInformations getInfoById(String bookId);

    /**
     * Fetches a {@link BookInformations} object by the book's ID.
     * @param bookId the ID of the book
     * @return the {@link BookInformations} object
     */
    BookInformations getInfoById(long bookId);

    /**
     * Fetches a {@link BookInformationFetcher} object using the book's ID that is retrieved from the current request object.
     * @return the {@link BookInformations} object
     */
    BookInformations getInfoByRequest();

}
