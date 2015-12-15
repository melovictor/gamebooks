package hu.zagor.gamebooks.character.handler;

import hu.zagor.gamebooks.character.item.ItemType;
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
 * Unit test for class {@link AttributeResolvingExpressionResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class AttributeResolvingExpressionResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private AttributeResolvingExpressionResolver underTest;
    @Inject private Logger logger;
    @Instance private PreparatedCharacter character;

    @BeforeMethod
    public void setUpMethod() {
        character.setStamina(24);
        character.setInitialStamina(24);
        character.setSkill(9);
        character.setInitialSkill(12);
        character.getEquipment().clear();
        mockControl.reset();
    }

    public void testResolveValueWhenCharacterHasEnoughStaminaShouldReturnPositive() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "stamina");
        // THEN
        Assert.assertEquals(returned, 24);
    }

    public void testResolveValueWhenCharacterHasZeroStaminaShouldReturnZero() {
        // GIVEN
        character.setStamina(0);
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "stamina");
        // THEN
        Assert.assertEquals(returned, 0);
    }

    public void testResolveValueWhenCharacterHasZeroStaminaAndItemWithStaminaBonusShouldReturnPositive() {
        // GIVEN
        character.setStamina(0);
        final PreparatedItem item = new PreparatedItem("9", "magic amulet", ItemType.necklace);
        item.getEquipInfo().setEquipped(true);
        item.setStamina(2);
        character.getEquipment().add(item);
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "stamina");
        // THEN
        Assert.assertEquals(returned, 2);
    }

    public void testResolveValueWhenCharacterHasSmallStaminaButItemWithNegativeStaminaBonusShouldReturnZero() {
        // GIVEN
        character.setStamina(2);
        final PreparatedItem item = new PreparatedItem("9", "magic amulet", ItemType.necklace);
        item.getEquipInfo().setEquipped(true);
        item.setStamina(-2);
        character.getEquipment().add(item);
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "stamina");
        // THEN
        Assert.assertEquals(returned, 0);
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

    public void testResolveValueWhenSkillIsRequestedWithEquippedItemWithSkillBonusButUnderInitialSkillShouldReturnTotal() {
        // GIVEN
        final PreparatedItem item = new PreparatedItem("9", "magic amulet", ItemType.necklace);
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
        final PreparatedItem item = new PreparatedItem("9", "magic amulet", ItemType.necklace);
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
        final PreparatedItem item = new PreparatedItem("9", "magic amulet", ItemType.necklace);
        item.getEquipInfo().setEquipped(true);
        item.setSkill(9);
        character.getEquipment().add(item);
        mockControl.replay();
        // WHEN
        final int returned = underTest.resolveValue(character, "skill");
        // THEN
        Assert.assertEquals(returned, 12);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
