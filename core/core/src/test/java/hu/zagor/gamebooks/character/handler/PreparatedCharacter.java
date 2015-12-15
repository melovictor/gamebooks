package hu.zagor.gamebooks.character.handler;

import hu.zagor.gamebooks.character.Character;

/**
 * Test class.
 * @author Tamas_Szekeres
 */
public class PreparatedCharacter extends Character {
    private int stamina;
    private int initialStamina;
    private int skill;
    private int initialSkill;

    public int getStamina() {
        return stamina;
    }

    public void setStamina(final int stamina) {
        this.stamina = stamina;
    }

    public int getInitialStamina() {
        return initialStamina;
    }

    public void setInitialStamina(final int initialStamina) {
        this.initialStamina = initialStamina;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(final int skill) {
        this.skill = skill;
    }

    public int getInitialSkill() {
        return initialSkill;
    }

    public void setInitialSkill(final int initialSkill) {
        this.initialSkill = initialSkill;
    }

}
