package hu.zagor.gamebooks.content.command.fight.roundresolver.domain;

/**
 * Interface for beans containing information for message logging.
 * @author Tamas_Szekeres
 */
public interface FightMessageLine {

    /**
     * Getter for the message key.
     * @return the message key
     */
    String getMessageKey();

    /**
     * Returns the parameters required for fully resolving the message.
     * @return parameters as an {@link Object} array
     */
    Object[] getParameters();

}
