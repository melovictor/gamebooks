package hu.zagor.gamebooks.ff.ff.votv.character;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff38CharacterPageData}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff38CharacterPageDataTest {

    private IMocksControl mockControl;
    private Ff38Character character;
    private FfCharacterHandler handler;
    private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        character = new Ff38Character();
        handler = new FfCharacterHandler();
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        handler.setAttributeHandler(attributeHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testConstructor() {
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

        expect(attributeHandler.resolveValue(character, "faith")).andReturn(5);
        mockControl.replay();
        // WHEN
        final Ff38CharacterPageData returned = new Ff38CharacterPageData(character, handler);
        // THEN
        Assert.assertEquals(returned.getFaith(), 5);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
