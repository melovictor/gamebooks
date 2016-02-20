package hu.zagor.gamebooks.ff.ff.sa.enemy;

import hu.zagor.gamebooks.character.enemy.FfEnemy;

/**
 * Enemy object for FF12.
 * @author Tamas_Szekeres
 */
public class Ff12Enemy extends FfEnemy {
    private String weapon;
    private int attackPerRound;
    private DeityWeapon activeWeapon;

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(final String weapon) {
        this.weapon = weapon;
    }

    public int getAttackPerRound() {
        return attackPerRound;
    }

    public void setAttackPerRound(final int attackPerRound) {
        this.attackPerRound = attackPerRound;
    }

    public DeityWeapon getActiveWeapon() {
        return activeWeapon;
    }

    public void setActiveWeapon(final DeityWeapon activeWeapon) {
        this.activeWeapon = activeWeapon;
    }

}
