package hu.zagor.gamebooks.ff.ff.tot.mvc.books.section.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.ui.Model;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff14Section13PreHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff14Section13PreHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private Ff14Section13PreHandler underTest;
    @Mock private FfBookInformations info;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private Model model;
    @Mock private Map<String, Enemy> enemyList;
    @Mock private FfEnemy enemy;
    @Mock private FfCharacter character;
    @Mock private FfCharacterHandler charHandler;
    @Mock private FfAttributeHandler attributeHandler;

    public void testHandleShouldSetFleeingRoundToCurrentSkill() {
        // GIVEN
        expect(wrapper.getEnemies()).andReturn(enemyList);
        expect(enemyList.get("29")).andReturn(enemy);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(info.getCharacterHandler()).andReturn(charHandler);
        expect(charHandler.getAttributeHandler()).andReturn(attributeHandler);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        enemy.setFleeAtRound(9);
        mockControl.replay();
        // WHEN
        underTest.handle(model, wrapper, info, false);
        // THEN
    }

}
