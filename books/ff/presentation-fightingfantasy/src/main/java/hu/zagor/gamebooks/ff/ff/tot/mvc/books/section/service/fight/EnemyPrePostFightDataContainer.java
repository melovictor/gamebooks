package hu.zagor.gamebooks.ff.ff.tot.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;

/**
 * Fight data container for FF14.
 * @author Tamas_Szekeres
 */
public class EnemyPrePostFightDataContainer extends BasicEnemyPrePostFightDataContainer {
    private int enemyStamina;

    public int getEnemyStamina() {
        return enemyStamina;
    }

    public void setEnemyStamina(final int enemyStamina) {
        this.enemyStamina = enemyStamina;
    }
}
