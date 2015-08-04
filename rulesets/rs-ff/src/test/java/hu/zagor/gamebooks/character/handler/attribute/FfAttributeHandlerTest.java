package hu.zagor.gamebooks.character.handler.attribute;

import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfAttributeHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class FfAttributeHandlerTest {

    private FfAttributeHandler underTest;
    private FfCharacter character;
    private IMocksControl mockControl;
    private Logger logger;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);

        underTest = new FfAttributeHandler();
        Whitebox.setInternalState(underTest, "logger", logger);
        character = new FfCharacter();
    }

    @BeforeMethod
    public void setUpMethod() {
        character.setSkill(9);
        character.setInitialSkill(12);
        character.setStamina(24);
        character.setInitialStamina(24);
        character.setLuck(10);
        character.setInitialLuck(10);
        character.setGold(99);
        character.getEquipment().clear();

        mockControl.reset();
    }

    public void testResolveValueWhenAgainstIsSingleValueShouldReturnValue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "9");
        // THEN
        Assert.assertEquals(returned, 9);
    }

    public void testResolveValueWhenAgainstIsOnlyMathematicalExpressionsShouldReturnValue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "  9+3 -  2 * 4 / 8    +3  ");
        // THEN
        Assert.assertEquals(returned, 14);
    }

    public void testResolveValueWhenAgainstIsSinglePropertyShouldReturnValue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, " skill ");
        // THEN
        Assert.assertEquals(returned, 9);
    }

    public void testResolveValueWhenAgainstIsMixedPropertyAndMathOperatorsShouldReturnValue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, " skill + 3 ");
        // THEN
        Assert.assertEquals(returned, 12);
    }

    public void testResolveValueWhenAgainstIsMixedPropertyWithMaxSearchShouldReturnValue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "skill > stamina ? skill : stamina");
        // THEN
        Assert.assertEquals(returned, 24);
    }

    public void testResolveValueWhenAgainstIsMoreThanOnePropertyWithMathOperatorsShouldReturnValue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, " skill + stamina ");
        // THEN
        Assert.assertEquals(returned, 33);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testResolveValueWhenNonexistentAttributeUsedShouldThrowException() {
        // GIVEN
        logger.error("Cannot resolve property '{}'.", "fear");
        mockControl.replay();
        // WHEN
        underTest.resolveValue(character, " fear + 3 ");
        // THEN throws exception
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testHandleModificationWhenCharacterHasNoSuchFieldShouldLogErrorAndThrowException() {
        // GIVEN
        logger.error("Failed to alter field '{}' on object type '{}'.", "fear", "class hu.zagor.gamebooks.ff.character.FfCharacter");
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, "fear", 1);
        // THEN throws exception
    }

    public void testHandleModificationWhenCharacterHasFieldShouldModifyItAccordingly() {
        // GIVEN
        final ModifyAttribute modAttrib = new ModifyAttribute("skill", 1);
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, modAttrib);
        // THEN
        Assert.assertEquals(character.getSkill(), 10);
    }

    public void testResolveValueWhenSkillIsRequestedWithEquippedItemWithSkillBonusButUnderInitialSkillShouldReturnTotal() {
        // GIVEN
        final FfItem item = new FfItem("9", "magic amulet", ItemType.necklace);
        item.getEquipInfo().setEquipped(true);
        item.setSkill(1);
        character.getEquipment().add(item);
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "skill");
        // THEN
        Assert.assertEquals(returned, 10);
    }

    public void testResolveValueWhenSkillIsRequestedWithUnequippedItemWithSkillBonusShouldReturnNormal() {
        // GIVEN
        final FfItem item = new FfItem("9", "magic amulet", ItemType.necklace);
        item.setSkill(1);
        character.getEquipment().add(item);
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "skill");
        // THEN
        Assert.assertEquals(returned, 9);
    }

    public void testResolveValueWhenSkillIsRequestedWithEquippedItemWithSkillBonusButOverInitialSkillShouldReturnInitial() {
        // GIVEN
        final FfItem item = new FfItem("9", "magic amulet", ItemType.necklace);
        item.getEquipInfo().setEquipped(true);
        item.setSkill(9);
        character.getEquipment().add(item);
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "skill");
        // THEN
        Assert.assertEquals(returned, 12);
    }

    public void testHandleModificationWhenCharacterHasFieldThatShouldBeRaisedAboveInitialValueShouldModifyItAccordingly() {
        // GIVEN
        final ModifyAttribute modAttrib = new ModifyAttribute("skill", 9);
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, modAttrib);
        // THEN
        Assert.assertEquals(character.getSkill(), 12);
    }

    public void testIsAliveWhenCharacterHasEnoughStaminaShouldReturnTrue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isAlive(character);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testIsAliveWhenCharacterHasZeroStaminaShouldReturnFalse() {
        // GIVEN
        character.setStamina(0);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isAlive(character);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsAliveWhenCharacterHasZeroStaminaAndItemWithStaminaBonusShouldReturnTrue() {
        // GIVEN
        character.setStamina(0);
        final FfItem item = new FfItem("9", "magic amulet", ItemType.necklace);
        item.getEquipInfo().setEquipped(true);
        item.setStamina(2);
        character.getEquipment().add(item);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isAlive(character);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testIsAliveWhenCharacterHasSmallStaminaButItemWithNegativeStaminaBonusShouldReturnFalse() {
        // GIVEN
        character.setStamina(2);
        final FfItem item = new FfItem("9", "magic amulet", ItemType.necklace);
        item.getEquipInfo().setEquipped(true);
        item.setStamina(-2);
        character.getEquipment().add(item);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isAlive(character);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testHandleModificationWhenStaminaBonusTooLargeShouldSetItToInitialStamina() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, "stamina", 111);
        // THEN
        Assert.assertEquals(character.getStamina(), character.getInitialStamina());
    }

    public void testHandleModificationWhenLuckBonusTooLargeShouldSetItToInitialLuck() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, "luck", 111);
        // THEN
        Assert.assertEquals(character.getLuck(), character.getInitialLuck());
    }

    public void testHandleModificationWhenLuckBonusTooSmallShouldSetItToZero() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, "luck", -111);
        // THEN
        Assert.assertEquals(character.getLuck(), 0);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }
}
