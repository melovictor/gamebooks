package hu.zagor.gamebooks.content.command.fight.subresolver.autolose;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;

import java.util.List;

/**
 * Interface with which we can check if an auto-lose condition has been fulfilled or not.
 * @author Tamas_Szekeres
 */
public interface AutoLoseHandler {

    /**
     * Checks if there are auto-losing conditions that would cause the current battle to be over prematurely.
     * @param command the {@link FightCommand} object
     * @param resolveList the list of {@link ParagraphData} objects for resolvation
     * @param resolvationData the {@link ResolvationData} object
     */
    void checkAutoEvents(FightCommand command, List<ParagraphData> resolveList, ResolvationData resolvationData);

}
