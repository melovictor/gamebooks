package hu.zagor.gamebooks.character.handler.item;

import hu.zagor.gamebooks.character.item.EquipInfo;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Complext item handler with some extra checks for equipped weapons and item categories.
 * @author Tamas_Szekeres
 */
public abstract class ComplexCharacterItemHandler extends DefaultCharacterItemHandler {

    /**
     * Executes an equipment sanity check where it removes conflicting item types, eg. only a single weapon-like item can be equipped at one time.
     * @param equipment the equipment list
     * @param newItem the item whose state has been newly changed
     */
    protected void doEquipSanityCheck(final List<Item> equipment, final Item newItem) {
        if (itemEquippableAndEquipped(newItem)) {
            if (noConflictingItemsArePresent(equipment, newItem)) {
                assureNoItemOverflowsArePresent(equipment, newItem);
            } else {
                newItem.getEquipInfo().setEquipped(false);
            }
        }
    }

    private boolean itemEquippableAndEquipped(final Item item) {
        return item.getEquipInfo().isEquippable() && item.getEquipInfo().isEquipped();
    }

    private boolean noConflictingItemsArePresent(final List<Item> equipment, final Item newItem) {
        final Set<ItemType> disallowedTypes = new HashSet<ItemType>();
        Collections.addAll(disallowedTypes, newItem.getItemType().getDisallows());
        boolean canRemoveDisalloweds = false;
        if (canRemoveAllDisalloweds(equipment, disallowedTypes)) {
            removeAllDisallowedTypes(equipment, disallowedTypes);
            canRemoveDisalloweds = true;
        }
        return canRemoveDisalloweds;
    }

    private boolean canRemoveAllDisalloweds(final List<Item> equipment, final Set<ItemType> disallowedTypes) {
        boolean hasStuckItem = false;
        for (final Item item : equipment) {
            final EquipInfo equipInfo = item.getEquipInfo();
            if (disallowedTypes.contains(item.getItemType()) && equipInfo.isEquipped()) {
                hasStuckItem |= !equipInfo.isRemovable();
            }
        }
        return !hasStuckItem;
    }

    private void removeAllDisallowedTypes(final List<Item> equipment, final Set<ItemType> disallowedTypes) {
        for (final Item item : equipment) {
            final EquipInfo equipInfo = item.getEquipInfo();
            if (disallowedTypes.contains(item.getItemType()) && equipInfo.isEquipped()) {
                equipInfo.setEquipped(false);
            }
        }
    }

    private void assureNoItemOverflowsArePresent(final List<Item> equipment, final Item newItem) {
        int totalEquipped = 1;
        final int nonRemovables = countNonRemovable(equipment, newItem);
        totalEquipped += nonRemovables;

        final int maxEquippable = newItem.getItemType().getMaxEquipped();
        if (nonRemovables >= maxEquippable) {
            newItem.getEquipInfo().setEquipped(false);
        } else {
            removeOverflowingItems(equipment, newItem, totalEquipped, maxEquippable);
        }
    }

    private void removeOverflowingItems(final List<Item> equipment, final Item newItem, final int equipped, final int maxEquippable) {
        int totalEquipped = equipped;
        final ItemType type = newItem.getItemType();
        for (final Item item : equipment) {
            if (item != newItem && item.getItemType() == type) {
                final EquipInfo ownItemEquipInfo = item.getEquipInfo();
                if (ownItemEquipInfo.isEquipped() && ownItemEquipInfo.isRemovable()) {
                    if (totalEquipped < maxEquippable) {
                        totalEquipped++;
                    } else {
                        ownItemEquipInfo.setEquipped(false);
                    }
                }
            }
        }
    }

    private int countNonRemovable(final List<Item> equipment, final Item newItem) {
        int totalNonRemovable = 0;
        final ItemType targetType = newItem.getItemType();
        for (final Item item : equipment) {
            final EquipInfo equipInfo = item.getEquipInfo();
            if (item != newItem && item.getItemType() == targetType && equipInfo.isEquipped() && !equipInfo.isRemovable()) {
                totalNonRemovable++;
            }
        }
        return totalNonRemovable;
    }

}
