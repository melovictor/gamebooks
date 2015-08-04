package hu.zagor.gamebooks.mvc.bookselection.domain;

import java.util.TreeMap;

/**
 * Collection for storing information about all the available series and books
 * on a given locale.
 * @author Tamas_Szekeres
 *
 */
public class SeriesCollection extends TreeMap<String, SeriesData> {

    /**
     * Adds the new series data to the collection.
     * @param seriesData the bean containing the series' data
     */
    public void add(final SeriesData seriesData) {
        super.put(seriesData.getName(), seriesData);
    }

}
