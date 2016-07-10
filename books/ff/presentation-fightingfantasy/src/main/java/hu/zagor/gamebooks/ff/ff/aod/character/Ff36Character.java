package hu.zagor.gamebooks.ff.ff.aod.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.lang.reflect.Field;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * Character object for FF36.
 * @author Tamas_Szekeres
 */
@Component("ff36Character")
@Scope("prototype")
public class Ff36Character extends FfCharacter {
    private int warriors;
    private int dwarves;
    private int elves;
    private int knights;
    private int wilders;
    private int northerns;
    private int marauders;
    private int whiteKnights;

    private int selfPie;
    private int pieEaterPie;

    public int getArmy() {
        return warriors + dwarves + elves + knights + wilders + northerns + marauders + whiteKnights;
    }

    /**
     * Makes changes so the new army size will equal to what is required.
     * @param army the new size of the army
     */
    public void setArmy(final int army) {
        final int currentSize = getArmy();
        int diff = currentSize - army;

        diff = deduct(diff, "warriors");
        diff = deduct(diff, "dwarves");
        diff = deduct(diff, "elves");
        diff = deduct(diff, "knights");
        diff = deduct(diff, "wilders");
        diff = deduct(diff, "northerns");
        diff = deduct(diff, "marauders");
        diff = deduct(diff, "whiteKnights");
    }

    private int deduct(final int toDeduct, final String attribute) {
        int diff = toDeduct;
        final Field field = ReflectionUtils.findField(this.getClass(), attribute);
        field.setAccessible(true);
        final int curAmount = (int) ReflectionUtils.getField(field, this);
        if (curAmount > diff) {
            ReflectionUtils.setField(field, this, curAmount - diff);
            diff = 0;
        } else {
            diff -= curAmount;
            ReflectionUtils.setField(field, this, 0);
        }
        field.setAccessible(false);
        return diff;
    }

    public int getWarriors() {
        return warriors;
    }

    public void setWarriors(final int warriors) {
        this.warriors = warriors;
    }

    public int getDwarves() {
        return dwarves;
    }

    public void setDwarves(final int dwarves) {
        this.dwarves = dwarves;
    }

    public int getElves() {
        return elves;
    }

    public void setElves(final int elves) {
        this.elves = elves;
    }

    public int getKnights() {
        return knights;
    }

    public void setKnights(final int knights) {
        this.knights = knights;
    }

    public int getWilders() {
        return wilders;
    }

    public void setWilders(final int wilders) {
        this.wilders = wilders;
    }

    public int getNortherns() {
        return northerns;
    }

    public void setNortherns(final int northerns) {
        this.northerns = northerns;
    }

    public int getMarauders() {
        return marauders;
    }

    public void setMarauders(final int marauders) {
        this.marauders = marauders;
    }

    public int getWhiteKnights() {
        return whiteKnights;
    }

    public void setWhiteKnights(final int whiteKnights) {
        this.whiteKnights = whiteKnights;
    }

    public int getSelfPie() {
        return selfPie;
    }

    public void setSelfPie(final int selfPie) {
        this.selfPie = selfPie;
    }

    public int getPieEaterPie() {
        return pieEaterPie;
    }

    public void setPieEaterPie(final int pieEaterPie) {
        this.pieEaterPie = pieEaterPie;
    }

}
