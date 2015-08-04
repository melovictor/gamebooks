package hu.zagor.gamebooks.books.contentstorage;

import hu.zagor.gamebooks.books.contentstorage.domain.BookEntryStorage;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.domain.BookInformations;

/**
 * Interface for loading the contents of a book from the storage into memory.
 * @author Tamas_Szekeres
 */
public interface BookContentLoader {

    /**
     * Loads the contents of a book into a {@link BookEntryStorage} bean.
     * @param bookInfo the {@link BookInformations} bean of the book to load
     * @return the loaded {@link BookEntryStorage} bean
     */
    BookItemStorage loadBookContent(BookInformations bookInfo);

}
