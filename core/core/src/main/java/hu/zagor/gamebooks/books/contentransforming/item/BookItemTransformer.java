package hu.zagor.gamebooks.books.contentransforming.item;

import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.character.item.Item;

import java.util.Map;

import org.w3c.dom.Document;

/**
 * Interface for transforming {@link Document} objects into book item beans.
 * @author Tamas_Szekeres
 *
 */
public interface BookItemTransformer {
    /**
     * Transforms a {@link Document} containing item info into a map of {@link Item} objects.
     * @param document the input data, cannot be null
     * @return the transformed data
     * @throws XmlTransformationException when there is a problem during the parsing of the {@link Document}
     */
    Map<String, Item> transformItems(Document document) throws XmlTransformationException;
}
