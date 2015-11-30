package hu.zagor.gamebooks.character.handler.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfCharacterItemHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class FfCharacterItemHandlerAddItemATest {
    @UnderTest private FfCharacterItemHandler underTest;
    @MockControl private IMocksControl mockControl;
    private FfCharacter character;
    @Inject private ItemFactory itemFactory;
    @Inject private Logger logger;
    private FfItem eSword;
    private FfItem ecSword;
    private FfItem dDagger;
    private FfItem eSpear;
    private FfItem dWarhammer;
    private FfItem eBroadsword;

    private FfItem cDice;
    private FfItem dRingA;
    private FfItem eRingB;
    private FfItem eRingC;
    private FfItem eRingD;
    private FfItem ecRingE;
    private FfItem eShield;

    @BeforeMethod
    public void setUpMethod() {
        character = new FfCharacter();
        character.setBackpackSize(99);
        mockControl.reset();

        eSword = new FfItem("1001", "Sword", ItemType.weapon1);
        eSword.getEquipInfo().setEquipped(true);
        dDagger = new FfItem("1002", "Dagger", ItemType.weapon1);
        eSpear = new FfItem("1003", "Spear", ItemType.weapon1);
        eSpear.getEquipInfo().setEquipped(true);
        dWarhammer = new FfItem("1004", "Warhammer", ItemType.weapon1);
        eBroadsword = new FfItem("1005", "Broadsword", ItemType.weapon2);
        eBroadsword.getEquipInfo().setEquipped(true);
        ecSword = new FfItem("1006", "Sword", ItemType.weapon1);
        ecSword.getEquipInfo().setEquipped(true);
        ecSword.getEquipInfo().setRemovable(false);

        cDice = new FfItem("3001", "Dice", ItemType.common);
        dRingA = new FfItem("3002", "Ring of Speed", ItemType.ring);
        eRingB = new FfItem("3003", "Ring of Luck", ItemType.ring);
        eRingB.getEquipInfo().setEquipped(true);
        eRingC = new FfItem("3004", "Ring of Strength", ItemType.ring);
        eRingC.getEquipInfo().setEquipped(true);
        eRingD = new FfItem("3005", "Ring of Light", ItemType.ring);
        eRingD.getEquipInfo().setEquipped(true);
        ecRingE = new FfItem("3006", "Cursed Ring of Weakness", ItemType.ring);
        ecRingE.getEquipInfo().setEquipped(true);
        ecRingE.getEquipInfo().setRemovable(false);
        eShield = new FfItem("3006", "Shield", ItemType.shield);
        eShield.getEquipInfo().setEquipped(true);
    }

    public void testAddItemWhenCharacterIsGivenACommonItemShouldBeAddedToTheEquipmentList() {
        // GIVEN
        logger.debug("Resolving item {} for addition.", cDice.getId());
        expect(itemFactory.resolveItem(cDice.getId())).andReturn(cDice);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, cDice.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 1);
        Assert.assertEquals(character.getEquipment().get(0), cDice);
    }

    public void testAddItemWhenCharacterIsGivenAOneHandedUnequippedWeaponWhenHeHasAnEquippedOneShouldBeAddedToTheEquipmentList() {
        // GIVEN
        character.getEquipment().add(eSword);
        logger.debug("Resolving item {} for addition.", dDagger.getId());
        expect(itemFactory.resolveItem(dDagger.getId())).andReturn(dDagger);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, dDagger.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 2);
        Assert.assertEquals(character.getEquipment().get(0), eSword);
        Assert.assertTrue(character.getEquipment().get(0).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(1), dDagger);
        Assert.assertFalse(character.getEquipment().get(1).getEquipInfo().isEquipped());
    }

    public void testAddItemWhenCharacterIsGivenAOneHandedEquippedWeaponWhenHeHasAnEquippedOneShouldBeAddedToTheEquipmentListAndOldOneShouldBeRemoved() {
        // GIVEN
        character.getEquipment().add(cDice);
        character.getEquipment().add(eSword);
        logger.debug("Resolving item {} for addition.", eSpear.getId());
        expect(itemFactory.resolveItem(eSpear.getId())).andReturn(eSpear);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, eSpear.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 3);
        Assert.assertEquals(character.getEquipment().get(0), cDice);
        Assert.assertEquals(character.getEquipment().get(1), eSword);
        Assert.assertFalse(character.getEquipment().get(1).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(2), eSpear);
        Assert.assertTrue(character.getEquipment().get(2).getEquipInfo().isEquipped());
    }

    public void testAddItemWhenCharacterIsGivenAOneHandedEquippedWeaponWhenHeDoesNotHaveAnEquippedOneShouldBeAddedToTheEquipmentList() {
        // GIVEN
        character.getEquipment().add(dDagger);
        logger.debug("Resolving item {} for addition.", eSpear.getId());
        expect(itemFactory.resolveItem(eSpear.getId())).andReturn(eSpear);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, eSpear.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 2);
        Assert.assertEquals(character.getEquipment().get(0), dDagger);
        Assert.assertFalse(character.getEquipment().get(0).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(1), eSpear);
        Assert.assertTrue(character.getEquipment().get(1).getEquipInfo().isEquipped());
    }

    public void testAddItemWhenCharacterIsGivenAOneHandedNonEquippedWeaponWhenHeDoesNotHaveAnEquippedOneShouldBeAddedToTheEquipmentList() {
        // GIVEN
        character.getEquipment().add(dDagger);
        logger.debug("Resolving item {} for addition.", dWarhammer.getId());
        expect(itemFactory.resolveItem(dWarhammer.getId())).andReturn(dWarhammer);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, dWarhammer.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 2);
        Assert.assertEquals(character.getEquipment().get(0), dDagger);
        Assert.assertFalse(character.getEquipment().get(0).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(1), dWarhammer);
        Assert.assertFalse(character.getEquipment().get(1).getEquipInfo().isEquipped());
    }

    public void testAddItemWhenCharacterIsGivenAnUnequippedRingWheHeDoesNotHaveOneShouldRingBeAdded() {
        // GIVEN
        logger.debug("Resolving item {} for addition.", dRingA.getId());
        expect(itemFactory.resolveItem(dRingA.getId())).andReturn(dRingA);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, dRingA.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 1);
        Assert.assertEquals(character.getEquipment().get(0), dRingA);
        Assert.assertFalse(character.getEquipment().get(0).getEquipInfo().isEquipped());
    }

    public void testAddItemWhenCharacterHasTwoEquippedRingsAndIsGivenAnUnequippedRingShouldRingBeAdded() {
        // GIVEN
        character.getEquipment().add(eRingB);
        character.getEquipment().add(eRingC);
        logger.debug("Resolving item {} for addition.", dRingA.getId());
        expect(itemFactory.resolveItem(dRingA.getId())).andReturn(dRingA);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, dRingA.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 3);
        Assert.assertEquals(character.getEquipment().get(0), eRingB);
        Assert.assertTrue(character.getEquipment().get(0).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(1), eRingC);
        Assert.assertTrue(character.getEquipment().get(1).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(2), dRingA);
        Assert.assertFalse(character.getEquipment().get(2).getEquipInfo().isEquipped());
    }

    public void testAddItemWhenCharacterHasTwoEquippedRingsAndIsGivenAnThirdEquippedRingShouldSecondBeRemovedAndNewRingAdded() {
        // GIVEN
        character.getEquipment().add(eRingB);
        character.getEquipment().add(eRingC);
        logger.debug("Resolving item {} for addition.", eRingD.getId());
        expect(itemFactory.resolveItem(eRingD.getId())).andReturn(eRingD);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, eRingD.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 3);
        Assert.assertEquals(character.getEquipment().get(0), eRingB);
        Assert.assertTrue(character.getEquipment().get(0).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(1), eRingC);
        Assert.assertFalse(character.getEquipment().get(1).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(2), eRingD);
        Assert.assertTrue(character.getEquipment().get(2).getEquipInfo().isEquipped());
    }

    public void testAddItemWhenCharacterHasTwoEquippedNonRemovableRingsAndIsGivenAnThirdEquippedRingShouldNewRingBeAddedButRemoved() {
        // GIVEN
        character.getEquipment().add(ecRingE);
        character.getEquipment().add(ecRingE);
        logger.debug("Resolving item {} for addition.", eRingD.getId());
        expect(itemFactory.resolveItem(eRingD.getId())).andReturn(eRingD);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, eRingD.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 3);
        Assert.assertEquals(character.getEquipment().get(0), ecRingE);
        Assert.assertTrue(character.getEquipment().get(0).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(1), ecRingE);
        Assert.assertTrue(character.getEquipment().get(1).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(2), eRingD);
        Assert.assertFalse(character.getEquipment().get(2).getEquipInfo().isEquipped());
    }

    public void testAddItemWhenCharacterHasTwoEquippedRingsOneIsNotRemovableAndIsGivenAnThirdEquippedRingShouldRemovableBeRemovedAndNewRingAdded() {
        // GIVEN
        character.getEquipment().add(eRingB);
        character.getEquipment().add(ecRingE);
        logger.debug("Resolving item {} for addition.", eRingD.getId());
        expect(itemFactory.resolveItem(eRingD.getId())).andReturn(eRingD);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, eRingD.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 3);
        Assert.assertEquals(character.getEquipment().get(0), eRingB);
        Assert.assertFalse(character.getEquipment().get(0).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(1), ecRingE);
        Assert.assertTrue(character.getEquipment().get(1).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(2), eRingD);
        Assert.assertTrue(character.getEquipment().get(2).getEquipInfo().isEquipped());
    }

    public void testAddItemWhenTwoHandedEquippedSwordAddedWhenOneHandedSwordAndShieldIsEquippedShouldRemoveOneHandedSwordAndShield() {
        // GIVEN
        character.getEquipment().add(eSword);
        character.getEquipment().add(eShield);
        logger.debug("Resolving item {} for addition.", eBroadsword.getId());
        expect(itemFactory.resolveItem(eBroadsword.getId())).andReturn(eBroadsword);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, eBroadsword.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 3);
        Assert.assertEquals(character.getEquipment().get(0), eSword);
        Assert.assertFalse(character.getEquipment().get(0).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(1), eShield);
        Assert.assertFalse(character.getEquipment().get(1).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(2), eBroadsword);
        Assert.assertTrue(character.getEquipment().get(2).getEquipInfo().isEquipped());
    }

    public void testAddItemWhenTwoHandedEquippedSwordAddedWhenOneHandedCursedSwordAndShieldIsEquippedShouldRemoveTwoHandedSword() {
        // GIVEN
        character.getEquipment().add(ecSword);
        character.getEquipment().add(eShield);
        character.getEquipment().add(eRingC);
        logger.debug("Resolving item {} for addition.", eBroadsword.getId());
        expect(itemFactory.resolveItem(eBroadsword.getId())).andReturn(eBroadsword);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, eBroadsword.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 4);
        Assert.assertEquals(character.getEquipment().get(0), ecSword);
        Assert.assertTrue(character.getEquipment().get(0).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(1), eShield);
        Assert.assertTrue(character.getEquipment().get(1).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(2), eRingC);
        Assert.assertTrue(character.getEquipment().get(2).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(3), eBroadsword);
        Assert.assertFalse(character.getEquipment().get(3).getEquipInfo().isEquipped());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
