package hu.zagor.gamebooks.character.item;

import java.io.Serializable;

/**
 * Bean for storing information about the given item's equipability.
 * @author Tamas_Szekeres
 */
public class EquipInfo implements Cloneable, Serializable {

    private boolean equipped;
    private final boolean equippable;
    private boolean removable;
    private boolean consumable;

    /**
     * Constructor for the deserializer.
     */
    EquipInfo() {
        equippable = false;
        equipped = true;
        removable = true;
    }

    /**
     * Basic constructor that creates a bean in a valid state.
     * @param itemType the type of the item
     */
    public EquipInfo(final ItemType itemType) {
        this.equippable = itemType.isEquipable();
        equipped = !itemType.isEquipable() && (itemType != ItemType.immediate);
        removable = true;
    }

    /**
     * Checks if the current piece of equipment is equipped or not.
     * @return true if the item is equippable and is equipped, or if the item is not equippable (meaning it is something that is usually held in the backpack)
     */
    public boolean isEquipped() {
        return equipped;
    }

    /**
     * Sets the equipped state of the given item. If it is equippable and not consumable, the appropriate field will be updated with the new value, but if it isn't, it will stay as
     * it was.
     * @param equiped the new state
     */
    public void setEquipped(final boolean equiped) {
        if (equippable && !consumable) {
            this.equipped = equiped;
        }
    }

    public boolean isEquippable() {
        return equippable;
    }

    @Override
    public EquipInfo clone() throws CloneNotSupportedException {
        return (EquipInfo) super.clone();
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(final boolean removable) {
        this.removable = removable;
    }

    public boolean isConsumable() {
        return consumable;
    }

    /**
     * Sets the consumable state for this item. Also, if it is consumable then it sets the item's equipped state to false - the effects of consumable items does not count when
     * calculating the character's properties.
     * @param consumable true if it is a consumable item, false otherwise
     */
    public void setConsumable(final boolean consumable) {
        this.consumable = consumable;
        if (consumable) {
            equipped = false;
        }
    }

}
