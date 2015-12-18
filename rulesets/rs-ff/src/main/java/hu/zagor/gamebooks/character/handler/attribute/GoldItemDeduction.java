package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.character.item.FfItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for stating which items and how many gold pieces are subject to be lost in a specific transaction.
 * @author Tamas_Szekeres
 */
public class GoldItemDeduction {
    private int gold;
    private final List<FfItem> items = new ArrayList<>();

    public int getGold() {
        return gold;
    }

    public List<FfItem> getItems() {
        return items;
    }

    public void setGold(final int gold) {
        this.gold = gold;
    }

}
