package hu.zagor.gamebooks.character.handler.enemy;

import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;

import java.util.Map;

/**
 * Class for returning data about the enemies.
 * @author Tamas_Szekeres
 */
public class FfEnemyHandler {

    private Map<String, Enemy> enemies;

    public void setEnemies(final Map<String, Enemy> enemies) {
        this.enemies = enemies;
    }

    /**
     * Checks whether a specific enemy is alive at the moment or not.
     * @param enemyId the id of the enemy to check
     * @return true if the enemy is alive, false otherwise
     */
    public boolean isEnemyAlive(final String enemyId) {
        final FfEnemy enemy = (FfEnemy) enemies.get(enemyId);
        return enemy.getStamina() > enemy.getFleeAtStamina();
    }

}
