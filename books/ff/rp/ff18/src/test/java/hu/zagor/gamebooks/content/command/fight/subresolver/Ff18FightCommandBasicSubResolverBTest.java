package hu.zagor.gamebooks.content.command.fight.subresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.util.Arrays;
import java.util.HashMap;
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
 * Unit test for class {@link Ff18FightCommandBasicSubResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff18FightCommandBasicSubResolverBTest {

    private Ff18FightCommandBasicSubResolver underTest;
    private IMocksControl mockControl;
    private FightCommand command;
    private ResolvationData resolvationData;
    private ParagraphData rootData;
    private FfCharacter character;
    private Map<String, Enemy> enemies;
    private FfBookInformations info;
    private FfCharacterHandler characterHandler;
    private FfUserInteractionHandler interactionHandler;
    private FightCommandBasicSubResolver superResolver;
    private List<ParagraphData> resolvedList;
    private FfCharacterItemHandler itemHandler;
    private FfItem weapon;
    private BattleStatistics battleStat;
    private RandomNumberGenerator generator;
    private DiceResultRenderer renderer;
    private FightCommandMessageList messageList;
    private FfEnemy enemy;
    private List<FfEnemy> resolvedEnemies;
    private List<String> proficientEnemies;

    @BeforeClass
    public void setUpClass() throws SecurityException {
        mockControl = EasyMock.createStrictControl();
        setUpClassUnchecked();
        underTest = new Ff18FightCommandBasicSubResolver();
        command = mockControl.createMock(FightCommand.class);
        character = mockControl.createMock(FfCharacter.class);
        info = new FfBookInformations(1L);
        characterHandler = new FfCharacterHandler();
        interactionHandler = mockControl.createMock(FfUserInteractionHandler.class);
        characterHandler.setInteractionHandler(interactionHandler);
        info.setCharacterHandler(characterHandler);
        resolvationData = new ResolvationData(rootData, character, enemies, info);
        superResolver = mockControl.createMock(FightCommandBasicSubResolver.class);
        Whitebox.setInternalState(underTest, "superResolver", superResolver);
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
        weapon = mockControl.createMock(FfItem.class);
        battleStat = mockControl.createMock(BattleStatistics.class);
        generator = mockControl.createMock(RandomNumberGenerator.class);
        Whitebox.setInternalState(underTest, "generator", generator);
        renderer = mockControl.createMock(DiceResultRenderer.class);
        Whitebox.setInternalState(underTest, "renderer", renderer);
        messageList = mockControl.createMock(FightCommandMessageList.class);
        enemy = mockControl.createMock(FfEnemy.class);
        proficientEnemies = Arrays.asList("2");
        underTest.setProficientEnemies(proficientEnemies);
    }

    @SuppressWarnings("unchecked")
    private void setUpClassUnchecked() {
        resolvedEnemies = mockControl.createMock(List.class);
        resolvedList = mockControl.createMock(List.class);
        enemies = mockControl.createMock(HashMap.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoResolveWhenFightingAgainstNormalEnemyWithFistWeaponAndEnemyAliveAndLastRoundWasATileShouldDoNothing() {
        // GIVEN
        final String enemyId = "1";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1000");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(8);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(command.getBattleStatistics(enemyId)).andReturn(battleStat);
        expect(battleStat.getSubsequentWin()).andReturn(0);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(command.getBattleStatistics(enemyId)).andReturn(battleStat);
        expect(battleStat.getSubsequentLose()).andReturn(0);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightingAgainstNormalEnemyWithFistWeaponAndEnemyAliveAndWonLastRoundButSuddenKillFailedShouldContinue() {
        // GIVEN
        final String enemyId = "1";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1000");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(8);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(command.getBattleStatistics(enemyId)).andReturn(battleStat);
        expect(battleStat.getSubsequentWin()).andReturn(2);
        expect(command.getMessages()).andReturn(messageList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getName()).andReturn("Arcadian");
        messageList.positionTo("page.ff.label.fight.single.successfulAttack", "Arcadian");
        final int[] randomRoll = new int[]{3, 3};
        expect(generator.getRandomNumber(1)).andReturn(randomRoll);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, randomRoll)).andReturn("The rolled number is 3.");
        expect(messageList.addKey("page.ff18.fight.suddenDeath.roll", "The rolled number is 3.", 3)).andReturn(true);
        superResolver.resolveBattlingParties(command, resolvationData);
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies);
        expect(resolvedEnemies.isEmpty()).andReturn(false);

        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightingAgainstNormalEnemyWithFistWeaponAndEnemyAliveAndWonLastRoundAndSuddenKillSucceededButOtherEnemyStayedAliveShouldContinue() {
        // GIVEN
        final String enemyId = "1";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1000");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(8);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(command.getBattleStatistics(enemyId)).andReturn(battleStat);
        expect(battleStat.getSubsequentWin()).andReturn(2);
        expect(command.getMessages()).andReturn(messageList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getName()).andReturn("Arcadian");
        messageList.positionTo("page.ff.label.fight.single.successfulAttack", "Arcadian");
        final int[] randomRoll = new int[]{6, 6};
        expect(generator.getRandomNumber(1)).andReturn(randomRoll);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, randomRoll)).andReturn("The rolled number is 6.");
        expect(messageList.addKey("page.ff18.fight.suddenDeath.roll", "The rolled number is 6.", 6)).andReturn(true);
        enemy.setStamina(0);
        expect(messageList.addKey("page.ff18.fight.suddenDeath.enemy", "Arcadian")).andReturn(true);
        superResolver.resolveBattlingParties(command, resolvationData);
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies);
        expect(resolvedEnemies.isEmpty()).andReturn(false);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightingAgainstNormalEnemyWithFistWeaponAndEnemyAliveAndWonLastRoundAndSuddenKillSucceededAndNoOtherEnemyStayedAliveShouldFinishBattle() {
        // GIVEN
        final String enemyId = "1";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1000");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(8);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(command.getBattleStatistics(enemyId)).andReturn(battleStat);
        expect(battleStat.getSubsequentWin()).andReturn(2);
        expect(command.getMessages()).andReturn(messageList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getName()).andReturn("Arcadian");
        messageList.positionTo("page.ff.label.fight.single.successfulAttack", "Arcadian");
        final int[] randomRoll = new int[]{6, 6};
        expect(generator.getRandomNumber(1)).andReturn(randomRoll);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, randomRoll)).andReturn("The rolled number is 6.");
        expect(messageList.addKey("page.ff18.fight.suddenDeath.roll", "The rolled number is 6.", 6)).andReturn(true);
        enemy.setStamina(0);
        expect(messageList.addKey("page.ff18.fight.suddenDeath.enemy", "Arcadian")).andReturn(true);
        superResolver.resolveBattlingParties(command, resolvationData);
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies);
        expect(resolvedEnemies.isEmpty()).andReturn(true);
        superResolver.winBattle(command, resolvedList, enemies);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightingAgainstNormalEnemyWithFistWeaponAndEnemyAliveAndLostLastRoundButEnemyIsNotSuddenKillCapableShouldContinueBattle() {
        // GIVEN
        final String enemyId = "1";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1000");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(8);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(command.getBattleStatistics(enemyId)).andReturn(battleStat);
        expect(battleStat.getSubsequentWin()).andReturn(0);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(command.getBattleStatistics(enemyId)).andReturn(battleStat);
        expect(battleStat.getSubsequentLose()).andReturn(1);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}