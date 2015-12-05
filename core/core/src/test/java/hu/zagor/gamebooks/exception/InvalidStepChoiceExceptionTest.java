package hu.zagor.gamebooks.exception;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link InvalidStepChoiceException}.
 * @author Tamas_Szekeres
 */
@Test
public class InvalidStepChoiceExceptionTest {

    public void testConstructorWhenPositionIsSetShouldCreateClassWithProperMessage() {
        // GIVEN
        final InvalidStepChoiceException underTest = new InvalidStepChoiceException("15", 3);
        // WHEN
        final String returned = underTest.getMessage();
        // THEN
        Assert.assertEquals(returned, "User tried to move from paragraph '15' to choice at position '3'");
    }

    public void testConstructorWhenNewParagraphIdIsSetShouldCreateClassWithProperMessage() {
        // GIVEN
        final InvalidStepChoiceException underTest = new InvalidStepChoiceException("15", "96");
        // WHEN
        final String returned = underTest.getMessage();
        // THEN
        Assert.assertEquals(returned, "User tried to move from paragraph '15' to paragraph '96'");
    }

}
