package hu.zagor.gamebooks.character.item;

/**
 * The type of the item.
 * @author Tamas_Szekeres
 */
public enum ItemType {
    shadow(false, 0, false, true),
    immediate(false, 0, false, true),
    common(false),
    info(false),
    weapon1(true, 1),
    shield(true, 1),
    weapon2(true, 1),
    shooting(true, 1),
    ring(true, 2),
    necklace(true, 1),
    boots(true, 1),
    armour(true, 1),
    helmet(true, 1),
    bracelet(true, 2),
    cloak(true, 1),
    brooch(true, 2),
    provision(false, true),
    potion(false, true),
    gloves(true, 1),
    curseSickness(false),
    valuable(false),
    special(false);

    static {
        weapon1.disallows = new ItemType[]{weapon2};
        shield.disallows = new ItemType[]{weapon2};
        weapon2.disallows = new ItemType[]{weapon1, shield};
    }

    private final boolean equipable;
    private final int maxEquipped;
    private ItemType[] disallows;
    private final boolean consumable;
    private boolean invisible;

    ItemType(final boolean equipable) {
        this(equipable, 0, false, false);
    }

    ItemType(final boolean equipable, final int maxEquipped) {
        this(equipable, maxEquipped, false, false);
    }

    ItemType(final boolean equipable, final boolean consumable) {
        this(equipable, 0, consumable, false);
    }

    ItemType(final boolean equipable, final int maxEquipped, final boolean consumable, final boolean invisible) {
        this.equipable = equipable;
        this.maxEquipped = maxEquipped;
        this.disallows = new ItemType[]{};
        this.consumable = consumable;
        this.invisible = invisible;
    }

    public boolean isEquipable() {
        return equipable;
    }

    public int getMaxEquipped() {
        return maxEquipped;
    }

    public ItemType[] getDisallows() {
        return disallows;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public boolean isInvisible() {
        return invisible;
    }
}
