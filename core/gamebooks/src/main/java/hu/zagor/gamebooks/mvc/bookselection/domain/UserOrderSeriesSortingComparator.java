package hu.zagor.gamebooks.mvc.bookselection.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Comparator to use for sorting the book titles based on user-specified data.
 * @author Tamas_Szekeres
 */
public class UserOrderSeriesSortingComparator implements Comparator<SeriesData> {
    private final List<Long> userOrder;

    /**
     * Basic constructor that provides the order that must be maintained during sorting.
     * @param seriesOrder the order specified by the user, can be null
     */
    public UserOrderSeriesSortingComparator(final String seriesOrder) {
        if (seriesOrder == null) {
            userOrder = Collections.<Long>emptyList();
        } else {
            userOrder = new ArrayList<>();
            for (final String seriesId : seriesOrder.split(" ")) {
                userOrder.add(Long.parseLong(seriesId));
            }
        }
    }

    @Override
    public int compare(final SeriesData o1, final SeriesData o2) {
        final int pos1 = userOrder.indexOf(o1.getId());
        final int pos2 = userOrder.indexOf(o2.getId());
        return (pos1 == pos2) ? o1.getName().compareTo(o2.getName()) : pos1 - pos2;
    }

}
