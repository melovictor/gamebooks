package hu.zagor.gamebooks.lw.character;

/**
 * The list of different weapons for which the Weaponskill kai discipline can be learned.
 * @author Tamas_Szekeres
 */
public class Weaponskill {
    private final boolean fist = false;
    private boolean dagger;
    private boolean spear;
    private boolean mace;
    private boolean shortSword;
    private boolean warhammer;
    private boolean sword;
    private boolean axe;
    private boolean quarterstaff;
    private boolean broadsword;
    private boolean bow;

    public boolean isSwords() {
        return sword || broadsword || shortSword;
    }

    public boolean isDagger() {
        return dagger;
    }

    public void setDagger(final boolean dagger) {
        this.dagger = dagger;
    }

    public boolean isSpear() {
        return spear;
    }

    public void setSpear(final boolean spear) {
        this.spear = spear;
    }

    public boolean isMace() {
        return mace;
    }

    public void setMace(final boolean mace) {
        this.mace = mace;
    }

    public boolean isShortSword() {
        return shortSword;
    }

    public void setShortSword(final boolean shortSword) {
        this.shortSword = shortSword;
    }

    public boolean isWarhammer() {
        return warhammer;
    }

    public void setWarhammer(final boolean warhammer) {
        this.warhammer = warhammer;
    }

    public boolean isSword() {
        return sword;
    }

    public void setSword(final boolean sword) {
        this.sword = sword;
    }

    public boolean isAxe() {
        return axe;
    }

    public void setAxe(final boolean axe) {
        this.axe = axe;
    }

    public boolean isQuarterstaff() {
        return quarterstaff;
    }

    public void setQuarterstaff(final boolean quarterstaff) {
        this.quarterstaff = quarterstaff;
    }

    public boolean isBroadsword() {
        return broadsword;
    }

    public void setBroadsword(final boolean broadsword) {
        this.broadsword = broadsword;
    }

    public boolean isBow() {
        return bow;
    }

    public void setBow(final boolean bow) {
        this.bow = bow;
    }

    /**
     * Determines whether the character has already learned to fight with a single weapon or not.
     * @return true if at least one specific weapon skill has been learned, false if none
     */
    public boolean isWeaponskillObtained() {
        final boolean hasSword = broadsword || shortSword || sword;
        final boolean hasBlunt = mace || warhammer;
        final boolean hasOther = bow || axe || dagger || spear || quarterstaff;
        return hasSword || hasBlunt || hasOther;
    }

    public boolean isFist() {
        return fist;
    }

}
