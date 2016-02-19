package hu.zagor.gamebooks.ff.ff.sa.character.handler.attribute;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.sa.character.Ff12Character;
import org.springframework.stereotype.Component;

/**
 * Attribute handler for FF12.
 * @author Tamas_Szekeres
 */
@Component
public class Ff12AttributeHandler extends FfAttributeHandler {

    @Override
    public void sanityCheck(final FfCharacter characterObject) {
        final Ff12Character character = (Ff12Character) characterObject;

        super.sanityCheck(character);

        if (character.getArmour() < 0) {
            character.setArmour(0);
        }
    }
}
