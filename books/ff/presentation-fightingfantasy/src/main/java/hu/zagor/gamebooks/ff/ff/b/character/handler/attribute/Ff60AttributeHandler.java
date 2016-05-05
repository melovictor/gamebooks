package hu.zagor.gamebooks.ff.ff.b.character.handler.attribute;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.b.character.Ff60Character;
import org.springframework.stereotype.Component;

/**
 * Attribute handler for FF60.
 * @author Tamas_Szekeres
 */
@Component
public class Ff60AttributeHandler extends FfAttributeHandler {
    @Override
    public void sanityCheck(final FfCharacter character) {
        super.sanityCheck(character);

        if (character instanceof Ff60Character) {
            final Ff60Character chr = (Ff60Character) character;
            if (chr.getArrowScore() < 0) {
                chr.setArrowScore(0);
            }
        }
    }
}
