package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.ff.sa.character.Ff12Character;
import hu.zagor.gamebooks.ff.ff.sa.enemy.DeityWeapon;
import hu.zagor.gamebooks.ff.ff.sa.enemy.Ff12Enemy;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.context.MessageSource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ShootingFf12FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class ShootingFf12FightRoundResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private ShootingFf12FightRoundResolver underTest;
    @Instance private FightCommand command;
    private ResolvationData resolvationData;
    @Instance private FightBeforeRoundResult beforeRoundResult;
    private Ff12Enemy enemy;
    @Mock private FightCommandMessageList messages;
    @Mock private Paragraph paragraph;
    private BookInformations info;
    @Instance private Ff12Character character;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfAttributeHandler attributeHandler;

    @Inject private RandomNumberGenerator generator;
    @Inject private DiceResultRenderer renderer;
    @Inject private LocaleProvider localeProvider;
    @Inject private MessageSource source;
    @Mock private Map<String, Enemy> enemies;
    @Mock private FfUserInteractionHandler interactionHandler;
    @Mock private FfItem item;
    @Instance(inject = true) private Map<Integer, DeityWeapon> deityWeapons;
    private DeityWeapon disintegrator;
    private DeityWeapon blaster;
    private DeityWeapon whip;
    private final Locale locale = Locale.ENGLISH;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(1L);
        info.setCharacterHandler(characterHandler);
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        Whitebox.setInternalState(command, "messages", messages);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();
        resolvationData.setEnemies(enemies);

        disintegrator = new DeityWeapon("disintegrator", 5, 99);
        blaster = new DeityWeapon("blaster", 8);
        whip = new DeityWeapon("whip", 7, 3);
        deityWeapons.put(3, whip);
        deityWeapons.put(5, blaster);
        deityWeapons.put(6, disintegrator);

        enemy = EasyMock.partialMockBuilder(Ff12Enemy.class).addMockedMethod("setActiveWeapon").createMock(mockControl);
    }

    @BeforeMethod
    public void setUpMethod() {
        command.getResolvedEnemies().clear();

        enemy.setCommonName("Orc");
        enemy.setId("5");
        enemy.setStamina(5);
        enemy.setSkill(7);
        enemy.setAttackPerRound(1);
        enemy.setWeapon("1002");
        enemy.setDamageAbsorption(0);

        character.setStamina(15);

        mockControl.reset();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testResolveFleeShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.resolveFlee(command, resolvationData);
        // THEN throws exception
    }

    public void testResolveRoundWhenNormalFightRoundWithSingleEnemyAndEverybodyMissingShouldPlayRoundDownProperly() {
        // GIVEN
        command.getResolvedEnemies().add(enemy);
        final int[] heroTarget = new int[]{11, 6, 5};
        final int[] enemyTarget = new int[]{9, 4, 5};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 11.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Orc", "Roll result: 11.", 11)).andReturn(true);
        expect(messages.addKey("page.ff12.fight.missedEnemy", "Orc")).andReturn(true);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.isAlive(character)).andReturn(true);
        expect(generator.getRandomNumber(2)).andReturn(enemyTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTarget)).andReturn("Roll result: 9.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Orc", "Roll result: 9.", 9)).andReturn(true);
        expect(messages.addKey("page.ff12.fight.missedHero", "Orc")).andReturn(true);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertEquals(character.getStamina(), 15);
    }

    public void testResolveRoundWhenHasRoundSkippingFlagWithSingleEnemyAndEverybodyMissingShouldPlayRoundDownProperly() {
        // GIVEN
        command.getResolvedEnemies().add(enemy);
        final int[] enemyTarget = new int[]{9, 4, 5};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>(Arrays.asList(item)));
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.isAlive(character)).andReturn(true);
        expect(generator.getRandomNumber(2)).andReturn(enemyTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTarget)).andReturn("Roll result: 9.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Orc", "Roll result: 9.", 9)).andReturn(true);
        expect(messages.addKey("page.ff12.fight.missedHero", "Orc")).andReturn(true);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertEquals(character.getStamina(), 15);
    }

    public void testResolveRoundWhenNormalFightRoundWithSingleEnemyWithZeroAttacksPerRoundAndEverybodyMissingShouldPlayRoundDownProperly() {
        // GIVEN
        enemy.setAttackPerRound(0);
        command.getResolvedEnemies().add(enemy);
        final int[] heroTarget = new int[]{11, 6, 5};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 11.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Orc", "Roll result: 11.", 11)).andReturn(true);
        expect(messages.addKey("page.ff12.fight.missedEnemy", "Orc")).andReturn(true);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertEquals(character.getStamina(), 15);
    }

    public void testResolveRoundWhenNormalFightRoundWithSingleEnemyAttackingWithLashTwiceAndHittingUsBothTimesOnceArmourDeflectedOnceArmourPiercedShouldPlayRoundDownProperly() {
        // GIVEN
        enemy.setWeapon("1001");
        enemy.setAttackPerRound(2);
        command.getResolvedEnemies().add(enemy);
        final int[] heroTarget = new int[]{11, 6, 5};
        final int[] enemyTargetA = new int[]{2, 1, 1};
        final int[] enemyTargetB = new int[]{5, 3, 2};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 11.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Orc", "Roll result: 11.", 11)).andReturn(true);
        expect(messages.addKey("page.ff12.fight.missedEnemy", "Orc")).andReturn(true);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.isAlive(character)).andReturn(true);
        expect(generator.getRandomNumber(2)).andReturn(enemyTargetA);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTargetA)).andReturn("Roll result: 2.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Orc", "Roll result: 2.", 2)).andReturn(true);
        expect(attributeHandler.resolveValue(character, "armour")).andReturn(7).times(2);
        final int[] armourProtects = new int[]{6, 2, 4};
        expect(generator.getRandomNumber(2)).andReturn(armourProtects);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, armourProtects)).andReturn("Roll result: 6.");
        expect(messages.addKey("page.ff12.fight.armourDefense", "Roll result: 6.", 6)).andReturn(true);
        expect(messages.addKey("page.ff12.fight.armourDeflected", "Orc")).andReturn(true);
        attributeHandler.handleModification(character, "armour", -1);
        expect(attributeHandler.isAlive(character)).andReturn(true);
        expect(generator.getRandomNumber(2)).andReturn(enemyTargetB);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTargetB)).andReturn("Roll result: 5.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Orc", "Roll result: 5.", 5)).andReturn(true);
        expect(attributeHandler.resolveValue(character, "armour")).andReturn(6).times(2);
        final int[] armourFails = new int[]{8, 5, 3};
        expect(generator.getRandomNumber(2)).andReturn(armourFails);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, armourFails)).andReturn("Roll result: 8.");
        expect(messages.addKey("page.ff12.fight.armourDefense", "Roll result: 8.", 8)).andReturn(true);
        expect(messages.addKey("page.ff12.fight.armourFailed", "Orc", 2)).andReturn(true);
        attributeHandler.handleModification(character, "armour", -1);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertEquals(character.getStamina(), 13);
    }

    public void testResolveRoundWhenNormalFightRoundWithSingleEnemyWithoutArmourUsingBlasterAndEverybodyHitsShouldPlayRoundDownProperly() {
        // GIVEN
        command.getResolvedEnemies().add(enemy);
        final int[] heroTarget = new int[]{5, 1, 4};
        final int[] enemyTarget = new int[]{6, 3, 3};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 5.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Orc", "Roll result: 5.", 5)).andReturn(true);

        expect(itemHandler.getEquippedWeapon(character)).andReturn(item);
        expect(item.getId()).andReturn("1002");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{4, 4});

        expect(messages.addKey("page.ff12.fight.hitEnemy", "Orc", 4)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.isAlive(character)).andReturn(true);
        expect(generator.getRandomNumber(2)).andReturn(enemyTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTarget)).andReturn("Roll result: 6.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Orc", "Roll result: 6.", 6)).andReturn(true);

        expect(attributeHandler.resolveValue(character, "armour")).andReturn(0);
        expect(generator.getRandomNumber(1)).andReturn(new int[]{1, 1});
        expect(messages.addKey("page.ff12.fight.noArmourHit", "Orc", 1)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 1);
        Assert.assertEquals(character.getStamina(), 14);
    }

    public void testResolveRoundWhenNormalFightRoundWithSingleEnemyWithNegDamageAbsorptionWhenDiesAfterWeHitShouldPlayRoundDownProperly() {
        // GIVEN
        command.getResolvedEnemies().add(enemy);
        enemy.setDamageAbsorption(-1);
        enemy.setStamina(3);
        final int[] heroTarget = new int[]{5, 1, 4};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 5.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Orc", "Roll result: 5.", 5)).andReturn(true);

        expect(itemHandler.getEquippedWeapon(character)).andReturn(item);
        expect(item.getId()).andReturn("1001");

        expect(messages.addKey("page.ff12.fight.hitEnemy", "Orc", 3)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 0);
        Assert.assertEquals(character.getStamina(), 15);
    }

    public void testResolveRoundWhenNormalFightRoundWithEnemy23WithNegDamageAbsorptionWhenDiesAfterWeHitShouldPlayRoundDownProperly() {
        // GIVEN
        command.getResolvedEnemies().add(enemy);
        enemy.setId("23");
        enemy.setDamageAbsorption(-1);
        enemy.setStamina(3);
        final int[] heroTarget = new int[]{5, 1, 4};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 5.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Orc", "Roll result: 5.", 5)).andReturn(true);

        expect(itemHandler.getEquippedWeapon(character)).andReturn(item);
        expect(item.getId()).andReturn("1001");

        expect(messages.addKey("page.ff12.fight.hitEnemy", "Orc", 3)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 0);
        Assert.assertEquals(character.getStamina(), 15);
    }

    public void testResolveRoundWhenNormalFightRoundWithEnemy23ShouldPlayRoundDownProperly() {
        // GIVEN
        command.getResolvedEnemies().add(enemy);
        enemy.setId("23");
        final int[] heroTarget = new int[]{5, 1, 4};
        final int[] enemyTarget = new int[]{6, 3, 3};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 5.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Orc", "Roll result: 5.", 5)).andReturn(true);

        expect(itemHandler.getEquippedWeapon(character)).andReturn(item);
        expect(item.getId()).andReturn("1001");

        expect(messages.addKey("page.ff12.fight.hitEnemy", "Orc", 2)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.isAlive(character)).andReturn(true);
        expect(generator.getRandomNumber(2)).andReturn(enemyTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTarget)).andReturn("Roll result: 6.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Orc", "Roll result: 6.", 6)).andReturn(true);

        expect(attributeHandler.resolveValue(character, "armour")).andReturn(0);
        expect(generator.getRandomNumber(1)).andReturn(new int[]{1, 1});
        expect(messages.addKey("page.ff12.fight.noArmourHit", "Orc", 1)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);

        expect(messages.addKey("page.ff12.fight.noArmourHit", "Orc", 2)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 3);
        Assert.assertEquals(character.getStamina(), 12);
    }

    public void testResolveRoundEnemyAttacksWithDisintegratorAndHitsUsShouldPlayRoundDownProperly() {
        // GIVEN
        enemy.setWeapon("1003");
        enemy.setAttackPerRound(2);
        command.getResolvedEnemies().add(enemy);
        final int[] heroTarget = new int[]{11, 6, 5};
        final int[] enemyTarget = new int[]{2, 1, 1};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 11.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Orc", "Roll result: 11.", 11)).andReturn(true);
        expect(messages.addKey("page.ff12.fight.missedEnemy", "Orc")).andReturn(true);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.isAlive(character)).andReturn(true);
        expect(generator.getRandomNumber(2)).andReturn(enemyTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTarget)).andReturn("Roll result: 2.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Orc", "Roll result: 2.", 2)).andReturn(true);
        expect(attributeHandler.resolveValue(character, "armour")).andReturn(7);

        expect(messages.addKey("page.ff12.fight.noArmourHit", "Orc", 24)).andReturn(true);
        expect(attributeHandler.isAlive(character)).andReturn(false);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 5);
        Assert.assertEquals(character.getStamina(), -9);
    }

    public void testResolveRoundWhenFightAgainstDeityWithBlasterShouldPlayRoundDownProperly() {
        // GIVEN
        command.getResolvedEnemies().add(enemy);
        enemy.setId("14");
        enemy.setCommonName("Deity");
        final int[] heroTarget = new int[]{5, 1, 4};
        final int[] enemyTarget = new int[]{6, 3, 3};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 5.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Deity", "Roll result: 5.", 5)).andReturn(true);

        expect(itemHandler.getEquippedWeapon(character)).andReturn(item);
        expect(item.getId()).andReturn("1002");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{4, 4});

        expect(messages.addKey("page.ff12.fight.hitEnemy", "Deity", 4)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);

        expect(generator.getRandomNumber(1)).andReturn(new int[]{5, 5});
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(source.getMessage("page.ff12.fight.deityWeapon.blaster", null, locale)).andReturn("Blaster");
        expect(messages.addKey("page.ff12.fight.deityWeaponRoll", 5, "Blaster")).andReturn(true);

        expect(attributeHandler.isAlive(character)).andReturn(true);

        expect(generator.getRandomNumber(2)).andReturn(enemyTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTarget)).andReturn("Roll result: 6.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Deity", "Roll result: 6.", 6)).andReturn(true);

        expect(attributeHandler.resolveValue(character, "armour")).andReturn(3).times(2);

        final int[] armourFailed = new int[]{12, 6, 6};
        expect(generator.getRandomNumber(2)).andReturn(armourFailed);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, armourFailed)).andReturn("Roll result: 12.");
        expect(messages.addKey("page.ff12.fight.armourDefense", "Roll result: 12.", 12)).andReturn(true);

        expect(generator.getRandomNumber(1)).andReturn(new int[]{1, 1});
        expect(messages.addKey("page.ff12.fight.armourFailed", "Deity", 1)).andReturn(true);

        attributeHandler.handleModification(character, "armour", -1);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 1);
        Assert.assertEquals(character.getStamina(), 14);
        Assert.assertEquals(enemy.getSkill(), 8);
        Assert.assertEquals(enemy.getWeapon(), "1002");
    }

    public void testResolveRoundWhenFightAgainstDeityWithDisintegratorAndNewSkillIsTooLowShouldPlayRoundDownProperly() {
        // GIVEN
        command.getResolvedEnemies().add(enemy);
        enemy.setId("14");
        enemy.setCommonName("Deity");
        final int[] heroTarget = new int[]{5, 1, 4};
        final int[] enemyTarget = new int[]{6, 3, 3};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 5.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Deity", "Roll result: 5.", 5)).andReturn(true);

        expect(itemHandler.getEquippedWeapon(character)).andReturn(item);
        expect(item.getId()).andReturn("1002");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{4, 4});

        expect(messages.addKey("page.ff12.fight.hitEnemy", "Deity", 4)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);

        expect(generator.getRandomNumber(1)).andReturn(new int[]{6, 6});
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(source.getMessage("page.ff12.fight.deityWeapon.disintegrator", null, locale)).andReturn("Disintegrator");
        expect(messages.addKey("page.ff12.fight.deityWeaponRoll", 6, "Disintegrator")).andReturn(true);

        enemy.setActiveWeapon(disintegrator);
        expectLastCall().andAnswer(setWeaponAnswer(disintegrator));

        expect(attributeHandler.isAlive(character)).andReturn(true);

        expect(generator.getRandomNumber(2)).andReturn(enemyTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTarget)).andReturn("Roll result: 6.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Deity", "Roll result: 6.", 6)).andReturn(true);

        expect(messages.addKey("page.ff12.fight.missedHero", "Deity")).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);
        expectLastCall().andAnswer(setWeaponAnswer(null));

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 1);
        Assert.assertEquals(character.getStamina(), 15);
        Assert.assertEquals(enemy.getSkill(), 5);
        Assert.assertEquals(enemy.getWeapon(), "1002");
    }

    public void testResolveRoundWhenFightAgainstDeityWithDisintegratorAndWeCanMeetNewSkillShouldPlayRoundDownProperly() {
        // GIVEN
        command.getResolvedEnemies().add(enemy);
        enemy.setId("14");
        enemy.setCommonName("Deity");
        final int[] heroTarget = new int[]{5, 1, 4};
        final int[] enemyTarget = new int[]{2, 1, 1};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 5.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Deity", "Roll result: 5.", 5)).andReturn(true);

        expect(itemHandler.getEquippedWeapon(character)).andReturn(item);
        expect(item.getId()).andReturn("1002");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{4, 4});

        expect(messages.addKey("page.ff12.fight.hitEnemy", "Deity", 4)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);

        expect(generator.getRandomNumber(1)).andReturn(new int[]{6, 6});
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(source.getMessage("page.ff12.fight.deityWeapon.disintegrator", null, locale)).andReturn("Disintegrator");
        expect(messages.addKey("page.ff12.fight.deityWeaponRoll", 6, "Disintegrator")).andReturn(true);
        enemy.setActiveWeapon(disintegrator);
        expectLastCall().andAnswer(setWeaponAnswer(disintegrator));

        expect(attributeHandler.isAlive(character)).andReturn(true);

        expect(generator.getRandomNumber(2)).andReturn(enemyTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTarget)).andReturn("Roll result: 2.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Deity", "Roll result: 2.", 2)).andReturn(true);

        expect(attributeHandler.resolveValue(character, "armour")).andReturn(3);

        expect(messages.addKey("page.ff12.fight.noArmourHit", "Deity", 99)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);
        expectLastCall().andAnswer(setWeaponAnswer(null));

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 1);
        Assert.assertEquals(character.getStamina(), -84);
        Assert.assertEquals(enemy.getSkill(), 5);
        Assert.assertEquals(enemy.getWeapon(), "1002");
    }

    public void testResolveRoundWhenFightAgainstDeityWithWhipShouldPlayRoundDownProperly() {
        // GIVEN
        command.getResolvedEnemies().add(enemy);
        enemy.setId("14");
        enemy.setCommonName("Deity");
        final int[] heroTarget = new int[]{5, 1, 4};
        final int[] enemyTarget = new int[]{6, 3, 3};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 5.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Deity", "Roll result: 5.", 5)).andReturn(true);

        expect(itemHandler.getEquippedWeapon(character)).andReturn(item);
        expect(item.getId()).andReturn("1002");
        expect(generator.getRandomNumber(1)).andReturn(new int[]{4, 4});

        expect(messages.addKey("page.ff12.fight.hitEnemy", "Deity", 4)).andReturn(true);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);

        expect(generator.getRandomNumber(1)).andReturn(new int[]{3, 3});
        expect(localeProvider.getLocale()).andReturn(locale);
        expect(source.getMessage("page.ff12.fight.deityWeapon.whip", null, locale)).andReturn("Whip");
        expect(messages.addKey("page.ff12.fight.deityWeaponRoll", 3, "Whip")).andReturn(true);
        enemy.setActiveWeapon(whip);
        expectLastCall().andAnswer(setWeaponAnswer(whip));

        expect(attributeHandler.isAlive(character)).andReturn(true);

        expect(generator.getRandomNumber(2)).andReturn(enemyTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTarget)).andReturn("Roll result: 6.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Deity", "Roll result: 6.", 6)).andReturn(true);

        expect(attributeHandler.resolveValue(character, "armour")).andReturn(3).times(2);

        final int[] armourFailed = new int[]{12, 6, 6};
        expect(generator.getRandomNumber(2)).andReturn(armourFailed);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, armourFailed)).andReturn("Roll result: 12.");
        expect(messages.addKey("page.ff12.fight.armourDefense", "Roll result: 12.", 12)).andReturn(true);

        expect(messages.addKey("page.ff12.fight.armourFailed", "Deity", 3)).andReturn(true);

        attributeHandler.handleModification(character, "armour", -1);

        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        enemy.setActiveWeapon(null);
        expectLastCall().andAnswer(setWeaponAnswer(null));

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 1);
        Assert.assertEquals(character.getStamina(), 12);
        Assert.assertEquals(enemy.getSkill(), 7);
        Assert.assertEquals(enemy.getWeapon(), "1002");
    }

    private IAnswer<Object> setWeaponAnswer(final DeityWeapon selectedWeapon) {
        return new IAnswer<Object>() {

            @Override
            public Object answer() throws Throwable {
                Whitebox.setInternalState(enemy, "activeWeapon", selectedWeapon);
                return null;
            }
        };
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
