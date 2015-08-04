package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * Attribute handler class specifically for the Wm5 book.
 * @author Tamas_Szekeres
 */
public class Wm5AttributeHandler extends FfAttributeHandler {

    private static final int LUCK_LIMIT = 6;

    @Override
    public void sanityCheck(final FfCharacter character) {
        super.sanityCheck(character);
        ensureLowerLuckLimit(character);
    }

    @Override
    public void handleModification(final FfCharacter character, final String attribute, final int amount) {
        super.handleModification(character, attribute, amount);
        ensureLowerLuckLimit(character);
    }

    private void ensureLowerLuckLimit(final FfCharacter character) {
        if (character.getParagraphs().contains("49")) {
            final int luck = resolveValue(character, "luck");
            if (luck < LUCK_LIMIT) {
                character.changeLuck(LUCK_LIMIT - luck);
            }
        }
    }

}
