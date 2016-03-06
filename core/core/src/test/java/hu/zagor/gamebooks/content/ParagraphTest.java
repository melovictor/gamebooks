package hu.zagor.gamebooks.content;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.content.choice.ChoicePositionCounter;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import java.util.List;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Paragraph}.
 * @author Tamas_Szekeres
 */
@Test
public class ParagraphTest {

    private static final String ITEM_ID = "3002";
    private static final String PARAGRAP_ID = "111";
    private static final String DISPLAY_ID = "32d";
    private Paragraph underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private ParagraphData paragraphData;
    @Mock private GatheredLostItem glItem;
    @Mock private ChoicePositionCounter posCounter;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new Paragraph();
        underTest.setData(paragraphData);
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
        underTest.removeValidItem(ITEM_ID, 0);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveValidItemWhenAmountIsNegativeShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.removeValidItem(ITEM_ID, -10);
        // THEN throws exception
    }

    public void testRemoveValidItemWhenValidItemsDoesNotContainIdToRemoveShouldDoNothing() {
        // GIEN
        mockControl.replay();
        // WHEN
        underTest.removeValidItem(ITEM_ID, 1);
        // THEN
        Assert.assertTrue(underTest.getValidItems().isEmpty());
    }

    public void testRemoveValidItemWhenRemovesAllAvailableItemsShouldRemoveElementAndNotifyData() {
        // GIEN
        underTest.addValidItem(ITEM_ID, 1);
        paragraphData.removeValidItem(ITEM_ID, 1);
        mockControl.replay();
        // WHEN
        underTest.removeValidItem(ITEM_ID, 1);
        // THEN
        Assert.assertTrue(underTest.getValidItems().isEmpty());
    }

    public void testRemoveValidItemWhenRemovesMoreItemsThanIsAvailableShouldRemoveElementAndNotifyData() {
        // GIEN
        underTest.addValidItem(ITEM_ID, 1);
        paragraphData.removeValidItem(ITEM_ID, 3);
        mockControl.replay();
        // WHEN
        underTest.removeValidItem(ITEM_ID, 3);
        // THEN
        Assert.assertTrue(underTest.getValidItems().isEmpty());
    }

    public void testRemoveValidItemWhenRemovesLessItemsThanIsAvailableShouldAlterAmountsAndNotifyData() {
        // GIEN
        underTest.addValidItem(ITEM_ID, 3);
        paragraphData.removeValidItem(ITEM_ID, 1);
        mockControl.replay();
        // WHEN
        underTest.removeValidItem(ITEM_ID, 1);
        // THEN
        Assert.assertEquals(underTest.getValidItems().size(), 1);
        Assert.assertEquals((int) underTest.getValidItems().get(ITEM_ID), 2);
    }

    public void testCloneShoulcCloneParagraphData() throws CloneNotSupportedException {
        // GIVEN
        final ParagraphData clonedData = mockControl.createMock(ParagraphData.class);
        underTest.setData(paragraphData);
        expect(paragraphData.clone()).andReturn(clonedData);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.clone();
        // THEN
        Assert.assertSame(returned.getData(), clonedData);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new Paragraph(null, PARAGRAP_ID, Integer.MAX_VALUE).getClass();
        // THEN throws exception
    }

    public void testConstructorWhenDisplayIdIsNullShouldCreateParagraphWithSameIdAndDisplayId() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final Paragraph returned = new Paragraph(PARAGRAP_ID, null, Integer.MAX_VALUE);
        // THEN
        Assert.assertEquals(returned.getId(), PARAGRAP_ID);
        Assert.assertEquals(returned.getDisplayId(), PARAGRAP_ID);
    }

    public void testConstructorWhenDisplayIdIsNotNullShouldCreateParagraphWithDifferentIdAndDisplayId() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final Paragraph returned = new Paragraph(PARAGRAP_ID, DISPLAY_ID, Integer.MAX_VALUE);
        // THEN
        Assert.assertEquals(returned.getId(), PARAGRAP_ID);
        Assert.assertEquals(returned.getDisplayId(), DISPLAY_ID);
    }

    public void testIsValidMoveWhenMoveIsNotValidShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isValidMove(PARAGRAP_ID);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsValidMoveWhenMoveIsValidShouldReturnTrue() {
        // GIVEN
        underTest.addValidMove(PARAGRAP_ID);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isValidMove(PARAGRAP_ID);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testCalculateValidEventsShouldCallDataCalculateValidEventMethod() {
        // GIVEN
        paragraphData.calculateValidEvents(underTest);
        mockControl.replay();
        // WHEN
        underTest.calculateValidEvents();
        // THEN
        Assert.assertTrue(true);
    }

    public void testCalculateValidEventsWhenValidElementsContainDataShouldClearThemAndRecalculate() {
        // GIVEN
        paragraphData.calculateValidEvents(underTest);
        underTest.addValidMove("111");
        mockControl.replay();
        // WHEN
        underTest.calculateValidEvents();
        // THEN
        Assert.assertFalse(underTest.isValidMove("111"));
    }

    public void testIsValidItemWhenItemIdIsNotValidShouldReturnFalse() {
        // GIVEN
        expect(glItem.getId()).andReturn(ITEM_ID);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isValidItem(glItem);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsValidItemWhenItemIdIsValidButAmountIsNotShouldReturnFalse() {
        // GIVEN
        underTest.addValidItem(ITEM_ID, 1);
        expect(glItem.getId()).andReturn(ITEM_ID);
        expect(glItem.getAmount()).andReturn(2);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isValidItem(glItem);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsValidItemWhenItemIdAndAmountAreValidShouldReturnTrue() {
        // GIVEN
        underTest.addValidItem(ITEM_ID, 2);
        expect(glItem.getId()).andReturn(ITEM_ID);
        expect(glItem.getAmount()).andReturn(1);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isValidItem(glItem);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testGetPositionCounterShouldReturnPositionCounter() {
        // GIVEN
        Whitebox.setInternalState(underTest, "positionCounter", posCounter);
        mockControl.replay();
        // WHEN
        final ChoicePositionCounter returned = underTest.getPositionCounter();
        // THEN
        Assert.assertNotNull(returned);
    }

    public void testGetActionsShouldReturnSettedValue() {
        // GIVEN
        underTest.setActions(11);
        mockControl.replay();
        // WHEN
        final int returned = underTest.getActions();
        // THEN
        Assert.assertEquals(returned, 11);
    }

    public void testClearValidMovesWhenCalledShouldNotRemainPreviouslySetValidMoves() {
        // GIVEN
        underTest.addValidMove(PARAGRAP_ID);
        mockControl.replay();
        // WHEN
        underTest.clearValidMoves();
        // THEN
        Assert.assertFalse(underTest.isValidMove(PARAGRAP_ID));
    }

    public void testAddValidItemWhenAddingValidItemAfterItHasBeenAddedShouldIncreaseAmountOfOriginal() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addValidItem(ITEM_ID, 4);
        underTest.addValidItem(ITEM_ID, 2);
        // THEN
        Assert.assertEquals(underTest.getValidItems().get(ITEM_ID).intValue(), 6);
    }

    public void testGetItemsToProcessShouldReturnProcessItemArray() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final List<ProcessableItemHolder> returned = underTest.getItemsToProcess();
        // THEN
        Assert.assertSame(returned, Whitebox.getInternalState(underTest, "itemsToProcess"));
    }

}
