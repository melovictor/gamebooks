package hu.zagor.gamebooks.character.domain.builder;

import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.player.PlayerUser;

import java.util.Map;

/**
 * {@link ResolvationDataBuilder} interface piece.
 * @author Tamas_Szekeres
 */
public interface ResolvationDataBuilderTriplet extends ResolvationDataBuilderFinish {

    /**
     * Sets the enemies.
     * @param enemies the enemies in a {@link Map} object
     * @return the next piece of the builder
     */
    ResolvationDataBuilderTriplet withEnemies(final Map<String, Enemy> enemies);

    /**
     * Sets the {@link PlayerUser} object.
     * @param player the {@link PlayerUser} object
     * @return the next piece of the builder
     */
    ResolvationDataBuilderTriplet withPlayer(final PlayerUser player);

    /**
     * Sets the position of the previous choice.
     * @param position the position
     * @return the next piece of the builder
     */
    ResolvationDataBuilderTriplet withPosition(final Integer position);
}
