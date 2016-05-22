package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * Class for resolving an attribute-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckGoldCommandResolver extends ItemCheckFfAttributeCommandResolver {

    @Override
    protected int getAttributeValue(final FfCharacter character, final FfCharacterHandler characterHandler, final String attribute) {
        return character.getGold() + getValuableValues(character);
    }

    private int getValuableValues(final FfCharacter character) {
        int valuable = 0;

        for (final Item itemObject : character.getEquipment()) {
            final FfItem item = (FfItem) itemObject;
            if (item.getItemType() == ItemType.valuable) {
                valuable += item.getGold();
            }
        }

        return valuable;
    }
}
