package hu.zagor.gamebooks.character.handler.item;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.character.item.EquipInfo;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Interface for doing generic item-related queries in a {@link Character}.
 * @author Tamas_Szekeres
 */
public class DefaultCharacterItemHandler implements CharacterItemHandler {

    private static final String ID_NOT_NULL = "The parameter 'id' cannot be null!";
    private static final String ITEMID_POSITIVE = "The parameter 'amount' must be positive!";
    private static final String ITEMID_NOT_NULL = "The parameter 'itemId' cannot be null!";
    private static final String CHARACTER_NOT_NULL = "The parameter 'character' cannot be null!";
    /**
     * The item factory instance to use for item instance creation.
     */
    private ItemFactory itemFactory;

    @Override
    public void setItemFactory(final ItemFactory itemFactory) {
        Assert.notNull(itemFactory);
        this.itemFactory = itemFactory;
    }

    @Override
    public int addItem(final Character character, final String itemId, final int amount) {
        Assert.notNull(character, CHARACTER_NOT_NULL);
        Assert.notNull(itemId, ITEMID_NOT_NULL);
        Assert.isTrue(amount > 0, ITEMID_POSITIVE);

        return addItem(character, itemFactory.resolveItem(itemId), amount);
    }

    /**
     * Adds the equipment item with the given id to the character's stock.
     * @param character the {@link Character} on which we must act
     * @param item the equipment to add, cannot be null
     * @param amount the amount of item to add, must be positive
     * @return the number of items successfully taken
     */
    protected int addItem(final Character character, final Item item, final int amount) {
        int totalAdded = 0;
        for (int i = 0; i < amount; i++) {
            if (canAddItem(character, item)) {
                character.getEquipment().add(item);
                totalAdded++;
            }
        }
        return totalAdded;
    }

    private boolean canAddItem(final Character character, final Item item) {
        final int backpackSize = character.getBackpackSize();
        int itemsInBackpack = 0;
        int itemSize = 0;

        if (itemIsInBackpack(item)) {
            for (final Item ownedItem : character.getEquipment()) {
                if (itemIsInBackpack(ownedItem)) {
                    itemsInBackpack += ownedItem.getBackpackSize() * ownedItem.getAmount();
                }
            }
            itemSize = item.getBackpackSize() * item.getAmount();
        }

        return backpackSize >= itemsInBackpack + itemSize;
    }

    private boolean itemIsInBackpack(final Item item) {
        final EquipInfo equipInfo = item.getEquipInfo();
        return !equipInfo.isEquippable() || !equipInfo.isEquipped();
    }

    @Override
    public void removeItem(final Character character, final GatheredLostItem item) {
        removeItem(character, item.getId(), item.getAmount(), item.isUnequippedOnly());
    }

    @Override
    public void removeItem(final Character character, final String itemId, final int amount) {
        removeItem(character, itemId, amount, false);
    }

    private void removeItem(final Character character, final String itemId, final int amount, final boolean unequippedOnly) {
        Assert.notNull(character, CHARACTER_NOT_NULL);
        Assert.notNull(itemId, ITEMID_NOT_NULL);
        Assert.isTrue(amount > 0, ITEMID_POSITIVE);

        final List<Item> equipment = character.getEquipment();
        if (isItemList(itemId)) {
            final List<String> availableItems = collectAvailableItems(itemId, equipment, unequippedOnly);
            for (int i = 0; i < amount; i++) {
                if (availableItems.size() > 0) {
                    final String randomElement = getRandomElement(availableItems);
                    equipment.remove(getItem(equipment, randomElement));
                }
            }
        } else {
            for (int i = 0; i < amount; i++) {
                returnSinglePiece(itemId, equipment, unequippedOnly);
            }
        }
    }

    private void returnSinglePiece(final String itemId, final List<Item> equipment, final boolean unequippedOnly) {
        final Item item = getItem(equipment, itemId);
        if (item != null) {
            if (!unequippedOnly || !item.getEquipInfo().isEquippable() || !item.getEquipInfo().isEquipped()) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    equipment.remove(item);
                }
            }
        }
    }

    private String getRandomElement(final List<String> availableItems) {
        final int rnd = (int) Math.floor(availableItems.size() * Math.random());
        return availableItems.remove(rnd);
    }

    private List<String> collectAvailableItems(final String itemId, final List<Item> equipment, final boolean unequippedOnly) {
        final List<String> availableItems = new ArrayList<String>();

        final List<String> itemsToLookFor = Arrays.asList(itemId.split(","));
        for (final Item item : equipment) {
            final String equipmentId = item.getId();
            if (itemsToLookFor.contains(equipmentId) && (!unequippedOnly || !item.getEquipInfo().isEquippable() || !item.getEquipInfo().isEquipped())) {
                availableItems.add(equipmentId);
            }
        }

        return availableItems;
    }

    private boolean isItemList(final String itemId) {
        return itemId.contains(",");
    }

    @Override
    public Item resolveItem(final String itemId) {
        return itemFactory.resolveItem(itemId);
    }

    @Override
    public Item getItem(final Character character, final String itemId) {
        return getItem(character.getEquipment(), itemId);
    }

    /**
     * Gets an existing item from an equipment list based on itemId.
     * @param equipment the list of equipment
     * @param itemId the id of the item
     * @return the item
     */
    protected Item getItem(final List<Item> equipment, final String itemId) {
        Item found = null;
        final Iterator<Item> iterator = equipment.iterator();
        while (iterator.hasNext()) {
            final Item item = iterator.next();
            if (itemId.equals(item.getId())) {
                found = item;
                break;
            }
        }
        return found;
    }

    @Override
    public boolean hasEquippedItem(final Character character, final String id) {
        Assert.notNull(character, CHARACTER_NOT_NULL);
        Assert.notNull(id, ID_NOT_NULL);

        final Item item = getItem(character.getEquipment(), id);
        return item != null && item.getEquipInfo().isEquippable() && item.getEquipInfo().isEquipped();
    }

    @Override
    public boolean hasItem(final Character character, final String id) {
        return hasItem(character, id, 1);
    }

    @Override
    public boolean hasItem(final Character character, final String id, final int amount) {
        Assert.notNull(character, CHARACTER_NOT_NULL);
        Assert.notNull(id, ID_NOT_NULL);
        Assert.isTrue(amount > 0, "The parameter 'id' must be greater than zero!");

        final List<Item> equipment = character.getEquipment();
        int possessedAmount = 0;
        final Iterator<Item> iterator = equipment.iterator();
        while (iterator.hasNext()) {
            final Item item = iterator.next();
            if (id.equals(item.getId())) {
                possessedAmount += item.getAmount();
            }
        }
        return possessedAmount >= amount;
    }

    @Override
    public Iterator<Item> getItemIterator(final Character character) {
        return character.getEquipment().iterator();
    }

}
