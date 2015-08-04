package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean for storing random roll-related info for battles.
 * @author Tamas_Szekeres
 */
public class FightRoundBoundingCommand extends Command {

    private boolean luckAllowed;
    private int nth = 1;
    private int roundNumber;
    private List<Command> commands = new ArrayList<>();
    private final FightCommandMessageList messages;

    /**
     * Basic constructor that creates a new bounding command with the originating {@link FightCommand} object inside.
     * @param command the {@link FightCommand} object
     */
    public FightRoundBoundingCommand(final FightCommand command) {
        this.messages = command.getMessages();
    }

    @Override
    public String getValidMove() {
        return null;
    }

    @Override
    public FightRoundBoundingCommand clone() throws CloneNotSupportedException {
        final FightRoundBoundingCommand cloned = (FightRoundBoundingCommand) super.clone();
        cloned.commands = new ArrayList<>();
        for (final Command command : commands) {
            cloned.commands.add(cloneObject(command));
        }
        return cloned;
    }

    public boolean isLuckAllowed() {
        return luckAllowed;
    }

    public void setLuckAllowed(final boolean useLuck) {
        this.luckAllowed = useLuck;
    }

    public FightCommandMessageList getMessages() {
        return messages;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setNth(final int nth) {
        this.nth = nth;
    }

    public boolean isActive() {
        return roundNumber % nth == 0;
    }

    public void setRoundNumber(final int roundNumber) {
        this.roundNumber = roundNumber;
    }

}
