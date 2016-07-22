package hu.zagor.gamebooks.ff.ff.ss.character.handler.item;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.ff.ff.ss.character.Ff8Character;

/**
 * Character item handler for FF8.
 * @author Tamas_Szekeres
 */
public class Ff8CharacterItemHandler extends FfCharacterItemHandler {

    @Override
    public int addItem(final Character characterObject, final String itemId, final int amount) {
        int addedItems;
        if (itemId != null && itemId.startsWith("maps-")) {
            final String mapId = itemId.split("\\-")[1];
            final Ff8Character character = (Ff8Character) characterObject;
            character.getMaps().add(mapId);
            addedItems = 1;
        } else {
            addedItems = super.addItem(characterObject, itemId, amount);
        }

        return addedItems;
    }
}
