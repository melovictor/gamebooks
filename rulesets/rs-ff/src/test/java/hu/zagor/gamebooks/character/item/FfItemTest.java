package hu.zagor.gamebooks.character.item;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfItem}.
 * @author Tamas_Szekeres
 */
@Test
public class FfItemTest {

    private FfItem underTest;

    @BeforeMethod
    public void setUpMethod() {
        underTest = new FfItem();
    }

    public void testGetSkillShouldReturnSkill() {
        // GIVEN
        underTest.setSkill(7);
        // WHEN
        final int returned = underTest.getSkill();
        // THEN
        Assert.assertEquals(returned, 7);
    }

    public void testGetStaminaShouldReturnStamina() {
        // GIVEN
        underTest.setStamina(7);
        // WHEN
        final int returned = underTest.getStamina();
        // THEN
        Assert.assertEquals(returned, 7);
    }

    public void testGetLuckShouldReturnLuck() {
        // GIVEN
        underTest.setLuck(7);
        // WHEN
        final int returned = underTest.getLuck();
        // THEN
        Assert.assertEquals(returned, 7);
    }

}
