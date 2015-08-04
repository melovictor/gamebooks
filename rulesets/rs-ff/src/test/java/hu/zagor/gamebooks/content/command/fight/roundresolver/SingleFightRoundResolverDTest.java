package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.luck.BattleLuckTestParameters;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.command.FfTextResolvingTest;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;

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
 * Unit test for class {@link SingleFightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SingleFightRoundResolverDTest extends FfTextResolvingTest {

    private SingleFightRoundResolver underTest;
    private IMocksControl mockControl;
    private Logger logger;
    private RandomNumberGenerator generator;
    private List<FfEnemy> enemies;
    private ResolvationData resolvationData;
    private FightCommand command;
    private FfEnemy selectedEnemy;
    private BookInformations info;
    private FfCharacterHandler characterHandler;
    private FfCharacter character;
    private FfUserInteractionHandler interactionHandler;
    private FfParagraphData rootData;
    private FfAttributeHandler attributeHandler;
    private FfCharacterItemHandler itemHandler;
    private BattleLuckTestParameters battleLuckTestParameters;
    private FightBeforeRoundResult beforeRoundResult;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);
        generator = mockControl.createMock(RandomNumberGenerator.class);
        setUpEnemy();
        setUpResolvationData();
        underTest = new SingleFightRoundResolver();
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "generator", generator);
        init(mockControl, underTest);
        beforeRoundResult = new FightBeforeRoundResult();
    }

    private void setUpResolvationData() {
        info = new FfBookInformations(1);
        characterHandler = new FfCharacterHandler();
        battleLuckTestParameters = new BattleLuckTestParameters(1, 1, 1, 1);
        characterHandler.setBattleLuckTestParameters(battleLuckTestParameters);
        interactionHandler = mockControl.createMock(FfUserInteractionHandler.class);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        characterHandler.setItemHandler(itemHandler);
        info.setCharacterHandler(characterHandler);
        character = mockControl.createMock(FfCharacter.class);
        rootData = new FfParagraphData();
        resolvationData = new ResolvationData(rootData, character, null, info);
    }

    private void setUpEnemy() {
        selectedEnemy = new FfEnemy();
        selectedEnemy.setId("9");
        selectedEnemy.setName("Goblin");
        selectedEnemy.setSkill(8);
        selectedEnemy.setStaminaDamage(2);
    }

    @BeforeMethod
    public void setUpMethod() {
        command = new FightCommand();
        init(command);
        Whitebox.setInternalState(command, "roundNumber", 1);
        fixEnemy();
        enemies = new ArrayList<>();
        command.getResolvedEnemies().clear();
        command.setLuckOnHit(false);
        command.setLuckOnDefense(false);
        mockControl.reset();
    }

    private void fixEnemy() {
        selectedEnemy.setStaminaAutoDamage(0);
        selectedEnemy.setKillableByNormal(true);
        selectedEnemy.setKillableByMagical(true);
        selectedEnemy.setKillableByBlessed(true);
        selectedEnemy.setStamina(3);
        selectedEnemy.setDamageAbsorptionEdged(0);
    }

    public void testResolveRoundWhenHeroIsHitWithSuccessfulLuckTestShouldSufferLessDamageRecordInformationAndReturnRoundResultWithLose() {
        // GIVEN
        command.setLuckOnDefense(true);
        enemies.add(selectedEnemy);
        command.getResolvedEnemies().addAll(enemies);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("9");
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        expect(generator.getRandomNumber(2, 0)).andReturn(new int[]{2, 1, 1});
        expect(generator.getRandomNumber(2)).andReturn(new int[]{7, 3, 4});
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{1, 1, 12});
        logger.debug("Attack strength for self: {}", 12);
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", 3, 4, 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        expect(character.getStoneSkin()).andReturn(0);
        character.changeStamina(-2);
        character.changeSkill(0);
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Goblin"});
        expect(generator.getRandomNumber(2)).andReturn(new int[]{2, 1, 1});
        expectText("page.ff.label.fight.luck.roll", new Object[]{1, 1, 2});
        expect(attributeHandler.resolveValue(character, "luck")).andReturn(9);
        logger.debug("Successful luck test ({}) while receiving damage.", 2);
        expectText("page.ff.label.fight.luck.defense.success", new Object[]{"Goblin"});
        character.changeStamina(1);
        character.changeLuck(-1);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned.length, 1);
        Assert.assertEquals(returned[0], FightRoundResult.LOSE);
        Assert.assertEquals(selectedEnemy.getStamina(), 3);
    }

    public void testResolveRoundWhenHeroIsHitShouldSufferDamageRecordInformationAndReturnRoundResultWithLose() {
        // GIVEN
        enemies.add(selectedEnemy);
        command.getResolvedEnemies().addAll(enemies);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("9");
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        expect(generator.getRandomNumber(2, 0)).andReturn(new int[]{2, 1, 1});
        expect(generator.getRandomNumber(2)).andReturn(new int[]{7, 3, 4});
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{1, 1, 12});
        logger.debug("Attack strength for self: {}", 12);
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", 3, 4, 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        expect(character.getStoneSkin()).andReturn(0);
        character.changeStamina(-2);
        character.changeSkill(0);
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Goblin"});
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned.length, 1);
        Assert.assertEquals(returned[0], FightRoundResult.LOSE);
        Assert.assertEquals(selectedEnemy.getStamina(), 3);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
