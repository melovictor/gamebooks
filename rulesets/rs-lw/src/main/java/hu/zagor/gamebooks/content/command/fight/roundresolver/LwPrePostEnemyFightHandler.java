package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.LwFightCommand;

/**
 * Interface for specifying objects which can be used to handle custom logic before and after battle rounds.
 * @author Tamas_Szekeres
 */
public interface LwPrePostEnemyFightHandler {
    /**
     * An empty handler that doesn't do anything.
     */
    LwPrePostEnemyFightHandler EMPTY = new LwPrePostEnemyFightHandler() {

        @Override
        public boolean shouldExecutePre(final LwFightCommand command, final ResolvationData resolvationData) {
            return false;
        }

        @Override
        public Object executePre(final LwFightCommand command, final ResolvationData resolvationData) {
            return null;
        }

        @Override
        public boolean shouldExecutePost(final LwFightCommand command, final ResolvationData resolvationData) {
            return false;
        }

        @Override
        public void executePost(final LwFightCommand command, final ResolvationData resolvationData, final Object preGenData) {
        }
    };

    /**
     * Checks whether the pre round actions should be executed.
     * @param command the {@link LwFightCommand} object
     * @param resolvationData the {@link ResolvationData} object
     * @return true if it should be executed, false otherwise.
     */
    boolean shouldExecutePre(LwFightCommand command, ResolvationData resolvationData);

    /**
     * Executes pre-round actions.
     * @param command the {@link LwFightCommand} object
     * @param resolvationData the {@link ResolvationData} object
     * @return data generated during the pre phase
     */
    Object executePre(LwFightCommand command, ResolvationData resolvationData);

    /**
     * Checks whether the post round actions should be executed.
     * @param command the {@link LwFightCommand} object
     * @param resolvationData the {@link ResolvationData} object
     * @return true if it should be executed, false otherwise.
     */
    boolean shouldExecutePost(LwFightCommand command, ResolvationData resolvationData);

    /**
     * Executes post-round actions.
     * @param command the {@link LwFightCommand} object
     * @param resolvationData the {@link ResolvationData} object
     * @param preGenData the data generated during the pre fight stage
     */
    void executePost(LwFightCommand command, ResolvationData resolvationData, Object preGenData);

}
