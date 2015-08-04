package hu.zagor.gamebooks.mvc.rules.domain;

import hu.zagor.gamebooks.domain.BookInformations;

import java.util.Set;
import java.util.TreeSet;

/**
 * Bean for storing the series for which help is available.
 * @author Tamas_Szekeres
 */
public class HelpSeriesBooks implements Comparable<HelpSeriesBooks> {

    private String seriesTitle;
    private String locale;
    private final Set<BookInformations> entries = new TreeSet<BookInformations>();

    public Set<BookInformations> getEntries() {
        return entries;
    }

    /**
     * Adds a new book entry to this specific series.
     * @param object the object to add
     */
    public void add(final BookInformations object) {
        entries.add(object);
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public void setSeriesTitle(final String seriesTitle) {
        this.seriesTitle = seriesTitle;
    }

    @Override
    public int compareTo(final HelpSeriesBooks o) {
        return seriesTitle.compareTo(o.seriesTitle);
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(final String locale) {
        this.locale = locale;
    }

}
