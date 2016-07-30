package hu.zagor.gamebooks.content.command.changeitem;

import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.Arrays;
import java.util.List;

/**
 * Main bean for resolving an item altering command.
 * @author Tamas_Szekeres
 */
public class FfChangeItemCommandResolver extends AbstractChangeItemCommandResolver<FfCharacter, FfCharacterItemHandler, FfItem> {

    @Override
    protected List<Item> getItemsToChange(final ChangeItemCommand command, final FfCharacter character, final FfCharacterItemHandler itemHandler) {
        List<Item> itemsToChange;
        if ("equippedWeapon".equals(command.getId())) {
            itemsToChange = Arrays.asList((Item) itemHandler.getEquippedWeapon(character));
        } else {
            itemsToChange = itemHandler.getItems(character, command.getId());
        }
        return itemsToChange;
    }

}
