package hu.zagor.gamebooks.content.command.fight.subresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.AttributeResolvingExpressionResolver;
import hu.zagor.gamebooks.character.handler.ExpressionResolver;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.luck.BattleLuckTestParameters;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.character.item.WeaponSubType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightBoundingCommandResolver;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightCommandRoundEventResolver;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.roundresolver.OnlyHighestLinkedFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.subresolver.autolose.AutoLoseHandler;
import hu.zagor.gamebooks.content.command.fight.subresolver.autolose.DefaultAutoLoseHandler;
import hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus.DefaultEnemyStatusEvaluator;
import hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus.EnemyStatusEvaluator;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Integration test for class {@link FightCommandBasicSubResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class FightCommandBasicSubResolverWithOnlyHighestLinkedFightRoundResolverMT extends FfMtTextResolvingTest {

    private static final int[] TW25 = new int[]{7, 2, 5};
    private static final int[] TW16 = new int[]{7, 1, 6};
    private static final int[] TW41 = new int[]{5, 4, 1};
    private static final int[] TW44 = new int[]{8, 4, 4};
    private static final int[] TW15 = new int[]{6, 1, 5};
    private static final int[] TW14 = new int[]{5, 1, 4};
    private static final int[] TW43 = new int[]{7, 4, 3};
    private FightCommandBasicSubResolver underTest;
    private IMocksControl mockControl;
    private BeanFactory beanFactory;
    private FightCommand command;
    private ResolvationData resolvationData;
    private FfParagraphData rootData;
    private FfCharacter character;
    private Map<String, Enemy> enemies;
    private BookInformations info;
    private FfEnemy enemyA;
    private FfEnemy enemyB;
    private FfParagraphData flee;
    private FfParagraphData win;
    private FfParagraphData lose;

    private FightRoundResolver fightRoundResolver;
    private Logger logger;
    private RandomNumberGenerator generator;

    private FightCommandRoundEventResolver roundEventResolver;
    private FightBoundingCommandResolver fightBoundingCommandResolver;
    private FightCommandBeforeEventResolver beforeEventResolver;
    private FfCharacterHandler characterHandler;
    private FfAttributeHandler attributeHandler;
    private FfUserInteractionHandler interactionHandler;
    private FfItem weapon;
    private CharacterItemHandler itemHandler;
    private BattleLuckTestParameters battleLuckTestParameters;
    private FightOutcome outcome;

    private EnemyStatusEvaluator enemyStatusEvaluator;
    private AutoLoseHandler autoLoseHandler;
    private DiceResultRenderer diceResultRenderer;
    private ExpressionResolver expressionResolver;
    private Paragraph paragraph;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        beanFactory = mockControl.createMock(BeanFactory.class);
        roundEventResolver = new FightCommandRoundEventResolver();
        fightBoundingCommandResolver = new FightBoundingCommandResolver();
        beforeEventResolver = new DefaultFightCommandBeforeEventResolver();
        logger = mockControl.createMock(Logger.class);
        generator = mockControl.createMock(RandomNumberGenerator.class);

        fightRoundResolver = new OnlyHighestLinkedFightRoundResolver();
        init(mockControl, fightRoundResolver);
        Whitebox.setInternalState(fightRoundResolver, "logger", logger);
        Whitebox.setInternalState(fightRoundResolver, "generator", generator);

        underTest = new FightCommandBasicSubResolver();
        underTest.setBeanFactory(beanFactory);
        enemyStatusEvaluator = new DefaultEnemyStatusEvaluator();
        autoLoseHandler = new DefaultAutoLoseHandler();
        Whitebox.setInternalState(underTest, "roundEventResolver", roundEventResolver);
        Whitebox.setInternalState(underTest, "fightBoundingCommandResolver", fightBoundingCommandResolver);
        Whitebox.setInternalState(underTest, "beforeEventResolver", beforeEventResolver);
        Whitebox.setInternalState(underTest, "enemyStatusEvaluator", enemyStatusEvaluator);
        Whitebox.setInternalState(underTest, "autoLoseHandler", autoLoseHandler);
        Whitebox.setInternalState(autoLoseHandler, "enemyStatusEvaluator", enemyStatusEvaluator);
        diceResultRenderer = mockControl.createMock(DiceResultRenderer.class);
        Whitebox.setInternalState(fightRoundResolver, "diceResultRenderer", diceResultRenderer);
    }

    private void setUpBusinessLogic() {
        flee = new FfParagraphData();
        win = new FfParagraphData();
        lose = new FfParagraphData();

        outcome = new FightOutcome();
        outcome.setParagraphData(win);

        command = new FightCommand();
        init(command);
        command.getEnemies().add("1");
        command.getEnemies().add("2");
        command.setBattleType("single");
        command.getWin().add(outcome);
        command.setLose(lose);
        command.setFlee(flee);

        rootData = new FfParagraphData();
        rootData.setBeanFactory(beanFactory);
        rootData.setText("<p>You must fight the Two-Headed Dog.</p>");

        weapon = new FfItem("1001", "Sword", ItemType.weapon1);
        weapon.setStaminaDamage(2);
        weapon.getEquipInfo().setEquipped(true);

        character = new FfCharacter();
        character.setInitialSkill(8);
        character.setSkill(8);
        character.setInitialStamina(17);
        character.setStamina(17);
        character.setInitialLuck(10);
        character.setLuck(10);
        character.getEquipment().add(weapon);

        enemyA = new FfEnemy();
        enemyA.setName("Two-Headed Dog, First Head");
        enemyA.setCommonName("Two-Headed Dog");
        enemyA.setSkill(8);
        enemyA.setStamina(9);
        enemyA.setId("1");
        enemyA.setStaminaDamage(2);

        enemyB = new FfEnemy();
        enemyB.setName("Two-Headed Dog, Second Head");
        enemyB.setCommonName("Two-Headed Dog");
        enemyB.setSkill(8);
        enemyB.setStamina(9);
        enemyB.setId("2");
        enemyB.setStaminaDamage(2);

        enemies = new HashMap<>();
        enemies.put("1", enemyA);
        enemies.put("2", enemyB);

        attributeHandler = new FfAttributeHandler();
        Whitebox.setInternalState(attributeHandler, "logger", logger);
        expressionResolver = new AttributeResolvingExpressionResolver();
        Whitebox.setInternalState(attributeHandler, "expressionResolver", expressionResolver);
        Whitebox.setInternalState(expressionResolver, "logger", logger);

        interactionHandler = new FfUserInteractionHandler();
        interactionHandler.setFightCommand(character, FightCommand.ATTACKING);
        interactionHandler.setFightCommand(character, "luckOnHit", "false");
        interactionHandler.setFightCommand(character, "luckOnDefense", "false");
        interactionHandler.setFightCommand(character, "enemyId", "1");

        itemHandler = new FfCharacterItemHandler();

        battleLuckTestParameters = new BattleLuckTestParameters(3, 2, 1, 4);

        characterHandler = new FfCharacterHandler();
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setBattleLuckTestParameters(battleLuckTestParameters);

        info = new FfBookInformations(1L);
        info.setResourceDir("ff3");
        info.setCharacterHandler(characterHandler);

        paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);

        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).withEnemies(enemies)
            .build();
    }

    @BeforeMethod
    public void setUpMethod() {
        setUpBusinessLogic();
        mockControl.reset();
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndWeTieShouldNobodyLoseHealth() {
        // GIVEN
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(generator.getRandomNumber(2)).andReturn(TW25);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 15);
        expect(diceResultRenderer.render(6, TW25)).andReturn("Thrown values: 2, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 2, 5.", 15});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 15);
        expectText("page.ff.label.fight.single.tied", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 9);
        Assert.assertEquals(enemyB.getStamina(), 9);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndOneLosesTheOtherTiesShouldNobodyLoseHealth() {
        // GIVEN
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(generator.getRandomNumber(2)).andReturn(TW25);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 14);
        expect(diceResultRenderer.render(6, TW25)).andReturn("Thrown values: 2, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 2, 5.", 15});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 15);
        expectText("page.ff.label.fight.single.tied", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 9);
        Assert.assertEquals(enemyB.getStamina(), 9);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndOneTiesTheOtherLosesShouldNobodyLoseHealth() {
        // GIVEN
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 15);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        expectText("page.ff.label.fight.single.tied", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 9);
        Assert.assertEquals(enemyB.getStamina(), 9);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothLosesShouldBothLoseHealth() {
        // GIVEN
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW14);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW14)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 4.", 13});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 13);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 7);
        Assert.assertEquals(enemyB.getStamina(), 7);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndOneLosesOneWinsShouldHeroLoseHealth() {
        // GIVEN
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW44);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW44)).andReturn("Thrown values: 4, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 4, 4.", 16});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 16);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        logger.error("Cannot resolve property '{}'.", "initialDamageProtection");
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 15);
        Assert.assertEquals(enemyA.getStamina(), 9);
        Assert.assertEquals(enemyB.getStamina(), 9);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothWinsShouldHeroLoseHealthOnlyOnce() {
        // GIVEN
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW41);
        expect(generator.getRandomNumber(2)).andReturn(TW44);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW41)).andReturn("Thrown values: 4, 1.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 1.", 13});
        logger.debug("Attack strength for self: {}", 13);
        expect(diceResultRenderer.render(6, TW44)).andReturn("Thrown values: 4, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 4, 4.", 16});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 16);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        logger.error("Cannot resolve property '{}'.", "initialDamageProtection");
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 15);
        Assert.assertEquals(enemyA.getStamina(), 9);
        Assert.assertEquals(enemyB.getStamina(), 9);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothWinsAndLuckTestFailsShouldHeroLoseSingleLuckAndMoreHealthOnce() {
        // GIVEN
        interactionHandler.setFightCommand(character, "luckOnDefense", "true");

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW41);
        expect(generator.getRandomNumber(2)).andReturn(TW44);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW41)).andReturn("Thrown values: 4, 1.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 1.", 13});
        logger.debug("Attack strength for self: {}", 13);
        expect(diceResultRenderer.render(6, TW44)).andReturn("Thrown values: 4, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 4, 4.", 16});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 16);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        logger.error("Cannot resolve property '{}'.", "initialDamageProtection");
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Two-Headed Dog"});
        expect(generator.getRandomNumber(2)).andReturn(new int[]{12, 6, 6});
        expectText("page.ff.label.fight.luck.roll", new Object[]{6, 6, 12});
        logger.debug("Unsuccessful luck test ({}) while receiving damage.", 12);
        expectText("page.ff.label.fight.luck.defense.failure", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 11);
        Assert.assertEquals(character.getLuck(), 9);
        Assert.assertEquals(enemyA.getStamina(), 9);
        Assert.assertEquals(enemyB.getStamina(), 9);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothWinsAndLuckTestSucceedsShouldHeroLoseSingleLuckAndLessHealthOnce() {
        // GIVEN
        interactionHandler.setFightCommand(character, "luckOnDefense", "true");

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW41);
        expect(generator.getRandomNumber(2)).andReturn(TW44);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW41)).andReturn("Thrown values: 4, 1.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 1.", 13});
        logger.debug("Attack strength for self: {}", 13);
        expect(diceResultRenderer.render(6, TW44)).andReturn("Thrown values: 4, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 4, 4.", 16});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 16);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        logger.error("Cannot resolve property '{}'.", "initialDamageProtection");
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Two-Headed Dog"});
        expect(generator.getRandomNumber(2)).andReturn(new int[]{2, 1, 1});
        expectText("page.ff.label.fight.luck.roll", new Object[]{1, 1, 2});
        logger.debug("Successful luck test ({}) while receiving damage.", 2);
        expectText("page.ff.label.fight.luck.defense.success", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 16);
        Assert.assertEquals(character.getLuck(), 9);
        Assert.assertEquals(enemyA.getStamina(), 9);
        Assert.assertEquals(enemyB.getStamina(), 9);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothLosesWithSuccessfulLuckTestShouldBothLoseMoreHealth() {
        // GIVEN
        interactionHandler.setFightCommand(character, "luckOnHit", "true");

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW14);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW14)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 4.", 13});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 13);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Two-Headed Dog"});

        expect(generator.getRandomNumber(2)).andReturn(new int[]{2, 1, 1});
        expectText("page.ff.label.fight.luck.roll", new Object[]{1, 1, 2});
        logger.debug("Successful luck test ({}) while dealing damage.", 2);
        expectText("page.ff.label.fight.luck.attack.success", new Object[]{"Two-Headed Dog"});

        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 4);
        Assert.assertEquals(enemyB.getStamina(), 4);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothLosesWithUnsuccessfulLuckTestShouldBothLoseLessHealth() {
        // GIVEN
        interactionHandler.setFightCommand(character, "luckOnHit", "true");

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW14);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW14)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 4.", 13});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 13);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Two-Headed Dog"});

        expect(generator.getRandomNumber(2)).andReturn(new int[]{12, 6, 6});
        expectText("page.ff.label.fight.luck.roll", new Object[]{6, 6, 12});
        logger.debug("Unsuccessful luck test ({}) while dealing damage.", 12);
        expectText("page.ff.label.fight.luck.attack.failure", new Object[]{"Two-Headed Dog"});

        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 9);
        Assert.assertEquals(enemyB.getStamina(), 9);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothLosesButHasImmunityToWeaponShouldNotLoseHealth() {
        // GIVEN
        enemyA.setKillableByNormal(false);
        enemyB.setKillableByNormal(false);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW14);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW14)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 4.", 13});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 13);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        expectText("page.ff.label.fight.single.successfulAttack.ineffectual", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 9);
        Assert.assertEquals(enemyB.getStamina(), 9);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothLosesAgainstEdgedWeaponButHasEdgedAbsorbtionShouldBothLoseLessHealth() {
        // GIVEN
        enemyA.setDamageAbsorptionEdged(1);
        enemyB.setDamageAbsorptionEdged(1);
        weapon.setSubType(WeaponSubType.edged);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW14);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW14)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 4.", 13});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 13);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 8);
        Assert.assertEquals(enemyB.getStamina(), 8);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothLosesAgainstWeakBluntWeaponButHasEdgedAbsorbtionShouldBothLoseLessHealth() {
        // GIVEN
        enemyA.setDamageAbsorptionEdged(1);
        enemyB.setDamageAbsorptionEdged(1);
        weapon.setSubType(WeaponSubType.weakBlunt);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW14);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW14)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 4.", 13});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 13);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 8);
        Assert.assertEquals(enemyB.getStamina(), 8);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothLosesAgainstBluntWeaponWhileHasEdgedAbsorbtionShouldBothLoseSameHealth() {
        // GIVEN
        enemyA.setDamageAbsorptionEdged(1);
        enemyB.setDamageAbsorptionEdged(1);
        weapon.setSubType(WeaponSubType.blunt);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW14);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW14)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 4.", 13});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 13);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 7);
        Assert.assertEquals(enemyB.getStamina(), 7);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothLosesWhileHasDamageAbsorbtionShouldBothLoseLessHealth() {
        // GIVEN
        enemyA.setDamageAbsorption(1);
        enemyB.setDamageAbsorption(1);
        weapon.setSubType(WeaponSubType.blunt);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW14);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW14)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 4.", 13});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 13);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 8);
        Assert.assertEquals(enemyB.getStamina(), 8);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndBothLosesWhileHasDamageAbsorbtionHigherThanDamageShouldNotLoseHealthNorGainHealth() {
        // GIVEN
        enemyA.setDamageAbsorption(4);
        enemyB.setDamageAbsorption(4);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW14);
        expect(generator.getRandomNumber(2)).andReturn(TW15);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW14)).andReturn("Thrown values: 1, 4.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, First Head", "Thrown values: 1, 4.", 13});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, First Head", 13);
        expect(diceResultRenderer.render(6, TW15)).andReturn("Thrown values: 1, 5.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Two-Headed Dog, Second Head", "Thrown values: 1, 5.", 14});
        logger.debug("Attack strength for {}: {}", "Two-Headed Dog, Second Head", 14);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyA.getStamina(), 9);
        Assert.assertEquals(enemyB.getStamina(), 9);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleFleeingFromTwoEnemiesShouldBothHitFinalAttack() {
        // GIVEN
        interactionHandler.setFightCommand(character, FightCommand.FLEEING);
        getRoundResolver();
        expectText("page.ff.label.fight.flee");
        expectText("page.ff.label.fight.single.flee", new Object[]{"Two-Headed Dog"});
        expectText("page.ff.label.fight.single.flee", new Object[]{"Two-Headed Dog"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(flee));
        Assert.assertEquals(character.getStamina(), 13);
        Assert.assertEquals(enemyA.getStamina(), 9);
        Assert.assertEquals(enemyB.getStamina(), 9);
        Assert.assertFalse(command.isOngoing());
        Assert.assertFalse(command.isKeepOpen());
    }

    private void getRoundResolver() {
        expect(beanFactory.containsBean("singleff3FightRoundResolver")).andReturn(true);
        expect(beanFactory.getBean("singleff3FightRoundResolver", FightRoundResolver.class)).andReturn(fightRoundResolver);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
