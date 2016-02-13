package hu.zagor.gamebooks.character.handler.userinteraction;

import hu.zagor.gamebooks.content.command.attributetest.AttributeTestDecision;
import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * User interaction handler for the Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
public class FfUserInteractionHandler extends DefaultUserInteractionHandler {

    private static final String ATTRIB_TEST_RESULT = "attribTestResult";
    private static final String FIGHT_COMMAND = "fightCommand";
    private static final String MARKET = "market";

    /**
     * Gets whether attribute test is set at the moment.
     * @param character the {@link FfCharacter}
     * @return true if the state is active, false otherwise
     */
    public boolean hasAttributeTestResult(final FfCharacter character) {
        return getInteractionState(character, ATTRIB_TEST_RESULT) != null;
    }

    /**
     * Sets the state for attribute test to "true".
     * @param character the {@link FfCharacter}
     */
    public void setAttributeTestResult(final FfCharacter character) {
        setInteractionState(character, ATTRIB_TEST_RESULT, "true");
    }

    /**
     * Sets the state for attribute test to "true".
     * @param character the {@link FfCharacter}
     * @param decision the decision the user made about how to handle the current attribute test
     */
    public void setAttributeTestResult(final FfCharacter character, final AttributeTestDecision decision) {
        setInteractionState(character, ATTRIB_TEST_RESULT + "Decision", decision.name());
    }

    /**
     * Gets the previously stored decision the user made about how to resolve the attribute test.
     * @param character the {@link FfCharacter} object
     * @return the decision, either do the test or skip it
     */
    public AttributeTestDecision getAttributeTestType(final FfCharacter character) {
        return AttributeTestDecision.valueOf(getInteractionState(character, ATTRIB_TEST_RESULT + "Decision"));
    }

    /**
     * Sets a state for the fight command.
     * @param character the character onto which the fight state must be set
     * @param state the state to be set
     */
    public void setFightCommand(final FfCharacter character, final String state) {
        setInteractionState(character, FIGHT_COMMAND, state);
    }

    /**
     * Returns the last set state for the fight.
     * @param character the {@link FfCharacter}
     * @return the last state that was set
     */
    public String getLastFightCommand(final FfCharacter character) {
        return getInteractionState(character, FIGHT_COMMAND);
    }

    /**
     * Returns the last set state for the fight without destroying the state.
     * @param character the {@link FfCharacter}
     * @return the last state that was set
     */
    public String peekLastFightCommand(final FfCharacter character) {
        return peekInteractionState(character, FIGHT_COMMAND);
    }

    /**
     * Returns the last set state for the fight without destroying the state.
     * @param character the {@link FfCharacter}
     * @param attribute the attribute we wish to peek for the fight command
     * @return the last state that was set
     */
    public String peekLastFightCommand(final FfCharacter character, final String attribute) {
        return peekInteractionState(character, FIGHT_COMMAND + attribute);
    }

    /**
     * Sets an attribute for the fight command.
     * @param character the character onto which the fight state must be set
     * @param attribute the attribute we wish to set for the fight command
     * @param state the state to be set
     */
    public void setFightCommand(final FfCharacter character, final String attribute, final String state) {
        setInteractionState(character, FIGHT_COMMAND + attribute, state);
    }

    /**
     * Returns the last set state for the fight.
     * @param character the {@link FfCharacter}
     * @param attribute the attribute we wish to know about
     * @return the last state that was set
     */
    public String getLastFightCommand(final FfCharacter character, final String attribute) {
        return getInteractionState(character, FIGHT_COMMAND + attribute);
    }

    /**
     * Returns the last state for the market.
     * @param character the {@link FfCharacter}
     * @return true if the user has finished marketing, false if he just started it
     */
    public boolean hasMarketCommand(final FfCharacter character) {
        return getInteractionState(character, MARKET) != null;
    }

    /**
     * Sets the state for market to "finished".
     * @param character the {@link FfCharacter}
     */
    public void setMarketCommand(final FfCharacter character) {
        setInteractionState(character, MARKET, "finished");
    }

}
