package hu.zagor.gamebooks.content.gathering;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link GatheredLostItem}.
 * @author Tamas_Szekeres
 */
@Test
public class GatheredLostItemTest {

    private static final int TEST_AMOUNT = 10;
    private static final String ID = "id";

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenIdIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new GatheredLostItem(null, TEST_AMOUNT, 0).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenIdIsEmptyShouldThrowException() {
        // GIVEN
        // WHEN
        new GatheredLostItem("", TEST_AMOUNT, 0).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenAmountAndDoseAreZeroShouldThrowException() {
        // GIVEN
        // WHEN
        new GatheredLostItem(ID, 0, 0).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenAmountIsNegativeShouldThrowException() {
        // GIVEN
        // WHEN
        new GatheredLostItem(ID, -TEST_AMOUNT, 1).getClass();
        // THEN throws exception
    }

    public void testConstructorDefaultOneShouldCreateEmptyObject() {
        // GIVEN
        // WHEN
        final GatheredLostItem returned = new GatheredLostItem();
        // THEN
        Assert.assertEquals(returned.getAmount(), 0);
        Assert.assertEquals(returned.getId(), "");
        Assert.assertEquals(returned.getDose(), 0);
    }

    public void testGetIdShouldReturnId() {
        // GIVEN
        final GatheredLostItem underTest = new GatheredLostItem(ID, TEST_AMOUNT, 0);
        // WHEN
        final String returned = underTest.getId();
        // THEN
        Assert.assertEquals(returned, ID);
    }

    public void testGetAmountShouldReturnAmount() {
        // GIVEN
        final GatheredLostItem underTest = new GatheredLostItem(ID, TEST_AMOUNT, 0);
        // WHEN
        final int returned = underTest.getAmount();
        // THEN
        Assert.assertEquals(returned, TEST_AMOUNT);
    }

    public void testGetDoseShouldReturnDose() {
        // GIVEN
        final GatheredLostItem underTest = new GatheredLostItem(ID, 0, 3);
        // WHEN
        final int returned = underTest.getDose();
        // THEN
        Assert.assertEquals(returned, 3);
    }

    public void testCloneShouldReturnClonedItem() throws CloneNotSupportedException {
        // GIVEN
        final GatheredLostItem underTest = new GatheredLostItem(ID, TEST_AMOUNT, 0);
        // WHEN
        final GatheredLostItem returned = underTest.clone();
        // THEN
        Assert.assertEquals(returned.getId(), ID);
        Assert.assertEquals(returned.getAmount(), TEST_AMOUNT);
        Assert.assertEquals(returned.getDose(), 0);
    }

}
