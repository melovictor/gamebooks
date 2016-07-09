package hu.zagor.gamebooks.ff.ff.aod.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for FF36.
 * @author Tamas_Szekeres
 */
@Component("ff36Character")
@Scope("prototype")
public class Ff36Character extends FfCharacter {
    // TODO: extend when new squadron is added
    private int warriors;
    private int dwarves;
    private int elves;
    private int knights;
    private int wilders;
    private int northerns;
    private int marauders;
    private int whiteKnights;

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

}
