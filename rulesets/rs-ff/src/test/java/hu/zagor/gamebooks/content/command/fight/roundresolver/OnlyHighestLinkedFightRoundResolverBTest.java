package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.luck.BattleLuckTestParameters;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.character.item.WeaponSubType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.FfTextResolvingTest;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link OnlyHighestLinkedFightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class OnlyHighestLinkedFightRoundResolverBTest extends FfTextResolvingTest {

    private OnlyHighestLinkedFightRoundResolver underTest;
    private IMocksControl mockControl;
    private FightCommand command;
    private ResolvationData resolvationData;
    private List<FfEnemy> enemies;
    private FfEnemy enemyA;
    private FfEnemy enemyB;
    private FfParagraphData rootData;
    private FfCharacter character;
    private RandomNumberGenerator generator;
    private Logger logger;
    private BookInformations info;
    private FfCharacterHandler characterHandler;
    private BattleLuckTestParameters battleLuckTestParameters;
    private FfAttributeHandler attributeHandler;
    private FfItem selectedWeapon;
    private FfCharacterItemHandler itemHandler;
    private FightBeforeRoundResult beforeRoundResult;
    private DiceResultRenderer diceResultRenderer;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new OnlyHighestLinkedFightRoundResolver();
        enemyA = createEnemy("26a", "First Head");
        enemyB = createEnemy("26b", "Second Head");
        enemies = new ArrayList<>();
        enemies.add(enemyA);
        enemies.add(enemyB);
        rootData = new FfParagraphData();
        character = mockControl.createMock(FfCharacter.class);
        info = new BookInformations(9L);
        characterHandler = new FfCharacterHandler();
        battleLuckTestParameters = new BattleLuckTestParameters(1, 1, 1, 1);
        characterHandler.setBattleLuckTestParameters(battleLuckTestParameters);
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        info.setCharacterHandler(characterHandler);
        resolvationData = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character).build();
        generator = mockControl.createMock(RandomNumberGenerator.class);
        logger = mockControl.createMock(Logger.class);
        Whitebox.setInternalState(underTest, "generator", generator);
        Whitebox.setInternalState(underTest, "logger", logger);
        selectedWeapon = new FfItem("1003", "Sword", ItemType.weapon1);
        selectedWeapon.setStaminaDamage(2);
        init(mockControl, underTest);
        beforeRoundResult = new FightBeforeRoundResult();
        diceResultRenderer = mockControl.createMock(DiceResultRenderer.class);
        Whitebox.setInternalState(underTest, "diceResultRenderer", diceResultRenderer);
    }

    @BeforeMethod
    public void setUpMethod() {
        command = new FightCommand();
        init(command);
        command.increaseBattleRound();
        command.getResolvedEnemies().addAll(enemies);
        command.setLuckOnHit(true);
        resetEnemy(enemyA);
        resetEnemy(enemyB);
        mockControl.reset();
    }

    private void resetEnemy(final FfEnemy enemy) {
        enemy.setStamina(5);
        enemy.setSkill(9);
        enemy.setStaminaDamage(2);
        enemy.setKillableByNormal(true);
    }

    private FfEnemy createEnemy(final String id, final String name) {
        final FfEnemy enemy = new FfEnemy();
        enemy.setId(id);
        enemy.setName("Two-Headed Dog, " + name);
        enemy.setCommonName("Two-Headed Dog");
        return enemy;
    }

    public void testResolveRoundWhenDoubleAttackingEnemyLosesWithBothHitsAndLuckTestIsSuccessfulShouldReturnWin() {
        // GIVEN
        selectedWeapon.setSubType(WeaponSubType.weakBlunt);
        final int[] selfAttackStrength = new int[]{5, 1, 4};
        expect(generator.getRandomNumber(2)).andReturn(selfAttackStrength);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        final int[] enemyAttackStrengthA = new int[]{2, 1, 1};
        expect(generator.getRandomNumber(2)).andReturn(enemyAttackStrengthA);
        final int[] enemyAttackStrengthB = new int[]{3, 1, 2};
        expect(generator.getRandomNumber(2)).andReturn(enemyAttackStrengthB);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(diceResultRenderer.render(6, selfAttackStrength)).andReturn("Thrown value: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown value: 1, 4.", 14});
        logger.debug("Attack strength for self: {}", 14);
        expect(diceResultRenderer.render(6, enemyAttackStrengthA)).andReturn("Thrown value: 1, 1.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown value: 1, 1.", 11});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 11);
        expect(diceResultRenderer.render(6, enemyAttackStrengthB)).andReturn("Thrown value: 1, 2.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown value: 1, 2.", 12});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 12);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(selectedWeapon);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Two-Headed Dog"});
        expect(generator.getRandomNumber(2)).andReturn(new int[]{2, 1, 1});
        expectText("page.ff.label.fight.luck.roll", new Object[]{1, 1, 2});
        expect(attributeHandler.resolveValue(character, "luck")).andReturn(7);
        logger.debug("Successful luck test ({}) while dealing damage.", 2);
        expectText("page.ff.label.fight.luck.attack.success", new Object[]{"Two-Headed Dog"});
        character.changeLuck(-1);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned[0], FightRoundResult.WIN);
        Assert.assertEquals(returned[1], FightRoundResult.WIN);
        Assert.assertEquals(enemyA.getStamina(), 2);
        Assert.assertEquals(enemyA.getStamina(), enemyB.getStamina());
    }

    public void testResolveRoundWhenDoubleAttackingEnemyLosesWithBothHitsAndLuckTestIsUnsuccessfulShouldReturnWin() {
        // GIVEN
        selectedWeapon.setSubType(WeaponSubType.blunt);
        final int[] selfAttackStrength = new int[]{5, 1, 4};
        expect(generator.getRandomNumber(2)).andReturn(selfAttackStrength);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        final int[] enemyAttackStrengthA = new int[]{2, 1, 1};
        expect(generator.getRandomNumber(2)).andReturn(enemyAttackStrengthA);
        final int[] enemyAttackStrengthB = new int[]{3, 1, 2};
        expect(generator.getRandomNumber(2)).andReturn(enemyAttackStrengthB);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(diceResultRenderer.render(6, selfAttackStrength)).andReturn("Thrown value: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown value: 1, 4.", 14});
        logger.debug("Attack strength for self: {}", 14);
        expect(diceResultRenderer.render(6, enemyAttackStrengthA)).andReturn("Thrown value: 1, 1.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown value: 1, 1.", 11});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 11);
        expect(diceResultRenderer.render(6, enemyAttackStrengthB)).andReturn("Thrown value: 1, 2.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown value: 1, 2.", 12});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 12);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(selectedWeapon);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Two-Headed Dog"});
        expect(generator.getRandomNumber(2)).andReturn(new int[]{2, 1, 1});
        expectText("page.ff.label.fight.luck.roll", new Object[]{1, 1, 2});
        expect(attributeHandler.resolveValue(character, "luck")).andReturn(1);
        logger.debug("Unsuccessful luck test ({}) while dealing damage.", 2);
        expectText("page.ff.label.fight.luck.attack.failure", new Object[]{"Two-Headed Dog"});
        character.changeLuck(-1);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned[0], FightRoundResult.WIN);
        Assert.assertEquals(returned[1], FightRoundResult.WIN);
        Assert.assertEquals(enemyA.getStamina(), 4);
        Assert.assertEquals(enemyA.getStamina(), enemyB.getStamina());
    }

    public void testResolveRoundWhenDoubleAttackingEnemyLosesWithBothHitsButWeaponIsIneffectiveShouldReturnWin() {
        // GIVEN
        enemyA.setKillableByNormal(false);
        enemyB.setKillableByNormal(false);
        command.setLuckOnHit(false);
        final int[] selfAttackStrength = new int[]{5, 1, 4};
        expect(generator.getRandomNumber(2)).andReturn(selfAttackStrength);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        final int[] enemyAttackStrengthA = new int[]{2, 1, 1};
        expect(generator.getRandomNumber(2)).andReturn(enemyAttackStrengthA);
        final int[] enemyAttackStrengthB = new int[]{3, 1, 2};
        expect(generator.getRandomNumber(2)).andReturn(enemyAttackStrengthB);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(9);
        expect(diceResultRenderer.render(6, selfAttackStrength)).andReturn("Thrown value: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown value: 1, 4.", 14});
        logger.debug("Attack strength for self: {}", 14);
        expect(diceResultRenderer.render(6, enemyAttackStrengthA)).andReturn("Thrown value: 1, 1.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown value: 1, 1.", 11});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 11);
        expect(diceResultRenderer.render(6, enemyAttackStrengthB)).andReturn("Thrown value: 1, 2.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown value: 1, 2.", 12});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 12);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(selectedWeapon);
        expectText("page.ff.label.fight.single.successfulAttack.ineffectual", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned[0], FightRoundResult.WIN);
        Assert.assertEquals(returned[1], FightRoundResult.WIN);
        Assert.assertEquals(enemyA.getStamina(), 5);
        Assert.assertEquals(enemyA.getStamina(), enemyB.getStamina());
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
