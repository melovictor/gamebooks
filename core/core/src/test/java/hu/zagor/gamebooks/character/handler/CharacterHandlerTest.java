package hu.zagor.gamebooks.character.handler;

import hu.zagor.gamebooks.character.handler.character.CharacterGenerator;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link CharacterHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class CharacterHandlerTest {

    public void testSetCharacterGeneratorShouldSetCharacterGenerator() {
        // GIVEN
        final CharacterHandler underTest = new CharacterHandler();
        final CharacterGenerator characterGenerator = EasyMock.createMock(CharacterGenerator.class);
        // WHEN
        underTest.setCharacterGenerator(characterGenerator);
        // THEN
        Assert.assertSame(underTest.getCharacterGenerator(), characterGenerator);
    }
}
