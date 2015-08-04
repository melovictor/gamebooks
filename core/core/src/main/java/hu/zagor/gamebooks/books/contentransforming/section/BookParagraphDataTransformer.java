package hu.zagor.gamebooks.books.contentransforming.section;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;

import org.w3c.dom.Node;

/**
 * Interface for parsing paragraph data into domain beans.
 * @author Tamas_Szekeres
 */
public interface BookParagraphDataTransformer {

    /**
     * Parses the data from a paragraph block. This can be the content of a <p> tag or a <have> in an
     * itemcheck.
     * @param positionCounter the position counter that belongs to the parent {@link Paragraph} object, cannot
     * be null
     * @param node the {@link Node} containing the data, cannot be null
     * @return a {@link ParagraphData} bean with the parsed info
     */
    ParagraphData parseParagraphData(final ChoicePositionCounter positionCounter, final Node node);

    /**
     * Gets an empty {@link ParagraphData} object.
     * @return the empty {@link ParagraphData} object
     */
    ParagraphData getParagraphData();
}
