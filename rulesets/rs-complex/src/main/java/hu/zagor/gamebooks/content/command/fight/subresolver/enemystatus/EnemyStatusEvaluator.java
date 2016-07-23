package hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus;

import hu.zagor.gamebooks.character.enemy.Enemy;
import java.util.List;

/**
 * Interface with which we can check statuses of {@link Enemy} objects.
 * @author Tamas_Szekeres
 * @param <E> the actual type of the enemy
 */
public interface EnemyStatusEvaluator<E extends Enemy> {

    /**
     * Check if all of the enemies are dead or otherwise incapacitated at the specific round.
     * @param enemies the list of {@link Enemy} objects to check
     * @param roundNumber the current fight round number
     * @return true if all enemies are dead, false otherwise
     */
    boolean enemiesAreDead(List<E> enemies, int roundNumber);

    /**
     * Checks if a specific enemy is alive at the moment or not.
     * @param enemy the {@link Enemy} object to check
     * @param roundNumber the current fight round number
     * @return true if the enemy is dead, false otherwise
     */
    boolean isAlive(E enemy, int roundNumber);

}
