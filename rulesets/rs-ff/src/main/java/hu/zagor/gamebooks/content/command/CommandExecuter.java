package hu.zagor.gamebooks.content.command;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.FfParagraphData;

/**
 * Interface that executes a specific set of commands inside a {@link FfParagraphData} object.
 * @author Tamas_Szekeres
 */
public interface CommandExecuter {

    /**
     * Executes the specific commands.
     * @param resolvationData the {@link ResolvationData} object
     * @param subData the {@link FfParagraphData} object that needs execution.
     */
    void execute(ResolvationData resolvationData, FfParagraphData subData);

}
