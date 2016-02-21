package hu.zagor.gamebooks.character.handler.item;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.character.item.EquipInfo;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
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
    @LogInject private Logger logger;

    @Override
    public void setItemFactory(final ItemFactory itemFactory) {
        Assert.notNull(itemFactory);
        logger.debug("Setting new item factory to DefaultCharacterItemHandler.");
        this.itemFactory = itemFactory;
    }

    @Override
    public int addItem(final Character character, final String itemId, final int amount) {
        Assert.notNull(character, CHARACTER_NOT_NULL);
        Assert.notNull(itemId, ITEMID_NOT_NULL);
        Assert.isTrue(amount > 0, ITEMID_POSITIVE);

        logger.debug("Resolving item {} for addition.", itemId);
        final Item resolvedItem = itemFactory.resolveItem(itemId);
        return addItem(character, resolvedItem, amount);
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
        double itemsInBackpack = 0;
        double itemSize = 0;

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
    public List<Item> removeItem(final Character character, final GatheredLostItem item) {
        return removeItem(character, item.getId(), item.getAmount(), item.isUnequippedOnly());
    }

    @Override
    public List<Item> removeItem(final Character character, final String itemId, final int amount) {
        return removeItem(character, itemId, amount, false);
    }

    private List<Item> removeItem(final Character character, final String itemId, final int amount, final boolean unequippedOnly) {
        Assert.notNull(character, CHARACTER_NOT_NULL);
        Assert.notNull(itemId, ITEMID_NOT_NULL);
        Assert.isTrue(amount > 0, ITEMID_POSITIVE);

        final List<Item> removedItems = new ArrayList<>();

        final List<Item> equipment = character.getEquipment();
        if (isItemList(itemId)) {
            final List<String> availableItems = collectAvailableItems(itemId, equipment, unequippedOnly);
            for (int i = 0; i < amount; i++) {
                if (availableItems.size() > 0) {
                    final String randomElement = getRandomElement(availableItems);
                    final Item item = getItem(equipment, randomElement);
                    equipment.remove(item);
                    removedItems.add(item);
                }
            }
        } else {
            for (int i = 0; i < amount; i++) {
                final Item itemToRemove = returnSinglePiece(itemId, equipment, unequippedOnly);
                if (itemToRemove != null) {
                    removedItems.add(itemToRemove);
                }
            }
        }

        return removedItems;
    }

    private Item returnSinglePiece(final String itemId, final List<Item> equipment, final boolean unequippedOnly) {
        Item item = getItem(equipment, itemId);
        if (item != null) {
            if (!unequippedOnly || !item.getEquipInfo().isEquippable() || !item.getEquipInfo().isEquipped()) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                    item = item.clone();
                    item.setAmount(1);
                } else {
                    equipment.remove(item);
                }
            }
        }
        return item;
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
        logger.debug("Resolving item {} for addition.", itemId);
        return itemFactory.resolveItem(itemId);
    }

    @Override
    public Item getItem(final Character character, final String itemId) {
        return getItem(character.getEquipment(), itemId);
    }

    @Override
    public List<Item> getItems(final Character character, final String itemId) {
        return getItems(character.getEquipment(), itemId);
    }

    /**
     * Gets an existing item from an equipment list based on itemId.
     * @param equipment the list of equipment
     * @param itemId the id of the item
     * @return the item
     */
    protected Item getItem(final List<Item> equipment, final String itemId) {
        final List<Item> items = getItems(equipment, itemId);
        return items.isEmpty() ? null : items.get(0);
    }

    /**
     * Gets a list of existing items from an equipment list based on itemId.
     * @param equipment the list of equipment
     * @param itemId the id of the item
     * @return the items
     */
    protected List<Item> getItems(final List<Item> equipment, final String itemId) {
        final List<Item> found = new ArrayList<>();
        final Iterator<Item> iterator = equipment.iterator();
        while (iterator.hasNext()) {
            final Item item = iterator.next();
            if (itemId.equals(item.getId())) {
                found.add(item);
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
        Assert.isTrue(amount > 0, "The parameter 'amount' must be greater than zero!");

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
