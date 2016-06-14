package hu.zagor.gamebooks.character.handler.userinteraction;

import hu.zagor.gamebooks.character.Character;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultUserInteractionHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultUserInteractionHandlerTest {

    private DefaultUserInteractionHandler underTest;
    private Character character;

    @BeforeClass
    public void setUpClass() {
        underTest = new DefaultUserInteractionHandler();
    }

    @BeforeMethod
    public void setUpMethod() {
        character = new Character();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetInteractionStateWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.getInteractionState(null, "interaction");
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetInteractionStateWhenInteractionIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.getInteractionState(character, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetInteractionStateWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.setInteractionState(null, "interaction", "state");
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetInteractionStateWhenInteractionIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.setInteractionState(character, null, "state");
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetInteractionStateWhenStateIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.setInteractionState(character, "interaction", null);
        // THEN throws exception
    }

    public void testSetUserInputShouldSetUserInput() {
        // GIVEN
        // WHEN
        underTest.setUserInput(character, "carrot");
        // THEN
        Assert.assertEquals(underTest.getUserInput(character), "carrot");
        Assert.assertNull(character.getUserInteraction().get("userInput"));
    }

    public void testSetUserInputTimeShouldSetUserInputTime() {
        // GIVEN
        // WHEN
        underTest.setUserInputTime(character, 6542);
        // THEN
        Assert.assertEquals(underTest.getUserInputTime(character), 6542);
        Assert.assertNull(character.getUserInteraction().get("userInputTime"));
    }

    public void testHasRandomResultWhenSetterWasNotCalledShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = underTest.hasRandomResult(character);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testHasRandomResultWhenSetterWasCalledShouldReturnFalse() {
        // GIVEN
        underTest.setRandomResult(character);
        // WHEN
        final boolean returned = underTest.hasRandomResult(character);
        // THEN
        Assert.assertTrue(returned);
        Assert.assertNull(character.getUserInteraction().get("random"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testPeekInteractionStateWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.peekInteractionState(null, "fight");
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testPeekInteractionStateWhenInteractionIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        underTest.peekInteractionState(character, null);
        // THEN throws exception
    }

    public void testPeekInteractionStateWhenParametersAreCorrectShouldReturnValue() {
        // GIVEN
        character.getUserInteraction().put("fight", "attack");
        // WHEN
        final String returned = underTest.peekInteractionState(character, "fight");
        // THEN
        Assert.assertEquals(returned, "attack");
        Assert.assertEquals(character.getUserInteraction().get("fight"), returned);
    }

}
