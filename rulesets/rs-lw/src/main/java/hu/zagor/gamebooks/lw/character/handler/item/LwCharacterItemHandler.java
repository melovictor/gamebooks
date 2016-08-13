package hu.zagor.gamebooks.lw.character.handler.item;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.ComplexCharacterItemHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.Money;
import hu.zagor.gamebooks.lw.character.item.LwItem;
import hu.zagor.gamebooks.lw.character.item.Placement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

/**
 * Lone Wolf-related implementation for doing ruleset-specific item-related queries in a {@link Character}.
 * @author Tamas_Szekeres
 */
public class LwCharacterItemHandler extends ComplexCharacterItemHandler {
    @Resource(name = "lwMaxPlacementValues") private Map<Placement, Integer> maxPlacementValues;

    @Override
    public int addItem(final Character characterObject, final String itemId, final int amount) {
        final LwCharacter character = (LwCharacter) characterObject;

        int result = 0;
        if ("gold".equals(itemId)) {
            final Money money = character.getMoney();
            result = money.addGoldCrowns(amount);
        } else {
            final LwItem resolvedItem = (LwItem) getItemFactory().resolveItem(itemId);
            for (int i = 0; i < amount; i++) {
                if (resolvedItem.getPlacement() != Placement.backpack || hasBackpack(character)) {
                    final List<Item> items = getItems(character, resolvedItem.getPlacement());
                    if (hasPlaceInpackage(resolvedItem, items) && equipStateNotClashing(resolvedItem, items)) {
                        result += super.addItem(character, resolvedItem, 1);
                    }
                }
            }
        }

        return result;
    }

    private boolean equipStateNotClashing(final LwItem resolvedItem, final List<Item> items) {
        boolean clash = false;

        if (resolvedItem.getPlacement() == Placement.special) {
            final ItemType itemType = resolvedItem.getItemType();
            final int maxEquipped = itemType.getMaxEquipped();
            if (maxEquipped > 0) {
                int totalEquipped = 1;
                for (final Item item : items) {
                    if (item.getItemType() == itemType) {
                        totalEquipped++;
                    }
                }
                clash = totalEquipped > maxEquipped;
            }
        }

        return !clash;
    }

    private boolean hasBackpack(final LwCharacter character) {
        return hasItem(character, "40000");
    }

    private boolean hasPlaceInpackage(final LwItem resolvedItem, final List<Item> items) {
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

    /**
     * Retrives either the selected weapon, or the first non-default weapon it finds with the character, or the default weapon.
     * @param character the {@link LwCharacter} whose currently selected weapon must be obtained.
     * @return the currently selected weapon, marked as equipped
     */
    public LwItem getEquippedWeapon(final LwCharacter character) {
        LwItem defWpn = null;
        LwItem selWpn = null;

        for (final Item itemObject : character.getEquipment()) {
            final LwItem item = (LwItem) itemObject;
            if (item.getPlacement() == Placement.weapon) {
                if ("defWpn".equals(item.getId())) {
                    defWpn = item;
                } else if (item.getEquipInfo().isEquipped()) {
                    selWpn = item;
                    break;
                } else if (selWpn == null) {
                    selWpn = item;
                }
            }
        }

        if (selWpn == null) {
            selWpn = defWpn;
        }
        if (selWpn != null) {
            selWpn.getEquipInfo().setEquipped(true);
        }
        return selWpn;
    }

    @Override
    public void setItemEquipState(final Character character, final String itemId, final boolean toEquip) {
        super.setItemEquipState(character, itemId, toEquip);
        doEquipSanityCheck(character.getEquipment(), getItem(character, itemId));
        getEquippedWeapon((LwCharacter) character);
    }

}
