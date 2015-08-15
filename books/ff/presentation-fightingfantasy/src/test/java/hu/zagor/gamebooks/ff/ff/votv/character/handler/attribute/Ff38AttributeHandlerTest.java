package hu.zagor.gamebooks.ff.ff.votv.character.handler.attribute;

import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.votv.character.Ff38Character;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff38AttributeHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff38AttributeHandlerTest {

    private Ff38AttributeHandler underTest;
    private Ff38Character character;

    @BeforeClass
    public void setUpClass() {
        underTest = new Ff38AttributeHandler();
        character = new Ff38Character();
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
    }

    public void testSanityCheckWhenFaithIsNegativeShouldSetToZero() {
        // GIVEN
        character.setFaith(-3);
        // WHEN
        underTest.sanityCheck(character);
        // THEN
        Assert.assertEquals(character.getFaith(), 0);
    }

    public void testSanityCheckWhenFaithIsNonNegativeShouldLeaveIt() {
        // GIVEN
        character.setFaith(5);
        // WHEN
        underTest.sanityCheck(character);
        // THEN
        Assert.assertEquals(character.getFaith(), 5);
    }

    public void testSanityCheckWhenCharacterIsNotFrom38ShouldSkipFaithCheck() {
        // GIVEN
        // WHEN
        underTest.sanityCheck(new FfCharacter());
        // THEN
    }

}
