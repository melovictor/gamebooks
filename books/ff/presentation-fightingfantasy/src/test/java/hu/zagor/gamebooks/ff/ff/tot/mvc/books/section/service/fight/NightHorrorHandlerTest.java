package hu.zagor.gamebooks.ff.ff.tot.mvc.books.section.service.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link NightHorrorHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class NightHorrorHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private NightHorrorHandler underTest;
    @Mock private FfFightCommand command;
    @Instance private EnemyPrePostFightDataContainer data;
    @Mock private FfEnemy enemy;
    @Mock private ResolvationData resolvationData;
    @Inject private RandomNumberGenerator generator;
    @Inject private DiceResultRenderer renderer;
    @Mock private FightCommandMessageList messages;

    @BeforeClass
    public void setUpClass() {
        data.setCurrentEnemy(enemy);
    }

    public void testShouldExecutePreHandlerShouldReturnTrue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePreHandler(command, data);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testExecutePreHandlerShouldStoreCurrentEnemysStamina() {
        // GIVEN
        expect(enemy.getStamina()).andReturn(5);
        mockControl.replay();
        // WHEN
        underTest.executePreHandler(command, data);
        // THEN
        Assert.assertEquals(data.getEnemyStamina(), 5);
    }

    public void testShouldExecutePostHandlerWhenWeWonTheBattleShouldReturnTrue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.WIN}, data);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testShouldExecutePostHandlerWhenWeTiedTheBattleShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.TIE}, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenWeLostTheBattleShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.LOSE}, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testGetEnemyIdShouldReturn35() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String returned = underTest.getEnemyId();
        // THEN
        Assert.assertEquals(returned, "35");
    }

    public void testExecutePostHandlerWhenRolledBelowBlockAmountShouldReportRollAndBlockageAndResetEnemyStamina() {
        // GIVEN
        data.setEnemyStamina(9);
        final int[] result = new int[]{2, 2};
        expect(generator.getRandomNumber(1)).andReturn(result);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, result)).andReturn("[2]");
        expect(command.getMessages()).andReturn(messages);
        expect(messages.addKey("page.ff.label.random.after", "[2]", 2)).andReturn(true);
        enemy.setStamina(9);
        expect(messages.addKey("page.ff14.fight.nightHorror.blocked")).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.executePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.WIN}, data);
        // THEN
    }

    public void testExecutePostHandlerWhenRolledAboveBlockAmountShouldReportRoll() {
        // GIVEN
        data.setEnemyStamina(9);
        final int[] result = new int[]{5, 5};
        expect(generator.getRandomNumber(1)).andReturn(result);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, result)).andReturn("[5]");
        expect(command.getMessages()).andReturn(messages);
        expect(messages.addKey("page.ff.label.random.after", "[5]", 5)).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.executePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.WIN}, data);
        // THEN
    }
}
