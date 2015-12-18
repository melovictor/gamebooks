package hu.zagor.gamebooks.character.item;

/**
 * Fighting Fantasy specific item.
 * @author Tamas_Szekeres
 */
public class FfItem extends Item {

    private int skillDamage;
    private int staminaDamage;

    private int skill;
    private int stamina;
    private int staminaNd6;
    private int luck;
    private int initialSkill;
    private int initialStamina;
    private int initialLuck;
    private int attackStrength;

    private boolean blessed;
    private boolean magical;

    private boolean preFight;
    private boolean usedInPreFight;

    private int dose;
    private int price;
    private int gold;

    private int actions;
    private int damageProtection;
    private int baseStaminaDamage;

    /**
     * Default constructor for the serializer.
     */
    FfItem() {
        super();
    }

    /**
     * Basic constructor that creates a new unmodifiable item.
     * @param id the id of the item, cannot be null
     * @param name the name of the item that can be displayed, cannot be null
     * @param itemType the type of the item
     */
    public FfItem(final String id, final String name, final ItemType itemType) {
        super(id, name, itemType);
        getEquipInfo().setConsumable(itemType.isConsumable());
    }

    public int getSkillDamage() {
        return skillDamage;
    }

    public void setSkillDamage(final int skillDamage) {
        this.skillDamage = skillDamage;
    }

    public int getStaminaDamage() {
        return staminaDamage;
    }

    public void setStaminaDamage(final int staminaDamage) {
        this.staminaDamage = staminaDamage;
    }

    public boolean isBlessed() {
        return blessed;
    }

    public void setBlessed(final boolean blessed) {
        this.blessed = blessed;
    }

    public boolean isMagical() {
        return magical;
    }

    public void setMagical(final boolean magical) {
        this.magical = magical;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(final int addToSkill) {
        this.skill = addToSkill;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(final int stamina) {
        this.stamina = stamina;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(final int luck) {
        this.luck = luck;
    }

    public int getInitialSkill() {
        return initialSkill;
    }

    public void setInitialSkill(final int initialSkill) {
        this.initialSkill = initialSkill;
    }

    public int getInitialStamina() {
        return initialStamina;
    }

    public void setInitialStamina(final int initialStamina) {
        this.initialStamina = initialStamina;
    }

    public int getInitialLuck() {
        return initialLuck;
    }

    public void setInitialLuck(final int initialLuck) {
        this.initialLuck = initialLuck;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(final int dose) {
        this.dose = dose;
    }

    @Override
    public FfItem clone() throws CloneNotSupportedException {
        return (FfItem) super.clone();
    }

    public int getAttackStrength() {
        return attackStrength;
    }

    public void setAttackStrength(final int attackStrength) {
        this.attackStrength = attackStrength;
    }

    public int getStaminaNd6() {
        return staminaNd6;
    }

    public void setStaminaNd6(final int staminaNd6) {
        this.staminaNd6 = staminaNd6;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public int getActions() {
        return actions;
    }

    public void setActions(final int actions) {
        this.actions = actions;
    }

    public boolean isPreFight() {
        return preFight;
    }

    public void setPreFight(final boolean preFight) {
        this.preFight = preFight;
    }

    public int getDamageProtection() {
        return damageProtection;
    }

    public void setDamageProtection(final int damageProtection) {
        this.damageProtection = damageProtection;
    }

    public int getBaseStaminaDamage() {
        return baseStaminaDamage;
    }

    public void setBaseStaminaDamage(final int baseStaminaDamage) {
        this.baseStaminaDamage = baseStaminaDamage;
    }

    public boolean isUsedInPreFight() {
        return usedInPreFight;
    }

    public void setUsedInPreFight(final boolean usedInPreFight) {
        this.usedInPreFight = usedInPreFight;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(final int gold) {
        this.gold = gold;
    }

}
