package hu.zagor.gamebooks.content.command.changeitem;

import hu.zagor.gamebooks.character.handler.item.LwCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.LwItem;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import java.util.Arrays;
import java.util.List;

/**
 * Main bean for resolving an item altering command.
 * @author Tamas_Szekeres
 */
public class LwChangeItemCommandResolver extends AbstractChangeItemCommandResolver<LwCharacter, LwCharacterItemHandler, LwItem> {

    @Override
    List<Item> getItemsToChange(final ChangeItemCommand command, final LwCharacter character, final LwCharacterItemHandler itemHandler) {
        List<Item> itemsToChange;
        if ("equippedWeapon".equals(command.getId())) {
            itemsToChange = Arrays.asList((Item) itemHandler.getEquippedWeapon(character));
        } else {
            itemsToChange = itemHandler.getItems(character, command.getId());
        }
        return itemsToChange;
    }

}
