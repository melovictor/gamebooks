package hu.zagor.gamebooks.ff.ff.tot.mvc.books.section.service.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FireflyHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class FireflyHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private FireflyHandler underTest;
    @Mock private FightCommand command;
    @Mock private ResolvationData resolvationData;
    @Instance private EnemyPrePostFightDataContainer data;
    @Mock private FfEnemy enemy;
    @Mock private List<FfEnemy> enemies;
    @Inject private RandomNumberGenerator generator;
    @Inject private DiceResultRenderer renderer;
    @Mock private FightCommandMessageList messages;
    private final FightRoundResult[] results = new FightRoundResult[]{FightRoundResult.TIE, FightRoundResult.LOSE, FightRoundResult.WIN};
    @Mock private FfCharacter character;
    @Mock private Map<String, Enemy> enemyList;

    @BeforeClass
    public void setUpClass() {
        data.setCurrentEnemy(enemy);
    }

    public void testShouldExecutePostHandlerWhenLostAgainstCurrentEnemyShouldReturnTrue() {
        // GIVEN
        expect(command.getResolvedEnemies()).andReturn(enemies);
        expect(enemies.indexOf(enemy)).andReturn(1);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testShouldExecutePostHandlerWhenWonAgainstCurrentEnemyShouldReturnFalse() {
        // GIVEN
        expect(command.getResolvedEnemies()).andReturn(enemies);
        expect(enemies.indexOf(enemy)).andReturn(2);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenTiedAgainstCurrentEnemyShouldReturnFalse() {
        // GIVEN
        expect(command.getResolvedEnemies()).andReturn(enemies);
        expect(enemies.indexOf(enemy)).andReturn(0);
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, results, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testExecutePostHandlerWhenRollAboveDischargeLimitShouldReportRoll() {
        // GIVEN
        final int[] rolled = new int[]{4, 4};
        expect(generator.getRandomNumber(1)).andReturn(rolled);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, rolled)).andReturn("[4]");
        expect(command.getMessages()).andReturn(messages);
        expect(messages.addKey("page.ff.label.random.after", "[4]", 4)).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.executePostHandler(command, resolvationData, results, data);
        // THEN
    }

    public void testExecutePostHandlerWhenRollBelowDischargeLimitShouldReportRollAndCauseExtraDamage() {
        // GIVEN
        data.setCurrentEnemy(enemy);
        final int[] rolled = new int[]{1, 1};
        expect(generator.getRandomNumber(1)).andReturn(rolled);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, rolled)).andReturn("[1]");
        expect(command.getMessages()).andReturn(messages);
        expect(messages.addKey("page.ff.label.random.after", "[1]", 1)).andReturn(true);
        expect(resolvationData.getCharacter()).andReturn(character);
        character.changeStamina(-2);
        expect(enemy.getName()).andReturn("Second Giant Firerly");
        expect(messages.addKey("page.ff14.fight.fireflyDischargeDamage", "Second Giant Firerly")).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.executePostHandler(command, resolvationData, results, data);
        // THEN
    }

    public void testGetEnemyIdsShouldReturnEnemyIds() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final String[] returned = underTest.getEnemyIds();
        // THEN
        Assert.assertEquals(returned, new String[]{"25", "26", "27"});
    }

}
