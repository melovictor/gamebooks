package hu.zagor.gamebooks.ff.ff.sob.character.handler.item;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.ff.ff.sob.character.Ff16Character;

/**
 * Item handler for ff16.
 * @author Tamas_Szekeres
 */
public class Ff16CharacterItemHandler extends FfCharacterItemHandler {
    @Override
    public int addItem(final Character characterObject, final String itemId, final int amount) {
        int addedItem;
        if ("slave".equals(itemId)) {
            addedItem = amount;
            final Ff16Character character = (Ff16Character) characterObject;
            character.setSlaves(character.getSlaves() + amount);
        } else {
            addedItem = super.addItem(characterObject, itemId, amount);
        }
        return addedItem;
    }
}
