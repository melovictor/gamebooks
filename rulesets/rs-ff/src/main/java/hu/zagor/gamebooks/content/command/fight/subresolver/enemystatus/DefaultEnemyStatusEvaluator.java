package hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus;

import hu.zagor.gamebooks.character.enemy.FfEnemy;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link EnemyStatusEvaluator} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultEnemyStatusEvaluator implements EnemyStatusEvaluator {

    @Override
    public boolean enemiesAreDead(final List<FfEnemy> enemies, final int roundNumber) {
        boolean enemiesDead = true;
        if (!enemies.isEmpty()) {
            for (final FfEnemy enemy : enemies) {
                if (!enemyFled(roundNumber, enemy)) {
                    enemiesDead &= !isAlive(enemy);
                }
            }
        }
        return enemiesDead;
    }

    private boolean enemyFled(final int roundNumber, final FfEnemy enemy) {
        return enemy.getStamina() <= enemy.getFleeAtStamina() || enemy.getFleeAtRound() <= roundNumber;
    }

    @Override
    public boolean isAlive(final FfEnemy enemy) {
        return enemy.getStamina() > 0;
    }
}
