package hu.zagor.gamebooks.content.command;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;

import java.util.List;
import java.util.Locale;

/**
 * Interface for resolvers that usually require user interactions, but can do their magic automatically as well.
 * @author Tamas_Szekeres
 * @param <C> the specific {@link Command} version to resolve
 */
public interface SilentCapableResolver<C extends Command> {

    /**
     * Silently resolves the action.
     * @param command the command that needs to be resolved
     * @param resolvationData the information to be used for the resolvation
     * @param messages the list of messages into which the output text has to be added
     * @param locale the current locale
     * @return the resolved {@link ParagraphData} objects
     */
    List<ParagraphData> resolveSilently(final Command command, final ResolvationData resolvationData, List<String> messages, Locale locale);
}
