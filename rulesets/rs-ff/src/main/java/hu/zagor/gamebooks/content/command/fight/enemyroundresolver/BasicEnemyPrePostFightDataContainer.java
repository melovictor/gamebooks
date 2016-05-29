package hu.zagor.gamebooks.content.command.fight.enemyroundresolver;

import hu.zagor.gamebooks.character.enemy.FfEnemy;

/**
 * Basic class for custom enemy pre post handler data containers.
 * @author Tamas_Szekeres
 */
public class BasicEnemyPrePostFightDataContainer {
    private String primaryEnemy;
    private FfEnemy currentEnemy;

    public String getPrimaryEnemy() {
        return primaryEnemy;
    }

    public void setPrimaryEnemy(final String primaryEnemy) {
        this.primaryEnemy = primaryEnemy;
    }

    public FfEnemy getCurrentEnemy() {
        return currentEnemy;
    }

    public void setCurrentEnemy(final FfEnemy currentEnemy) {
        this.currentEnemy = currentEnemy;
    }

}
