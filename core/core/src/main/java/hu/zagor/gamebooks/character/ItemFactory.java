package hu.zagor.gamebooks.character;

import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.exception.InvalidItemException;

/**
 * Interface for classes that are capable of resolving the id of a given item and return a new {@link Item}
 * instance of them.
 * @author Tamas_Szekeres
 */
public interface ItemFactory {

    /**
     * Resolves the given id into an {@link Item} bean.
     * @param itemId the id to resolve
     * @return the resolved {@link Item}
     * @throws InvalidItemException when the item id cannot be resolved to a proper item
     */
    Item resolveItem(final String itemId);

}
