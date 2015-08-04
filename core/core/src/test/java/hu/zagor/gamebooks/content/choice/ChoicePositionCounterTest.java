package hu.zagor.gamebooks.content.choice;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ChoicePositionCounter}.
 * @author Tamas_Szekeres
 */
@Test
public class ChoicePositionCounterTest {

    private ChoicePositionCounter underTest;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ChoicePositionCounter();
    }

    public void testUpdateAndGetPositionWhenInputIsNullAndCalledOnceShouldReturnZero() {
        // GIVEN
        final int expected = 0;
        // WHEN
        final int returned = underTest.updateAndGetPosition(null);
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testUpdateAndGetPositionWhenInputIsNullAndCalledTwiceShouldReturnZero() {
        // GIVEN
        final int expected = 1;
        underTest.updateAndGetPosition(null);
        // WHEN
        final int returned = underTest.updateAndGetPosition(null);
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testUpdateAndGetPositionWhenInputIsBigValueShouldReturnSame() {
        // GIVEN
        final int expected = 10;
        // WHEN
        final int returned = underTest.updateAndGetPosition(10);
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testUpdateAndGetPositionWhenInputIsBigThenSmallValueShouldReturnSmaller() {
        // GIVEN
        final int expected = 5;
        underTest.updateAndGetPosition(10);
        // WHEN
        final int returned = underTest.updateAndGetPosition(5);
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testUpdateAndGetPositionWhenInputIsBigThenSmallThenNullValueShouldReturnBiggerPlusOne() {
        // GIVEN
        final int expected = 11;
        underTest.updateAndGetPosition(10);
        underTest.updateAndGetPosition(5);
        // WHEN
        final int returned = underTest.updateAndGetPosition(null);
        // THEN
        Assert.assertEquals(returned, expected);
    }

}
