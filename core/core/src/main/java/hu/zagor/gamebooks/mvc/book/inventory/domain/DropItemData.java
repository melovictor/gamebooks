package hu.zagor.gamebooks.mvc.book.inventory.domain;

/**
 * Bean for storing information about the item dropping action.
 * @author Tamas_Szekeres
 */
public class DropItemData {
    private String itemId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }
}
