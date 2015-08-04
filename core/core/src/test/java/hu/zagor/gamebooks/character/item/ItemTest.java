package hu.zagor.gamebooks.character.item;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Item}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemTest {

    private static final int NON_ITEM_OBJECT = 99;
    private static final String OTHER_ID = "3002";
    private static final String ID = "3001";
    private static final String NAME = "Key";
    private static final ItemType ITEM_TYPE = ItemType.common;

    private final Item underTest = new Item(ID, NAME, ITEM_TYPE);

    public void testConstructorWhenDefaultIsUsedShouldCreateEmptyItem() {
        // GIVEN
        // WHEN
        final Item returned = new Item();
        // THEN
        Assert.assertEquals(returned.getId(), "");
        Assert.assertEquals(returned.getName(), "");
        Assert.assertEquals(returned.getItemType(), ItemType.common);
        Assert.assertFalse(returned.getEquipInfo().isEquippable());
        Assert.assertTrue(returned.getEquipInfo().isEquipped());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenIdIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new Item(null, NAME, ITEM_TYPE).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenNameIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new Item(ID, null, ITEM_TYPE).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenItemTypeIsNullShouldThrowException() {
        // GIVEN
        // WHEN
        new Item(ID, NAME, null).getClass();
        // THEN throws exception
    }

    public void testGetSubTypeShouldReturnWhatWasSet() {
        // GIVEN
        // WHEN
        underTest.setSubType(WeaponSubType.blunt);
        // THEN
        Assert.assertEquals(underTest.getSubType(), WeaponSubType.blunt);
    }

    public void testGetAmountShouldReturnWhatWasSet() {
        // GIVEN
        // WHEN
        underTest.setAmount(9);
        // THEN
        Assert.assertEquals(underTest.getAmount(), 9);
    }

    public void testCloneShouldReturnEqualButNotSameObject() throws CloneNotSupportedException {
        // GIVEN
        // WHEN
        final Item returned = underTest.clone();
        // THEN
        Assert.assertNotSame(returned, underTest);
        Assert.assertEquals(returned.getId(), underTest.getId());
        Assert.assertEquals(returned.getName(), underTest.getName());
        Assert.assertSame(returned.getItemType(), underTest.getItemType());
        Assert.assertNotSame(returned.getEquipInfo(), underTest.getEquipInfo());
        Assert.assertEquals(returned.getEquipInfo().isEquippable(), underTest.getEquipInfo().isEquippable());
        Assert.assertEquals(returned.getEquipInfo().isEquipped(), underTest.getEquipInfo().isEquipped());
    }

    public void testEqualsWhenSameIdProvidedShouldReturnTrue() {
        // GIVEN
        // WHEN
        final boolean returned = underTest.equals(ID);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testEqualsWhenDifferentIdProvidedShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = underTest.equals(OTHER_ID);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testEqualsWhenSelfProvidedShouldReturnTrue() {
        // GIVEN
        // WHEN
        final boolean returned = underTest.equals(underTest);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testEqualsWhenCloneProvidedShouldReturnTrue() throws CloneNotSupportedException {
        // GIVEN
        // WHEN
        final boolean returned = underTest.equals(underTest.clone());
        // THEN
        Assert.assertTrue(returned);
    }

    public void testEqualsWhenItemWithDifferentIdProvidedShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = underTest.equals(new Item(OTHER_ID, NAME, ITEM_TYPE));
        // THEN
        Assert.assertFalse(returned);
    }

    public void testEqualsWhenRandomObjectProvidedShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = underTest.equals(NON_ITEM_OBJECT);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testHashCodeShouldReturnSameForOriginalAndClonedItem() throws CloneNotSupportedException {
        // GIVEN
        final int originalHash = underTest.hashCode();
        // WHEN
        final int clonedHash = underTest.clone().hashCode();
        // THEN
        Assert.assertEquals(clonedHash, originalHash);
    }

}
