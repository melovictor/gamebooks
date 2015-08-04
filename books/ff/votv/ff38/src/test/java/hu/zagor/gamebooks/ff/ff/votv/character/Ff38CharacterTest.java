package hu.zagor.gamebooks.ff.ff.votv.character;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff38Character}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff38CharacterTest {

    public void testGetFaithShouldReturnSetValue() {
        // GIVEN
        final Ff38Character underTest = new Ff38Character();
        underTest.setFaith(6);
        // WHEN
        final int returned = underTest.getFaith();
        // THEN
        Assert.assertEquals(returned, 6);
    }

}
