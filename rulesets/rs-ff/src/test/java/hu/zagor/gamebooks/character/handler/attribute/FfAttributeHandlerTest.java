package hu.zagor.gamebooks.character.handler.attribute;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.ExpressionResolver;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
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

    @BeforeMethod
    public void setUpMethod() {
        character.setSkill(9);
        character.setInitialSkill(12);
        character.setStamina(24);
        character.setInitialStamina(24);
        character.setLuck(10);
        character.setInitialLuck(10);
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
