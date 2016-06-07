package hu.zagor.gamebooks.ff.ff.ss.character.handler.item;

import hu.zagor.gamebooks.ff.ff.ss.character.Ff8Character;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff8CharacterItemHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff8CharacterItemHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Ff8CharacterItemHandler underTest;
    @Instance private Ff8Character character;

    @BeforeMethod
    public void setUpMethod() {
        character.setGold(0);
        character.getMaps().clear();
    }

    public void testAddItemWhenMapPieceShouldAddToCharacterMap() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.addItem(character, "maps-5", 1);
        // THEN
        Assert.assertEquals(returned, 1);
        Assert.assertTrue(character.getMaps().contains("5"));
    }

    public void testAddItemWhenNormalItemShouldAddToCharacterMap() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.addItem(character, "gold", 3);
        // THEN
        Assert.assertEquals(returned, 3);
        Assert.assertEquals(character.getGold(), 3);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddItemWhenItemIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addItem(character, null, 3);
        // THEN throws exception
    }

}
