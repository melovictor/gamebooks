package hu.zagor.gamebooks.character.handler.userinteraction;

import hu.zagor.gamebooks.character.Character;
import org.springframework.util.Assert;

/**
 * Implementation of {@link UserInteractionHandler} with the most basic user interactions.
 * @author Tamas_Szekeres
 */
public class DefaultUserInteractionHandler implements UserInteractionHandler {

    private static final String USER_INPUT = "userInput";
    private static final String USER_INPUT_TIME = "userInputTime";
    private static final String RANDOM = "random";

    @Override
    public String getInteractionState(final Character character, final String interaction) {
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(interaction, "The parameter 'interaction' cannot be null!");
        return character.getUserInteraction().remove(interaction);
    }

    @Override
    public String peekInteractionState(final Character character, final String interaction) {
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(interaction, "The parameter 'interaction' cannot be null!");
        return character.getUserInteraction().get(interaction);
    }

    @Override
    public void setInteractionState(final Character character, final String interaction, final String state) {
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(interaction, "The parameter 'interaction' cannot be null!");
        Assert.notNull(state, "The parameter 'state' cannot be null!");
        character.getUserInteraction().put(interaction, state);
    }

    /**
     * Gets the user input's response.
     * @param character the {@link Character}
     * @return the value the user entered
     */
    public String getUserInput(final Character character) {
        return getInteractionState(character, USER_INPUT);
    }

    /**
     * Sets the answer provided by the user during the last request.
     * @param character the {@link Character}
     * @param answer the answer provided by the user, cannot be null
     */
    public void setUserInput(final Character character, final String answer) {
        Assert.notNull(answer, "The parameter 'answer' cannot be null!");
        setInteractionState(character, USER_INPUT, answer);
    }

    /**
     * Gets the user input time.
     * @param character the {@link Character}
     * @return the value the user entered
     */
    public int getUserInputTime(final Character character) {
        return Integer.parseInt(getInteractionState(character, USER_INPUT_TIME));
    }

    /**
     * Sets the time after which the user provided an answer during the last request.
     * @param character the {@link Character}
     * @param time the time after which the answer was provided by the user
     */
    public void setUserInputTime(final Character character, final int time) {
        setInteractionState(character, USER_INPUT_TIME, String.valueOf(time));
    }

    /**
     * Gets whether random is set at the moment.
     * @param character the {@link Character}
     * @return true if the state is active, false otherwise
     */
    public boolean hasRandomResult(final Character character) {
        return getInteractionState(character, RANDOM) != null;
    }

    /**
     * Sets the state for random to "true".
     * @param character the {@link Character}
     */
    public void setRandomResult(final Character character) {
        setInteractionState(character, RANDOM, "true");
    }
}
