package hu.zagor.gamebooks.ff.ff.sa.enemy;

/**
 * Class for describing a specific wepaon of the Deity.
 * @author Tamas_Szekeres
 */
public class DeityWeapon {
    private final String nameKeyPostfix;
    private final int skill;
    private final int damage;
    private final boolean variableDamage;

    /**
     * Basic constructor for a single-damage weapon.
     * @param nameKeyPostfix the postfix for the text resource for this weapon's name
     * @param skill the skill belonging to this weapon
     * @param damage the static damage of the weapon
     */
    public DeityWeapon(final String nameKeyPostfix, final int skill, final int damage) {
        this.nameKeyPostfix = nameKeyPostfix;
        this.skill = skill;
        this.damage = damage;
        this.variableDamage = false;
    }

    /**
     * Basic constructor for a variable-damage weapon.
     * @param nameKeyPostfix the postfix for the text resource for this weapon's name
     * @param skill the skill belonging to this weapon
     */
    public DeityWeapon(final String nameKeyPostfix, final int skill) {
        this.nameKeyPostfix = nameKeyPostfix;
        this.skill = skill;
        this.damage = 0;
        this.variableDamage = true;
    }

    public String getNameKeyPostfix() {
        return nameKeyPostfix;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isVariableDamage() {
        return variableDamage;
    }

    public int getSkill() {
        return skill;
    }

}
