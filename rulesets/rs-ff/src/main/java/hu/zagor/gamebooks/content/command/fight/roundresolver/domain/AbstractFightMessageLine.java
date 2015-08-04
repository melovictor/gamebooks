package hu.zagor.gamebooks.content.command.fight.roundresolver.domain;

/**
 * Abstract class for beans containing information for message logging.
 * @author Tamas_Szekeres
 */
public abstract class AbstractFightMessageLine implements FightMessageLine {

    private final String messageKey;

    /**
     * Basic constructor that expects the key for the message.
     * @param messageKey the key for the message to be displayed
     */
    public AbstractFightMessageLine(final String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

}
