package hu.zagor.gamebooks.character.handler.item;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.Money;
import java.util.ArrayList;
import java.util.List;

/**
 * Lone Wolf-related implementation for doing ruleset-specific item-related queries in a {@link Character}.
 * @author Tamas_Szekeres
 */
public class LwCharacterItemHandler extends DefaultCharacterItemHandler {
    @Override
    public int addItem(final Character characterObject, final String itemId, final int amount) {
        final LwCharacter character = (LwCharacter) characterObject;

        final int result;
        if ("gold".equals(itemId)) {
            final Money money = character.getMoney();
            result = money.addGoldCrowns(amount);
        } else {
            final Item resolvedItem = getItemFactory().resolveItem(itemId);
            final List<Item> items = getItems(character, resolvedItem.getItemType());
            if (items.size() <= resolvedItem.getItemType().getMaxEquipped()) {
                result = super.addItem(character, itemId, amount);
            } else {
                result = 0;
            }
        }

        return result;
    }

    private List<Item> getItems(final LwCharacter character, final ItemType itemType) {
        final List<Item> items = new ArrayList<>();
        for (final Item item : character.getEquipment()) {
            if (item.getItemType() == itemType) {
                items.add(item);
            }
        }
        return items;
    }

}
