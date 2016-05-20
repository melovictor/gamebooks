package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicBeforeAfterRoundEnemyHandler;

/**
 * Ff11 before-after enemy handler base class.
 * @author Tamas_Szekeres
 */
public abstract class Ff11BeforeAfterRoundEnemyHandler extends BasicBeforeAfterRoundEnemyHandler<EnemyPrePostFightDataContainer> {

    /**
     * Kills the provided enemy.
     * @param enemyObject the enemy to kill
     */
    protected void killEnemy(final Enemy enemyObject) {
        final FfEnemy enemy = (FfEnemy) enemyObject;
        enemy.setStamina(0);
    }
}
