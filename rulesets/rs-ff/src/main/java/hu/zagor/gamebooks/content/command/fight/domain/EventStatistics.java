package hu.zagor.gamebooks.content.command.fight.domain;

/**
 * Bean for storing a total and a subsequent statistic value.
 * @author Tamas_Szekeres
 */
public class EventStatistics {

    private final int total;
    private final int subsequent;

    /**
     * Basic constructor.
     * @param total number of events in total
     * @param subsequent number of events subsequently
     */
    public EventStatistics(final int total, final int subsequent) {
        this.total = total;
        this.subsequent = subsequent;
    }

    public int getTotal() {
        return total;
    }

    public int getSubsequent() {
        return subsequent;
    }

}
