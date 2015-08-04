package hu.zagor.gamebooks.character.handler.item;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.item.EquipInfo;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Interface for doing generic item-related queries in a {@link Character}.
 * @author Tamas_Szekeres
 */
public class FfCharacterItemHandler extends DefaultCharacterItemHandler {

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;

    @Override
    public void removeItem(final Character character, final String itemId, final int amount) {
        super.removeItem(character, itemId, amount);
        getEquippedWeapon((FfCharacter) character);
    }

    @Override
    public int addItem(final Character characterObject, final String itemId, final int amount) {
        final FfCharacter character = (FfCharacter) characterObject;

        final int result;
        if ("gold".equals(itemId)) {
            character.setGold(character.getGold() + amount);
            result = amount;
        } else {
            result = super.addItem(character, itemId, amount);
        }

        final List<Item> equipment = character.getEquipment();
        final Item item = getItem(equipment, itemId);
        if (item != null) {
            doEquipSanityCheck(equipment, item);
        }

        return result;
    }

    @Override
    protected int addItem(final Character characterObject, final Item itemObject, final int amount) {
        if (itemObject.getItemType() == ItemType.immediate) {
            final FfItem item = (FfItem) itemObject;
            final FfCharacter character = (FfCharacter) characterObject;
            final int stamina = item.getStamina();
            final int skill = item.getSkill();
            final int luck = item.getLuck();
            character.changeLuck(luck);
            character.changeSkill(skill);
            character.changeStamina(stamina);
        }
        return super.addItem(characterObject, itemObject, amount);
    }

    /**
     * Returns the weapon currently selected by the character.
     * @param character the {@link FfCharacter} whose weapon we want to get
     * @return the equipped weapon
     */
    public FfItem getEquippedWeapon(final FfCharacter character) {
        return getEquippedWeapon(character, Arrays.asList(new ItemType[]{ItemType.weapon1, ItemType.weapon2}));
    }

    /**
     * Returns the weapon currently selected by the character.
     * @param character the {@link FfCharacter} whose weapon we want to get
     * @param validWeaponTypes the type of the weapons for which we're looking among the equipped ones
     * @return the equipped weapon
     */
    public FfItem getEquippedWeapon(final FfCharacter character, final List<ItemType> validWeaponTypes) {
        FfItem equippedWeapon = null;
        FfItem firstWeapon = null;
        FfItem defaultWeapon = null;
        for (final Item itemObject : character.getEquipment()) {
            final FfItem item = (FfItem) itemObject;
            if (validWeaponTypes.contains(item.getItemType())) {
                if (defaultWeapon(item)) {
                    defaultWeapon = item;
                } else if (isEquipped(item)) {
                    equippedWeapon = item;
                } else if (firstWeapon == null) {
                    firstWeapon = item;
                }
            }
        }
        final FfItem actualEquippedWeapon = equippedWeapon != null ? equippedWeapon : firstWeapon == null ? defaultWeapon : firstWeapon;
        if (actualEquippedWeapon != null) {
            actualEquippedWeapon.getEquipInfo().setEquipped(true);
        }
        return actualEquippedWeapon;
    }

    /**
     * Changes the equipped state of an item.
     * @param character the {@link Character} owning the item
     * @param itemId the id of the item
     * @param toEquip the new equip state; true if it has to be equipped, false if it has to be removed
     */
    public void setItemEquipState(final Character character, final String itemId, final boolean toEquip) {
        for (final Item item : character.getEquipment()) {
            final EquipInfo equipInfo = item.getEquipInfo();
            if (itemId.equals(item.getId()) && equipInfo.isEquipped() != toEquip && (equipInfo.isRemovable() || toEquip)) {
                equipInfo.setEquipped(toEquip);
                doEquipSanityCheck(character.getEquipment(), item);
                break;
            }
        }
        getEquippedWeapon((FfCharacter) character);
    }

    private void doEquipSanityCheck(final List<Item> equipment, final Item newItem) {
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

    private boolean defaultWeapon(final FfItem item) {
        return "defWpn".equals(item.getId());
    }

    private boolean isEquipped(final FfItem item) {
        return item.getEquipInfo().isEquipped();
    }

    /**
     * Consumes an item.
     * @param character the {@link Character} owning the item
     * @param itemId the id of the item to consume
     * @param attributeHandler the {@link FfAttributeHandler} object
     */

    public void consumeItem(final FfCharacter character, final String itemId, final FfAttributeHandler attributeHandler) {
        final FfItem item = (FfItem) getItem(character.getEquipment(), itemId);
        if (item != null && item.getEquipInfo().isConsumable()) {
            if (item.getItemType() == ItemType.provision) {
                applyConsumableChanges(character, item);
                removeItem(character, itemId, 1);
            } else if (item.getItemType() == ItemType.potion) {
                applyConsumableChanges(character, item);
                if (item.getDose() == 1) {
                    removeItem(character, itemId, 1);
                } else {
                    item.setDose(item.getDose() - 1);
                }
            } else {
                throw new IllegalArgumentException("Don't know how to consume item " + item.getId() + " of type " + item.getItemType() + ".");
            }
        }
        attributeHandler.sanityCheck(character);
    }

    private void applyConsumableChanges(final FfCharacter character, final FfItem item) {
        character.changeStamina(item.getStamina());
        character.changeSkill(item.getSkill());
        character.changeLuck(item.getLuck());

        if (item.getStaminaNd6() > 0) {
            character.changeStamina(generator.getRandomNumber(item.getStaminaNd6())[0]);
        }

        character.setInitialStamina(character.getInitialStamina() + item.getInitialStamina());
        character.setInitialSkill(character.getInitialSkill() + item.getInitialSkill());
        character.setInitialLuck(character.getInitialLuck() + item.getInitialLuck());
    }

}
