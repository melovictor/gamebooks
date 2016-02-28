package hu.zagor.gamebooks.character.handler.attribute;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.ExpressionResolver;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Arrays;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfAttributeHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class FfAttributeHandlerTest {
    @UnderTest private FfAttributeHandler underTest;
    @Instance private FfCharacter character;
    @MockControl private IMocksControl mockControl;
    @Inject private Logger logger;
    @Inject private ExpressionResolver expressionResolver;
    @Mock private FfAllyCharacter allyCharacter;
    private FfItem itemA;
    private FfItem itemB;
    @Inject private DeductionCalculator deductionCalculator;

    @BeforeMethod
    public void setUpMethod() {
        character.setSkill(9);
        character.setInitialSkill(12);
        character.setStamina(24);
        character.setInitialStamina(24);
        character.setLuck(10);
        character.setInitialLuck(10);
        character.setGold(5);
        character.getEquipment().clear();
        itemA = new FfItem("10", "itemA", ItemType.valuable);
        itemB = new FfItem("20", "itemB", ItemType.valuable);
        mockControl.reset();
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
        final ModifyAttribute modAttrib = new ModifyAttribute("skill", 1, ModifyAttributeType.change);
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, modAttrib);
        // THEN
        Assert.assertEquals(character.getSkill(), 10);
    }

    public void testHandleModificationWhenCharacterHasFieldThatShouldBeRaisedAboveInitialValueShouldModifyItAccordingly() {
        // GIVEN
        final ModifyAttribute modAttrib = new ModifyAttribute("skill", 9, ModifyAttributeType.change);
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, modAttrib);
        // THEN
        Assert.assertEquals(character.getSkill(), 12);
    }

    public void testIsAliveWhenExpressionResolverRespondsWithZeroShouldReturnFalse() {
        // GIVEN
        expect(expressionResolver.resolveValue(character, "stamina")).andReturn(0);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isAlive(character);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsAliveWhenExpressionResolverRespondsWithPositiveShouldReturnTrue() {
        // GIVEN
        expect(expressionResolver.resolveValue(character, "stamina")).andReturn(6);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isAlive(character);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testIsAliveWhenAllyCharacterHasZeroStaminaShouldReturnFalse() {
        // GIVEN
        expect(allyCharacter.getStamina()).andReturn(0);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isAlive((FfCharacter) allyCharacter);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testIsAliveWhenWhenAllyCharacterHasNonZeroStaminaShouldReturnTrue() {
        // GIVEN
        expect(allyCharacter.getStamina()).andReturn(3);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.isAlive((FfCharacter) allyCharacter);
        // THEN
        Assert.assertTrue(returned);
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

    public void testHandleModificationWhenGoldBonusTooSmallShouldSetItToZero() {
        // GIVEN
        final ModifyAttribute modAttr = new ModifyAttribute("gold", -111, ModifyAttributeType.change, true);
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, modAttr);
        // THEN
        Assert.assertEquals(character.getGold(), 0);
    }

    public void testHandleModificationWhenGoldIsBeingSetShouldSetItToValue() {
        // GIVEN
        final ModifyAttribute modAttr = new ModifyAttribute("gold", 13, ModifyAttributeType.set);
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, modAttr);
        // THEN
        Assert.assertEquals(character.getGold(), 13);
    }

    public void testHandleModificationWhenGoldIsBeingAddedShouldChangeItToProperValue() {
        // GIVEN
        final ModifyAttribute modAttr = new ModifyAttribute("gold", 1, ModifyAttributeType.change);
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, modAttr);
        // THEN
        Assert.assertEquals(character.getGold(), 6);
    }

    public void testHandleModificationWhenGoldBonusCanBePaidByItemsShouldCallForItemChangesAndApplyThose() {
        // GIVEN
        final ModifyAttribute modAttr = new ModifyAttribute("gold", -8, ModifyAttributeType.change, false);
        character.getEquipment().addAll(Arrays.asList(itemA, itemB));
        final GoldItemDeduction goldItemDeduction = new GoldItemDeduction();
        goldItemDeduction.setGold(3);
        goldItemDeduction.getItems().add(itemB);
        expect(deductionCalculator.calculateDeductibleElements(character, 8)).andReturn(goldItemDeduction);
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, modAttr);
        // THEN
        Assert.assertEquals(character.getGold(), 2);
        Assert.assertEquals(character.getEquipment().size(), 1);
        Assert.assertTrue(character.getEquipment().contains(itemA));
        Assert.assertFalse(character.getEquipment().contains(itemB));
    }

    public void testHandleModificationWhenSettingStaminaToAcceptableValueShouldSetItToValue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, "stamina", 3, ModifyAttributeType.set);
        // THEN
        Assert.assertEquals(character.getStamina(), 3);
    }

    public void testHandleModificationWhenMustHandleComplextPathShouldBeAbleToChangeTheValue() {
        // GIVEN
        final TestCharacter character = new TestCharacter();
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, "subThing.a", 4);
        underTest.handleModification(character, "subThing.b", -2);
        // THEN
        Assert.assertEquals(character.getSubThing().getA(), 14);
        Assert.assertEquals(character.getSubThing().getB(), 9);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testHandleModificationWhenComplextPathFieldDoesNotExistsShouldThrowException() {
        // GIVEN
        final TestCharacter character = new TestCharacter();
        logger.error("Failed to fetch contents from field '{}' on object type '{}'.", "x",
            "class hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandlerTest$TestCharacter$SubThing");
        mockControl.replay();
        // WHEN
        underTest.handleModification(character, "subThing.x.a", 4);
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

    private class TestCharacter extends FfCharacter {
        private final SubThing subThing;

        TestCharacter() {
            subThing = new SubThing();
            subThing.setA(10);
            subThing.setB(11);
        }

        public SubThing getSubThing() {
            return subThing;
        }

        private class SubThing {
            private int a;
            private int b;

            public int getA() {
                return a;
            }

            public void setA(final int a) {
                this.a = a;
            }

            public int getB() {
                return b;
            }

            public void setB(final int b) {
                this.b = b;
            }
        }
    }

}
