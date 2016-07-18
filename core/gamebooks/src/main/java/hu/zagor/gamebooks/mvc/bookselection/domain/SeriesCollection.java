package hu.zagor.gamebooks.mvc.bookselection.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Collection for storing information about all the available series and books on a given locale.
 * @author Tamas_Szekeres
 */
public class SeriesCollection extends TreeSet<SeriesData> {
    private final Map<String, SeriesData> map = new HashMap<>();

    /**
     * Constructor to set the sorting order for the series.
     * @param seriesOrder the order to consider during sorting
     */
    public SeriesCollection(final String seriesOrder) {
        super(new UserOrderSeriesSortingComparator(seriesOrder));
    }

    @Override
    public boolean add(final SeriesData e) {
        super.add(e);
        map.put(e.getName(), e);
        return true;
    }

    /**
     * Fetches a {@link SeriesData} object that belongs to a specific series name, if exists, or returns null if it does not.
     * @param seriesName the name of the series to fetch
     * @return the {@link SeriesData} object, or null if it doesn't exists yet
     */
    public SeriesData get(final String seriesName) {
        return map.get(seriesName);
    }

    /**
     * Checks whether a book series with a specific name is already part of this set or not.
     * @param seriesName the name we are looking for
     * @return true, if the name is already part of the set, false otherwise
     */
    public boolean contains(final String seriesName) {
        return map.containsKey(seriesName);
    }

}
