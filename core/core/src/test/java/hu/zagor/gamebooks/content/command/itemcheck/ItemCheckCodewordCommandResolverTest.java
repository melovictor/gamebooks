package hu.zagor.gamebooks.content.command.itemcheck;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.google.common.collect.Sets;

/**
 * Unit test for class {@link ItemCheckCodewordCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckCodewordCommandResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private ItemCheckCodewordCommandResolver underTest;
    @Mock private ItemCheckCommand parent;
    @Instance private ResolvationData resolvationData;
    @Mock private ParagraphData have;
    @Mock private ParagraphData dontHave;
    @Instance private Character character;

    @BeforeClass
    public void setUpClass() {
        resolvationData.setCharacter(character);
        character.getCodeWords().addAll(Sets.newHashSet("ship"));
    }

    public void testResolveWhenCharacterHasCodewordShouldReturnHave() {
        // GIVEN
        expect(parent.getId()).andReturn("ship");
        expect(parent.getHave()).andReturn(have);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, have);
    }

    public void testResolveWhenCharacterDoesNotHaveCodewordShouldReturnDontHave() {
        // GIVEN
        expect(parent.getId()).andReturn("flag");
        expect(parent.getDontHave()).andReturn(dontHave);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, dontHave);
    }

}
