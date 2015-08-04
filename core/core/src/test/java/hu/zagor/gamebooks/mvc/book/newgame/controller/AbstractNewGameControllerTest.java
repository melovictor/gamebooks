package hu.zagor.gamebooks.mvc.book.newgame.controller;

import hu.zagor.gamebooks.character.Character;

import java.util.Locale;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AbstractNewGameController}.
 * @author Tamas_Szekeres
 */
@Test
public class AbstractNewGameControllerTest {

    private TestingNewGameController underTest;

    @BeforeClass
    public void setUpClass() {
        underTest = new TestingNewGameController();
    }

    public void testGetCharacterShouldReturnNull() {
        // GIVEN
        // WHEN
        final Character returned = underTest.getCharacter(null);
        // THEN
        Assert.assertNull(returned);
    }

    private class TestingNewGameController extends AbstractNewGameController {

        @Override
        public Character getCharacter(final Locale locale) {
            return null;
        }

    }

}
