package hu.zagor.gamebooks.content.command.fight.domain;

import java.util.List;

/**
 * Bean for storing the statuses of the old and new weapons.
 * @author Tamas_Szekeres
 */
public class WeaponReplacementData {

    private String origWeapon;
    private boolean switchedWeaponRemovable;
    private List<String> forceWeapons;

    public String getOrigWeapon() {
        return origWeapon;
    }

    public void setOrigWeapon(final String origWeapon) {
        this.origWeapon = origWeapon;
    }

    public boolean isSwitchedWeaponRemovable() {
        return switchedWeaponRemovable;
    }

    public void setSwitchedWeaponRemovable(final boolean origWeaponRemovable) {
        this.switchedWeaponRemovable = origWeaponRemovable;
    }

    public List<String> getForceWeapons() {
        return forceWeapons;
    }

    public void setForceWeapons(final List<String> forceWeapons) {
        this.forceWeapons = forceWeapons;
    }

}
