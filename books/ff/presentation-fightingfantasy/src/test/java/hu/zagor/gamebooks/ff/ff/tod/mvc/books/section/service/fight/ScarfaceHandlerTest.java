package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ScarfaceHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class ScarfaceHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private ScarfaceHandler underTest;
    @Mock private BasicEnemyPrePostFightDataContainer data;
    private final FightRoundResult[] results = new FightRoundResult[]{FightRoundResult.WIN};
    @Mock private ResolvationData resolvationData;
    @Mock private FfFightCommand command;
    @Mock private Map<String, Enemy> enemyList;
    @Mock private FfEnemy enemyA;
    @Mock private FfEnemy enemyB;

    public void testGetEnemyIdShouldReturnProperId() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.getEnemyId();
        // THEN
        Assert.assertEquals(returned, "22");
    }

    public void testShouldExecutePostHandlerWhenThreeEnemiesStillAliveShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData,
            new FightRoundResult[]{FightRoundResult.WIN, FightRoundResult.TIE, FightRoundResult.TIE}, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenTwoEnemiesStillAliveButLostTheRoundWithFirstShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.LOSE, FightRoundResult.TIE}, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenTwoEnemiesStillAliveButTiedTheRoundWithFirstShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.TIE, FightRoundResult.TIE}, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenTwoEnemiesStillAliveAndWonTheRoundWithFirstShouldReturnTrue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.WIN, FightRoundResult.TIE}, data);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testExecutePostHandlerWhenEnemyStillTooStrongShouldDoNothing() {
        // GIVEN
        expect(resolvationData.getEnemies()).andReturn(enemyList);
        expect(enemyList.get("22")).andReturn(enemyA);
        expect(enemyA.getStamina()).andReturn(15);
        mockControl.replay();
        // WHEN
        underTest.executePostHandler(command, resolvationData, results, data);
        // THEN
    }

    public void testExecutePostHandlerWhenEnemyTooWeakShouldKillOtherAndFinishBattle() {
        // GIVEN
        expect(resolvationData.getEnemies()).andReturn(enemyList);
        expect(enemyList.get("22")).andReturn(enemyA);
        expect(enemyA.getStamina()).andReturn(5);
        expect(enemyList.get("23")).andReturn(enemyB);
        enemyB.setStamina(0);
        mockControl.replay();
        // WHEN
        underTest.executePostHandler(command, resolvationData, results, data);
        // THEN
    }
}
