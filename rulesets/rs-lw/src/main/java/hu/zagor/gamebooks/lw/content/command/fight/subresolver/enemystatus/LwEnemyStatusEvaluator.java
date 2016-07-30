package hu.zagor.gamebooks.lw.content.command.fight.subresolver.enemystatus;

import hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus.EnemyStatusEvaluator;
import hu.zagor.gamebooks.lw.character.enemy.LwEnemy;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Lw implementation of the {@link EnemyStatusEvaluator} interface.
 * @author Tamas_Szekeres
 */
@Component
public class LwEnemyStatusEvaluator implements EnemyStatusEvaluator<LwEnemy> {

    @Override
    public boolean enemiesAreDead(final List<LwEnemy> enemies, final int roundNumber) {
        boolean enemiesDead = true;
        if (!enemies.isEmpty()) {
            for (final LwEnemy enemy : enemies) {
                enemiesDead &= !isAlive(enemy, roundNumber);
            }
        }
        return enemiesDead;
    }

    @Override
    public boolean isAlive(final LwEnemy enemy, final int roundNumber) {
        return enemy.getEndurance() > 0;
    }
}
