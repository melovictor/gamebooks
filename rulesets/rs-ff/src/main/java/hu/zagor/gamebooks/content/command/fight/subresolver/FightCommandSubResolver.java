package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphResolver;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.fight.FightCommand;

import java.util.List;

/**
 * Interface for different resolvation logics for the {@link FightCommand} bean.
 * @author Tamas_Szekeres
 */
public interface FightCommandSubResolver {

    /**
     * Do the resolving.
     * @param command the {@link FightCommand} to be resolved
     * @param resolvationData all the data required for resolving the current command
     * @return a list of {@link ParagraphData} beans that also need to be resolved afterwards by the main {@link BookParagraphResolver}, an empty list if no paragraph has to be
     *         resolved but also no (more) user interaction is required for the successful resolvation of the current command, or null when user-interaction is required for
     *         resolving the {@link Command} properly
     */
    List<ParagraphData> doResolve(FightCommand command, ResolvationData resolvationData);

}
