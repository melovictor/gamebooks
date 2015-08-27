package hu.zagor.gamebooks.character.domain.builder;

import hu.zagor.gamebooks.domain.BookInformations;

/**
 * {@link ResolvationDataBuilder} interface piece.
 * @author Tamas_Szekeres
 */
public interface ResolvationDataBuilderInfo {

    /**
     * Sets the {@link BookInformations} object.
     * @param info the {@link BookInformations} object
     * @return the next piece of the builder
     */
    ResolvationDataBuilderCharacter withBookInformations(final BookInformations info);
}
