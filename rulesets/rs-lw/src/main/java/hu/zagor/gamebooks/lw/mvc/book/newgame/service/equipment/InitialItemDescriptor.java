package hu.zagor.gamebooks.lw.mvc.book.newgame.service.equipment;

public class InitialItemDescriptor {
    private String itemId;
    private int amount;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }
}
