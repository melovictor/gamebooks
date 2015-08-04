package hu.zagor.gamebooks.ff.ff.hoh.character.handler.attribute;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.hoh.character.Ff10Character;

/**
 * Attribute handler for FF10.
 * @author Tamas_Szekeres
 */
public class Ff10AttributeHandler extends FfAttributeHandler {

    @Override
    public boolean isAlive(final FfCharacter characterObject) {
        final Ff10Character character = (Ff10Character) characterObject;
        return notDeadByFear(character) && super.isAlive(character);
    }

    private boolean notDeadByFear(final Ff10Character character) {
        return character.getInitialFear() > character.getFear();
    }

    @Override
    public void sanityCheck(final FfCharacter characterObject) {
        final Ff10Character character = (Ff10Character) characterObject;

        super.sanityCheck(character);

        if (character.getFear() < 0) {
            character.setFear(0);
        }
    }
}
