package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.controller;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus.EnemyStatusEvaluator;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.HuntService;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.ui.Model;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff23BookSectionController}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff23BookSectionControllerTest {

    private Ff23BookSectionController underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private SectionHandlingService sectionHandlingService;
    @Mock private Paragraph paragraph;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private Model model;
    @Mock private HttpServletRequest request;
    @Mock private HuntRoundResult huntRoundResult;
    @Mock private HttpSession session;
    @Inject private BeanFactory beanFactory;
    @Inject private HuntService huntService;
    private FfBookInformations info;
    @Mock private ParagraphData data;
    @Mock private CommandList commands;
    @Mock private FfFightCommand ffFightCommand;
    @Mock private FfCharacter character;
    @Mock private List<String> enemies;
    @Mock private List<FfEnemy> resolvedEnemies;
    @Mock private Map<String, Enemy> enemyMap;
    @Mock private FfEnemy enemy;
    @Inject private EnemyStatusEvaluator<FfEnemy> enemyStatusEvaluator;
    @Mock private FfFightCommand singleFfFightCommand;
    @Mock private BattleStatistics battleStatistics;
    @Mock private FfEnemy enemyB;
    @Mock private Iterator<FfEnemy> iterator;
    @Inject private Logger logger;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(3L);
        Whitebox.setInternalState(underTest, "info", info);
    }

    @UnderTest
    public Ff23BookSectionController underTest() {
        return new Ff23BookSectionController(sectionHandlingService);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenSectionHandlerIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new Ff23BookSectionController(null).getClass();
        // THEN throws exception
    }

    public void testHandleCustomSectionsWhenAtSection240ShouldClearAllValidMoves() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("240");
        paragraph.clearValidMoves();
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPost(model, wrapper, true);
        // THEN
    }

    public void testHandleCustomSectionsWhenAtSectionIsNot240ShouldDoNothing() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getId()).andReturn("160");
        mockControl.replay();
        // WHEN
        underTest.handleCustomSectionsPost(model, wrapper, true);
        // THEN
    }

    public void testHandleHuntRoundWhenCalledShouldCallHuntServiceAndReturnResult() {
        // GIVEN
        logger.info("Starting to play a hunt round.");
        expectWrapper();
        expect(huntService.playRound(wrapper, info)).andReturn(huntRoundResult);
        mockControl.replay();
        // WHEN
        final HuntRoundResult returned = underTest.handleHuntRound(request);
        // THEN
        Assert.assertSame(returned, huntRoundResult);
    }

    public void testHandleBeforeFightWhenFightingWithGeneralEnemyShouldDoNothing() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleBeforeFight(wrapper, "1");
        // THEN
    }

    @Test(dataProvider = "mordidaProvider")
    public void testHandleBeforeFightWhenFightingWithMordidaShouldSwitchEnemyOrder(final String enemyId) {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commands);
        expect(commands.get(0)).andReturn(ffFightCommand);
        expect(ffFightCommand.getEnemies()).andReturn(enemies);
        expect(enemies.remove(0)).andReturn(enemyId);
        expect(enemies.add(enemyId)).andReturn(true);
        mockControl.replay();
        // WHEN
        underTest.handleBeforeFight(wrapper, enemyId);
        // THEN
    }

    public void testHandleAfterFightWhenGenericEnemyShouldDoNothing() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, "1");
        // THEN
    }

    public void testHandleAfterFightWhenEnemyIsTribesmanAndOnlyOneIsAliveShoundSetNextFightRoundsIndex() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commands);
        expect(commands.get(0)).andReturn(ffFightCommand);
        expect(ffFightCommand.getEnemies()).andReturn(enemies);
        expect(ffFightCommand.getResolvedEnemies()).andReturn(resolvedEnemies);
        enemies.clear();
        resolvedEnemies.clear();
        expect(wrapper.getEnemies()).andReturn(enemyMap);
        expect(enemyMap.get("44")).andReturn(enemy);
        expect(enemyStatusEvaluator.isAlive(enemy, 2)).andReturn(false);
        expect(enemyMap.get("45")).andReturn(enemyB);
        expect(enemyStatusEvaluator.isAlive(enemyB, 2)).andReturn(true);
        expect(enemies.add("45")).andReturn(true);
        expect(resolvedEnemies.add(enemyB)).andReturn(true);
        expect(enemies.size()).andReturn(1);
        expect(commands.size()).andReturn(1);
        expect(commands.get(0)).andReturn(singleFfFightCommand);
        expect(ffFightCommand.getRoundNumber()).andReturn(7);
        singleFfFightCommand.increaseBattleRound();
        expectLastCall().times(7);
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, "44");
        // THEN
    }

    public void testHandleAfterFightWhenEnemyIsTribesmanAndDidNotWinLastRoundShoundSendBackBothEnemies() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commands);
        expect(commands.get(0)).andReturn(ffFightCommand);
        expect(ffFightCommand.getEnemies()).andReturn(enemies);
        expect(ffFightCommand.getResolvedEnemies()).andReturn(resolvedEnemies);
        enemies.clear();
        resolvedEnemies.clear();
        expect(wrapper.getEnemies()).andReturn(enemyMap);
        expect(enemyMap.get("44")).andReturn(enemy);
        expect(enemyStatusEvaluator.isAlive(enemy, 2)).andReturn(true);
        expect(enemies.add("44")).andReturn(true);
        expect(resolvedEnemies.add(enemy)).andReturn(true);
        expect(enemyMap.get("45")).andReturn(enemyB);
        expect(enemyStatusEvaluator.isAlive(enemyB, 2)).andReturn(true);
        expect(enemies.add("45")).andReturn(true);
        expect(resolvedEnemies.add(enemyB)).andReturn(true);
        expect(enemies.size()).andReturn(2);
        expect(ffFightCommand.getBattleStatistics("45")).andReturn(battleStatistics);
        expect(battleStatistics.getSubsequentWin()).andReturn(0);
        expect(commands.size()).andReturn(2);
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, "45");
        // THEN
    }

    public void testHandleAfterFightWhenEnemyIsTribesmanAndWonLastRoundShoundSendBackOtherEnemyOnly() {
        // GIVEN
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commands);
        expect(commands.get(0)).andReturn(ffFightCommand);
        expect(ffFightCommand.getEnemies()).andReturn(enemies);
        expect(ffFightCommand.getResolvedEnemies()).andReturn(resolvedEnemies);
        enemies.clear();
        resolvedEnemies.clear();
        expect(wrapper.getEnemies()).andReturn(enemyMap);
        expect(enemyMap.get("44")).andReturn(enemy);
        expect(enemyStatusEvaluator.isAlive(enemy, 2)).andReturn(true);
        expect(enemies.add("44")).andReturn(true);
        expect(resolvedEnemies.add(enemy)).andReturn(true);
        expect(enemyMap.get("45")).andReturn(enemyB);
        expect(enemyStatusEvaluator.isAlive(enemyB, 2)).andReturn(true);
        expect(enemies.add("45")).andReturn(true);
        expect(resolvedEnemies.add(enemyB)).andReturn(true);
        expect(enemies.size()).andReturn(2);
        expect(ffFightCommand.getBattleStatistics("45")).andReturn(battleStatistics);
        expect(battleStatistics.getSubsequentWin()).andReturn(1);

        expect(enemies.remove("45")).andReturn(true);
        expect(resolvedEnemies.iterator()).andReturn(iterator);
        expect(iterator.hasNext()).andReturn(true);
        expect(iterator.next()).andReturn(enemy);
        expect(enemy.getId()).andReturn("44");
        expect(iterator.hasNext()).andReturn(true);
        expect(iterator.next()).andReturn(enemyB);
        expect(enemyB.getId()).andReturn("45");
        iterator.remove();
        expect(iterator.hasNext()).andReturn(false);

        expect(commands.size()).andReturn(2);
        mockControl.replay();
        // WHEN
        underTest.handleAfterFight(wrapper, "45");
        // THEN
    }

    @DataProvider(name = "mordidaProvider")
    public Object[][] dataProviderMordida() {
        return new Object[][]{{"27"}, {"28"}};
    }

    private void expectWrapper() {
        expect(beanFactory.getBean("httpSessionWrapper", request)).andReturn(wrapper);
    }

}
