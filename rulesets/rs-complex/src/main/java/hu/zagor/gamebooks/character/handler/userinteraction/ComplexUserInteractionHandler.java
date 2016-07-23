package hu.zagor.gamebooks.character.handler.userinteraction;

import hu.zagor.gamebooks.character.Character;

/**
 * User interaction handler for complex rulesets.
 * @author Tamas_Szekeres
 */
public class ComplexUserInteractionHandler extends DefaultUserInteractionHandler {

    private static final String FIGHT_COMMAND = "fightCommand";
    private static final String MARKET = "market";

    /**
     * Sets a state for the fight command.
     * @param character the character onto which the fight state must be set
     * @param state the state to be set
     */
    public void setFightCommand(final Character character, final String state) {
        setInteractionState(character, FIGHT_COMMAND, state);
    }

    /**
     * Returns the last set state for the fight.
     * @param character the {@link Character}
     * @return the last state that was set
     */
    public String getLastFightCommand(final Character character) {
        return getInteractionState(character, FIGHT_COMMAND);
    }

    /**
     * Returns the last set state for the fight without destroying the state.
     * @param character the {@link Character}
     * @return the last state that was set
     */
    public String peekLastFightCommand(final Character character) {
        return peekInteractionState(character, FIGHT_COMMAND);
    }

    /**
     * Returns the last set state for the fight without destroying the state.
     * @param character the {@link Character}
     * @param attribute the attribute we wish to peek for the fight command
     * @return the last state that was set
     */
    public String peekLastFightCommand(final Character character, final String attribute) {
        return peekInteractionState(character, FIGHT_COMMAND + attribute);
    }

    /**
     * Sets an attribute for the fight command.
     * @param character the character onto which the fight state must be set
     * @param attribute the attribute we wish to set for the fight command
     * @param state the state to be set
     */
    public void setFightCommand(final Character character, final String attribute, final String state) {
        setInteractionState(character, FIGHT_COMMAND + attribute, state);
    }

    /**
     * Returns the last set state for the fight.
     * @param character the {@link Character}
     * @param attribute the attribute we wish to know about
     * @return the last state that was set
     */
    public String getLastFightCommand(final Character character, final String attribute) {
        return getInteractionState(character, FIGHT_COMMAND + attribute);
    }

    /**
     * Returns the last state for the market.
     * @param character the {@link Character}
     * @return true if the user has finished marketing, false if he just started it
     */
    public boolean hasMarketCommand(final Character character) {
        return getInteractionState(character, MARKET) != null;
    }

    /**
     * Sets the state for market to "finished".
     * @param character the {@link Character}
     */
    public void setMarketCommand(final Character character) {
        setInteractionState(character, MARKET, "finished");
    }

}
