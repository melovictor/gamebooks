package hu.zagor.gamebooks.ff.ff.sob.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data object for FF16.
 * @author Tamas_Szekeres
 */
@Component("ff16CharacterPageData")
@Scope("prototype")
public class Ff16CharacterPageData extends FfCharacterPageData {
    private final int crewStrike;
    private final int crewStrength;
    private final int initialCrewStrike;
    private final int initialCrewStrength;
    private final int time;
    private final int slave;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy 16 book.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff16CharacterPageData(final Ff16Character character, final FfCharacterHandler handler) {
        super(character, handler);

        crewStrike = character.getCrewStrike();
        crewStrength = character.getCrewStrength();
        initialCrewStrike = character.getInitialCrewStrike();
        initialCrewStrength = character.getInitialCrewStrength();
        time = character.getTime();
        slave = character.getSlave();
    }

    public int getCrewStrike() {
        return crewStrike;
    }

    public int getCrewStrength() {
        return crewStrength;
    }

    public int getTime() {
        return time;
    }

    public int getSlave() {
        return slave;
    }

    public int getInitialCrewStrike() {
        return initialCrewStrike;
    }

    public int getInitialCrewStrength() {
        return initialCrewStrength;
    }

}
