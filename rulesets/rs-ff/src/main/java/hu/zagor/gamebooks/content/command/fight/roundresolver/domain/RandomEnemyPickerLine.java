package hu.zagor.gamebooks.content.command.fight.roundresolver.domain;

/**
 * Class for passing information about battle log line for outputting.
 * @author Tamas_Szekeres
 */
public class RandomEnemyPickerLine extends AbstractFightMessageLine {

    private final int rollResult;
    private final String enemyName;

    /**
     * Basic constructor that expects the name of the enemy and the rolled value.
     * @param rollResult the rolled value
     * @param enemyName the name of the enemy
     */
    public RandomEnemyPickerLine(final int rollResult, final String enemyName) {
        super("page.ff.label.fight.random.selectedEnemy");
        this.rollResult = rollResult;
        this.enemyName = enemyName;
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{rollResult, enemyName};
    }

}
