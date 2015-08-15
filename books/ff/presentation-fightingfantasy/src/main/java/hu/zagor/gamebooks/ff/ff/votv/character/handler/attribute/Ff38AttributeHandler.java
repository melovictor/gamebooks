package hu.zagor.gamebooks.ff.ff.votv.character.handler.attribute;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.votv.character.Ff38Character;

/**
 * Attribute handler for FF38.
 * @author Tamas_Szekeres
 */

public class Ff38AttributeHandler extends FfAttributeHandler {

    @Override
    public void sanityCheck(final FfCharacter characterObject) {
        super.sanityCheck(characterObject);

        if (characterObject instanceof Ff38Character) {
            final Ff38Character character = (Ff38Character) characterObject;

            if (character.getFaith() < 0) {
                character.setFaith(0);
            }
        }
    }
}
