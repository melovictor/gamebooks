package hu.zagor.gamebooks.content.command.fight.enemyroundresolver;

/**
 * Basic class for custom enemy pre post handler data containers.
 * @author Tamas_Szekeres
 */
public class BasicEnemyPrePostFightDataContainer {
    private String primaryEnemy;

    public String getPrimaryEnemy() {
        return primaryEnemy;
    }

    public void setPrimaryEnemy(final String primaryEnemy) {
        this.primaryEnemy = primaryEnemy;
    }

}
