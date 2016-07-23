package hu.zagor.gamebooks.character.handler.item;

import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfCharacterItemHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class FfCharacterItemHandlerTest {

    private FfCharacterItemHandler underTest;
    private FfCharacter character;
    private FfItem normalItem;
    private FfItem defaultWeapon;
    private FfItem selectedWeapon;
    private FfItem unselectedWeaponA;
    private FfItem unselectedWeaponB;

    @BeforeClass
    public void setUpClass() {
        underTest = new FfCharacterItemHandler();
        character = new FfCharacter();
        character.setBackpackSize(99);
    }

    @BeforeMethod
    public void setUpMethod() {
        normalItem = new FfItem("3001", "Pack of Cards", ItemType.common);
        defaultWeapon = new FfItem("defWpn", "Fist", ItemType.weapon1);
        selectedWeapon = new FfItem("1001", "Sword", ItemType.weapon1);
        selectedWeapon.getEquipInfo().setEquipped(true);
        unselectedWeaponA = new FfItem("1002", "Mace", ItemType.weapon1);
        unselectedWeaponB = new FfItem("1003", "Spear", ItemType.weapon2);
        character.getEquipment().clear();
        character.getEquipment().add(defaultWeapon);
        character.getEquipment().add(normalItem);
    }

    public void testGetEquippedWeaponWhenHasNonDefaultEquippedWeaponShouldReturnIt() {
        // GIVEN
        character.getEquipment().add(selectedWeapon);
        // WHEN
        final FfItem returned = underTest.getEquippedWeapon(character);
        // THEN
        Assert.assertSame(returned, selectedWeapon);
    }

    public void testGetEquippedWeaponWhenHasNonDefaultNonEquippedWeaponShouldReturnAndEquipIt() {
        // GIVEN
        character.getEquipment().add(unselectedWeaponA);
        character.getEquipment().add(unselectedWeaponB);
        // WHEN
        final FfItem returned = underTest.getEquippedWeapon(character);
        // THEN
        Assert.assertSame(returned, unselectedWeaponA);
        Assert.assertTrue(unselectedWeaponA.getEquipInfo().isEquipped());
    }

    public void testGetEquippedWeaponWhenOnlyHasDefaultWeaponShouldReturnAndEquipIt() {
        // GIVEN
        // WHEN
        final FfItem returned = underTest.getEquippedWeapon(character);
        // THEN
        Assert.assertSame(returned, defaultWeapon);
        Assert.assertTrue(defaultWeapon.getEquipInfo().isEquipped());
    }

    public void testGetEquippedWeaponWhenHasNoWeaponShouldReturnNull() {
        // GIVEN
        character.getEquipment().clear();
        // WHEN
        final FfItem returned = underTest.getEquippedWeapon(character);
        // THEN
        Assert.assertNull(returned);
    }

    public void testRemoveItemWhenItemIsRemovedShouldResetSelectedWeaponAndRemoveItem() {
        // GIVEN
        // WHEN
        underTest.removeItem(character, "3001", 1);
        // THEN
        Assert.assertFalse(character.getEquipment().contains(normalItem));
        Assert.assertTrue(defaultWeapon.getEquipInfo().isEquipped());
    }

    public void testSetItemEquipStateWhenTryingToUnequipCommonItemShouldDoNothing() {
        // GIVEN
        // WHEN
        underTest.setItemEquipState(character, "3001", false);
        // THEN
        Assert.assertTrue(normalItem.getEquipInfo().isEquipped());
    }

    public void testSetItemEquipStateWhenTryingToUnequipUnequippedWeaponButNoOtherWeaponIsPresentShouldBecomeActive() {
        // GIVEN
        character.getEquipment().add(unselectedWeaponA);
        // WHEN
        underTest.setItemEquipState(character, "1002", false);
        // THEN
        Assert.assertTrue(unselectedWeaponA.getEquipInfo().isEquipped());
    }

    public void testSetItemEquipStateWhenTryingToUnequipUnequippedWeaponAndOtherWeaponIsAvailableAndEquippedShouldDoNothing() {
        // GIVEN
        character.getEquipment().add(selectedWeapon);
        character.getEquipment().add(unselectedWeaponA);
        // WHEN
        underTest.setItemEquipState(character, "1002", false);
        // THEN
        Assert.assertFalse(unselectedWeaponA.getEquipInfo().isEquipped());
        Assert.assertTrue(selectedWeapon.getEquipInfo().isEquipped());
    }

    public void testSetItemEquipStateWhenTryingToEquipUnequippedWeaponShouldEquipAndDisequipCurrentWeapon() {
        // GIVEN
        defaultWeapon.getEquipInfo().setEquipped(true);
        character.getEquipment().add(unselectedWeaponA);
        // WHEN
        underTest.setItemEquipState(character, "1002", true);
        // THEN
        Assert.assertTrue(unselectedWeaponA.getEquipInfo().isEquipped());
        Assert.assertFalse(defaultWeapon.getEquipInfo().isEquipped());
    }

    public void testSetItemEquipStateWhenTryingToEquipNewItemWHenNonRemovableWeaponIsAlreadyEquippedShouldDoNothing() {
        // GIVEN
        character.getEquipment().add(selectedWeapon);
        selectedWeapon.getEquipInfo().setRemovable(false);
        // WHEN
        underTest.setItemEquipState(character, "defWpn", true);
        // THEN
        Assert.assertTrue(selectedWeapon.getEquipInfo().isEquipped());
        Assert.assertFalse(defaultWeapon.getEquipInfo().isEquipped());
    }

    public void testSetItemEquipStateWhenTryingToUnequipNonRemovableWeaponShouldDoNothing() {
        // GIVEN
        character.getEquipment().add(selectedWeapon);
        selectedWeapon.getEquipInfo().setRemovable(false);
        // WHEN
        underTest.setItemEquipState(character, "1001", false);
        // THEN
        Assert.assertTrue(selectedWeapon.getEquipInfo().isEquipped());
    }

    public void testSetItemEquipStateWhenTryingToEquipNonRemovableWeaponWhenWeHaveNothingElseShouldEquipIt() {
        // GIVEN
        character.getEquipment().add(unselectedWeaponA);
        unselectedWeaponA.getEquipInfo().setRemovable(false);
        // WHEN
        underTest.setItemEquipState(character, "1002", true);
        // THENm
        Assert.assertTrue(unselectedWeaponA.getEquipInfo().isEquipped());
    }

}
