package hu.zagor.gamebooks.ff.ff.sa.mvc.books.newgame.service;

/**
 * Bean for storing the user's initial weapon choices.
 * @author Tamas_Szekeres
 */
public class Ff12WeaponChoice {
    private int lash;
    private int blaster;
    private int grenade;
    private int bomb;
    private int armour;

    public int getLash() {
        return lash;
    }

    public void setLash(final int lash) {
        this.lash = lash;
    }

    public int getBlaster() {
        return blaster;
    }

    public void setBlaster(final int blaster) {
        this.blaster = blaster;
    }

    public int getGrenade() {
        return grenade;
    }

    public void setGrenade(final int grenade) {
        this.grenade = grenade;
    }

    public int getBomb() {
        return bomb;
    }

    public void setBomb(final int bomb) {
        this.bomb = bomb;
    }

    public int getArmour() {
        return armour;
    }

    public void setArmour(final int armour) {
        this.armour = armour;
    }
}
