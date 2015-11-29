package hu.zagor.gamebooks.ff.mvc.book.inventory.domain;

import hu.zagor.gamebooks.mvc.book.inventory.domain.TakeItemData;

/**
 * Bean for storing item taking / purchasing information.
 * @author Tamas_Szekeres
 */
public class TakePurchaseItemData extends TakeItemData {
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

}
