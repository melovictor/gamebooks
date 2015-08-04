package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;

import java.util.List;

/**
 * Interface for handling fight events before the actual round starts.
 * @author Tamas_Szekeres
 */
public interface FightCommandBeforeEventResolver {

    /**
     * Handles the events that occur before the battle round starts.
     * @param command the {@link FightCommand} object
     * @param resolvationData the {@link ResolvationData} object
     * @param resolveList the list of {@link ParagraphData} objects that need to be resolved
     * @return the result object for the action
     */
    FightBeforeRoundResult handleBeforeRoundEvent(final FightCommand command, final ResolvationData resolvationData, final List<ParagraphData> resolveList);
}
