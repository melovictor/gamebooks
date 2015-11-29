package hu.zagor.gamebooks.mvc.book.inventory.domain;

/**
 * Bean for storing item replacement information.
 * @author Tamas_Szekeres
 */
public class ReplaceItemData {
    private String loseId;
    private String gatherId;
    private int amount;

    public String getLoseId() {
        return loseId;
    }

    public void setLoseId(final String loseId) {
        this.loseId = loseId;
    }

    public String getGatherId() {
        return gatherId;
    }

    public void setGatherId(final String gatherId) {
        this.gatherId = gatherId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }
}
