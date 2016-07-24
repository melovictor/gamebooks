package hu.zagor.gamebooks.mvc.book.inventory.domain;

/**
 * Bean for storing response information for item taking.
 * @author Tamas_Szekeres
 */
public class TakeItemResponse {
    private int totalTaken;
    private String warningMessage;

    /**
     * Default constructor.
     */
    public TakeItemResponse() {
    }

    /**
     * Basic constructor that takes the amount of total taken items.
     * @param totalTaken the number of items taken
     */
    public TakeItemResponse(final int totalTaken) {
        this.totalTaken = totalTaken;
    }

    public int getTotalTaken() {
        return totalTaken;
    }

    public void setTotalTaken(final int totalTaken) {
        this.totalTaken = totalTaken;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(final String warningMessage) {
        this.warningMessage = warningMessage;
    }

}
