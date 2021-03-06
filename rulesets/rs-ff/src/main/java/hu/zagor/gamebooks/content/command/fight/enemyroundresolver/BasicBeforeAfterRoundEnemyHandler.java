package hu.zagor.gamebooks.content.command.fight.enemyroundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Implements the least commonly used features so actual implementations doesn't have to do it.
 * @author Tamas_Szekeres
 * @param <T> the type which will contain the data compiled during the pre fight phase
 */
public abstract class BasicBeforeAfterRoundEnemyHandler<T> implements CustomBeforeAfterRoundEnemyHandler<T> {
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer renderer;

    /**
     * Kills the provided enemy.
     * @param enemyObject the enemy to kill
     */
    protected void killEnemy(final Enemy enemyObject) {
        final FfEnemy enemy = (FfEnemy) enemyObject;
        enemy.setStamina(0);
    }

    @Override
    public boolean shouldExecutePreHandler(final FfFightCommand command, final T data) {
        return false;
    }

    @Override
    public void executePreHandler(final FfFightCommand command, final T data) {
    }

    /**
     * Rolls the specified number of dices, records the roll and returns the values.
     * @param dices the number of dices to throw
     * @param command the {@link FfFightCommand} object
     * @return the rolled dices
     */
    protected int[] rollRecord(final int dices, final FfFightCommand command) {
        final int[] roll = getGenerator().getRandomNumber(dices);
        record(command, roll);
        return roll;
    }

    /**
     * Records a roll value for the player to see.
     * @param command the {@link FfFightCommand}
     * @param roll the rolled values
     */
    protected void record(final FfFightCommand command, final int[] roll) {
        record(command, roll, null);
    }

    /**
     * Records a roll value for the player to see.
     * @param command the {@link FfFightCommand}
     * @param roll the rolled values
     * @param index the index at which the message should be inserted; use null for insertion at the end
     */
    protected void record(final FfFightCommand command, final int[] roll, final Integer index) {
        final String renderedRoll = getRenderer().render(getGenerator().getDefaultDiceSide(), roll);
        final FightCommandMessageList messages = command.getMessages();
        if (index == null) {
            messages.addKey("page.ff.label.random.after", renderedRoll, roll[0]);
        } else {
            messages.addKey(index, "page.ff.label.random.after", renderedRoll, roll[0]);
        }
    }

    @Override
    public String[] getEnemyIds() {
        return new String[]{getEnemyId()};
    }

    /**
     * Returns the id of the enemy this resolver is supposed to handle. Optional shortcut version for the {@link #getEnemyIds()} method in case there is only a single enemy to
     * handle (which is most of the time).
     * @return the id of the enemy to be handled
     */
    protected String getEnemyId() {
        throw new IllegalStateException("This method must be overwritten, if it is expected to be used!");
    }

    public RandomNumberGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(final RandomNumberGenerator generator) {
        this.generator = generator;
    }

    public DiceResultRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(final DiceResultRenderer renderer) {
        this.renderer = renderer;
    }

}
