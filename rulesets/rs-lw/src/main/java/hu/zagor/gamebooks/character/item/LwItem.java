package hu.zagor.gamebooks.character.item;

/**
 * Lone Wolf specific item.
 * @author Tamas_Szekeres
 */
public class LwItem extends Item {
    private int endurance;
    private int initialEndurance;
    private int combatSkill;
    private Placement placement;
    private String weaponType;

    /**
     * Default constructor for the serializer.
     */
    LwItem() {
        super();
    }

    /**
     * Basic constructor that creates a new unmodifiable item.
     * @param id the id of the item, cannot be null
     * @param name the name of the item that can be displayed, cannot be null
     * @param itemType the type of the item
     */
    public LwItem(final String id, final String name, final ItemType itemType) {
        super(id, name, itemType);
        getEquipInfo().setConsumable(itemType.isConsumable());
    }

    public int getEndurance() {
        return endurance;
    }

    public void setEndurance(final int endurance) {
        this.endurance = endurance;
    }

    public int getInitialEndurance() {
        return initialEndurance;
    }

    public void setInitialEndurance(final int initialEndurance) {
        this.initialEndurance = initialEndurance;
    }

    public int getCombatSkill() {
        return combatSkill;
    }

    public void setCombatSkill(final int combatSkill) {
        this.combatSkill = combatSkill;
    }

    public Placement getPlacement() {
        return placement;
    }

    public void setPlacement(final Placement placement) {
        this.placement = placement;
    }

    public String getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(final String weaponType) {
        this.weaponType = weaponType;
    }

}
