package hu.zagor.gamebooks.ff.ff.sob.character;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character object for FF16.
 * @author Tamas_Szekeres
 */
@Component("ff16Character")
@Scope("prototype")
public class Ff16Character extends FfCharacter {
    private int crewStrike;
    private int crewStrength;
    private int initialCrewStrike;
    private int initialCrewStrength;
    private int time;
    private int slaves;

    public int getCrewStrike() {
        return crewStrike;
    }

    public void setCrewStrike(final int crewStrike) {
        this.crewStrike = crewStrike;
    }

    public int getCrewStrength() {
        return crewStrength;
    }

    public void setCrewStrength(final int crewStrength) {
        this.crewStrength = crewStrength;
    }

    public int getTime() {
        return time;
    }

    public void setTime(final int time) {
        this.time = time;
    }

    public int getSlaves() {
        return slaves;
    }

    public void setSlaves(final int slaves) {
        this.slaves = slaves;
    }

    public int getInitialCrewStrike() {
        return initialCrewStrike;
    }

    public void setInitialCrewStrike(final int initialCrewStrike) {
        this.initialCrewStrike = initialCrewStrike;
    }

    public int getInitialCrewStrength() {
        return initialCrewStrength;
    }

    public void setInitialCrewStrength(final int initialCrewStrength) {
        this.initialCrewStrength = initialCrewStrength;
    }

}
