package hu.zagor.gamebooks.character.handler.userinteraction;

import hu.zagor.gamebooks.ff.character.FfCharacter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfUserInteractionHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class FfUserInteractionHandlerTest {

    private FfUserInteractionHandler underTest;
    private FfCharacter character;

    @BeforeClass
    public void setUpClass() {
        underTest = new FfUserInteractionHandler();
        character = new FfCharacter();
    }

    @BeforeMethod
    public void setUpMethod() {
        character.getUserInteraction().clear();
    }

    public void testHasAttributeTestResultWhenSetterWasNotCalledShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = underTest.hasAttributeTestResult(character);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testHasAttributeTestResultWhenSetterWasCalledShouldReturnFalse() {
        // GIVEN
        underTest.setAttributeTestResult(character);
        // WHEN
        final boolean returned = underTest.hasAttributeTestResult(character);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testGetLastFightCommandWhenNoAttributeIsSetShouldReturnLastStateSet() {
        // GIVEN
        underTest.setFightCommand(character, "fight");
        // WHEN
        final String returned = underTest.getLastFightCommand(character);
        // THEN
        Assert.assertEquals(returned, "fight");
    }

    public void testGetLastFightCommandWhenAttributeIsSetShouldReturnLastAttributeStateSet() {
        // GIVEN
        underTest.setFightCommand(character, "luckOnDefense", "true");
        // WHEN
        final String returned = underTest.getLastFightCommand(character, "luckOnDefense");
        // THEN
        Assert.assertEquals(returned, "true");
    }

}
