package hu.zagor.gamebooks.lw.character;

public class KaiDisciplines {
    private boolean camouflage;
    private boolean hunting;
    private boolean sixthSense;
    private boolean tracking;
    private boolean healing;
    private boolean mindshield;
    private boolean mindblast;
    private boolean animalKinship;
    private boolean mindOverMatter;

    private WeaponSkill weaponSkill = new WeaponSkill();

    public boolean isCamouflage() {
        return camouflage;
    }

    public void setCamouflage(final boolean camouflage) {
        this.camouflage = camouflage;
    }

    public boolean isHunting() {
        return hunting;
    }

    public void setHunting(final boolean hunting) {
        this.hunting = hunting;
    }

    public boolean isSixthSense() {
        return sixthSense;
    }

    public void setSixthSense(final boolean sixthSense) {
        this.sixthSense = sixthSense;
    }

    public boolean isTracking() {
        return tracking;
    }

    public void setTracking(final boolean tracking) {
        this.tracking = tracking;
    }

    public boolean isHealing() {
        return healing;
    }

    public void setHealing(final boolean healing) {
        this.healing = healing;
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

    public boolean isAnimalKinship() {
        return animalKinship;
    }

    public void setAnimalKinship(final boolean animalKinship) {
        this.animalKinship = animalKinship;
    }

    public boolean isMindOverMatter() {
        return mindOverMatter;
    }

    public void setMindOverMatter(final boolean mindOverMatter) {
        this.mindOverMatter = mindOverMatter;
    }

    public WeaponSkill getWeaponSkill() {
        return weaponSkill;
    }

    public void setWeaponSkill(final WeaponSkill weaponSkill) {
        this.weaponSkill = weaponSkill;
    }
}
