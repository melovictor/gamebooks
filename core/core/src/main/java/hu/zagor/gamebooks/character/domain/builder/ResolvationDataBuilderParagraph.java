package hu.zagor.gamebooks.character.domain.builder;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.domain.BookInformations;

/**
 * {@link ResolvationDataBuilder} interface piece.
 * @author Tamas_Szekeres
 */
public interface ResolvationDataBuilderParagraph {

    /**
     * Sets the {@link ParagraphData} object.
     * @param paragraph the {@link Paragraph} object
     * @return the next piece of the builder
     */
    ResolvationDataBuilderInfo withParagraph(final Paragraph paragraph);

    /**
     * Sets the {@link ParagraphData}, enemies and {@link BookInformations} objects based on an existing {@link ResolvationData} object.
     * @param resolvationData the {@link ResolvationData} object
     * @return the next piece of the builder
     */
    ResolvationDataBuilderCharacter usingResolvationData(final ResolvationData resolvationData);
}
