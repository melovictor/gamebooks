package hu.zagor.gamebooks.mvc.bookselection.domain;

import java.util.Set;
import java.util.TreeSet;

/**
 * Bean containing series-specific information.
 * @author Tamas_Szekeres
 *
 */
public class SeriesData implements Comparable<SeriesData> {
    private String name;
    private final Set<BookData> books = new TreeSet<BookData>();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<BookData> getBooks() {
        return books;
    }

    /**
     * Adds a book to the collection.
     * @param bookData the book to add
     */
    public void addBook(final BookData bookData) {
        books.add(bookData);
    }

    @Override
    public int compareTo(final SeriesData other) {
        return name.compareTo(other.name);
    }

}
