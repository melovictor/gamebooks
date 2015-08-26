package hu.zagor.gamebooks.content.command.fight.subresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.luck.BattleLuckTestParameters;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.ChoicePositionComparator;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.content.choice.DefaultChoiceSet;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.SilentCapableResolver;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommandResolver;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestSuccessType;
import hu.zagor.gamebooks.content.command.changeenemy.ChangeEnemyCommand;
import hu.zagor.gamebooks.content.command.fight.FightBoundingCommandResolver;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.FightCommandRoundEventResolver;
import hu.zagor.gamebooks.content.command.fight.FightOutcome;
import hu.zagor.gamebooks.content.command.fight.FightRoundBoundingCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.RoundEvent;
import hu.zagor.gamebooks.content.command.fight.roundresolver.FightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.roundresolver.SingleFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.stat.StatisticsProvider;
import hu.zagor.gamebooks.content.command.fight.stat.WinStatisticsProvider;
import hu.zagor.gamebooks.content.command.fight.subresolver.autolose.AutoLoseHandler;
import hu.zagor.gamebooks.content.command.fight.subresolver.autolose.DefaultAutoLoseHandler;
import hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus.DefaultEnemyStatusEvaluator;
import hu.zagor.gamebooks.content.command.fight.subresolver.enemystatus.EnemyStatusEvaluator;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import hu.zagor.gamebooks.content.command.random.RandomCommandResolver;
import hu.zagor.gamebooks.content.command.random.RandomResult;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttributeType;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.section.FfRuleBookParagraphResolver;
import hu.zagor.gamebooks.renderer.DefaultDiceResultRenderer;
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
public class FightCommandBasicSubResolverWithSingleFightRoundResolverIT extends FfItTextResolvingTest {

    private static final int[] TW16 = new int[]{7, 1, 6};
    private static final int[] TW43 = new int[]{7, 4, 3};
    private static final int[] TW46 = new int[]{10, 4, 6};
    private static final int[] TW53 = new int[]{8, 5, 3};
    private FightCommandBasicSubResolver underTest;
    private IMocksControl mockControl;
    private BeanFactory beanFactory;
    private FightCommand command;
    private ResolvationData resolvationData;
    private FfParagraphData rootData;
    private FfCharacter character;
    private Map<String, Enemy> enemies;
    private BookInformations info;
    private FfEnemy enemy;
    private FfEnemy enemyB;
    private FfParagraphData flee;
    private FfParagraphData win;
    private FfParagraphData winLater;
    private FfParagraphData winSooner;
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
    private FightFleeData fleeData;
    private FightRoundBoundingCommand bounding;
    private RandomCommand interruptingRandom;
    private RandomResult interruptingResult;
    private FfParagraphData interruptingData;
    private RandomCommandResolver randomResolver;
    private AttributeTestCommandResolver testResolver;
    private DiceConfiguration diceConfiguration;
    private DiceResultRenderer diceRenderer;
    private ChoiceSet choiceSet;
    private RoundEvent interruptingWinRound;
    private RoundEvent battleRoundWinEnemyChangingRound;
    private FfParagraphData interruptingWinData;
    private Map<FightRoundResult, StatisticsProvider> statProviders;
    private StatisticsProvider statProvider;
    private FfParagraphData battleRoundWinEnemyChangingData;
    private ChangeEnemyCommand enemyChangeCommand;
    private FightRoundBoundingCommand afterBoundingSkillTest;
    private AttributeTestCommand test;
    private FfParagraphData failingSkillTestInterruptData;
    private FfRuleBookParagraphResolver paragraphResolver;
    private Map<String, AttributeTestSuccessType> attributeTestDefaultSuccessTypes;
    private FfParagraphData failingSkillTestLoseRoundData;
    private FightOutcome outcome;
    private FightOutcome soonerOutcome;
    private FightOutcome laterOutcome;
    private RandomCommand extraDamageCausingRandom;
    private RandomResult extraDamageCausingResult;
    private FfParagraphData damagingRandom;
    private ModifyAttribute reduceStamina;
    private BattleLuckTestParameters battleLuckTestParameters;
    private FightRoundBoundingCommand afterBoundingRandomTest;
    private RandomCommand afterBoundingRandom;
    private RandomResult randomResult;
    private FfParagraphData smallDamagingRandom;
    private Map<Class<? extends Command>, SilentCapableResolver<? extends Command>> boundingResolvers;

    private EnemyStatusEvaluator enemyStatusEvaluator;
    private AutoLoseHandler autoLoseHandler;
    private DiceResultRenderer diceResultRenderer;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        beanFactory = mockControl.createMock(BeanFactory.class);
        roundEventResolver = new FightCommandRoundEventResolver();
        fightBoundingCommandResolver = new FightBoundingCommandResolver();
        beforeEventResolver = new DefaultFightCommandBeforeEventResolver();
        randomResolver = new RandomCommandResolver();
        testResolver = new AttributeTestCommandResolver();
        diceRenderer = new DefaultDiceResultRenderer();
        logger = mockControl.createMock(Logger.class);
        generator = mockControl.createMock(RandomNumberGenerator.class);
        choiceSet = new DefaultChoiceSet(new ChoicePositionComparator());

        boundingResolvers = new HashMap<>();
        boundingResolvers.put(RandomCommand.class, randomResolver);
        boundingResolvers.put(AttributeTestCommand.class, testResolver);
        fightBoundingCommandResolver.setBoundingResolvers(boundingResolvers);
        Whitebox.setInternalState(fightBoundingCommandResolver, "generator", generator);
        Whitebox.setInternalState(fightBoundingCommandResolver, "diceRenderer", diceRenderer);

        statProvider = new WinStatisticsProvider();

        statProviders = new HashMap<FightRoundResult, StatisticsProvider>();
        statProviders.put(FightRoundResult.WIN, statProvider);

        fightRoundResolver = new SingleFightRoundResolver();
        init(mockControl, fightRoundResolver);
        init(mockControl, testResolver);
        init(mockControl, fightBoundingCommandResolver);
        Whitebox.setInternalState(fightRoundResolver, "logger", logger);
        Whitebox.setInternalState(fightRoundResolver, "generator", generator);

        underTest = new FightCommandBasicSubResolver();
        underTest.setBeanFactory(beanFactory);
        enemyStatusEvaluator = new DefaultEnemyStatusEvaluator();
        autoLoseHandler = new DefaultAutoLoseHandler();
        Whitebox.setInternalState(underTest, "roundEventResolver", roundEventResolver);
        roundEventResolver.setStatProviders(statProviders);
        Whitebox.setInternalState(underTest, "fightBoundingCommandResolver", fightBoundingCommandResolver);
        Whitebox.setInternalState(underTest, "beforeEventResolver", beforeEventResolver);
        Whitebox.setInternalState(beforeEventResolver, "fightBoundingCommandResolver", fightBoundingCommandResolver);
        Whitebox.setInternalState(testResolver, "logger", logger);
        Whitebox.setInternalState(testResolver, "generator", generator);
        Whitebox.setInternalState(testResolver, "diceRenderer", diceRenderer);
        testResolver.setBeanFactory(beanFactory);
        Whitebox.setInternalState(randomResolver, "logger", logger);
        Whitebox.setInternalState(randomResolver, "generator", generator);
        Whitebox.setInternalState(randomResolver, "diceRenderer", diceRenderer);
        init(mockControl, randomResolver);
        randomResolver.setBeanFactory(beanFactory);
        Whitebox.setInternalState(underTest, "enemyStatusEvaluator", enemyStatusEvaluator);
        Whitebox.setInternalState(underTest, "autoLoseHandler", autoLoseHandler);
        Whitebox.setInternalState(autoLoseHandler, "enemyStatusEvaluator", enemyStatusEvaluator);
        diceResultRenderer = mockControl.createMock(DiceResultRenderer.class);
        Whitebox.setInternalState(fightRoundResolver, "diceResultRenderer", diceResultRenderer);
    }

    @BeforeMethod
    public void setUpMethod() {
        setUpBusinessLogic();
        mockControl.reset();
        Whitebox.setInternalState(this, "messageList", getMessageList());
    }

    private void setUpBusinessLogic() {
        flee = new FfParagraphData();
        win = new FfParagraphData();
        winSooner = new FfParagraphData();
        winLater = new FfParagraphData();
        lose = new FfParagraphData();

        soonerOutcome = new FightOutcome();
        soonerOutcome.setMax(3);
        soonerOutcome.setParagraphData(winSooner);

        laterOutcome = new FightOutcome();
        laterOutcome.setMin(4);
        laterOutcome.setParagraphData(winLater);

        outcome = new FightOutcome();
        outcome.setParagraphData(win);

        command = new FightCommand();
        Whitebox.setInternalState(command, "messages", getMessageList());
        command.getEnemies().add("1");
        command.setBattleType("single");
        command.getWin().add(outcome);
        command.setLose(lose);
        command.setFlee(flee);

        rootData = new FfParagraphData();
        rootData.setBeanFactory(beanFactory);
        rootData.setText("<p>You must fight the Orc.</p>");

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
        character.setStoneSkin(0);
        character.getEquipment().add(weapon);

        enemy = new FfEnemy();
        enemy.setName("Orc");
        enemy.setSkill(8);
        enemy.setStamina(5);
        enemy.setId("1");
        enemy.setStaminaDamage(2);

        enemyB = new FfEnemy();
        enemyB.setName("Goblin");
        enemyB.setSkill(8);
        enemyB.setStamina(5);
        enemyB.setId("2");
        enemyB.setStaminaDamage(2);

        enemies = new HashMap<>();
        enemies.put("1", enemy);
        enemies.put("2", enemyB);

        attributeHandler = new FfAttributeHandler();

        interactionHandler = new FfUserInteractionHandler();
        interactionHandler.setFightCommand(character, FightCommand.ATTACKING);
        interactionHandler.setFightCommand(character, "luckOnHit", "false");
        interactionHandler.setFightCommand(character, "luckOnDefense", "false");
        interactionHandler.setFightCommand(character, "enemyId", "1");

        itemHandler = new FfCharacterItemHandler();

        battleLuckTestParameters = new BattleLuckTestParameters(1, 1, 1, 1);

        characterHandler = new FfCharacterHandler();
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setBattleLuckTestParameters(battleLuckTestParameters);

        attributeTestDefaultSuccessTypes = new HashMap<>();
        attributeTestDefaultSuccessTypes.put("skill", AttributeTestSuccessType.lowerEquals);

        paragraphResolver = new FfRuleBookParagraphResolver();
        paragraphResolver.setAttributeTestDefaultSuccessTypes(attributeTestDefaultSuccessTypes);

        info = new FfBookInformations(1L);
        info.setCharacterHandler(characterHandler);
        info.setParagraphResolver(paragraphResolver);
        info.setResourceDir("ff3");

        resolvationData = new ResolvationData(rootData, character, enemies, info);

        fleeData = new FightFleeData();
        fleeData.setAfterRound(3);

        interruptingData = new FfParagraphData();
        interruptingData.setInterrupt(true);
        interruptingData.setText("<p>You yield to the Bone Devil's power.</p>");
        interruptingData.setBeanFactory(beanFactory);
        interruptingData.setChoices(choiceSet);

        interruptingResult = new RandomResult();
        interruptingResult.setMin(1);
        interruptingResult.setMax(2);
        interruptingResult.setParagraphData(interruptingData);

        interruptingRandom = new RandomCommand();
        interruptingRandom.addResult(interruptingResult);
        interruptingRandom.setDiceConfig("dice1d6");

        reduceStamina = new ModifyAttribute("stamina", -1, ModifyAttributeType.change);

        damagingRandom = new FfParagraphData();
        damagingRandom.setText("The Orc breathed fire at you.");
        damagingRandom.setChoices(choiceSet);
        damagingRandom.setBeanFactory(beanFactory);
        damagingRandom.addModifyAttributes(reduceStamina);

        extraDamageCausingResult = new RandomResult();
        extraDamageCausingResult.setMin(1);
        extraDamageCausingResult.setMax(3);
        extraDamageCausingResult.setParagraphData(damagingRandom);

        extraDamageCausingRandom = new RandomCommand();
        extraDamageCausingRandom.addResult(extraDamageCausingResult);
        extraDamageCausingRandom.setDiceConfig("dice1d6");

        bounding = new FightRoundBoundingCommand(command);
        bounding.getCommands().clear();
        bounding.getCommands().add(interruptingRandom);

        diceConfiguration = new DiceConfiguration(1, 1, 6);

        interruptingWinData = new FfParagraphData();
        interruptingWinData.setText("<p>Let's take a break...</p>");
        interruptingWinData.setChoices(choiceSet);
        interruptingWinData.setInterrupt(true);

        interruptingWinRound = new RoundEvent();
        interruptingWinRound.setEnemyId("1");
        interruptingWinRound.setParagraphData(interruptingWinData);
        interruptingWinRound.setSubsequentCount(3);
        interruptingWinRound.setRoundResult(FightRoundResult.WIN);

        enemyChangeCommand = new ChangeEnemyCommand();
        enemyChangeCommand.setId("1");
        enemyChangeCommand.setAttribute("stamina");
        enemyChangeCommand.setChangeValue(3);

        battleRoundWinEnemyChangingData = new FfParagraphData();
        battleRoundWinEnemyChangingData.getImmediateCommands().add(enemyChangeCommand);

        battleRoundWinEnemyChangingRound = new RoundEvent();
        battleRoundWinEnemyChangingRound.setEnemyId("1");
        battleRoundWinEnemyChangingRound.setParagraphData(battleRoundWinEnemyChangingData);
        battleRoundWinEnemyChangingRound.setTotalCount(3);
        battleRoundWinEnemyChangingRound.setRoundResult(FightRoundResult.WIN);

        failingSkillTestInterruptData = new FfParagraphData();
        failingSkillTestInterruptData.setText("You drop your weapon.");
        failingSkillTestInterruptData.setInterrupt(true);
        failingSkillTestInterruptData.setBeanFactory(beanFactory);
        failingSkillTestInterruptData.setChoices(choiceSet);

        failingSkillTestLoseRoundData = new FfParagraphData();
        failingSkillTestLoseRoundData.setText("You drop your weapon.");
        failingSkillTestLoseRoundData.setLoseBattleRound(true);
        failingSkillTestLoseRoundData.setBeanFactory(beanFactory);
        failingSkillTestLoseRoundData.setChoices(choiceSet);

        test = new AttributeTestCommand();
        test.setAgainst("skill");
        test.setConfigurationName("dice1d6");
        test.setFailure(failingSkillTestInterruptData);

        afterBoundingSkillTest = new FightRoundBoundingCommand(command);
        afterBoundingSkillTest.getCommands().add(test);

        afterBoundingRandomTest = new FightRoundBoundingCommand(command);
        afterBoundingRandom = new RandomCommand();
        afterBoundingRandom.setDiceConfig("dice1d6");
        randomResult = new RandomResult();
        randomResult.setMin(1);
        randomResult.setMax(3);
        smallDamagingRandom = new FfParagraphData();
        smallDamagingRandom.setChoices(choiceSet);
        smallDamagingRandom.setBeanFactory(beanFactory);
        smallDamagingRandom.setText("The Orc hit you with acid causing 1 STAMINA point of damage.");
        smallDamagingRandom.addModifyAttributes(new ModifyAttribute("stamina", -1, ModifyAttributeType.change));
        randomResult.setParagraphData(smallDamagingRandom);
        afterBoundingRandom.addResult(randomResult);

        afterBoundingRandomTest.getCommands().add(afterBoundingRandom);

    }

    public void testDoResolveWhenInSingleBattleThereIsOnlyOneEnemyAndWeTieNobodyShouldLoseHealth() {
        // GIVEN
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.tied", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereIsOnlyOneEnemyAndWeWinShouldEnemyLoseStaminaByWeapon() {
        // GIVEN
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereIsOnlyOneEnemyAndWeWinAndKillEnemyShouldReturnWinSection() {
        // GIVEN
        weapon.setStaminaDamage(5);
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(win));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 0);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereIsOnlyOneEnemyAndWeLoseShouldLoseHealth() {
        // GIVEN
        weapon.setStaminaDamage(5);
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW46);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW46)).andReturn("Thrown values: 4, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 4, 6.", 18});
        logger.debug("Attack strength for {}: {}", "Orc", 18);
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 15);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereIsOnlyOneEnemyAndWeLoseButHaveStoneSkinShouldLoseOneStoneSkin() {
        // GIVEN
        character.setStoneSkin(3);
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW46);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW46)).andReturn("Thrown values: 4, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 4, 6.", 18});
        logger.debug("Attack strength for {}: {}", "Orc", 18);
        expectText("page.ff.label.fight.single.failedDefense.noEffect", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
        Assert.assertEquals(character.getStoneSkin(), 2);
    }

    public void testDoResolveWhenInSingleBattleThereIsOnlyOneEnemyAndWeLoseAndDieShouldLoseHealthAndReturnLoseSection() {
        // GIVEN
        enemy.setStaminaDamage(17);
        weapon.setStaminaDamage(5);
        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW46);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW46)).andReturn("Thrown values: 4, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 4, 6.", 18});
        logger.debug("Attack strength for {}: {}", "Orc", 18);
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(lose));
        Assert.assertEquals(character.getStamina(), 0);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndWeTieNobodyShouldLoseHealth() {
        // GIVEN
        command.getEnemies().add("2");

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.tied", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertEquals(enemyB.getStamina(), 5);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereAreTwoEnemiesAndWeTieWithSecondNobodyShouldLoseHealth() {
        // GIVEN
        interactionHandler.setFightCommand(character, "enemyId", "2");
        command.getEnemies().add("2");

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        expectText("page.ff.label.fight.single.tied", new Object[]{"Goblin"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemyB.getStamina(), 5);
        Assert.assertEquals(enemyB.getStamina(), 5);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenSecondEnemyAttacksWhenIdleAndAttackingFirstShouldLoseHealth() {
        // GIVEN
        enemyB.setStaminaDamageWhileInactive(1);
        command.getEnemies().add("2");

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.tied", new Object[]{"Orc"});
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Goblin"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 16);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertEquals(enemyB.getStamina(), 5);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenSecondEnemyAttacksWhenIdleAndAttackingSecondShouldNotLoseHealth() {
        // GIVEN
        enemyB.setStaminaDamageWhileInactive(1);
        command.getEnemies().add("2");
        interactionHandler.setFightCommand(character, "enemyId", "2");

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW43);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW43)).andReturn("Thrown values: 4, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 4, 3.", 15});
        logger.debug("Attack strength for self: {}", 15);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        expectText("page.ff.label.fight.single.tied", new Object[]{"Goblin"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertEquals(enemyB.getStamina(), 5);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenEnemyIsDeadShouldReportVictory() {
        // GIVEN
        enemy.setStamina(0);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(win));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 0);
        Assert.assertEquals(enemyB.getStamina(), 5);
        Assert.assertFalse(command.isOngoing());
        Assert.assertFalse(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenCharacterIsDeadShouldReportFailure() {
        // GIVEN
        character.setStamina(0);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(lose));
        Assert.assertEquals(character.getStamina(), 0);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertEquals(enemyB.getStamina(), 5);
        Assert.assertFalse(command.isOngoing());
        Assert.assertFalse(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereIsOnlyOneEnemyAndWeWinAndKillEnemyShouldReturnWinSectionAndNotLoseStaminaFromAutoDamage() {
        // GIVEN
        enemy.setStaminaAutoDamage(1);
        weapon.setStaminaDamage(5);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(win));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 0);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereIsOnlyOneEnemyAndWeWinAndHitEnemyShouldLoseStaminaFromAutoDamage() {
        // GIVEN
        enemy.setStaminaAutoDamage(1);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 16);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAutoLoseRoundIsNotReachedYetShouldBattleContinue() {
        // GIVEN
        command.setAutoLoseRound(4);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAutoLoseRoundIsReachedAndEnemyIsStillAliveShouldLoseBattle() {
        // GIVEN
        command.setAutoLoseRound(4);
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();

        getMessageList().setRoundMessage(4);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(lose));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAutoLoseRoundIsReachedAndEnemyIsDeadShouldWinBattle() {
        // GIVEN
        command.setAutoLoseRound(4);
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();
        enemy.setStamina(2);

        getMessageList().setRoundMessage(4);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(win));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 0);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleFleeRoundIsNotReachedYetShouldNotAllowFleeing() {
        // GIVEN
        command.setFleeData(fleeData);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleFleeRoundIsReachedShouldAllowFleeing() {
        // GIVEN
        command.setFleeData(fleeData);
        command.increaseBattleRound();
        command.increaseBattleRound();

        getMessageList().setRoundMessage(3);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertTrue((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleFleeRoundIsPassedShouldStillAllowFleeing() {
        // GIVEN
        command.setFleeData(fleeData);
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();

        getMessageList().setRoundMessage(7);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertTrue((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleFleeRoundIsPassedButEnemiesAreDeadShouldStillAllowFleeing() {
        // GIVEN
        command.setFleeData(fleeData);
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();
        enemy.setStamina(2);

        getMessageList().setRoundMessage(7);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(win));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 0);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertTrue((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenNoCommandIsSetShouldDoNothing() {
        // GIVEN
        interactionHandler.setFightCommand(character, "");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertTrue(command.isOngoing());
        Assert.assertFalse(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndBeforeRoundDoesNotResultInInterruptionShouldContinueBattle() {
        // GIVEN
        command.setBeforeBounding(bounding);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        expect(generator.getRandomNumber(diceConfiguration)).andReturn(new int[]{5, 5});
        logger.debug("Random command generated the number '{}'.", 5);
        expectTextWoLocale("page.raw.label.random.after", "Thrown value: 5.", true, "<span class='diced65'></span>", 5);
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndBeforeRoundDoesResultInInterruptionShouldAbortBattle() {
        // GIVEN
        command.setBeforeBounding(bounding);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        expect(generator.getRandomNumber(diceConfiguration)).andReturn(new int[]{1, 1});
        logger.debug("Random command generated the number '{}'.", 1);
        expectTextWoLocale("page.raw.label.random.after", "Thrown value: 1.", true, "<span class='diced61'></span>", 1);
        expect(beanFactory.getBean(ChoiceSet.class)).andReturn(choiceSet);
        getMessageList().switchToRoundMessages();
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        final FfParagraphData paragraphData = (FfParagraphData) returned.get(0);
        Assert.assertTrue(paragraphData.isInterrupt());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertFalse(command.isOngoing());
        Assert.assertFalse(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndInterruptingRoundIsWonAgainstSelectedEnemyButNotEnoughTimesShouldContinueBattle() {
        // GIVEN
        command.getRoundEvents().clear();
        command.getRoundEvents().add(interruptingWinRound);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndInterruptingRoundIsWonAgainstSelectedEnemyButTooMuchTimesShouldContinueBattle() {
        // GIVEN
        command.getRoundEvents().add(interruptingWinRound);
        command.getBattleStatistics("1").updateStats(FightRoundResult.WIN);
        command.getBattleStatistics("1").updateStats(FightRoundResult.WIN);
        command.getBattleStatistics("1").updateStats(FightRoundResult.WIN);
        command.getBattleStatistics("1").updateStats(FightRoundResult.WIN);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndInterruptingRoundIsWonAgainstSelectedEnemyExactlyTheRightAmountOfTimesShouldInterruptBattle() {
        // GIVEN
        command.getRoundEvents().add(interruptingWinRound);
        command.getBattleStatistics("1").updateStats(FightRoundResult.WIN);
        command.getBattleStatistics("1").updateStats(FightRoundResult.WIN);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(interruptingWinData));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndTotalRoundEventNotMatchedYetShouldDoNothing() {
        // GIVEN
        command.getRoundEvents().add(this.battleRoundWinEnemyChangingRound);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndTotalRoundEventIsReachedShouldRequestImmediateChangeToEnemy() {
        // GIVEN
        command.getRoundEvents().add(this.battleRoundWinEnemyChangingRound);
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.getBattleStatistics("1").updateStats(FightRoundResult.WIN);
        command.getBattleStatistics("1").updateStats(FightRoundResult.WIN);

        getMessageList().setRoundMessage(3);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(battleRoundWinEnemyChangingData));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndTotalRoundEventIsReachedButCurrentResultIsLoseShouldNotRequestImmediateChangeToEnemy() {
        // GIVEN
        command.getRoundEvents().add(this.battleRoundWinEnemyChangingRound);
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.getBattleStatistics("1").updateStats(FightRoundResult.WIN);
        command.getBattleStatistics("1").updateStats(FightRoundResult.WIN);
        command.getBattleStatistics("1").updateStats(FightRoundResult.WIN);
        enemy.setSkill(12);

        getMessageList().setRoundMessage(4);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 19});
        logger.debug("Attack strength for {}: {}", "Orc", 19);
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 15);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndAutoLoseRoundIsNotReachedButBattleIsWonAgainstSelectedEnemyShouldReportWin() {
        // GIVEN
        command.setAutoLoseRound(3);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndAutoLoseRoundIsReachedButBattleIsWonAgainstSelectedEnemyShouldFinishBattleWithLose() {
        // GIVEN
        command.setAutoLoseRound(3);
        command.increaseBattleRound();
        command.increaseBattleRound();

        getMessageList().setRoundMessage(3);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(lose));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleFailedSkillTestAfterRoundResultsInInterruptionButTestIsSuccessfulShouldReportTestAndContinueBattle() {
        // GIVEN
        command.setAfterBounding(afterBoundingSkillTest);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        getMessageList().switchToPostRoundMessages();
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        final int[] thrownValues = new int[]{5, 3, 2};
        expect(generator.getRandomNumber(diceConfiguration, 0)).andReturn(thrownValues);
        expectTextWoLocale("page.ff.label.test.success", "The test is successful.", false);
        expectTextWoLocale("page.ff.label.test.skill.compact", "The throw: 3|2. The total: 5. The test is successful.", true,
            "<span class='diced63'></span><span class='diced62'></span>", 5, "The test is successful.");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleFailedSkillTestAfterRoundResultsInInterruptionAndTestIsFailedShouldReportTestAndInterruptBattle() {
        // GIVEN
        command.setAfterBounding(afterBoundingSkillTest);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        getMessageList().switchToPostRoundMessages();
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        final int[] thrownValues = new int[]{12, 6, 6};
        expect(generator.getRandomNumber(diceConfiguration, 0)).andReturn(thrownValues);
        expectTextWoLocale("page.ff.label.test.failure", "The test failed.", false);
        expectTextWoLocale("page.ff.label.test.skill.compact", "The throw: 6|6. The total: 12. The test failed.", true,
            "<span class='diced66'></span><span class='diced66'></span>", 12, "The test failed.");
        expect(beanFactory.getBean(ChoiceSet.class)).andReturn(choiceSet);
        mockControl.replay();
        // WHEN
        underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleFailedSkillTestBeforeRoundResultsInRoundLoseAndTestIsSuccessfulShouldExecuteBattleRoundNormally() {
        // GIVEN
        command.setBeforeBounding(afterBoundingSkillTest);
        test.setFailure(failingSkillTestLoseRoundData);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        final int[] thrownValues = new int[]{6, 2, 4};
        expect(generator.getRandomNumber(diceConfiguration, 0)).andReturn(thrownValues);
        expectTextWoLocale("page.ff.label.test.success", "The test succeeded.", false);
        expectTextWoLocale("page.ff.label.test.skill.compact", "The throw: 2|4. The total: 6. The test succeeded.", true,
            "<span class='diced62'></span><span class='diced64'></span>", 6, "The test succeeded.");
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleFailedSkillTestBeforeRoundResultsInRoundLoseAndTestIsFailureShouldAutomaticallyLoseRound() {
        // GIVEN
        command.setBeforeBounding(afterBoundingSkillTest);
        test.setFailure(failingSkillTestLoseRoundData);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        final int[] thrownValues = new int[]{12, 6, 6};
        expect(generator.getRandomNumber(diceConfiguration, 0)).andReturn(thrownValues);
        expectTextWoLocale("page.ff.label.test.failure", "The test failed.", false);
        expectTextWoLocale("page.ff.label.test.skill.compact", "The throw: 6|6. The total: 12. The test failed.", true,
            "<span class='diced66'></span><span class='diced66'></span>", 12, "The test failed.");
        expect(beanFactory.getBean(ChoiceSet.class)).andReturn(choiceSet);
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(returned.size(), 1);
        Assert.assertEquals(character.getStamina(), 15);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereIsOnlyOneEnemyAndWeWinAndKillEnemySoonerShouldReturnSoonerWinSection() {
        // GIVEN
        command.getWin().clear();
        command.getWin().add(soonerOutcome);
        command.getWin().add(laterOutcome);
        enemy.setStamina(2);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(winSooner));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 0);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereIsOnlyOneEnemyAndWeWinAndKillEnemyRightOnSoonerShouldReturnSoonerWinSection() {
        // GIVEN
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.getWin().clear();
        command.getWin().add(soonerOutcome);
        command.getWin().add(laterOutcome);
        enemy.setStamina(2);

        getMessageList().setRoundMessage(3);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(winSooner));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 0);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleThereIsOnlyOneEnemyAndWeWinAndKillEnemyLaterShouldReturnLaterWinSection() {
        // GIVEN
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.increaseBattleRound();
        command.getWin().clear();
        command.getWin().add(soonerOutcome);
        command.getWin().add(laterOutcome);
        enemy.setStamina(2);

        getMessageList().setRoundMessage(4);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.contains(winLater));
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 0);
        Assert.assertFalse(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndAfterRoundCanButDoNotCauseExtraDamageShouldNotSufferDamage() {
        // GIVEN
        command.setAfterBounding(bounding);
        bounding.getCommands().clear();
        bounding.getCommands().add(extraDamageCausingRandom);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        getMessageList().switchToPostRoundMessages();
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        expect(generator.getRandomNumber(diceConfiguration)).andReturn(new int[]{5, 5});
        logger.debug("Random command generated the number '{}'.", 5);
        expectTextWoLocale("page.raw.label.random.after", "Thrown number: 5.", true, "<span class='diced65'></span>", 5);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertTrue(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndBeforeRoundCanAndDoCauseExtraDamageAndNoLuckTestExecutedShouldSufferExtraDamage() {
        // GIVEN
        command.setAfterBounding(bounding);
        bounding.getCommands().clear();
        bounding.getCommands().add(extraDamageCausingRandom);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        getMessageList().switchToPostRoundMessages();
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        expect(generator.getRandomNumber(diceConfiguration)).andReturn(new int[]{1, 1});
        logger.debug("Random command generated the number '{}'.", 1);
        expectTextWoLocale("page.raw.label.random.after", "Thrown value: 1.", true, "<span class='diced61'></span>", 1);
        expect(beanFactory.getBean(ChoiceSet.class)).andReturn(choiceSet);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertFalse(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 16);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndAfterRoundCanAndDoCauseExtraDamageAndLuckTestIsSuccessfulShouldSufferReducedExtraDamage() {
        // GIVEN
        bounding.setLuckAllowed(true);
        command.setAfterBounding(bounding);
        bounding.getCommands().clear();
        bounding.getCommands().add(extraDamageCausingRandom);
        interactionHandler.setFightCommand(character, "luckOnOther", "true");

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        getMessageList().switchToPostRoundMessages();
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        expect(generator.getRandomNumber(diceConfiguration)).andReturn(new int[]{1, 1});
        logger.debug("Random command generated the number '{}'.", 1);
        expectTextWoLocale("page.raw.label.random.after", "Thrown value: 1.", true, "<span class='diced61'></span>", 1);
        expect(beanFactory.getBean(ChoiceSet.class)).andReturn(choiceSet);
        final int[] thrown = new int[]{2, 1, 1};
        expect(generator.getRandomNumber(2)).andReturn(thrown);
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expectTextWoLocale("page.ff.label.test.success", "The test succeeded.", false);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expectText("page.ff.label.test.luck.compact", new Object[]{"<span class='diced61'></span><span class='diced61'></span>", 2, "The test succeeded."});
        expectText("page.ff.label.fight.luck.defense.success.unknown");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertFalse(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 17);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndAfterRoundCanAndDoCauseExtraDamageAndLuckTestFailsShouldSufferIncreasedExtraDamage() {
        // GIVEN
        bounding.setLuckAllowed(true);
        command.setAfterBounding(bounding);
        bounding.getCommands().clear();
        bounding.getCommands().add(extraDamageCausingRandom);
        interactionHandler.setFightCommand(character, "luckOnOther", "true");

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW16);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW16)).andReturn("Thrown values: 1, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 1, 6.", 15});
        logger.debug("Attack strength for {}: {}", "Orc", 15);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Orc"});
        getMessageList().switchToPostRoundMessages();
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        expect(generator.getRandomNumber(diceConfiguration)).andReturn(new int[]{1, 1});
        logger.debug("Random command generated the number '{}'.", 1);
        expectTextWoLocale("page.raw.label.random.after", "Thrown number: 1.", true, "<span class='diced61'></span>", 1);
        expect(beanFactory.getBean(ChoiceSet.class)).andReturn(choiceSet);
        final int[] thrown = new int[]{12, 6, 6};
        expect(generator.getRandomNumber(2)).andReturn(thrown);
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expectTextWoLocale("page.ff.label.test.failure", "The test failed.", false);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expectText("page.ff.label.test.luck.compact", "<span class='diced66'></span><span class='diced66'></span>", 12, "The test failed.");
        expectText("page.ff.label.fight.luck.defense.failure.unknown");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertFalse(returned.isEmpty());
        Assert.assertEquals(character.getStamina(), 15);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertTrue(command.isOngoing());
        Assert.assertTrue(command.isKeepOpen());
        Assert.assertFalse((boolean) Whitebox.getInternalState(command, "fleeAllowed"));
    }

    public void testDoResolveWhenInSingleBattleAndEnemyHitsTheHeroPlusDamagesInAfterRandomAndHeroIsKilledDueToTheRandomPartShouldLoseFight() {
        // GIVEN
        character.setStamina(3);
        command.setAfterBounding(afterBoundingRandomTest);

        getMessageList().setRoundMessage(1);
        getMessageList().switchToPreRoundMessages();
        getMessageList().switchToRoundMessages();
        getRoundResolver();
        expect(generator.getRandomNumber(2)).andReturn(TW53);
        expect(generator.getRandomNumber(2)).andReturn(TW46);
        expect(diceResultRenderer.render(6, TW53)).andReturn("Thrown values: 5, 3.");
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{"Thrown values: 5, 3.", 16});
        logger.debug("Attack strength for self: {}", 16);
        expect(diceResultRenderer.render(6, TW46)).andReturn("Thrown values: 4, 6.");
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Orc", "Thrown values: 4, 6.", 18});
        logger.debug("Attack strength for {}: {}", "Orc", 18);
        expectText("page.ff.label.fight.single.failedDefense", new Object[]{"Orc"});
        getMessageList().switchToPostRoundMessages();
        expect(getMessageList().getLocale()).andReturn(getLocale());
        expect(beanFactory.getBean("dice1d6", DiceConfiguration.class)).andReturn(diceConfiguration);
        expect(generator.getRandomNumber(diceConfiguration)).andReturn(new int[]{2, 2});
        logger.debug("Random command generated the number '{}'.", 2);
        expectTextWoLocale("page.raw.label.random.after", "Thrown value: 2.", true, "<span class='diced62'></span>", 2);
        expect(beanFactory.getBean(ChoiceSet.class)).andReturn(choiceSet);

        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertEquals(character.getStamina(), 0);
        Assert.assertFalse(command.isOngoing());
        Assert.assertSame(returned.get(1), lose);
    }

    private void getRoundResolver() {
        expect(beanFactory.containsBean("singleff3FightRoundResolver")).andReturn(false);
        expect(beanFactory.getBean("singleFightRoundResolver", FightRoundResolver.class)).andReturn(fightRoundResolver);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
