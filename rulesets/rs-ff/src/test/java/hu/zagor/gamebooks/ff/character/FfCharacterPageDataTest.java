package hu.zagor.gamebooks.ff.character;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfCharacterPageData}.
 * @author Tamas_Szekeres
 */
@Test
public class FfCharacterPageDataTest {

    private FfCharacter character;
    private IMocksControl mockControl;
    private FfCharacterHandler handler;
    private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        handler = new FfCharacterHandler();
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        handler.setAttributeHandler(attributeHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        character = new FfCharacter();
        mockControl.reset();
    }

    public void testConstructorIfCharacterIsNotInitializedAndHasZeroStaminaShouldBeAlive() {
        // GIVEN
        character.setInitialLuck(7);
        character.setInitialStamina(19);
        character.setInitialSkill(11);
        character.setInitialized(false);
        expect(attributeHandler.resolveValue(character, "initialSkill")).andReturn(11);
        expect(attributeHandler.resolveValue(character, "initialStamina")).andReturn(19);
        expect(attributeHandler.resolveValue(character, "initialLuck")).andReturn(7);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(0);
        expect(attributeHandler.resolveValue(character, "luck")).andReturn(5);
        mockControl.replay();
        // WHEN
        final FfCharacterPageData returned = new FfCharacterPageData(character, handler);
        // THEN
        Assert.assertEquals(returned.getInitialLuck(), 7);
        Assert.assertEquals(returned.getInitialStamina(), 19);
        Assert.assertEquals(returned.getInitialSkill(), 11);
        Assert.assertEquals(returned.getLuck(), 5);
        Assert.assertEquals(returned.getStamina(), 0);
        Assert.assertEquals(returned.getSkill(), 10);
        Assert.assertTrue(returned.isAlive());
    }

    public void testConstructorIfCharacterIsInitializedAndHasZeroStaminaShouldBeDead() {
        // GIVEN
        character.setInitialLuck(7);
        character.setInitialStamina(19);
        character.setInitialSkill(11);
        character.setInitialized(true);
        expect(attributeHandler.resolveValue(character, "initialSkill")).andReturn(11);
        expect(attributeHandler.resolveValue(character, "initialStamina")).andReturn(19);
        expect(attributeHandler.resolveValue(character, "initialLuck")).andReturn(7);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(0);
        expect(attributeHandler.resolveValue(character, "luck")).andReturn(5);
        expect(attributeHandler.isAlive(character)).andReturn(false);
        mockControl.replay();
        // WHEN
        final FfCharacterPageData returned = new FfCharacterPageData(character, handler);
        // THEN
        Assert.assertEquals(returned.getInitialLuck(), 7);
        Assert.assertEquals(returned.getInitialStamina(), 19);
        Assert.assertEquals(returned.getInitialSkill(), 11);
        Assert.assertEquals(returned.getLuck(), 5);
        Assert.assertEquals(returned.getStamina(), 0);
        Assert.assertEquals(returned.getSkill(), 10);
        Assert.assertFalse(returned.isAlive());
    }

    public void testConstructorIfCharacterIsInitializedAndHasStaminaShouldBeAlive() {
        // GIVEN
        character.setInitialLuck(7);
        character.setInitialStamina(19);
        character.setInitialSkill(11);
        character.setInitialized(true);
        expect(attributeHandler.resolveValue(character, "initialSkill")).andReturn(11);
        expect(attributeHandler.resolveValue(character, "initialStamina")).andReturn(19);
        expect(attributeHandler.resolveValue(character, "initialLuck")).andReturn(7);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(9);
        expect(attributeHandler.resolveValue(character, "luck")).andReturn(5);
        expect(attributeHandler.isAlive(character)).andReturn(true);
        mockControl.replay();
        // WHEN
        final FfCharacterPageData returned = new FfCharacterPageData(character, handler);
        // THEN
        Assert.assertEquals(returned.getInitialLuck(), 7);
        Assert.assertEquals(returned.getInitialStamina(), 19);
        Assert.assertEquals(returned.getInitialSkill(), 11);
        Assert.assertEquals(returned.getLuck(), 5);
        Assert.assertEquals(returned.getStamina(), 9);
        Assert.assertEquals(returned.getSkill(), 10);
        Assert.assertTrue(returned.isAlive());
    }

    public void testConstructorIfCharacterHasNoWeaponShouldNotAddDefaultToItems() {
        // GIVEN
        character.setInitialLuck(7);
        character.setInitialStamina(19);
        character.setInitialSkill(11);
        character.setInitialized(true);
        final FfItem dice = new FfItem("3001", "Dice", ItemType.common);
        character.getEquipment().add(dice);
        expect(attributeHandler.resolveValue(character, "initialSkill")).andReturn(11);
        expect(attributeHandler.resolveValue(character, "initialStamina")).andReturn(19);
        expect(attributeHandler.resolveValue(character, "initialLuck")).andReturn(7);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(9);
        expect(attributeHandler.resolveValue(character, "luck")).andReturn(5);
        mockControl.replay();
        // WHEN
        final FfCharacterPageData returned = new FfCharacterPageData(character, handler);
        // THEN
        Assert.assertEquals(returned.getInitialLuck(), 7);
        Assert.assertEquals(returned.getInitialStamina(), 19);
        Assert.assertEquals(returned.getInitialSkill(), 11);
        Assert.assertEquals(returned.getLuck(), 5);
        Assert.assertEquals(returned.getStamina(), 9);
        Assert.assertEquals(returned.getSkill(), 10);
        Assert.assertSame(returned.getItems().get(0), dice);
    }

    public void testConstructorIfCharacterOnlyHasDefaultWeaponShouldAddDefaultToItems() {
        // GIVEN
        character.setInitialLuck(7);
        character.setInitialStamina(19);
        character.setInitialSkill(11);
        character.setInitialized(true);
        final FfItem fist = new FfItem("defWpn", "Fist", ItemType.weapon1);
        final FfItem dice = new FfItem("3001", "Dice", ItemType.common);
        character.getEquipment().add(dice);
        character.getEquipment().add(fist);
        expect(attributeHandler.resolveValue(character, "initialSkill")).andReturn(11);
        expect(attributeHandler.resolveValue(character, "initialStamina")).andReturn(19);
        expect(attributeHandler.resolveValue(character, "initialLuck")).andReturn(7);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(9);
        expect(attributeHandler.resolveValue(character, "luck")).andReturn(5);
        mockControl.replay();
        // WHEN
        final FfCharacterPageData returned = new FfCharacterPageData(character, handler);
        // THEN
        Assert.assertEquals(returned.getInitialLuck(), 7);
        Assert.assertEquals(returned.getInitialStamina(), 19);
        Assert.assertEquals(returned.getInitialSkill(), 11);
        Assert.assertEquals(returned.getLuck(), 5);
        Assert.assertEquals(returned.getStamina(), 9);
        Assert.assertEquals(returned.getSkill(), 10);
        Assert.assertSame(returned.getItems().get(0), dice);
        Assert.assertSame(returned.getItems().get(1), fist);
    }

    public void testConstructorIfCharacterHasNonDefaultWeaponShouldNotAddDefaultToItems() {
        // GIVEN
        character.setInitialLuck(7);
        character.setInitialStamina(19);
        character.setInitialSkill(11);
        character.setInitialized(true);
        character.setGold(335);
        final FfItem fist = new FfItem("defWpn", "Fist", ItemType.weapon1);
        final FfItem sword = new FfItem("1002", "Fist", ItemType.weapon1);
        final FfItem dice = new FfItem("3001", "Dice", ItemType.common);
        final FfItem broadsword = new FfItem("1001", "Broadsword", ItemType.weapon2);
        character.getEquipment().add(broadsword);
        character.getEquipment().add(fist);
        character.getEquipment().add(dice);
        character.getEquipment().add(sword);
        expect(attributeHandler.resolveValue(character, "initialSkill")).andReturn(11);
        expect(attributeHandler.resolveValue(character, "initialStamina")).andReturn(19);
        expect(attributeHandler.resolveValue(character, "initialLuck")).andReturn(7);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(9);
        expect(attributeHandler.resolveValue(character, "luck")).andReturn(5);
        mockControl.replay();
        // WHEN
        final FfCharacterPageData returned = new FfCharacterPageData(character, handler);
        // THEN
        Assert.assertEquals(returned.getInitialLuck(), 7);
        Assert.assertEquals(returned.getInitialStamina(), 19);
        Assert.assertEquals(returned.getInitialSkill(), 11);
        Assert.assertEquals(returned.getLuck(), 5);
        Assert.assertEquals(returned.getStamina(), 9);
        Assert.assertEquals(returned.getGold(), 335);
        Assert.assertEquals(returned.getSkill(), 10);
        Assert.assertSame(returned.getItems().get(0), broadsword);
        Assert.assertSame(returned.getItems().get(1), dice);
        Assert.assertSame(returned.getItems().get(2), sword);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
