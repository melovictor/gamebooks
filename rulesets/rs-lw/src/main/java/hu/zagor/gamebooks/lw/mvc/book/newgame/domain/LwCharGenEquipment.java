package hu.zagor.gamebooks.lw.mvc.book.newgame.domain;

/**
 * Bean for storing information about a single, specifically selected equipment.
 * @author Tamas_Szekeres
 */
public class LwCharGenEquipment {
    private String id;
    private int amount;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

}
