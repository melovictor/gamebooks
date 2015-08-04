package hu.zagor.gamebooks.content.command;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;

/**
 * An interface for resolving a specific command.
 * @author Tamas_Szekeres
 */
public interface CommandResolver {

    /**
     * Runs the command against the character and the root {@link ParagraphData}.
     * @param commandObject the command to resolve
     * @param resolvationData all the data required for resolving the current command
     * @return {@link CommandResolveResult} containing a list of {@link ParagraphData} beans that also needs
     *         to be resolved afterwards by the main {@link BookParagraphResolver} (can be null or empty as
     *         well)
     */
    CommandResolveResult resolve(Command commandObject, ResolvationData resolvationData);

}
