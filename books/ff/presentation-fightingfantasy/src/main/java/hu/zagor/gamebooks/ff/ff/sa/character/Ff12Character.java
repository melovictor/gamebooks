package hu.zagor.gamebooks.ff.ff.sa.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for FF12.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Ff12Character extends FfCharacter {
    private static final int MAX_STATUS_VALUE = 16;
    private static final int INITIAL_ENEMY_SHIELD = 4;
    private static final int INITIAL_SELF_SHIELD = 5;
    private static final int INITIAL_STATUS = 0;
    private int armour;
    private int initialArmour;
    private int initialWeapons;

    private int status = INITIAL_STATUS;
    private int selfShield = INITIAL_SELF_SHIELD;
    private int enemyShield = INITIAL_ENEMY_SHIELD;

    public int getArmour() {
        return armour;
    }

    public void setArmour(final int armour) {
        this.armour = armour;
    }

    public int getInitialArmour() {
        return initialArmour;
    }

    public void setInitialArmour(final int initialArmour) {
        this.initialArmour = initialArmour;
    }

    public int getInitialWeapons() {
        return initialWeapons;
    }

    public void setInitialWeapons(final int initialWeapons) {
        this.initialWeapons = initialWeapons;
    }

    public int getSelfShield() {
        return selfShield;
    }

    public void setSelfShield(final int selfShield) {
        this.selfShield = selfShield;
    }

    public int getEnemyShield() {
        return enemyShield;
    }

    public void setEnemyShield(final int enemyShield) {
        this.enemyShield = enemyShield;
    }

    public int getStatus() {
        return status;
    }

    /**
     * Sets the current status taking into consideration that during normal operating circumstances the status must be a value in the 1--16 range.
     * @param status the new status
     */
    public void setStatus(final int status) {
        int setValue = status;
        while (setValue > MAX_STATUS_VALUE) {
            setValue -= MAX_STATUS_VALUE;
        }
        this.status = setValue;
    }

}
