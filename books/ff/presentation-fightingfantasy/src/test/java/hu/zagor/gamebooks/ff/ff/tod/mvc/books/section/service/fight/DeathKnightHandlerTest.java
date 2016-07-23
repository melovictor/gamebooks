package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DeathKnightHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class DeathKnightHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private DeathKnightHandler underTest;
    @Mock private BasicEnemyPrePostFightDataContainer data;
    private final FightRoundResult[] results = new FightRoundResult[]{FightRoundResult.WIN};
    @Mock private ResolvationData resolvationData;
    @Mock private FfFightCommand command;
    @Mock private Map<String, Enemy> enemyList;
    @Mock private FfEnemy enemy;
    @Mock private FfCharacterHandler characterHandler;
    @Mock private FfAttributeHandler attributeHandler;
    @Mock private FfCharacter character;
    @Mock private FfCharacterItemHandler itemHandler;

    public void testGetEnemyIdShouldReturnProperId() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.getEnemyId();
        // THEN
        Assert.assertEquals(returned, "10");
    }

    public void testShouldExecutePostHandlerWhenPlayerWonShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.WIN}, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenPlayerTiedShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.TIE}, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenPlayerLostButTooStrongShouldReturnFalse() {
        // GIVEN
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getAttributeHandler()).andReturn(attributeHandler);
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(20);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.LOSE}, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenPlayerLostAndTooWeakShouldReturnTrue() {
        // GIVEN
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getAttributeHandler()).andReturn(attributeHandler);
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(5);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.LOSE}, data);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testExecutePostHandlerShouldKillEnemyAndAddSuddenDeathMarker() {
        // GIVEN
        expect(resolvationData.getEnemies()).andReturn(enemyList);
        expect(enemyList.get("10")).andReturn(enemy);
        enemy.setStamina(0);
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getItemHandler()).andReturn(itemHandler);
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, "4002", 1)).andReturn(1);
        mockControl.replay();
        // WHEN
        underTest.executePostHandler(command, resolvationData, results, data);
        // THEN
    }
}
