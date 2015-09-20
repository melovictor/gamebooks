package hu.zagor.gamebooks.content.command.userinput.domain;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import hu.zagor.gamebooks.content.ParagraphData;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link UserInputNumericResponse}.
 * @author Tamas_Szekeres
 */
@Test
public class UserInputNumericResponseTest {

    private static final String UNACCEPTABLE_NONNUMBERIC_RESPONSE = "valami";
    private static final String UNACCEPTABLE_RESPONSE_HIGH = "99";
    private static final String UNACCEPTABLE_RESPONSE_LOW = "-99";
    private static final String ACCEPTABLE_RESPONSE = "5";
    private static final int MIN_BOUND = 1;
    private static final int MAX_BOUND = 10;

    public void testIsFallbackWhenDefaultConstructorUsedShouldCreateFallbackUserInputResponse() {
        // GIVEN
        final UserInputNumericResponse underTest = new UserInputNumericResponse();
        // WHEN
        final boolean returned = underTest.isFallback();
        // THEN
        Assert.assertTrue(returned);
    }

    public void testIsFallbackWhenNonDefaultConstructorUsedShouldCreateNonFallbackUserInputResponse() {
        // GIVEN
        final UserInputNumericResponse underTest = new UserInputNumericResponse(MIN_BOUND, MAX_BOUND);
        // WHEN
        final boolean returned = underTest.isFallback();
        // THEN
        Assert.assertFalse(returned);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenMinValueIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new UserInputNumericResponse(null, MAX_BOUND).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenMinValueIsGreaterThanMaxValueShouldThrowException() {
        // GIVEN
        // WHEN
        new UserInputNumericResponse(MAX_BOUND, MIN_BOUND).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenMaxValueIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new UserInputNumericResponse(MIN_BOUND, null).getClass();
        // THEN throws exception
    }

    public void testMatchesWhenAnswerIsInAcceptableRangeSameShouldReturnTrue() {
        // GIVEN
        final UserInputNumericResponse underTest = new UserInputNumericResponse(MIN_BOUND, MAX_BOUND);
        // WHEN
        final boolean returned = underTest.matches(ACCEPTABLE_RESPONSE);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testSetMinBoundShouldChangeMinBound() {
        // GIVEN
        final UserInputNumericResponse underTest = new UserInputNumericResponse(MIN_BOUND, MAX_BOUND);
        // WHEN
        underTest.setMinBound(3);
        // THEN
        Assert.assertEquals(underTest.getMinBound().intValue(), 3);
    }

    public void testSetMaxBoundShouldChangeMaxBound() {
        // GIVEN
        final UserInputNumericResponse underTest = new UserInputNumericResponse(MIN_BOUND, MAX_BOUND);
        // WHEN
        underTest.setMaxBound(8);
        // THEN
        Assert.assertEquals(underTest.getMaxBound().intValue(), 8);
    }

    public void testMatchesWhenAnswerIsAboveAcceptableRangeShouldReturnFalse() {
        // GIVEN
        final UserInputNumericResponse underTest = new UserInputNumericResponse(MIN_BOUND, MAX_BOUND);
        // WHEN
        final boolean returned = underTest.matches(UNACCEPTABLE_RESPONSE_HIGH);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testMatchesWhenAnswerIsBelowAcceptableRangeShouldReturnFalse() {
        // GIVEN
        final UserInputNumericResponse underTest = new UserInputNumericResponse(MIN_BOUND, MAX_BOUND);
        // WHEN
        final boolean returned = underTest.matches(UNACCEPTABLE_RESPONSE_LOW);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testMatchesWhenAnswerIsNotNumericShouldReturnFalse() {
        // GIVEN
        final UserInputNumericResponse underTest = new UserInputNumericResponse(MIN_BOUND, MAX_BOUND);
        // WHEN
        final boolean returned = underTest.matches(UNACCEPTABLE_NONNUMBERIC_RESPONSE);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testGetDataShouldReturnSettedObject() {
        // GIVEN
        final UserInputNumericResponse underTest = new UserInputNumericResponse();
        final ParagraphData data = createStrictMock(ParagraphData.class);
        underTest.setData(data);
        replay(data);
        // WHEN
        final ParagraphData returned = underTest.getData();
        // THEN
        verify(data);
        Assert.assertSame(returned, data);
    }

    public void testCloneShouldReturnClonedObject() throws CloneNotSupportedException {
        // GIVEN
        final UserInputNumericResponse underTest = new UserInputNumericResponse(MIN_BOUND, MAX_BOUND);
        final ParagraphData data = createStrictMock(ParagraphData.class);
        final ParagraphData clonedData = createStrictMock(ParagraphData.class);
        underTest.setData(data);
        expect(data.clone()).andReturn(clonedData);
        replay(data, clonedData);
        // WHEN
        final UserInputNumericResponse returned = (UserInputNumericResponse) underTest.clone();
        // THEN
        verify(data, clonedData);
        Assert.assertSame(returned.getData(), clonedData);
        Assert.assertEquals(returned.getMaxBound().intValue(), MAX_BOUND);
        Assert.assertEquals(returned.getMinBound().intValue(), MIN_BOUND);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMatchesWhenAnswerIsNullShouldThrowException() {
        // GIVEN
        final UserInputNumericResponse underTest = new UserInputNumericResponse(MIN_BOUND, MAX_BOUND);
        // WHEN
        underTest.matches(null);
        // THEN throws exception
    }

}
