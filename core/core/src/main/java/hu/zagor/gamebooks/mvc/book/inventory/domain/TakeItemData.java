package hu.zagor.gamebooks.mvc.book.inventory.domain;

/**
 * Bean for storing item taking information.
 * @author Tamas_Szekeres
 */
public class TakeItemData {
    private String itemId;
    private int amount;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(final String id) {
        this.itemId = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

}
