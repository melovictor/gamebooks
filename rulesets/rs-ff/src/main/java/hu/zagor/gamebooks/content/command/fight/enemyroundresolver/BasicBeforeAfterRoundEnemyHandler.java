package hu.zagor.gamebooks.content.command.fight.enemyroundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
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
    public boolean shouldExecutePreHandler(final FightCommand command, final T data) {
        return false;
    }

    @Override
    public void executePreHandler(final FightCommand command, final T data) {
    }

    @Override
    public String[] getEnemyIds() {
        return new String[]{getEnemyId()};
    }

    /**
     * Fetches the enemy we are currently fighting.
     * @param resolvationData the {@link ResolvationData} object
     * @return the resolved enemy
     */
    protected final FfEnemy getEnemy(final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final String enemyId = interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID);
        return (FfEnemy) resolvationData.getEnemies().get(enemyId);
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
