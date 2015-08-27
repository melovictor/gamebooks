package hu.zagor.gamebooks.content.command.fight.subresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.Custom18FightRoundResolver;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff18FightCommandCustomSubResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff18FightCommandCustomSubResolverTest {

    private Ff18FightCommandCustomSubResolver underTest;
    private IMocksControl mockControl;
    private ResolvationData resolvationData;
    private FightCommand command;
    private ParagraphData rootData;
    private FfCharacter character;
    private Map<String, Enemy> enemies;
    private FfBookInformations info;
    private FfCharacterHandler characterHandler;
    private FfUserInteractionHandler interactionHandler;
    private FfAttributeHandler attributeHandler;
    private FightCommandMessageList messageList;
    private BeanFactory beanFactory;
    private Custom18FightRoundResolver roundResolver;
    private List<FfEnemy> enemyList;
    private FfEnemy enemyA;
    private FfEnemy enemyB;
    private FfEnemy enemyC;
    private FfParagraphData resultParDataA;
    private FfParagraphData resultParDataB;
    private FfParagraphData resultParDataC;
    private FfParagraphData resultParDataD;
    private FfParagraphData resultParDataE;
    private List<FightOutcome> wins;
    private FightOutcome outcomeA;
    private FightOutcome outcomeB;
    private FightOutcome outcomeC;
    private FightOutcome outcomeD;
    private FightOutcome outcomeE;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        setUpClassUnchecked();
        underTest = EasyMock.createMockBuilder(Ff18FightCommandCustomSubResolver.class).addMockedMethods("prepareLuckTest", "resolveBattlingParties")
            .createMock(mockControl);
        character = mockControl.createMock(FfCharacter.class);
        info = new FfBookInformations(1L);
        resolvationData = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character).withEnemies(enemies).build();
        command = mockControl.createMock(FightCommand.class);
        characterHandler = new FfCharacterHandler();
        interactionHandler = mockControl.createMock(FfUserInteractionHandler.class);
        characterHandler.setInteractionHandler(interactionHandler);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        info.setCharacterHandler(characterHandler);
        messageList = mockControl.createMock(FightCommandMessageList.class);
        beanFactory = mockControl.createMock(BeanFactory.class);
        underTest.setBeanFactory(beanFactory);
        roundResolver = mockControl.createMock(Custom18FightRoundResolver.class);
        enemyA = mockControl.createMock(FfEnemy.class);
        enemyB = mockControl.createMock(FfEnemy.class);
        enemyC = mockControl.createMock(FfEnemy.class);
        enemyList = Arrays.asList(enemyA, enemyB, enemyC);
        resultParDataA = mockControl.createMock(FfParagraphData.class);
        resultParDataB = mockControl.createMock(FfParagraphData.class);
        resultParDataC = mockControl.createMock(FfParagraphData.class);
        resultParDataD = mockControl.createMock(FfParagraphData.class);
        resultParDataE = mockControl.createMock(FfParagraphData.class);
        wins = new ArrayList<>();
        outcomeA = getOutcome(0, 3, resultParDataA);
        outcomeB = getOutcome(2, 5, resultParDataB);
        outcomeC = getOutcome(3, 6, resultParDataC);
        outcomeD = getOutcome(5, 9, resultParDataD);
        outcomeE = getOutcome(6, 10, resultParDataE);
        wins.add(outcomeA);
        wins.add(outcomeB);
        wins.add(outcomeC);
        wins.add(outcomeD);
        wins.add(outcomeE);
    }

    @SuppressWarnings("unchecked")
    private void setUpClassUnchecked() {
        enemies = mockControl.createMock(Map.class);
    }

    private FightOutcome getOutcome(final int min, final int max, final FfParagraphData data) {
        final FightOutcome outcome = new FightOutcome();
        outcome.setMin(min);
        outcome.setMax(max);
        outcome.setParagraphData(data);
        return outcome;
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testConstructor() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new Ff18FightCommandCustomSubResolver().getClass();
        // THEN
    }

    public void testDoResolveWhenNotAttackingYetShouldJustSetBattleTypeToCustom() {
        // GIVEN
        underTest.prepareLuckTest(command, character, interactionHandler);
        underTest.resolveBattlingParties(command, resolvationData);
        command.setOngoing(true);
        expect(interactionHandler.getLastFightCommand(character)).andReturn(null);
        command.setBattleType("custom-18");
        command.setOngoing(true);
        attributeHandler.sanityCheck(character);
        underTest.resolveBattlingParties(command, resolvationData);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
    }

    public void testDoResolveWhenAttackingAndBattleNotFinishedShouldCallFightRoundResolverAndContinueBattling() {
        // GIVEN
        underTest.prepareLuckTest(command, character, interactionHandler);
        underTest.resolveBattlingParties(command, resolvationData);
        command.setOngoing(true);
        expect(interactionHandler.getLastFightCommand(character)).andReturn("attacking");

        command.increaseBattleRound();
        expect(command.getMessages()).andReturn(messageList);
        expect(command.getRoundNumber()).andReturn(2);
        messageList.setRoundMessage(2);
        expect(beanFactory.getBean(Custom18FightRoundResolver.class)).andReturn(roundResolver);
        expect(roundResolver.resolveRound(command, resolvationData, null)).andReturn(null);
        expect(command.getResolvedEnemies()).andReturn(enemyList);
        expect(enemyA.getStamina()).andReturn(0);
        expect(enemyB.getStamina()).andReturn(3);
        expect(enemyC.getStamina()).andReturn(7);
        expect(attributeHandler.isAlive(character)).andReturn(true);
        command.setOngoing(true);
        command.setKeepOpen(true);

        attributeHandler.sanityCheck(character);
        underTest.resolveBattlingParties(command, resolvationData);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
    }

    public void testDoResolveWhenAttackingAndWeDieShouldCallFightRoundResolverAndFinishBattling() {
        // GIVEN
        underTest.prepareLuckTest(command, character, interactionHandler);
        underTest.resolveBattlingParties(command, resolvationData);
        command.setOngoing(true);
        expect(interactionHandler.getLastFightCommand(character)).andReturn("attacking");

        command.increaseBattleRound();
        expect(command.getMessages()).andReturn(messageList);
        expect(command.getRoundNumber()).andReturn(2);
        messageList.setRoundMessage(2);
        expect(beanFactory.getBean(Custom18FightRoundResolver.class)).andReturn(roundResolver);
        expect(roundResolver.resolveRound(command, resolvationData, null)).andReturn(null);
        expect(command.getResolvedEnemies()).andReturn(enemyList);
        expect(enemyA.getStamina()).andReturn(0);
        expect(enemyB.getStamina()).andReturn(3);
        expect(enemyC.getStamina()).andReturn(7);
        expect(attributeHandler.isAlive(character)).andReturn(false);
        command.setOngoing(false);
        expect(command.getLose()).andReturn(resultParDataA);

        attributeHandler.sanityCheck(character);
        underTest.resolveBattlingParties(command, resolvationData);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(resultParDataA));
        Assert.assertEquals(returned.size(), 1);
    }

    public void testDoResolveWhenAttackingAndAllEnemiesAreDeadShouldCallFightRoundResolverAndFinishBattling() {
        // GIVEN
        underTest.prepareLuckTest(command, character, interactionHandler);
        underTest.resolveBattlingParties(command, resolvationData);
        command.setOngoing(true);
        expect(interactionHandler.getLastFightCommand(character)).andReturn("attacking");

        command.increaseBattleRound();
        expect(command.getMessages()).andReturn(messageList);
        expect(command.getRoundNumber()).andReturn(5);
        messageList.setRoundMessage(5);
        expect(beanFactory.getBean(Custom18FightRoundResolver.class)).andReturn(roundResolver);
        expect(roundResolver.resolveRound(command, resolvationData, null)).andReturn(null);
        expect(command.getResolvedEnemies()).andReturn(enemyList);
        expect(enemyA.getStamina()).andReturn(0);
        expect(enemyB.getStamina()).andReturn(0);
        expect(enemyC.getStamina()).andReturn(0);
        command.setOngoing(false);

        expect(command.getRoundNumber()).andReturn(5);
        expect(command.getWin()).andReturn(wins);

        attributeHandler.sanityCheck(character);
        underTest.resolveBattlingParties(command, resolvationData);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(resultParDataB));
        Assert.assertTrue(returned.contains(resultParDataC));
        Assert.assertTrue(returned.contains(resultParDataD));
        Assert.assertEquals(returned.size(), 3);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
