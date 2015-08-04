package hu.zagor.gamebooks.books.contentransforming.item;

import hu.zagor.gamebooks.books.contentransforming.AbstractTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.character.item.WeaponSubType;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Abstract transformer for items that contains common parts of the transforming process.
 * @author Tamas_Szekeres
 */
public abstract class AbstractBookItemTransformer extends AbstractTransformer implements BookItemTransformer {

    @LogInject
    private Logger logger;

    @Override
    public Map<String, Item> transformItems(final Document document) throws XmlTransformationException {
        Assert.notNull(document, "Parameter 'document' can not be null!");
        final Map<String, Item> contents = new HashMap<>();

        final NodeList childNodes = document.getChildNodes();
        if (childNodes.getLength() > 0) {
            parseItems(childNodes.item(0), contents);
        } else {
            logger.error("Couldn't find 'items' element in xml file!");
            throw new XmlTransformationException("Missing element 'items'.");
        }

        return contents;
    }

    private void parseItems(final Node node, final Map<String, Item> contents) {
        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        for (int i = 0; i < count; i++) {
            final Node itemNode = childNodes.item(i);
            if (isItem(itemNode)) {
                final Item item = parseItem(itemNode);
                if (item != null) {
                    final String id = item.getId();
                    if (contents.containsKey(id)) {
                        throw new IllegalStateException("Book contains at least two instances of the item id '" + id + "'!");
                    }
                    contents.put(id, item);
                }
            }
        }
    }

    private Item parseItem(final Node node) {
        Item createdItem = null;
        final String id = extractAttribute(node, "id");
        final String name = extractAttribute(node, "name");
        final String type = extractAttribute(node, "type");
        final String subType = extractAttribute(node, "weaponSubType");
        final int backpackSize = extractIntegerAttribute(node, "backpackSize", 1);
        ItemType itemType;
        WeaponSubType weaponSubType = null;
        try {
            itemType = ItemType.valueOf(type);
            createdItem = getItem(id, name, itemType);
            if (subType != null) {
                weaponSubType = WeaponSubType.valueOf(subType);
                createdItem.setSubType(weaponSubType);
            }
            if (itemType == ItemType.shadow) {
                createdItem.setBackpackSize(0);
            } else {
                createdItem.setBackpackSize(backpackSize);
            }
            finishItemCreation(createdItem, node);
        } catch (final IllegalArgumentException exception) {
            logger.error("Found item '{}/{}' with unparseable type '{}'!", name, id, type);
        }
        return createdItem;
    }

    private boolean isItem(final Node childNode) {
        return "item".equals(childNode.getNodeName());
    }

    /**
     * Returns an instance of an {@link Item} bean or one of its descendant that the transformer will have to fill with the data.
     * @param id the id of the item
     * @param name the displayed name of the item
     * @param itemType the type of the item
     * @return the instance
     */
    protected abstract Item getItem(String id, String name, ItemType itemType);

    /**
     * Method that will be called after the default attributes has already been read and parsed.
     * @param item the half-finished {@link Item} object
     * @param node the {@link Node}
     */
    protected abstract void finishItemCreation(Item item, Node node);
}
