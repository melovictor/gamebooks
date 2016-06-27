package hu.zagor.gamebooks.content.command.itemcheck;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.AttributeHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ItemCheckAttributeCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckAttributeCommandResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private ItemCheckAttributeCommandResolver underTest;

    @Instance private ItemCheckCommand parent;
    @Instance private ResolvationData resolvationData;
    @Mock private ParagraphData have;
    @Mock private ParagraphData dontHave;
    @Instance private Character character;
    private BookInformations info;
    @Instance private CharacterHandler characterHandler;
    @Mock private AttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        parent.setId("skill");
        parent.setAmount(10);
        info = new BookInformations(1);
        characterHandler.setAttributeHandler(attributeHandler);
        info.setCharacterHandler(characterHandler);
        resolvationData.setInfo(info);
        parent.setHave(have);
        parent.setDontHave(dontHave);
        resolvationData.setCharacter(character);
    }

    public void testResolveWhenResolvedValueIsSmallerThanRequiredShouldReturnDontHave() {
        // GIVEN
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(7);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, dontHave);
    }

    public void testResolveWhenResolvedValueIsHigherThanRequiredShouldReturnHave() {
        // GIVEN
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(11);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, have);
    }

    public void testResolveWhenResolvedValueIsSameAsRequiredShouldReturnHave() {
        // GIVEN
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, have);
    }

}
