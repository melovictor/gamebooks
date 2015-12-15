package hu.zagor.gamebooks.character.handler;

import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;

/**
 * Test class.
 * @author Tamas_Szekeres
 */
public class PreparatedItem extends Item {
    private int skill;
    private int initialSkill;
    private int stamina;
    private int initialStamina;

    PreparatedItem(final String id, final String name, final ItemType type) {
        super(id, name, type);
    }

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

}
