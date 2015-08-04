package hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus;

import hu.zagor.gamebooks.character.enemy.FfEnemy;

import java.util.List;

/**
 * Interface with which we can check statuses of {@link FfEnemy} objects.
 * @author Tamas_Szekeres
 */
public interface EnemyStatusEvaluator {

    /**
     * Check if all of the enemies are dead or otherwise incapacitated at the specific round.
     * @param enemies the list of {@link FfEnemy} objects to check
     * @param roundNumber the current fight round number
     * @return true if all enemies are dead, false otherwise
     */
    boolean enemiesAreDead(List<FfEnemy> enemies, int roundNumber);

    /**
     * Checks if a specific enemy is alive at the moment or not.
     * @param enemy the {@link FfEnemy} object to check
     * @return true if the enemy is dead, false otherwise
     */
    boolean isAlive(FfEnemy enemy);

}
