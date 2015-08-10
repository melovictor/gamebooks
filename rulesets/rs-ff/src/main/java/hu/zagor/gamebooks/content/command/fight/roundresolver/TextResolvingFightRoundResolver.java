package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.AbstractFightMessageLine;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightMessageLine;
import hu.zagor.gamebooks.support.locale.LocaleProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;

/**
 * Abstract class providing a simple method to resolve battle messages.
 * @author Tamas_Szekeres
 */
public abstract class TextResolvingFightRoundResolver implements FightRoundResolver {

    @Autowired
    private HierarchicalMessageSource messageSource;
    @Autowired
    private LocaleProvider localeProvider;

    /**
     * Provides the initial list of messages for fleeing.
     * @param messages the message list
     */
    protected void getFleeTextResourceList(final FightCommandMessageList messages) {
        messages.addKey("page.ff.label.fight.flee");
    }

    /**
     * Records a resolved event line into the system.
     * @param command the {@link FightCommand} containing the message container
     * @param line the {@link AbstractFightMessageLine} containing the data needed to create the message
     */
    protected void recordLine(final FightCommand command, final FightMessageLine line) {
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey(line.getMessageKey(), line.getParameters());
    }

}
