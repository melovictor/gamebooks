package hu.zagor.gamebooks.lw.mvc.book.newgame.domain;

import java.util.List;

/**
 * Bean storing character generation parameters for LW.
 * @author Tamas_Szekeres
 */
public class LwCharGenInput {
    private boolean weaponskill;
    private boolean camouflage;
    private boolean hunting;
    private boolean sixthSense;
    private boolean tracking;
    private boolean healing;
    private boolean mindshield;
    private boolean mindblast;
    private boolean animalKinship;
    private boolean mindOverMatter;
    private List<LwCharGenEquipment> equipments;

    public boolean isWeaponskill() {
        return weaponskill;
    }

    public void setWeaponskill(final boolean weaponskill) {
        this.weaponskill = weaponskill;
    }

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

    public List<LwCharGenEquipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(final List<LwCharGenEquipment> equipments) {
        this.equipments = equipments;
    }

}
