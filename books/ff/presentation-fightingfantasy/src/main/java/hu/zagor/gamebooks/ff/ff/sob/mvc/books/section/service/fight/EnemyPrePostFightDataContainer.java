package hu.zagor.gamebooks.ff.ff.sob.mvc.books.section.service.fight;

import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;

/**
 * Enemy pre-post info container for FF16.
 * @author Tamas_Szekeres
 */
public class EnemyPrePostFightDataContainer extends BasicEnemyPrePostFightDataContainer {
    private int[] randomRoll;

    public int[] getRandomRoll() {
        return randomRoll;
    }

    public void setRandomRoll(final int[] randomRoll) {
        this.randomRoll = randomRoll;
    }

}
