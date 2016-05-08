package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;

/**
 * Fight data container for the enemy pre-post fight handling.
 * @author Tamas_Szekeres
 */
public class EnemyPrePostFightDataContainer extends BasicEnemyPrePostFightDataContainer {
    private int[] preFightRoll;

    public int[] getPreFightRoll() {
        return preFightRoll;
    }

    public void setPreFightRoll(final int[] preFightRoll) {
        this.preFightRoll = preFightRoll;
    }
}
