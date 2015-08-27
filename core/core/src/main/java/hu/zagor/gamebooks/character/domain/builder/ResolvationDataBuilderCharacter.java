package hu.zagor.gamebooks.character.domain.builder;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.player.PlayerUser;

/**
 * {@link ResolvationDataBuilder} interface piece.
 * @author Tamas_Szekeres
 */
public interface ResolvationDataBuilderCharacter {

    /**
     * Sets the {@link Character}, the enemies and the {@link PlayerUser} from the {@link HttpSessionWrapper} object.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @return the next piece of the builder
     */
    ResolvationDataBuilderPosition usingWrapper(final HttpSessionWrapper wrapper);

    /**
     * Sets the {@link Character} object.
     * @param character the {@link Character} object
     * @return the next piece of the builder
     */
    ResolvationDataBuilderTriplet withCharacter(Character character);
}
