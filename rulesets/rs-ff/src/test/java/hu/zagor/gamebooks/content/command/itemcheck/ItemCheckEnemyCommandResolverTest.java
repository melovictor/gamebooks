package hu.zagor.gamebooks.content.command.itemcheck;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.enemy.FfEnemyHandler;
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
 * Unit test for class {@link ItemCheckEnemyCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckEnemyCommandResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private ItemCheckEnemyCommandResolver underTest;

    @Instance private ResolvationData resolvationData;
    @Mock private ItemCheckCommand parent;
    private BookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private ParagraphData have;
    @Mock private ParagraphData dontHave;
    @Mock private FfEnemyHandler enemyHandler;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(1);
        info.setCharacterHandler(characterHandler);
        characterHandler.setEnemyHandler(enemyHandler);
        resolvationData.setInfo(info);
    }

    public void testResolveWhenEnemyIsAliveShouldReturnHave() {
        // GIVEN
        expect(parent.getId()).andReturn("34");
        expect(enemyHandler.isEnemyAlive("34")).andReturn(true);
        expect(parent.getHave()).andReturn(have);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, have);
    }

    public void testResolveWhenEnemyIsNotAliveShouldReturnHave() {
        // GIVEN
        expect(parent.getId()).andReturn("34");
        expect(enemyHandler.isEnemyAlive("34")).andReturn(false);
        expect(parent.getDontHave()).andReturn(dontHave);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, dontHave);
    }

}
