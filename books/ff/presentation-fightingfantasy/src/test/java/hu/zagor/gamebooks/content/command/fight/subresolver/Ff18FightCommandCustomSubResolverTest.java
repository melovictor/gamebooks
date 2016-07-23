package hu.zagor.gamebooks.content.command.fight.subresolver;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.roundresolver.Custom18FightRoundResolver;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
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
    @MockControl private IMocksControl mockControl;
    private ResolvationData resolvationData;
    @Mock private FfFightCommand command;
    private ParagraphData rootData;
    @Mock private FfCharacter character;
    @Mock private Map<String, Enemy> enemies;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfUserInteractionHandler interactionHandler;
    @Mock private FfAttributeHandler attributeHandler;
    @Mock private FightCommandMessageList messageList;
    @Inject private BeanFactory beanFactory;
    @Mock private Custom18FightRoundResolver roundResolver;
    private List<FfEnemy> enemyList;
    @Mock private FfEnemy enemyA;
    @Mock private FfEnemy enemyB;
    @Mock private FfEnemy enemyC;
    @Mock private FfParagraphData resultParDataA;
    @Mock private FfParagraphData resultParDataB;
    @Mock private FfParagraphData resultParDataC;
    @Mock private FfParagraphData resultParDataD;
    @Mock private FfParagraphData resultParDataE;
    @Instance private List<FightOutcome> wins;
    private FightOutcome outcomeA;
    private FightOutcome outcomeB;
    private FightOutcome outcomeC;
    private FightOutcome outcomeD;
    private FightOutcome outcomeE;
    private Capture<List<ParagraphData>> resolveList;

    @BeforeClass
    public void setUpClass() {
        info = new FfBookInformations(1L);
        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).withEnemies(enemies)
            .build();
        characterHandler.setInteractionHandler(interactionHandler);
        characterHandler.setAttributeHandler(attributeHandler);
        info.setCharacterHandler(characterHandler);
        enemyList = Arrays.asList(enemyA, enemyB, enemyC);
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

    @UnderTest
    public Ff18FightCommandCustomSubResolver underTest() {
        return EasyMock.createMockBuilder(Ff18FightCommandCustomSubResolver.class).addMockedMethods("prepareLuckTest", "resolveBattlingParties").createMock(mockControl);
    }

    @BeforeMethod
    public void setUpMethod() {
        resolveList = newCapture();
    }

    private FightOutcome getOutcome(final int min, final int max, final FfParagraphData data) {
        final FightOutcome outcome = new FightOutcome();
        outcome.setMin(min);
        outcome.setMax(max);
        outcome.setParagraphData(data);
        return outcome;
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
        underTest.resolveBattlingParties(command, resolvationData, null);
        command.setOngoing(true);
        expect(interactionHandler.getLastFightCommand(character)).andReturn(null);
        command.setBattleType("custom-18");
        command.setOngoing(true);
        attributeHandler.sanityCheck(character);
        underTest.resolveBattlingParties(eq(command), eq(resolvationData), capture(resolveList));
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertSame(resolveList.getValue(), returned);
    }

    public void testDoResolveWhenAttackingAndBattleNotFinishedShouldCallFightRoundResolverAndContinueBattling() {
        // GIVEN
        underTest.prepareLuckTest(command, character, interactionHandler);
        underTest.resolveBattlingParties(command, resolvationData, null);
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
        underTest.resolveBattlingParties(eq(command), eq(resolvationData), capture(resolveList));
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertSame(resolveList.getValue(), returned);
    }

    public void testDoResolveWhenAttackingAndWeDieShouldCallFightRoundResolverAndFinishBattling() {
        // GIVEN
        underTest.prepareLuckTest(command, character, interactionHandler);
        underTest.resolveBattlingParties(command, resolvationData, null);
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
        underTest.resolveBattlingParties(eq(command), eq(resolvationData), capture(resolveList));
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(resultParDataA));
        Assert.assertEquals(returned.size(), 1);
        Assert.assertSame(resolveList.getValue(), returned);
    }

    public void testDoResolveWhenAttackingAndAllEnemiesAreDeadShouldCallFightRoundResolverAndFinishBattling() {
        // GIVEN
        underTest.prepareLuckTest(command, character, interactionHandler);
        underTest.resolveBattlingParties(command, resolvationData, null);
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
        underTest.resolveBattlingParties(eq(command), eq(resolvationData), capture(resolveList));
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(resultParDataB));
        Assert.assertTrue(returned.contains(resultParDataC));
        Assert.assertTrue(returned.contains(resultParDataD));
        Assert.assertEquals(returned.size(), 3);
        Assert.assertSame(resolveList.getValue(), returned);
    }

}
