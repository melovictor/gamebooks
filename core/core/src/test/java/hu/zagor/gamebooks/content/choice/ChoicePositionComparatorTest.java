package hu.zagor.gamebooks.content.choice;

import static org.easymock.EasyMock.expect;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ChoicePositionComparatorTest}.
 * @author Tamas_Szekeres
 */
@Test
public class ChoicePositionComparatorTest {

    private ChoicePositionComparator underTest;
    private IMocksControl mockControl;
    private Choice choiceA;
    private Choice choiceB;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        choiceA = mockControl.createMock(Choice.class);
        choiceB = mockControl.createMock(Choice.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ChoicePositionComparator();
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCompareWhenChoiceAIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.compare(null, choiceB);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCompareWhenChoiceBIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.compare(choiceA, null);
        // THEN throws exception
    }

    public void testCompareWhenChoiceAIsSameAsChoiceBShouldReturnZero() {
        // GIVEN
        final int expected = 0;
        expect(choiceA.getPosition()).andReturn(1).times(2);
        mockControl.replay();
        // WHEN
        final int returned = underTest.compare(choiceA, choiceA);
        // THEN
        Assert.assertEquals(returned, expected);
    }

    public void testCompareWhenChoiceAIsBiggerThanChoiceBShouldReturnPositive() {
        // GIVEN
        expect(choiceA.getPosition()).andReturn(4);
        expect(choiceB.getPosition()).andReturn(1);
        mockControl.replay();
        // WHEN
        final int returned = underTest.compare(choiceA, choiceB);
        // THEN
        Assert.assertTrue(returned > 0);
    }

    public void testCompareWhenChoiceAIsSmallerThanChoiceBShouldReturnNegative() {
        // GIVEN
        expect(choiceA.getPosition()).andReturn(1);
        expect(choiceB.getPosition()).andReturn(4);
        mockControl.replay();
        // WHEN
        final int returned = underTest.compare(choiceA, choiceB);
        // THEN
        Assert.assertTrue(returned < 0);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
