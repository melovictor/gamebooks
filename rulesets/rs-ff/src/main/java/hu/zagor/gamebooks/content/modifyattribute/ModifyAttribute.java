package hu.zagor.gamebooks.content.modifyattribute;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean for storing attribute modification requests.
 * @author Tamas_Szekeres
 */
@Component("ffModifyAttribute")
@Scope("prototype")
public class ModifyAttribute implements Serializable, Cloneable {

    private String attribute;
    private int amount;

    /**
     * Default constructor for the serializer.
     */
    ModifyAttribute() {
        attribute = "";
        amount = 0;
    }

    /**
     * Basic constructor setting the name of the attribute and the amount of the modification.
     * @param attribute the attribute to change
     * @param amount the amount by which to change the attribute
     */
    public ModifyAttribute(final String attribute, final int amount) {
        this.attribute = attribute;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(final String attribute) {
        this.attribute = attribute;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    @Override
    public ModifyAttribute clone() throws CloneNotSupportedException {
        return (ModifyAttribute) super.clone();
    }
}
