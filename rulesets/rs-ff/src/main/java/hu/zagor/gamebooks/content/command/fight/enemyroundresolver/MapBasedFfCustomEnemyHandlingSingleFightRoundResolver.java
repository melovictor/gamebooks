package hu.zagor.gamebooks.content.command.fight.enemyroundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Abstract class for doing custom enemy handling before and after executing a single battle round with a single enemy.
 * @author Tamas_Szekeres
 * @param <T> the type which will contain the data that was compiled during the pre-fight phase
 */
public abstract class MapBasedFfCustomEnemyHandlingSingleFightRoundResolver<T> implements ApplicationContextAware {
    private Map<String, CustomBeforeAfterRoundEnemyHandler<T>> enemyHandlers;
    private ApplicationContext applicationContext;

    /**
     * Initializator method.
     */
    @PostConstruct
    public void init() {
        enemyHandlers = new HashMap<>();
        final Map<String, ? extends CustomBeforeAfterRoundEnemyHandler<T>> beansOfType = applicationContext.getBeansOfType(getType());
        for (final CustomBeforeAfterRoundEnemyHandler<T> entry : beansOfType.values()) {
            for (final String id : entry.getEnemyIds()) {
                enemyHandlers.put(id, entry);
            }
        }
    }

    /**
     * Attempts to execute a pre round action for the current enemy.
     * @param command the {@link FightCommand}
     * @param resolvationData the {@link ResolvationData}
     * @return the data that was compiled during the pre-fight phase
     */
    protected T executePreRoundActions(final FightCommand command, final ResolvationData resolvationData) {
        final FfEnemy enemy = getEnemy(resolvationData);
        final CustomBeforeAfterRoundEnemyHandler<T> handler = enemyHandlers.get(enemy.getId());
        T data = null;
        if (handler != null) {
            if (handler.shouldExecutePreHandler(command)) {
                data = handler.executePreHandler(command);
            }
        }
        return data;
    }

    /**
     * Attempts to execute a post round action for the current enemy.
     * @param command the {@link FightCommand}
     * @param resolvationData the {@link ResolvationData}
     * @param results the {@link FightRoundResult} array containing the result for the previous battle round
     * @param data the data that was compiled during the pre-fight phase
     */
    protected void executePostRoundActions(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results, final T data) {
        final FfEnemy enemy = getEnemy(resolvationData);
        final CustomBeforeAfterRoundEnemyHandler<T> handler = enemyHandlers.get(enemy.getId());
        if (handler != null) {
            if (handler.shouldExecutePostHandler(command, resolvationData, results)) {
                handler.executePostHandler(command, resolvationData, results, data);
            }
        }

    }

    /**
     * Fetches the enemy we are currently fighting.
     * @param resolvationData the {@link ResolvationData} object
     * @return the resolved enemy
     */
    protected FfEnemy getEnemy(final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final String enemyId = interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID);
        return (FfEnemy) resolvationData.getEnemies().get(enemyId);
    }

    /**
     * Method to return the type of the enemy handlers which are relevant for this specific resolver.
     * @return the type of the specific enemy handlers
     */
    protected abstract Class<? extends CustomBeforeAfterRoundEnemyHandler<T>> getType();

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
