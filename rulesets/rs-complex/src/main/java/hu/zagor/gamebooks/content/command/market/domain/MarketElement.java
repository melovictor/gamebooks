package hu.zagor.gamebooks.content.command.market.domain;

import hu.zagor.gamebooks.content.TrueCloneable;

/**
 * Bean to store a single item for sale or purchase in the market.
 * @author Tamas_Szekeres
 */
public class MarketElement implements TrueCloneable {

    private String id;
    private String name;
    private int price;
    private int stock = 1;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(final int stock) {
        this.stock = stock;
    }

    @Override
    public MarketElement clone() throws CloneNotSupportedException {
        return (MarketElement) super.clone();
    }
}
