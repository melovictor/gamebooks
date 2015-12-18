package hu.zagor.gamebooks.ff.character;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.item.FfItem;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Player character for Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
@Component("ffCharacter")
@Scope("prototype")
public class FfCharacter extends Character {

    private boolean initialized;

    private int skill;
    private int stamina;
    private int luck;
    private int initialSkill;
    private int initialStamina;
    private int initialLuck;
    private int attackStrength;
    private int stoneSkin;
    private final int initialAttackStrength = Integer.MAX_VALUE;

    private int gold;
    private int damageProtection;
    private int baseStaminaDamage;

    public int getSkill() {
        return skill;
    }

    public void setSkill(final int skill) {
        this.skill = skill;
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

    public int getGold() {
        return gold;
    }

    public void setGold(final int gold) {
        this.gold = gold;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(final boolean initialized) {
        this.initialized = initialized;
    }

    /**
     * Changes the value of the stamina of the character by the provided amount.
     * @param change the amount by which the stamina has to be changed
     */
    public void changeStamina(final int change) {
        this.stamina += change;
    }

    /**
     * Changes the value of the skill of the character by the provided amount.
     * @param change change the amount by which the skill has to be changed
     */
    public void changeSkill(final int change) {
        this.skill += change;
    }

    /**
     * Changes the value of the luck of the character by the provided amount.
     * @param change change the amount by which the luck has to be changed
     */
    public void changeLuck(final int change) {
        this.luck += change;
    }

    public int getAttackStrength() {
        return attackStrength;
    }

    public int getInitialAttackStrength() {
        return initialAttackStrength;
    }

    public int getStoneSkin() {
        return stoneSkin;
    }

    public void setStoneSkin(final int stoneSkin) {
        this.stoneSkin = stoneSkin;
    }

    public String getName() {
        return null;
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<FfItem> getFfEquipment() {
        return (List) getEquipment();
    }
}
