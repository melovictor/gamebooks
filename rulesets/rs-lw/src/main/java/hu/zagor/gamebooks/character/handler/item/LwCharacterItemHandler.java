package hu.zagor.gamebooks.character.handler.item;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.LwItem;
import hu.zagor.gamebooks.character.item.Placement;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.Money;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

/**
 * Lone Wolf-related implementation for doing ruleset-specific item-related queries in a {@link Character}.
 * @author Tamas_Szekeres
 */
public class LwCharacterItemHandler extends DefaultCharacterItemHandler {
    @Resource(name = "lwMaxPlacementValues") private Map<Placement, Integer> maxPlacementValues;

    @Override
    public int addItem(final Character characterObject, final String itemId, final int amount) {
        final LwCharacter character = (LwCharacter) characterObject;

        final int result;
        if ("gold".equals(itemId)) {
            final Money money = character.getMoney();
            result = money.addGoldCrowns(amount);
        } else {
            final LwItem resolvedItem = (LwItem) getItemFactory().resolveItem(itemId);
            final List<Item> items = getItems(character, resolvedItem.getPlacement());
            if (canTakeItem(resolvedItem, items)) {
                result = super.addItem(character, itemId, amount);
            } else {
                result = 0;
            }
        }

        return result;
    }

    private boolean canTakeItem(final LwItem resolvedItem, final List<Item> items) {
        boolean canTake;
        final String itemId = resolvedItem.getId();
        if ("defWpn".equals(itemId)) {
            canTake = true;
            for (final Item item : items) {
                if (itemId.equals(item.getId())) {
                    canTake = false;
                }
            }
        } else {
            int totalSize = (int) (resolvedItem.getAmount() * resolvedItem.getBackpackSize());
            for (final Item item : items) {
                totalSize += item.getAmount() * item.getBackpackSize();
            }
            canTake = totalSize <= maxPlacementValues.get(resolvedItem.getPlacement());
        }
        return canTake;
    }

    private List<Item> getItems(final LwCharacter character, final Placement placement) {
        final List<Item> items = new ArrayList<>();
        for (final Item item : character.getEquipment()) {
            if (((LwItem) item).getPlacement() == placement) {
                items.add(item);
            }
        }
        return items;
    }

    public void setMaxPlacementValues(final Map<Placement, Integer> maxPlacementValues) {
        this.maxPlacementValues = maxPlacementValues;
    }

}
