package hu.zagor.gamebooks.ff.ff.sob.character.handler.attribute;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.sob.character.Ff16Character;

/**
 * Attribute handler for FF16.
 * @author Tamas_Szekeres
 */
public class Ff16AttributeHandler extends FfAttributeHandler {
    @Override
    public void sanityCheck(final FfCharacter characterObject) {
        super.sanityCheck(characterObject);
        final Ff16Character character = (Ff16Character) characterObject;
        if (character.getCrewStrength() > character.getInitialCrewStrength()) {
            character.setCrewStrength(character.getInitialCrewStrength());
        }
        if (character.getCrewStrike() > character.getInitialCrewStrike()) {
            character.setCrewStrike(character.getInitialCrewStrike());
        }
    }

    @Override
    public boolean isAlive(final FfCharacter character) {
        return super.isAlive(character) && crewAlive((Ff16Character) character);
    }

    private boolean crewAlive(final Ff16Character character) {
        return character.getCrewStrength() > 0;
    }
}
