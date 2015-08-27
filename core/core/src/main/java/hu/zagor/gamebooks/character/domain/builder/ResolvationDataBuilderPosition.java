package hu.zagor.gamebooks.character.domain.builder;

/**
 * {@link ResolvationDataBuilder} interface piece.
 * @author Tamas_Szekeres
 */
public interface ResolvationDataBuilderPosition extends ResolvationDataBuilderFinish {

    /**
     * Sets the position of the previous choice.
     * @param position the position
     * @return the next piece of the builder
     */
    ResolvationDataBuilderFinish withPosition(final Integer position);
}
