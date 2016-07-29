package hu.zagor.gamebooks.character.enemy;

/**
 * Enemy object for the Lone Wolf ruleset.
 * @author Tamas_Szekeres
 */
public class LwEnemy extends Enemy {
    private int combatSkill;
    private int endurance;
    private boolean mindforce;
    private boolean mindshield;
    private boolean mindblast;

    public int getCombatSkill() {
        return combatSkill;
    }

    public void setCombatSkill(final int combatSkill) {
        this.combatSkill = combatSkill;
    }

    public int getEndurance() {
        return endurance;
    }

    public void setEndurance(final int endurance) {
        this.endurance = endurance;
    }

    public boolean isMindforce() {
        return mindforce;
    }

    public void setMindforce(final boolean mindforce) {
        this.mindforce = mindforce;
    }

    public boolean isMindshield() {
        return mindshield;
    }

    public void setMindshield(final boolean mindshield) {
        this.mindshield = mindshield;
    }

    public boolean isMindblast() {
        return mindblast;
    }

    public void setMindblast(final boolean mindblast) {
        this.mindblast = mindblast;
    }

}
