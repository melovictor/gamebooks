package hu.zagor.gamebooks.content.command.itemcheck;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ItemCheckAttributeEqualityCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckAttributeEqualityCommandResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private ItemCheckAttributeEqualityCommandResolver underTest;
    @Instance private ResolvationData resolvationData;
    @Mock private ItemCheckCommand parent;
    @Mock private SorCharacter character;
    private BookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfAttributeHandler attributeHandler;
    @Mock private ParagraphData have;
    @Mock private ParagraphData dontHave;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(1);
        info.setCharacterHandler(characterHandler);
        characterHandler.setAttributeHandler(attributeHandler);
        resolvationData.setCharacter(character);
        resolvationData.setInfo(info);
    }

    public void testResolveWhenResolvedEnumValueEqualsAmountShouldReturnHave() {
        // GIVEN
        expect(parent.getCheckType()).andReturn(CheckType.enumAttribute);
        expect(parent.getId()).andReturn("specialSkill");
        expect(parent.getAmount()).andReturn(3);
        expect(attributeHandler.resolveValue(character, "specialSkill")).andReturn(3);
        expect(parent.getHave()).andReturn(have);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, have);
    }

    public void testResolveWhenResolvedNonEnumValueDoesNotEqualAmountShouldReturnDontHave() {
        // GIVEN
        expect(parent.getCheckType()).andReturn(CheckType.status);
        expect(parent.getId()).andReturn("1,4,5");
        expect(attributeHandler.resolveValue(character, "status")).andReturn(3);
        expect(parent.getDontHave()).andReturn(dontHave);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, dontHave);
    }

}
