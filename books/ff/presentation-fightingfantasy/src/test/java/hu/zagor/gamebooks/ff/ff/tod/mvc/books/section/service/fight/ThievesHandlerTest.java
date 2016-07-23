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
 * Unit test for class {@link ThievesHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class ThievesHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private ThievesHandler underTest;
    @Mock private BasicEnemyPrePostFightDataContainer data;
    private final FightRoundResult[] results = new FightRoundResult[]{FightRoundResult.WIN, FightRoundResult.TIE};
    @Mock private ResolvationData resolvationData;
    @Mock private FfFightCommand command;
    @Mock private Map<String, Enemy> enemyList;
    @Mock private FfEnemy enemyA;
    @Mock private FfEnemy enemyB;

    public void testGetEnemyIdsShouldReturnExpectedIds() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String[] returned = underTest.getEnemyIds();
        // THEN
        Assert.assertEquals(returned, new String[]{"27", "28"});
    }

    public void testShouldExecutePostHandlerWhenBothEnemiesAliveShouldReturnFalse() {
        // GIVEN
        expect(resolvationData.getEnemies()).andReturn(enemyList);
        expect(enemyList.get("27")).andReturn(enemyA);
        expect(enemyList.get("28")).andReturn(enemyB);
        expect(enemyA.getStamina()).andReturn(10);
        expect(enemyB.getStamina()).andReturn(7);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenFirstEnemyDeadShouldReturnTrue() {
        // GIVEN
        expect(resolvationData.getEnemies()).andReturn(enemyList);
        expect(enemyList.get("27")).andReturn(enemyA);
        expect(enemyList.get("28")).andReturn(enemyB);
        expect(enemyA.getStamina()).andReturn(0);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testShouldExecutePostHandlerWhenSecondEnemyDeadShouldReturnTrue() {
        // GIVEN
        expect(resolvationData.getEnemies()).andReturn(enemyList);
        expect(enemyList.get("27")).andReturn(enemyA);
        expect(enemyList.get("28")).andReturn(enemyB);
        expect(enemyA.getStamina()).andReturn(10);
        expect(enemyB.getStamina()).andReturn(0);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testExecutePostHandlerShouldKillBothEnemies() {
        // GIVEN
        expect(resolvationData.getEnemies()).andReturn(enemyList);
        expect(enemyList.get("27")).andReturn(enemyA);
        enemyA.setStamina(0);
        expect(enemyList.get("28")).andReturn(enemyB);
        enemyB.setStamina(0);
        mockControl.replay();
        // WHEN
        underTest.executePostHandler(command, resolvationData, results, data);
        // THEN
    }
}
