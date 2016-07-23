package hu.zagor.gamebooks.content.command.fight.enemyroundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;

/**
 * Interface for executing pre- and post-battle actions for a single, specific enemy.
 * @author Tamas_Szekeres
 * @param <T> the type which will contain the data compiled during the pre fight phase
 */
public interface CustomBeforeAfterRoundEnemyHandler<T> {

    /**
     * Decides whether the execution of the pre handler is necessary or not.
     * @param command the {@link FfFightCommand}
     * @param data the data bean containing compiled data - if any - from the pre handling phase
     * @return true if it should be executed, false if not
     */
    boolean shouldExecutePreHandler(FfFightCommand command, T data);

    /**
     * Executes the pre fight handler.
     * @param command the {@link FfFightCommand}
     * @param data the data bean containing compiled data - if any - from the pre handling phase
     */
    void executePreHandler(FfFightCommand command, T data);

    /**
     * Decides whether the execution of the post handler is necessary or not.
     * @param command the {@link FfFightCommand}
     * @param resolvationData the {@link ResolvationData}
     * @param results the {@link FightRoundResult} array containing the result for the previous battle round
     * @param data the data bean containing compiled data - if any - from the pre handling phase
     * @return true if it should be executed, false if not
     */
    boolean shouldExecutePostHandler(FfFightCommand command, ResolvationData resolvationData, FightRoundResult[] results, T data);

    /**
     * Executes the post fight handler.
     * @param command the {@link FfFightCommand}
     * @param resolvationData the {@link ResolvationData}
     * @param results the {@link FightRoundResult} array containing the result for the previous battle round
     * @param data the data that was compiled during the pre-fight phase
     */
    void executePostHandler(FfFightCommand command, ResolvationData resolvationData, FightRoundResult[] results, T data);

    /**
     * Returns the ids of the enemies this resolver is supposed to handle.
     * @return the ids of the enemies to be handled
     */
    String[] getEnemyIds();
}
