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
    private ModifyAttributeType type;
    private boolean goldOnly;

    /**
     * Default constructor for the serializer.
     */
    ModifyAttribute() {
        attribute = "";
        amount = 0;
        type = ModifyAttributeType.change;
        goldOnly = false;
    }

    /**
     * Basic constructor setting the name of the attribute and the amount of the modification.
     * @param attribute the attribute to change
     * @param amount the amount by which to change the attribute
     * @param type the type of the {@link ModifyAttribute} object
     */
    public ModifyAttribute(final String attribute, final int amount, final ModifyAttributeType type) {
        this(attribute, amount, type, false);
    }

    /**
     * Basic constructor setting the name of the attribute and the amount of the modification.
     * @param attribute the attribute to change
     * @param amount the amount by which to change the attribute
     * @param type the type of the {@link ModifyAttribute} object
     * @param goldOnly true if the attribute is gold, and we must use only gold pieces during the deduction, not other valuables
     */
    public ModifyAttribute(final String attribute, final int amount, final ModifyAttributeType type, final boolean goldOnly) {
        this.attribute = attribute;
        this.amount = amount;
        this.type = type;
        this.goldOnly = goldOnly;
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

    public ModifyAttributeType getType() {
        return type;
    }

    public void setType(final ModifyAttributeType type) {
        this.type = type;
    }

    public boolean isGoldOnly() {
        return goldOnly;
    }

    public void setGoldOnly(final boolean goldOnly) {
        this.goldOnly = goldOnly;
    }
}
