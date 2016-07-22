package hu.zagor.gamebooks.content.command;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ComplexParagraphData;

/**
 * Interface that executes a specific set of commands inside a {@link ComplexParagraphData} object.
 * @author Tamas_Szekeres
 */
public interface CommandExecuter {

    /**
     * Executes the specific commands.
     * @param resolvationData the {@link ResolvationData} object
     * @param subData the {@link ComplexParagraphData} object that needs execution.
     */
    void execute(ResolvationData resolvationData, ComplexParagraphData subData);

}
