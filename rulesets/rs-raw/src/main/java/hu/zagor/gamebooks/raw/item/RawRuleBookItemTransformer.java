package hu.zagor.gamebooks.raw.item;

import hu.zagor.gamebooks.books.contentransforming.item.AbstractBookItemTransformer;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;

import org.w3c.dom.Node;

/**
 * Implementation of the {@link AbstractBookItemTransformer} for the raw ruleset.
 * @author Tamas_Szekeres
 */
public class RawRuleBookItemTransformer extends AbstractBookItemTransformer {

    @Override
    protected Item getItem(final String id, final String name, final ItemType itemType) {
        return new Item(id, name, itemType);
    }

    @Override
    protected void finishItemCreation(final Item item, final Node node) {
    }

}
