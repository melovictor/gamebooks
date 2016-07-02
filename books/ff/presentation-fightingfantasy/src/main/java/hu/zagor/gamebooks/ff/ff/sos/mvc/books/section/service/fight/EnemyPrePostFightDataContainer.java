package hu.zagor.gamebooks.ff.ff.sos.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;

/**
 * Enemy pre-post info container for FF34.
 * @author Tamas_Szekeres
 */
public class EnemyPrePostFightDataContainer extends BasicEnemyPrePostFightDataContainer {
    private int[] roll;

    public int[] getRoll() {
        return roll;
    }

    public void setRoll(final int[] roll) {
        this.roll = roll;
    }

}
