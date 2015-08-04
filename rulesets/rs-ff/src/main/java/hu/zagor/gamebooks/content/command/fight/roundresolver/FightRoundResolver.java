package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;

/**
 * Interface for resolvers that resolve a single round of fight.
 * @author Tamas_Szekeres
 */
public interface FightRoundResolver {

    /**
     * Resolves a single round of fight.
     * @param command the {@link FightCommand}
     * @param resolvationData the {@link ResolvationData} object required to resolve the current round
     * @param beforeRoundResult the {@link FightBeforeRoundResult} object
     * @return the results of the round with all the enemies
     */
    FightRoundResult[] resolveRound(FightCommand command, final ResolvationData resolvationData, FightBeforeRoundResult beforeRoundResult);

    /**
     * Resolves fleeing from the battle.
     * @param command the {@link FightCommand}
     * @param resolvationData the {@link ResolvationData} object required to resolve the fleeing
     */
    void resolveFlee(FightCommand command, ResolvationData resolvationData);

}
