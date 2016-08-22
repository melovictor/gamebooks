package hu.zagor.gamebooks.ff.ff.bnc.character.handler.attribute;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.bnc.character.Ff25Character;

/**
 * Attribute handler for FF25.
 * @author Tamas_Szekeres
 */
public class Ff25AttributeHandler extends FfAttributeHandler {
    @Override
    public void sanityCheck(final FfCharacter characterObject) {
        final Ff25Character character = (Ff25Character) characterObject;

        super.sanityCheck(character);

        if (character.getWillpower() < 0) {
            character.setWillpower(0);
        }
        if (character.getWillpower() > character.getInitialWillpower()) {
            character.setWillpower(character.getInitialWillpower());
        }
    }
}
