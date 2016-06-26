package hu.zagor.gamebooks.mvc.bookselection.domain;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Bean containing the data of books.
 * @author Tamas_Szekeres
 */
public class BookData implements Comparable<BookData> {

    private static final NumberFormat NUMBER_FORMATTER = NumberFormat.getNumberInstance(Locale.ENGLISH);
    private final Long id;
    private String series;
    private String title;
    private Double position;
    private Integer innerPosition;
    private String coverPath;
    private boolean disabled;
    private boolean unfinished;
    private Locale locale;
    private String resourceDir;

    /**
     * Basic constructor that creates a new item with the specified id.
     * @param id the id of the book
     */
    public BookData(final long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String name) {
        title = name;
    }

    /**
     * Gets the properly formatted string representation of the position.
     * @return the formatted position or null if the book doesn't have a position itself
     */
    public String getPosition() {
        String formattedPosition = null;
        if (position != null) {
            formattedPosition = NUMBER_FORMATTER.format(position);
        }

        return formattedPosition;
    }

    public void setPosition(final Double position) {
        this.position = position;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setInnerPosition(final Integer innerPosition) {
        this.innerPosition = innerPosition;
    }

    public void setCoverPath(final String coverLocation) {
        coverPath = coverLocation;
    }

    public Long getId() {
        return id;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(final String series) {
        this.series = series;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public int compareTo(final BookData other) {
        Integer result = compare(position, other.position);
        if (result == null) {
            result = compare(innerPosition, other.innerPosition);
        }
        if (result == null) {
            result = compare(title, other.title);
        }
        if (result == null) {
            result = 0;
        }
        return result;
    }

    private <T extends Comparable<T>> Integer compare(final T self, final T other) {
        Integer result = null;
        if (self != null) {
            result = self.compareTo(other);
        } else if (other != null) {
            result = -other.compareTo(self);
        }
        return result;
    }

    public boolean isUnfinished() {
        return unfinished;
    }

    public void setUnfinished(final boolean unfinished) {
        this.unfinished = unfinished;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    public String getResourceDir() {
        return resourceDir;
    }

    public void setResourceDir(final String resourceDir) {
        this.resourceDir = resourceDir;
    }

}
