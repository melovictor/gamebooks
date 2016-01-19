package hu.zagor.gamebooks.character.domain.builder;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultResolvationDataBuilder}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultResolvationDataBuilderTest {

    @MockControl private IMocksControl mockControl;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private ParagraphData rootData;
    @Mock private Paragraph paragraph;
    @Mock private BookInformations info;
    @Mock private Character character;
    @Mock private Map<String, Enemy> enemies;
    @Mock private PlayerUser player;

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testBuildWhenUsingWrapperShouldCopyProperFields() {
        // GIVEN
        expect(wrapper.getCharacter()).andReturn(character);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(wrapper.getPlayer()).andReturn(player);
        mockControl.replay();
        // WHEN
        final ResolvationData returned = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).usingWrapper(wrapper).build();
        // THEN
        Assert.assertSame(returned.getCharacter(), character);
        Assert.assertSame(returned.getEnemies(), enemies);
        Assert.assertSame(returned.getPlayerUser(), player);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
