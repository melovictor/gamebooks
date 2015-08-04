package hu.zagor.gamebooks.ff.ff.trok.character.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Attributes for the spaceship in FF15.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Ff15ShipAttributes {

    private int weaponStrength;
    private int initialWeaponStrength;
    private int shield;
    private int initialShield;
    private int smartMissile;

    public int getWeaponStrength() {
        return weaponStrength;
    }

    public void setWeaponStrength(final int weaponStrength) {
        this.weaponStrength = weaponStrength;
    }

    public int getInitialWeaponStrength() {
        return initialWeaponStrength;
    }

    public void setInitialWeaponStrength(final int initialWeaponStrength) {
        this.initialWeaponStrength = initialWeaponStrength;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(final int shield) {
        this.shield = shield;
    }

    public int getInitialShield() {
        return initialShield;
    }

    public void setInitialShield(final int initialShield) {
        this.initialShield = initialShield;
    }

    public int getSmartMissile() {
        return smartMissile;
    }

    public void setSmartMissile(final int smartMissile) {
        this.smartMissile = smartMissile;
    }

}
