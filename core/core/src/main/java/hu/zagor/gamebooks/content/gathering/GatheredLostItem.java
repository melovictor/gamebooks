package hu.zagor.gamebooks.content.gathering;

import hu.zagor.gamebooks.content.TrueCloneable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Bean for storing what item and how much of it has been gathered or lost at a given section.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class GatheredLostItem implements TrueCloneable {

    private final String id;
    private final int amount;
    private final int dose;

    /**
     * Default constructor for the deserializer.
     */
    GatheredLostItem() {
        id = "";
        amount = 0;
        dose = 0;
    }

    /**
     * Basic constructor.
     * @param id the id of the item that has been gathered or lost, cannot be null
     * @param amount the amount of the given item that has been gathered or lost, must be positive
     * @param dose the number of doses to lose
     */
    public GatheredLostItem(final String id, final int amount, final int dose) {
        Assert.notNull(id, "The parameter 'id' cannot be null!");
        Assert.isTrue(id.length() > 0, "The parameter 'id' cannot be empty!");
        Assert.isTrue((amount > 0 && dose == 0) || (amount == 0 && dose > 0), "The parameter 'amount' or 'dose' must be positive, the other one must be zero!");

        this.id = id;
        this.amount = amount;
        this.dose = dose;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public GatheredLostItem clone() throws CloneNotSupportedException {
        return (GatheredLostItem) super.clone();
    }

    public int getDose() {
        return dose;
    }
}
