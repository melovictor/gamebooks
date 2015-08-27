package hu.zagor.gamebooks.character.domain.builder;

import hu.zagor.gamebooks.character.domain.ResolvationData;

/**
 * {@link ResolvationDataBuilder} interface piece.
 * @author Tamas_Szekeres
 */
public interface ResolvationDataBuilderFinish {

    /**
     * Fetches the finished {@link ResolvationData} object.
     * @return the finished object
     */
    ResolvationData build();
}
