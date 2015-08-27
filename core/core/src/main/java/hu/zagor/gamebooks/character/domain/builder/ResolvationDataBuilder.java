package hu.zagor.gamebooks.character.domain.builder;

/**
 * Builder interface for creating a {@link ResolvationDataBuilder} object.
 * @author Tamas_Szekeres
 */
public interface ResolvationDataBuilder extends ResolvationDataBuilderRootData, ResolvationDataBuilderCharacter, ResolvationDataBuilderInfo,
    ResolvationDataBuilderPosition, ResolvationDataBuilderTriplet {

}
