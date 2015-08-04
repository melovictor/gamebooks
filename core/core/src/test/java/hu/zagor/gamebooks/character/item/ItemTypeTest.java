package hu.zagor.gamebooks.character.item;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ItemType}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemTypeTest {

    public void testIsEquipableWhenItemIsCommonShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = ItemType.common.isEquipable();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsEquipableWhenItemIsShadowShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = ItemType.shadow.isEquipable();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsEquipableWhenItemIsInfoShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = ItemType.info.isEquipable();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testGetMaxEquippedWhenRingsShouldReturnTwo() {
        // GIVEN
        // WHEN
        final int returned = ItemType.ring.getMaxEquipped();
        // THEN
        Assert.assertEquals(returned, 2);
    }

    public void testGetMaxEquippedWhenWeaponShouldReturnOne() {
        // GIVEN
        // WHEN
        final int returned = ItemType.weapon1.getMaxEquipped();
        // THEN
        Assert.assertEquals(returned, 1);
    }

    public void testGetDisallowsWhenRingsShouldReturnEmptyArray() {
        // GIVEN
        // WHEN
        final ItemType[] returned = ItemType.ring.getDisallows();
        // THEN
        Assert.assertEquals(returned, new ItemType[]{});
    }

    public void testGetDisallowsWhenWeapon2ShouldReturnArrayWithWeapon1AndShield() {
        // GIVEN
        // WHEN
        final ItemType[] returned = ItemType.weapon2.getDisallows();
        // THEN
        Assert.assertEqualsNoOrder(returned, new ItemType[]{ItemType.weapon1, ItemType.shield});
    }

    public void testIsConsumableWhenProvisionShouldReturnTrue() {
        // GIVEN
        // WHEN
        final boolean returned = ItemType.provision.isConsumable();
        // THEN
        Assert.assertTrue(returned);
    }

    public void testIsConsumableWhenWeapon1ShouldReturnFalse() {
        // GIVEN
        // WHEN
        final boolean returned = ItemType.weapon1.isConsumable();
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsInvisibleWhenShadowItemShouldBeTrue() {
        // GIVEN
        // WHEN
        final ItemType returned = ItemType.shadow;
        // THEN
        Assert.assertTrue(returned.isInvisible());
    }

    public void testIsInvisibleWhenImmediateItemShouldBeTrue() {
        // GIVEN
        // WHEN
        final ItemType returned = ItemType.immediate;
        // THEN
        Assert.assertTrue(returned.isInvisible());
    }

    public void testIsInvisibleWhenWeapon1ItemShouldBeTrue() {
        // GIVEN
        // WHEN
        final ItemType returned = ItemType.weapon1;
        // THEN
        Assert.assertFalse(returned.isInvisible());
    }
}
