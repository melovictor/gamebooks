package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.ParagraphData;

/**
 * Interface for stub commands for {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public interface ItemCheckStubCommand {

    /**
     * Resolves a {@link ParagraphData} for a certain check-type.
     * @param parent the parent {@link ItemCheckCommand} object
     * @param character the {@link Character}
     * @param characterHandler the {@link CharacterHandler} item
     * @return the resolved {@link ParagraphData}
     */
    ParagraphData resolve(final ItemCheckCommand parent, final Character character, final CharacterHandler characterHandler);
}
