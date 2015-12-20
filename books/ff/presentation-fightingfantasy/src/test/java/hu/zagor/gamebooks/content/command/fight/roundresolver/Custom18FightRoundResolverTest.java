package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.List;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Custom18FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class Custom18FightRoundResolverTest {

    private Custom18FightRoundResolver underTest;
    private IMocksControl mockControl;
    private FightCommand command;
    private ResolvationData resolvationData;
    private FightBeforeRoundResult beforeRoundResult;
    private ParagraphData rootData;
    private FfCharacter character;
    private Map<String, Enemy> enemies;
    private BookInformations info;
    private RandomNumberGenerator generator;
    private DiceResultRenderer diceResultRenderer;
    private SingleFightRoundResolver superResolver;
    private FightCommandMessageList messageList;
    private List<FfEnemy> enemyList;
    private FfEnemy enemy;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        setUpClassUnchecked();
        underTest = new Custom18FightRoundResolver();
        command = mockControl.createMock(FightCommand.class);
        messageList = mockControl.createMock(FightCommandMessageList.class);
        character = mockControl.createMock(FfCharacter.class);
        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).withEnemies(enemies)
            .build();
        generator = mockControl.createMock(RandomNumberGenerator.class);
        diceResultRenderer = mockControl.createMock(DiceResultRenderer.class);
        superResolver = mockControl.createMock(SingleFightRoundResolver.class);
        Whitebox.setInternalState(underTest, "generator", generator);
        Whitebox.setInternalState(underTest, "diceResultRenderer", diceResultRenderer);
        Whitebox.setInternalState(underTest, "superResolver", superResolver);
        enemy = mockControl.createMock(FfEnemy.class);
    }

    @SuppressWarnings("unchecked")
    private void setUpClassUnchecked() {
        enemyList = mockControl.createMock(List.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testResolveRoundWhenHeroWinsRoundShouldDisplayWinningMessageAndDamageEnemy() {
        // GIVEN
        final int[] randomRoll = new int[]{8, 3, 5};
        expect(generator.getRandomNumber(2)).andReturn(randomRoll);
        expect(command.getMessages()).andReturn(messageList);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(diceResultRenderer.render(6, randomRoll)).andReturn("The thrown value is 3, 5");
        expect(messageList.addKey("page.ff.label.random.after", "The thrown value is 3, 5", 8)).andReturn(true);
        expect(command.getResolvedEnemies()).andReturn(enemyList);
        expect(enemyList.get(0)).andReturn(enemy);
        expect(enemy.getCommonName()).andReturn("Bush");
        expect(messageList.addKey("page.ff.label.fight.single.successfulAttack", "Bush")).andReturn(true);
        expect(enemy.getStamina()).andReturn(7);
        enemy.setStamina(5);
        superResolver.handleVictoryLuckTest(eq(command), anyObject(FightDataDto.class));
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned[0], FightRoundResult.WIN);
        Assert.assertEquals(returned.length, 1);
    }

    public void testResolveRoundWhenHeroLosesRoundShouldDisplayLosingMessageAndDamageEnemy() {
        // GIVEN
        final int[] randomRoll = new int[]{3, 1, 2};
        expect(generator.getRandomNumber(2)).andReturn(randomRoll);
        expect(command.getMessages()).andReturn(messageList);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(diceResultRenderer.render(6, randomRoll)).andReturn("The thrown value is 1, 2");
        expect(messageList.addKey("page.ff.label.random.after", "The thrown value is 1, 2", 3)).andReturn(true);
        expect(command.getResolvedEnemies()).andReturn(enemyList);
        expect(enemyList.get(0)).andReturn(enemy);
        expect(enemy.getCommonName()).andReturn("Bush");
        expect(messageList.addKey("page.ff.label.fight.single.failedDefense", "Bush")).andReturn(true);
        character.changeStamina(-2);
        superResolver.handleDefeatLuckTest(eq(command), anyObject(FightDataDto.class));
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned[0], FightRoundResult.LOSE);
        Assert.assertEquals(returned.length, 1);
    }

    public void testResolveFleeShouldCallSuper() {
        // GIVEN
        superResolver.resolveFlee(command, resolvationData);
        mockControl.replay();
        // WHEN
        underTest.resolveFlee(command, resolvationData);
        // THEN
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
