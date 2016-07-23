package hu.zagor.gamebooks.character.handler.userinteraction;

import hu.zagor.gamebooks.content.command.attributetest.AttributeTestDecision;
import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * User interaction handler for the Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
public class FfUserInteractionHandler extends ComplexUserInteractionHandler {

    private static final String ATTRIB_TEST_RESULT = "attribTestResult";

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

}
