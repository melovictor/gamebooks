package hu.zagor.gamebooks.character.domain.builder;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.domain.BookInformations;

/**
 * {@link ResolvationDataBuilder} interface piece.
 * @author Tamas_Szekeres
 */
public interface ResolvationDataBuilderRootData {

    /**
     * Sets the {@link ParagraphData} object.
     * @param rootData the {@link ParagraphData} object
     * @return the next piece of the builder
     */
    ResolvationDataBuilderInfo withRootData(final ParagraphData rootData);

    /**
     * Sets the {@link ParagraphData}, enemies and {@link BookInformations} objects based on an existing {@link ResolvationData} object.
     * @param resolvationData the {@link ResolvationData} object
     * @return the next piece of the builder
     */
    ResolvationDataBuilderCharacter usingResolvationData(final ResolvationData resolvationData);
}
