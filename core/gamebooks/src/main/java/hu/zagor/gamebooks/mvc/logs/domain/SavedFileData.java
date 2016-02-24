package hu.zagor.gamebooks.mvc.logs.domain;

/**
 * Bean for storing information about a single saved game.
 * @author Tamas_Szekeres
 */
public class SavedFileData implements Comparable<SavedFileData> {
    private long bookId;
    private String bookTitle;

    public long getBookId() {
        return bookId;
    }

    public void setBookId(final long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(final String bookTitle) {
        this.bookTitle = bookTitle;
    }

    @Override
    public int compareTo(final SavedFileData o) {
        return (int) Math.signum(bookId - o.bookId);
    }

}
