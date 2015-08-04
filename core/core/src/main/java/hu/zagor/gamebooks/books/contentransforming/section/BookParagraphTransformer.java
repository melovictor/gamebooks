package hu.zagor.gamebooks.books.contentransforming.section;

import hu.zagor.gamebooks.content.Paragraph;

import java.util.Map;

import org.w3c.dom.Document;

/**
 * Interface for transforming {@link Document} objects into book content beans.
 * @author Tamas_Szekeres
 */
public interface BookParagraphTransformer {

    /**
     * Transforms a {@link Document} containing paragraph info into a map of {@link Paragraph} objects.
     * @param document the input data, cannot be null
     * @return the transformed data
     * @throws XmlTransformationException when there is a problem during the parsing of the {@link Document}
     */
    Map<String, Paragraph> transformParagraphs(Document document) throws XmlTransformationException;
}
