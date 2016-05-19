package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Arrays;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.google.common.collect.ImmutableMap;

/**
 * Unit test for class {@link CaptainHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class CaptainHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private CaptainHandler underTest;
    @Mock private EnemyPrePostFightDataContainer data;
    private FightRoundResult[] results;
    @Mock private ResolvationData resolvationData;
    @Mock private FightCommand command;
    @Mock private FfEnemy captain;
    @Mock private FfEnemy elvira;
    @Mock private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfCharacter character;

    public void testShouldExecutePostHandlerWhenEnemiesDoesNotContainSevenShouldReturnFalse() {
        // GIVEN
        expect(command.getEnemies()).andReturn(Arrays.asList("3", "75", "44"));
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenAttackStrengthsDoesNotContainSevenShouldReturnFalse() {
        // GIVEN
        expect(command.getEnemies()).andReturn(Arrays.asList("7", "8"));
        final ImmutableMap<String, Integer> attackStrengths = ImmutableMap.of("8", 13);
        expect(command.getAttackStrengths()).andReturn(attackStrengths);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenCaptainAttackStrengthTooLowShouldReturnFalse() {
        // GIVEN
        expect(command.getEnemies()).andReturn(Arrays.asList("7", "8"));
        final ImmutableMap<String, Integer> attackStrengths = ImmutableMap.of("7", 14, "8", 13);
        expect(command.getAttackStrengths()).andReturn(attackStrengths).times(2);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenTiedWithHighCaptainAttackStrengthShouldReturnFalse() {
        // GIVEN
        results = new FightRoundResult[]{FightRoundResult.TIE, FightRoundResult.TIE};
        expect(command.getEnemies()).andReturn(Arrays.asList("7", "8"));
        final ImmutableMap<String, Integer> attackStrengths = ImmutableMap.of("7", 18, "8", 13);
        expect(command.getAttackStrengths()).andReturn(attackStrengths).times(2);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenWonAgainstHighCaptainAttackStrengthShouldReturnFalse() {
        // GIVEN
        results = new FightRoundResult[]{FightRoundResult.WIN, FightRoundResult.TIE};
        expect(command.getEnemies()).andReturn(Arrays.asList("7", "8"));
        final ImmutableMap<String, Integer> attackStrengths = ImmutableMap.of("7", 18, "8", 13);
        expect(command.getAttackStrengths()).andReturn(attackStrengths).times(2);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenLostAgainstHighCaptainAttackStrengthShouldReturnTrue() {
        // GIVEN
        results = new FightRoundResult[]{FightRoundResult.LOSE, FightRoundResult.TIE};
        expect(command.getEnemies()).andReturn(Arrays.asList("7", "8"));
        final ImmutableMap<String, Integer> attackStrengths = ImmutableMap.of("7", 18, "8", 13);
        expect(command.getAttackStrengths()).andReturn(attackStrengths).times(2);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testExecutePostHandlerShouldKillEnemiesAndAddMarkerItem() {
        // GIVEN
        final ImmutableMap<String, Enemy> enemiesMap = ImmutableMap.of("7", (Enemy) captain, "8", elvira);
        expect(resolvationData.getEnemies()).andReturn(enemiesMap);
        captain.setStamina(0);
        elvira.setStamina(0);
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getItemHandler()).andReturn(itemHandler);
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, "4001", 1)).andReturn(1);
        mockControl.replay();
        // WHEN
        underTest.executePostHandler(command, resolvationData, results, data);
        // THEN
    }

    public void testGetEnemyIdShouldReturnSeven() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.getEnemyId();
        // THEN
        Assert.assertEquals(returned, "7");
    }

}
