package hu.zagor.gamebooks.books.random;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link DefaultRandomNumberGenerator}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultRandomNumberGeneratorTest {

    private static final List<Integer> RESULT_LIST = new ArrayList<>();
    private DefaultRandomNumberGenerator underTest;
    private IMocksControl mockControl;
    private EnvironmentDetector environmentDetector;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        environmentDetector = mockControl.createMock(EnvironmentDetector.class);
        RESULT_LIST.add(3);
        RESULT_LIST.add(2);
        RESULT_LIST.add(6);
        RESULT_LIST.add(6);
        RESULT_LIST.add(1);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new DefaultRandomNumberGenerator(6);
        Whitebox.setInternalState(underTest, "environmentDetector", environmentDetector);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testConstructorWhenDefaultDiceSideIsZeroShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new DefaultRandomNumberGenerator(0).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetRandomNumberWhenDicePieceIsZeroShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getRandomNumber(0);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetRandomNumberWhenDiceSideIsZeroShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getRandomNumber(1, 0, 0);
        // THEN throws exception
    }

    public void testGetRandomNumberWhenOneDiceSixThrownWithoutAdditionShouldGenerateNumberBetweenOneAndSixIncl() {
        // GIVEN
        final Set<Integer> resultSet = new HashSet<>();
        expect(environmentDetector.isRecordState()).andReturn(false).times(10000);
        mockControl.replay();
        // WHEN
        for (int i = 0; i < 10000; i++) {
            resultSet.add(underTest.getRandomNumber(1)[0]);
        }
        // THEN
        for (int i = 1; i <= 6; i++) {
            Assert.assertTrue(resultSet.contains(i), "Result set must contain the number " + i);
            resultSet.remove(i);
        }
        Assert.assertEquals(resultSet.size(), 0);
    }

    public void testGetRandomNumberWhenTwoDiceSixThrownWithoutAdditionShouldGenerateNumberBetweenTwoAndTwelveIncl() {
        // GIVEN
        final Set<Integer> resultSet = new HashSet<>();
        expect(environmentDetector.isRecordState()).andReturn(false).times(20000);
        mockControl.replay();
        // WHEN
        for (int i = 0; i < 10000; i++) {
            resultSet.add(underTest.getRandomNumber(2)[0]);
        }
        // THEN
        for (int i = 2; i <= 12; i++) {
            Assert.assertTrue(resultSet.contains(i), "Result set must contain the number " + i);
            resultSet.remove(i);
        }
        Assert.assertEquals(resultSet.size(), 0);
    }

    public void testGetRandomNumberWhenTwoDiceSixPlusFiveThrownShouldGenerateNumberBetweenSevenAndSeventeenIncl() {
        // GIVEN
        final Set<Integer> resultSet = new HashSet<>();
        expect(environmentDetector.isRecordState()).andReturn(false).times(20000);
        mockControl.replay();
        // WHEN
        for (int i = 0; i < 10000; i++) {
            resultSet.add(underTest.getRandomNumber(2, 5)[0]);
        }
        // THEN
        for (int i = 7; i <= 17; i++) {
            Assert.assertTrue(resultSet.contains(i), "Result set must contain the number " + i);
            resultSet.remove(i);
        }
        Assert.assertEquals(resultSet.size(), 0);
    }

    public void testGetRandomNumberWhenTwoDiceFourMinusThreeThrownShouldGenerateNumberBetweenMinusOneAndFiveIncl() {
        // GIVEN
        final Set<Integer> resultSet = new HashSet<>();
        expect(environmentDetector.isRecordState()).andReturn(false).times(20000);
        mockControl.replay();
        // WHEN
        for (int i = 0; i < 10000; i++) {
            resultSet.add(underTest.getRandomNumber(2, 4, -3)[0]);
        }
        // THEN
        for (int i = -1; i <= 5; i++) {
            Assert.assertTrue(resultSet.contains(i), "Result set must contain the number " + i);
            resultSet.remove(i);
        }
        Assert.assertEquals(resultSet.size(), 0);
    }

    public void testGetRandomNumberWhenOneDiceOneThrownShouldGenerateNumberOne() {
        // GIVEN
        final Set<Integer> resultSet = new HashSet<>();
        expect(environmentDetector.isRecordState()).andReturn(false).times(10000);
        mockControl.replay();
        // WHEN
        for (int i = 0; i < 10000; i++) {
            resultSet.add(underTest.getRandomNumber(1, 1, 0)[0]);
        }
        // THEN
        for (int i = 1; i <= 1; i++) {
            Assert.assertTrue(resultSet.contains(i), "Result set must contain the number " + i);
            resultSet.remove(i);
        }
        Assert.assertEquals(resultSet.size(), 0);
    }

    public void testGetRandomNumberWhen1d6ConfigurationIsSetUpShouldGenerateNumbersBetweenOneAndSix() {
        // GIVEN
        final DiceConfiguration diceConfiguration = new DiceConfiguration(1, 1, 6);
        final Set<Integer> resultSet = new HashSet<>();
        expect(environmentDetector.isRecordState()).andReturn(false).times(10000);
        mockControl.replay();
        // WHEN
        for (int i = 0; i < 10000; i++) {
            resultSet.add(underTest.getRandomNumber(diceConfiguration)[0]);
        }
        // THEN
        for (int i = 1; i <= 6; i++) {
            Assert.assertTrue(resultSet.contains(i), "Result set must contain the number " + i);
            resultSet.remove(i);
        }
        Assert.assertEquals(resultSet.size(), 0);
    }

    public void testGetRandomNumberWhen2d6ConfigurationIsSetUpShouldGenerateNumbersBetweenTwoAndTwelve() {
        // GIVEN
        final DiceConfiguration diceConfiguration = new DiceConfiguration(2, 1, 6);
        final Set<Integer> resultSet = new HashSet<>();
        expect(environmentDetector.isRecordState()).andReturn(false).times(20000);
        mockControl.replay();
        // WHEN
        for (int i = 0; i < 10000; i++) {
            resultSet.add(underTest.getRandomNumber(diceConfiguration)[0]);
        }
        // THEN
        for (int i = 2; i <= 12; i++) {
            Assert.assertTrue(resultSet.contains(i), "Result set must contain the number " + i);
            resultSet.remove(i);
        }
        Assert.assertEquals(resultSet.size(), 0);
    }

    public void testGetRandomNumberWhen1d10WithZeroConfigurationIsSetUpShouldGenerateNumbersBetweenZeroAndNine() {
        // GIVEN
        final DiceConfiguration diceConfiguration = new DiceConfiguration(1, 0, 9);
        final Set<Integer> resultSet = new HashSet<>();
        expect(environmentDetector.isRecordState()).andReturn(false).times(10000);
        mockControl.replay();
        // WHEN
        for (int i = 0; i < 10000; i++) {
            resultSet.add(underTest.getRandomNumber(diceConfiguration)[0]);
        }
        // THEN
        for (int i = 0; i <= 9; i++) {
            Assert.assertTrue(resultSet.contains(i), "Result set must contain the number " + i);
            resultSet.remove(i);
        }
        Assert.assertEquals(resultSet.size(), 0);
    }

    public void testGetRandomNumberWhen1d10PlusSixWithTenConfigurationIsSetUpShouldGenerateNumbersBetweenSevenAndSixteen() {
        // GIVEN
        final DiceConfiguration diceConfiguration = new DiceConfiguration(1, 1, 10);
        final Set<Integer> resultSet = new HashSet<>();
        expect(environmentDetector.isRecordState()).andReturn(false).times(10000);
        mockControl.replay();
        // WHEN
        for (int i = 0; i < 10000; i++) {
            resultSet.add(underTest.getRandomNumber(diceConfiguration, 6)[0]);
        }
        // THEN
        for (int i = 7; i <= 16; i++) {
            Assert.assertTrue(resultSet.contains(i), "Result set must contain the number " + i);
            resultSet.remove(i);
        }
        Assert.assertEquals(resultSet.size(), 0);
    }

    public void testGetDefaultDiceSideShouldReturnDefaultDiceSide() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.getDefaultDiceSide();
        // THEN
        Assert.assertEquals(returned, 6);
    }

    @SuppressWarnings("unchecked")
    public void testSetUpThrowResultQueueWhenNotSeleniumTestingShouldDoNothing() {
        // GIVEN
        expect(environmentDetector.isSeleniumTesting()).andReturn(false);
        mockControl.replay();
        // WHEN
        underTest.setUpThrowResultQueue(RESULT_LIST);
        // THEN
        Assert.assertTrue(((List<Integer>) Whitebox.getInternalState(underTest, "throwResultQueue")).isEmpty());
    }

    @SuppressWarnings("unchecked")
    public void testGetRandomNumberWhenSeleniumTestingAndQueueIsNotEmptyShouldReturnPresetValue() {
        // GIVEN
        expect(environmentDetector.isSeleniumTesting()).andReturn(true);
        expect(environmentDetector.isSeleniumTesting()).andReturn(true);
        mockControl.replay();
        underTest.setUpThrowResultQueue(RESULT_LIST);
        // WHEN
        final int[] returned = underTest.getRandomNumber(1);
        // THEN
        Assert.assertEquals(returned[1], 3);
        Assert.assertEquals(((List<Integer>) Whitebox.getInternalState(underTest, "throwResultQueue")).get(0), Integer.valueOf(2));
    }

    @SuppressWarnings("unchecked")
    public void testGetRandomNumberWhenNotSeleniumTestingAndQueueIsNotEmptyShouldReturnRandomValue() {
        // GIVEN
        expect(environmentDetector.isSeleniumTesting()).andReturn(true);
        expect(environmentDetector.isSeleniumTesting()).andReturn(false);
        expect(environmentDetector.isRecordState()).andReturn(false);
        mockControl.replay();
        underTest.setUpThrowResultQueue(RESULT_LIST);
        // WHEN
        underTest.getRandomNumber(1);
        // THEN
        Assert.assertEquals(((List<Integer>) Whitebox.getInternalState(underTest, "throwResultQueue")).get(0), Integer.valueOf(3));
    }

    public void testGetRandomNumberWhenInRecordModeShouldReturnAndStoreRandomValue() {
        // GIVEN
        expect(environmentDetector.isRecordState()).andReturn(true);
        mockControl.replay();
        // WHEN
        final int[] returned = underTest.getRandomNumber(1);
        // THEN
        Assert.assertEquals(underTest.getThrownResults().get(0), Integer.valueOf(returned[1]));
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
