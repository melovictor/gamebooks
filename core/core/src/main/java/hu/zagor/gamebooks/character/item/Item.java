package hu.zagor.gamebooks.character.item;

import hu.zagor.gamebooks.content.TrueCloneable;

import org.springframework.util.Assert;

/**
 * Class representing an item in the world. An item always has an id, a name that can be displayed to the user and a type. An item can have other properties as well based on the
 * world we're playing in.
 * @author Tamas_Szekeres
 */
public class Item implements TrueCloneable {

    private final String id;
    private final String name;
    private final ItemType itemType;
    private int amount = 1;
    private WeaponSubType subType;
    private EquipInfo equipInfo;
    private int backpackSize = 1;
    private String description;

    /**
     * Constructor for the deserializer.
     */
    Item() {
        id = "";
        name = "";
        itemType = ItemType.common;
    }

    /**
     * Basic constructor that creates a new unmodifiable item.
     * @param id the id of the item, cannot be null
     * @param name the name of the item that can be displayed, cannot be null
     * @param itemType the type of the item
     */
    public Item(final String id, final String name, final ItemType itemType) {
        Assert.notNull(id, "Parameter 'id' can not be null!");
        Assert.notNull(name, "Parameter 'name' can not be null!");
        Assert.notNull(itemType, "Parameter 'itemType' can not be null!");

        this.id = id;
        this.name = name;
        this.itemType = itemType;
        equipInfo = new EquipInfo(itemType);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ItemType getItemType() {
        return itemType;
    }

    /**
     * Gets the {@link EquipInfo} bean.
     * @return the {@link EquipInfo} bean
     */
    public EquipInfo getEquipInfo() {
        if (equipInfo == null) {
            equipInfo = new EquipInfo(itemType);
        }
        return equipInfo;
    }

    @Override
    public Item clone() throws CloneNotSupportedException {
        final Item cloned = (Item) super.clone();
        cloned.equipInfo = equipInfo.clone();
        return cloned;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + id.hashCode();
        result = prime * result + name.hashCode();
        return prime * result + itemType.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof String) {
            return id.equals(obj);
        } else if (obj instanceof Item) {
            final Item other = (Item) obj;
            return other.getId().equals(id);
        }
        return false;
    }

    public WeaponSubType getSubType() {
        return subType;
    }

    public void setSubType(final WeaponSubType subType) {
        this.subType = subType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public int getBackpackSize() {
        return backpackSize;
    }

    public void setBackpackSize(final int backpackSize) {
        this.backpackSize = backpackSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

}
