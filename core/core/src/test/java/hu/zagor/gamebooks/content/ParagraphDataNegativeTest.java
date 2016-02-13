package hu.zagor.gamebooks.content;

import hu.zagor.gamebooks.content.choice.ChoicePositionComparator;
import hu.zagor.gamebooks.content.choice.DefaultChoiceSet;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ParagraphData}.
 * @author Tamas_Szekeres
 */
@Test
public class ParagraphDataNegativeTest {

    private ParagraphData underTest;
    private ChoicePositionComparator choiceComparator;
    private Logger logger;
    private IMocksControl mockControl;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);
        choiceComparator = mockControl.createMock(ChoicePositionComparator.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ParagraphData();
        underTest.setText("");
        Whitebox.setInternalState(underTest, "logger", logger);
        underTest.setChoices(new DefaultChoiceSet(choiceComparator));
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCalculateValidElementsWhenParagraphIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.calculateValidEvents(null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveValidItemWhenItemIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.removeValidItem(null, 10);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveValidItemWhenAmountIsZeroShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.removeValidItem("3002", 0);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveValidItemWhenAmountIsNegativeShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.removeValidItem("3002", -10);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddGatheredItemWhenItemIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addGatheredItem(null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddLostItemWhenItemIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addLostItem(null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddCommandItemWhenCommandIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addCommand(null);
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
