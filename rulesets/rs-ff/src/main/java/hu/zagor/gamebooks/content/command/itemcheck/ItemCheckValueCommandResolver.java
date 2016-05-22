package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.Iterator;

/**
 * Class for resolving a value-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckValueCommandResolver extends ItemCheckFfAttributeCommandResolver {

    @Override
    protected int getAttributeValue(final FfCharacter character, final FfCharacterHandler characterHandler, final String attribute) {
        return super.getAttributeValue(character, characterHandler, "gold") + getItemsValue(character, characterHandler);
    }

    private int getItemsValue(final FfCharacter character, final FfCharacterHandler characterHandler) {
        int totalValue = 0;
        final Iterator<Item> itemIterator = characterHandler.getItemHandler().getItemIterator(character);
        while (itemIterator.hasNext()) {
            final FfItem item = (FfItem) itemIterator.next();
            totalValue += item.getPrice() * item.getAmount();
        }
        return totalValue;
    }
}
