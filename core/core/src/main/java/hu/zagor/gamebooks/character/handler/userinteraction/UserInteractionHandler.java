package hu.zagor.gamebooks.character.handler.userinteraction;

import hu.zagor.gamebooks.character.Character;

/**
 * Interface for handling user interactions.
 * @author Tamas_Szekeres
 */
public interface UserInteractionHandler {

    /**
     * Gets a generic interaction's state.
     * @param character the {@link Character} owning the interaction
     * @param interaction the interaction we're interested in
     * @return the state
     */
    String getInteractionState(Character character, String interaction);

    /**
     * Gets a generic interaction's state without destroying it.
     * @param character the {@link Character} owning the interaction
     * @param interaction the interaction we're interested in
     * @return the state
     */
    String peekInteractionState(Character character, String interaction);

    /**
     * Sets a specific interaction to a specific state.
     * @param character the {@link Character} on which the interaction has to be set
     * @param interaction the interaction to set
     * @param state the state to set the interaction to
     */
    void setInteractionState(Character character, String interaction, String state);

}
