package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.AbstractFightMessageLine;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightMessageLine;

/**
 * Abstract class providing a simple method to resolve battle messages.
 * @author Tamas_Szekeres
 */
public abstract class TextResolvingFightRoundResolver implements FightRoundResolver {

    /**
     * Provides the initial list of messages for fleeing.
     * @param messages the message list
     * @param fightFleeData the {@link FightFleeData} object containing a potential text to be printed on screen on fleeing
     */
    protected void getFleeTextResourceList(final FightCommandMessageList messages, final FightFleeData fightFleeData) {
        final String text = fightFleeData == null ? null : fightFleeData.getText();
        if (text == null) {
            messages.addKey("page.ff.label.fight.flee");
        } else {
            messages.add(text + ".");
        }
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
