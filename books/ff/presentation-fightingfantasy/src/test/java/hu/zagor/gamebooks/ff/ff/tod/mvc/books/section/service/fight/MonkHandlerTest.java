package hu.zagor.gamebooks.ff.ff.tod.mvc.books.section.service.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.enemyroundresolver.BasicEnemyPrePostFightDataContainer;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link MonkHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class MonkHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private MonkHandler underTest;
    @Mock private BasicEnemyPrePostFightDataContainer data;
    private final FightRoundResult[] results = new FightRoundResult[]{FightRoundResult.WIN};
    @Mock private ResolvationData resolvationData;
    @Mock private FightCommand command;
    @Mock private Map<String, Enemy> enemyList;
    @Mock private FfEnemy enemy;

    @Inject private RandomNumberGenerator generator;
    @Inject private DiceResultRenderer renderer;
    @Mock private FightCommandMessageList messages;

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
        Assert.assertEquals(returned, "37");
    }

    public void testShouldExecutePostHandlerWhenWonLastRoundShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.WIN}, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenTiedLastRoundShouldReturnFalse() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.TIE}, data);
        // THEN
        Assert.assertFalse(returned);
    }

    public void testShouldExecutePostHandlerWhenLostLastRoundShouldReturnTrue() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final boolean returned = underTest.shouldExecutePostHandler(command, resolvationData, new FightRoundResult[]{FightRoundResult.LOSE}, data);
        // THEN
        Assert.assertTrue(returned);
    }

    public void testExecutePostHandlerWhenRolledNumberIsNotCriticalShouldReportAndDoNothing() {
        // GIVEN
        final int[] result = new int[]{3, 3};
        expect(generator.getRandomNumber(1)).andReturn(result);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, result)).andReturn("rolled: 3");
        expect(command.getMessages()).andReturn(messages);
        expect(messages.addKey("page.ff.label.random.after", "rolled: 3", 3)).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.executePostHandler(command, resolvationData, results, data);
        // THEN
    }

    public void testExecutePostHandlerWhenRolledNumberIsCriticalShouldReportCloseBattleAndRecordCriticalHitMarket() {
        // GIVEN
        final int[] result = new int[]{6, 6};
        expect(generator.getRandomNumber(1)).andReturn(result);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, result)).andReturn("rolled: 6");
        expect(command.getMessages()).andReturn(messages);
        expect(messages.addKey("page.ff.label.random.after", "rolled: 6", 6)).andReturn(true);
        expect(resolvationData.getEnemies()).andReturn(enemyList);
        expect(enemyList.get("37")).andReturn(enemy);
        enemy.setStamina(0);
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getItemHandler()).andReturn(itemHandler);
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(itemHandler.addItem(character, "4008", 1)).andReturn(1);
        mockControl.replay();
        // WHEN
        underTest.executePostHandler(command, resolvationData, results, data);
        // THEN
    }

}
