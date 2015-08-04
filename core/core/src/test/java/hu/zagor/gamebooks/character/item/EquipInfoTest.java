package hu.zagor.gamebooks.character.item;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link EquipInfo}.
 * @author Tamas_Szekeres
 */
@Test
public class EquipInfoTest {

    public void testConstructorWhenItemIsEquippableShouldNotBeEquippedByDefault() {
        // GIVEN
        // WHEN
        final EquipInfo underTest = new EquipInfo(ItemType.weapon1);
        // THEN
        Assert.assertFalse(underTest.isEquipped());
    }

    public void testConstructorWhenItemIsNotEquippableShouldBeEquippedByDefault() {
        // GIVEN
        // WHEN
        final EquipInfo underTest = new EquipInfo(ItemType.common);
        // THEN
        Assert.assertTrue(underTest.isEquipped());
    }

    public void testSetEquippedWhenItemIsEquippableAndToBeEquippedShouldBeEquipped() {
        // GIVEN
        final EquipInfo underTest = new EquipInfo(ItemType.weapon1);
        // WHEN
        underTest.setEquipped(true);
        // THEN
        Assert.assertTrue(underTest.isEquipped());
    }

    public void testSetEquippedWhenItemIsEquippableAndToBeUnequippedShouldBeUnequipped() {
        // GIVEN
        final EquipInfo underTest = new EquipInfo(ItemType.weapon1);
        // WHEN
        underTest.setEquipped(false);
        // THEN
        Assert.assertFalse(underTest.isEquipped());
    }

    public void testSetEquippedWhenItemIsNotEquippableAndToBeUnequippedShouldStayEquipped() {
        // GIVEN
        final EquipInfo underTest = new EquipInfo(ItemType.common);
        // WHEN
        underTest.setEquipped(false);
        // THEN
        Assert.assertTrue(underTest.isEquipped());
    }

    public void testIsRemovableShouldReturnWhatIsSet() {
        // GIVEN
        final EquipInfo underTest = new EquipInfo(ItemType.common);
        underTest.setRemovable(true);
        // WHEN
        final boolean returned = underTest.isRemovable();
        // THEN
        Assert.assertTrue(returned);
    }

    public void testSetEquippedWhenItemIsConsumableAndBeingEquippedEquippedShouldStayFalse() {
        // GIVEN
        final EquipInfo underTest = new EquipInfo(ItemType.common);
        underTest.setConsumable(true);
        // WHEN
        underTest.setEquipped(true);
        // THEN
        Assert.assertFalse(underTest.isEquipped());
    }

    public void testSetEquippedWhenItemIsConsumableAndBeingRemovedEquippedShouldStayFalse() {
        // GIVEN
        final EquipInfo underTest = new EquipInfo(ItemType.common);
        underTest.setConsumable(true);
        // WHEN
        underTest.setEquipped(false);
        // THEN
        Assert.assertFalse(underTest.isEquipped());
    }

    public void testSetConsumableWhenItemIsConsumableShouldUnequipIt() {
        // GIVEN
        final EquipInfo underTest = new EquipInfo(ItemType.common);
        // WHEN
        underTest.setConsumable(true);
        // THEN
        Assert.assertFalse(underTest.isEquipped());
    }

    public void testIsConsumableShouldReturnProperState() {
        // GIVEN
        final EquipInfo underTest = new EquipInfo(ItemType.common);
        // WHEN
        underTest.setConsumable(true);
        // THEN
        Assert.assertTrue(underTest.isConsumable());
    }

    public void testSetConsumableWhenItemIsNotConsumableShouldLeaveItUnequipped() {
        // GIVEN
        final EquipInfo underTest = new EquipInfo(ItemType.common);
        underTest.setConsumable(true);
        // WHEN
        underTest.setConsumable(false);
        // THEN
        Assert.assertFalse(underTest.isEquipped());
    }

    public void testSetEquippedWhenItemIsEquippableAndConsumableShouldLeaveState() {
        // GIVEN
        final EquipInfo underTest = new EquipInfo(ItemType.weapon1);
        underTest.setConsumable(true);
        // WHEN
        underTest.setEquipped(true);
        // THEN
        Assert.assertFalse(underTest.isEquipped());
    }
}
