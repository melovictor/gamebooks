package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.FfTextResolvingTest;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
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
 * Unit test for class {@link SingleFightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SingleFightRoundResolverATest extends FfTextResolvingTest {

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
    private FfEnemy notSelectedEnemy;
    private FfAttributeHandler attributeHandler;
    private FfCharacterItemHandler itemHandler;
    private FightBeforeRoundResult beforeRoundResult;
    private DiceResultRenderer diceResultRenderer;

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
        diceResultRenderer = mockControl.createMock(DiceResultRenderer.class);
        Whitebox.setInternalState(underTest, "diceResultRenderer", diceResultRenderer);
    }

    private void setUpResolvationData() {
        info = new FfBookInformations(1);
        characterHandler = new FfCharacterHandler();
        interactionHandler = mockControl.createMock(FfUserInteractionHandler.class);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        characterHandler.setItemHandler(itemHandler);
        info.setCharacterHandler(characterHandler);
        character = mockControl.createMock(FfCharacter.class);
        rootData = new FfParagraphData();
        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();
    }

    private void setUpEnemy() {
        selectedEnemy = new FfEnemy();
        selectedEnemy.setId("9");
        selectedEnemy.setName("Goblin");
        selectedEnemy.setSkill(8);
        selectedEnemy.setSkillDamage(0);
        selectedEnemy.setDamageAbsorption(0);
        notSelectedEnemy = new FfEnemy();
        notSelectedEnemy.setName("Orc");
        notSelectedEnemy.setId("33");
        notSelectedEnemy.setStamina(9);
    }

    @BeforeMethod
    public void setUpMethod() {
        command = new FightCommand();
        init(command);
        Whitebox.setInternalState(command, "roundNumber", 1);
        fixEnemy();
        enemies = new ArrayList<>();
        command.getResolvedEnemies().clear();
        mockControl.reset();
    }

    private void fixEnemy() {
        notSelectedEnemy.setStaminaAutoDamage(0);
        notSelectedEnemy.setStaminaDamageWhileInactive(0);
        selectedEnemy.setStaminaAutoDamage(0);
        selectedEnemy.setKillableByNormal(true);
        selectedEnemy.setKillableByMagical(true);
        selectedEnemy.setKillableByBlessed(true);
        selectedEnemy.setStamina(3);
    }

    public void testResolveRoundWhenSingleEnemyIsSelectedAndTiedShouldRecordInformationAndReturnRoundResultWithTie() {
        // GIVEN
        enemies.add(selectedEnemy);
        command.getResolvedEnemies().addAll(enemies);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("9");
        final int[] selfAttackStrength = new int[]{5, 1, 4};
        expect(generator.getRandomNumber(2)).andReturn(selfAttackStrength);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        final int[] enemyAttackStrength = new int[]{7, 3, 4};
        expect(generator.getRandomNumber(2)).andReturn(enemyAttackStrength);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(diceResultRenderer.render(6, selfAttackStrength)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 1, 4.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, enemyAttackStrength)).andReturn("Thrown values: 3, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", "Thrown values: 3, 4.", 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        expectText("page.ff.label.fight.single.tied", new Object[]{"Goblin"});
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned.length, 1);
        Assert.assertEquals(returned[0], FightRoundResult.TIE);
    }

    public void testResolveRoundWhenSingleEnemyIsSelectedWithAutoAttackingSecondEnemyAndTiedShouldRecordInformationAndReturnRoundResultWithTie() {
        // GIVEN
        enemies.add(selectedEnemy);
        enemies.add(notSelectedEnemy);
        notSelectedEnemy.setStaminaAutoDamage(1);
        command.getResolvedEnemies().addAll(enemies);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("9");
        final int[] selfAttackStrength = new int[]{5, 1, 4};
        expect(generator.getRandomNumber(2)).andReturn(selfAttackStrength);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        final int[] enemyAttackStrength = new int[]{7, 3, 4};
        expect(generator.getRandomNumber(2)).andReturn(enemyAttackStrength);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(diceResultRenderer.render(6, selfAttackStrength)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 1, 4.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, enemyAttackStrength)).andReturn("Thrown values: 3, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", "Thrown values: 3, 4.", 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        expectText("page.ff.label.fight.single.tied", new Object[]{"Goblin"});
        character.changeStamina(-1);
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned.length, 2);
        Assert.assertEquals(returned[0], FightRoundResult.TIE);
        Assert.assertEquals(returned[1], FightRoundResult.IDLE);
    }

    public void testResolveRoundWhenSingleEnemyIsSelectedWithInactiveAutoAttackingSecondEnemyAndTiedShouldRecordInformationAndReturnRoundResultWithTie() {
        // GIVEN
        enemies.add(selectedEnemy);
        enemies.add(notSelectedEnemy);
        command.getResolvedEnemies().addAll(enemies);
        notSelectedEnemy.setStaminaDamageWhileInactive(1);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("9");
        final int[] selfAttackStrength = new int[]{5, 1, 4};
        expect(generator.getRandomNumber(2)).andReturn(selfAttackStrength);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        final int[] enemyAttackStrength = new int[]{7, 3, 4};
        expect(generator.getRandomNumber(2)).andReturn(enemyAttackStrength);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(diceResultRenderer.render(6, selfAttackStrength)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 1, 4.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, enemyAttackStrength)).andReturn("Thrown values: 3, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", "Thrown values: 3, 4.", 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        expectText("page.ff.label.fight.single.tied", new Object[]{"Goblin"});
        character.changeStamina(-1);
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned.length, 2);
        Assert.assertEquals(returned[0], FightRoundResult.TIE);
        Assert.assertEquals(returned[1], FightRoundResult.IDLE);
    }

    public void testResolveRoundWhenSingleEnemyWithAutoAttackIsHitAndKilledShouldNotAutoAttackRecordInformationAndReturnRoundResultWithWin() {
        // GIVEN
        enemies.add(selectedEnemy);
        selectedEnemy.setStaminaAutoDamage(2);
        command.getResolvedEnemies().addAll(enemies);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("9");
        final int[] selfAttackStrength = new int[]{7, 1, 6};
        expect(generator.getRandomNumber(2)).andReturn(selfAttackStrength);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        final int[] enemyAttackStrength = new int[]{7, 3, 4};
        expect(generator.getRandomNumber(2)).andReturn(enemyAttackStrength);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(diceResultRenderer.render(6, selfAttackStrength)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 1, 6.", 17});
        logger.debug("Attack strength for self: {}", 17);
        expect(diceResultRenderer.render(6, enemyAttackStrength)).andReturn("Thrown values: 3, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", "Thrown values: 3, 4.", 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        final FfItem weapon = new FfItem("7", "Sword", ItemType.weapon1);
        weapon.setSkillDamage(0);
        weapon.setStaminaDamage(3);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(attributeHandler.resolveValue(character, "baseStaminaDamage")).andReturn(0);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Goblin"});
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned.length, 1);
        Assert.assertEquals(returned[0], FightRoundResult.WIN);
    }

    public void testResolveRoundWhenSingleMagicalEnemyIsHitButWeaponIsIneffectiveShouldNotCauseDamageAndReturnRoundResultWithWin() {
        // GIVEN
        enemies.add(selectedEnemy);
        selectedEnemy.setKillableByNormal(false);
        selectedEnemy.setKillableByBlessed(false);
        command.getResolvedEnemies().addAll(enemies);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("9");
        final int[] selfAttackStrength = new int[]{7, 1, 6};
        expect(generator.getRandomNumber(2)).andReturn(selfAttackStrength);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        final int[] enemyAttackStrength = new int[]{7, 3, 4};
        expect(generator.getRandomNumber(2)).andReturn(enemyAttackStrength);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(diceResultRenderer.render(6, selfAttackStrength)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 1, 6.", 17});
        logger.debug("Attack strength for self: {}", 17);
        expect(diceResultRenderer.render(6, enemyAttackStrength)).andReturn("Thrown values: 3, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", "Thrown values: 3, 4.", 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        final FfItem weapon = new FfItem("7", "Sword", ItemType.weapon1);
        weapon.setStaminaDamage(3);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expectText("page.ff.label.fight.single.successfulAttack.ineffectual", new Object[]{"Goblin"});
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned.length, 1);
        Assert.assertEquals(returned[0], FightRoundResult.WIN);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
