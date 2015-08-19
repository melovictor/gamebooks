package hu.zagor.gamebooks.character.handler.item;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.exception.InvalidItemException;

import java.util.Iterator;

/**
 * Interface for doing generic item-related queries in a {@link Character}.
 * @author Tamas_Szekeres
 */
public interface CharacterItemHandler {

    /**
     * Sets the {@link ItemFactory} instance to be used for this particular {@link Character}.
     * @param itemFactory the {@link ItemFactory} instance to use
     */
    void setItemFactory(final ItemFactory itemFactory);

    /**
     * Adds the equipment item with the given id to the character's stock.
     * @param character the {@link Character} on which we must act
     * @param itemId the id of the equipment to add, cannot be null
     * @param amount the amount of item to add, must be positive
     * @return the number of items successfully taken
     * @throws InvalidItemException occurs if the given item id cannot be resolved
     */
    int addItem(final Character character, final String itemId, final int amount);

    /**
     * Removes the given equipment from the character's stock.
     * @param character the {@link Character} on which we must act
     * @param itemId the id of the item to remove
     * @param amount the amount of the items to remove
     */
    void removeItem(final Character character, final String itemId, final int amount);

    /**
     * Removes the given equipment from the character's stock.
     * @param character the {@link Character} on which we must act
     * @param item the item to remove
     */
    void removeItem(final Character character, GatheredLostItem item);

    /**
     * Checks if one piece of the equipment item identified by the given id has been collected or not.
     * @param character the {@link Character} on which we must act
     * @param itemId the id of the equipment
     * @return true if the item has already been collected, false otherwise
     */
    boolean hasItem(final Character character, final String itemId);

    /**
     * Checks if the provided amount of the equipment item identified by the given id has been collected or not.
     * @param character the {@link Character} on which we must act
     * @param itemId the id of the equipment
     * @param amount the number of items to check
     * @return true if the item has already been collected, false otherwise
     */
    boolean hasItem(final Character character, final String itemId, int amount);

    /**
     * Checks if the equipment item identified by the given id has been collected and is equipped by the character or not.
     * @param character the {@link Character} on which we must act
     * @param id the id of the equipment, cannot be null
     * @return true if the item has been collected and is equipped by the character (meaning that it is either a non-equipable item (eg. a key) or an equipable item (eg. a sword)
     *         that has been equipped, false otherwise
     */
    boolean hasEquippedItem(final Character character, final String id);

    /**
     * Resolves a single item by its id.
     * @param itemId the id of the item to resolve
     * @return the resolved item, or null if the item cannot be found in the currently used {@link ItemFactory}
     */
    Item resolveItem(String itemId);

    /**
     * Returns the item with the specified id if the provided character has that item with himself.
     * @param character the {@link Character} on which we must act
     * @param itemId the id of the item to obtain
     * @return the item if the character has it, or null, if it doesn't have it
     */
    Item getItem(Character character, String itemId);

    /**
     * Returns an iterator that can go through all the items.
     * @param character the {@link Character} for whose items we want to have the iterator
     * @return the {@link Iterator} itself
     */
    Iterator<Item> getItemIterator(Character character);
}
