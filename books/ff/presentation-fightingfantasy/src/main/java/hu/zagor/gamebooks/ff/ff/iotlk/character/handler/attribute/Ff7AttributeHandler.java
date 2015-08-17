package hu.zagor.gamebooks.ff.ff.iotlk.character.handler.attribute;

import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * Attribute handler for FF7.
 * @author Tamas_Szekeres
 */
public class Ff7AttributeHandler extends FfAttributeHandler {

    private static final String LUCKY_AMULET_ID = "3005";
    private static final int LUCKY_AMULET_LUCK_LIMIT = 7;

    @Override
    public void sanityCheck(final FfCharacter character) {
        super.sanityCheck(character);

        if (hasLuckyAmulet(character)) {
            final int luck = resolveValue(character, "luck");
            if (luck < LUCKY_AMULET_LUCK_LIMIT) {
                character.changeLuck(LUCKY_AMULET_LUCK_LIMIT - luck);
            }
        }
    }

    private boolean hasLuckyAmulet(final FfCharacter character) {
        boolean hasAmulet = false;
        for (final Item item : character.getEquipment()) {
            hasAmulet |= LUCKY_AMULET_ID.equals(item.getId()) && item.getEquipInfo().isEquipped();
        }
        return hasAmulet;
    }
}
