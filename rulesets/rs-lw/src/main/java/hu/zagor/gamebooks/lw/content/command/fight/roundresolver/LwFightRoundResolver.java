package hu.zagor.gamebooks.lw.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.lw.content.command.fight.LwFightCommand;

/**
 * Interface for resolvers that resolve a single round of fight.
 * @author Tamas_Szekeres
 */
public interface LwFightRoundResolver {

    /**
     * Resolves a single round of fight.
     * @param command the {@link LwFightCommand}
     * @param resolvationData the {@link ResolvationData} object required to resolve the current round
     */
    void resolveRound(LwFightCommand command, final ResolvationData resolvationData);

    /**
     * Resolves fleeing from the battle.
     * @param command the {@link LwFightCommand}
     * @param resolvationData the {@link ResolvationData} object required to resolve the fleeing
     */
    void resolveFlee(LwFightCommand command, ResolvationData resolvationData);

}
