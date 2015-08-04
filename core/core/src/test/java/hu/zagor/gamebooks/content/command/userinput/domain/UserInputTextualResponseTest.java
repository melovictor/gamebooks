package hu.zagor.gamebooks.content.command.userinput.domain;

import hu.zagor.gamebooks.content.ParagraphData;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link UserInputTextualResponse}.
 * @author Tamas_Szekeres
 */

@Test
public class UserInputTextualResponseTest {

    private static final String UNACCEPTABLE_RESPONSE = "false answer";
    private static final String ACCEPTABLE_RESPONSE = "response";

    public void testIsFallbackWhenDefaultConstructorUsedShouldCreateFallbackUserInputResponse() {
        // GIVEN
        final UserInputTextualResponse underTest = new UserInputTextualResponse();
        // WHEN
        final boolean returned = underTest.isFallback();
        // THEN
        Assert.assertTrue(returned);
    }

    public void testIsFallbackWhenNonDefaultConstructorUsedShouldCreateNonFallbackUserInputResponse() {
        // GIVEN
        final UserInputTextualResponse underTest = new UserInputTextualResponse(ACCEPTABLE_RESPONSE);
        // WHEN
        final boolean returned = underTest.isFallback();
        // THEN
        Assert.assertFalse(returned);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenResponseIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new UserInputTextualResponse(null).getClass();
        // THEN throws exception
    }

    public void testMatchesWhenAnswerIsSameAsResponseShouldReturnTrue() {
        // GIVEN
        final UserInputTextualResponse underTest = new UserInputTextualResponse(ACCEPTABLE_RESPONSE);
        // WHEN
        final boolean returned = underTest.matches(ACCEPTABLE_RESPONSE);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testMatchesWhenAnswerIsNotSameAsResponseShouldReturnFalse() {
        // GIVEN
        final UserInputTextualResponse underTest = new UserInputTextualResponse(ACCEPTABLE_RESPONSE);
        // WHEN
        final boolean returned = underTest.matches(UNACCEPTABLE_RESPONSE);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testGetDataShouldReturnSettedObject() {
        // GIVEN
        final UserInputTextualResponse underTest = new UserInputTextualResponse();
        final ParagraphData data = EasyMock.createStrictMock(ParagraphData.class);
        underTest.setData(data);
        // WHEN
        final ParagraphData returned = underTest.getData();
        // THEN
        Assert.assertSame(returned, data);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMatchesWhenAnswerIsNullShouldThrowException() {
        // GIVEN
        final UserInputTextualResponse underTest = new UserInputTextualResponse(ACCEPTABLE_RESPONSE);
        // WHEN
        underTest.matches(null);
        // THEN throws exception
    }

}
