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
import hu.zagor.gamebooks.character.item.WeaponSubType;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.FfTextResolvingTest;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
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
public class SingleFightRoundResolverBTest extends FfTextResolvingTest {

    private static final int[] SELF_ATTACK_STRENGTH_ROLL = new int[]{7, 1, 6};
    private static final String SELF_ATTACK_STRENGTH_RENDER = "Thrown values: 1, 6.";
    private static final int[] ENEMY_ATTACK_STRENGTH_ROLL = new int[]{7, 3, 4};
    private static final String ENEMY_ATTACK_STRENGTH_RENDER = "Thrown values: 3, 4.";
    private SingleFightRoundResolver underTest;
    private IMocksControl mockControl;
    private Logger logger;
    private RandomNumberGenerator generator;
    private List<FfEnemy> enemies;
    private ResolvationData resolvationData;
    private FfFightCommand command;
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
        notSelectedEnemy = new FfEnemy();
        notSelectedEnemy.setName("Orc");
        notSelectedEnemy.setId("33");
        notSelectedEnemy.setStamina(9);
    }

    @BeforeMethod
    public void setUpMethod() {
        command = new FfFightCommand();
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
        selectedEnemy.setDamageAbsorptionEdged(0);
    }

    public void testResolveRoundWhenSingleBlessedEnemyIsHitButWeaponIsIneffectiveShouldNotCauseDamageAndReturnRoundResultWithWin() {
        // GIVEN
        enemies.add(selectedEnemy);
        command.getResolvedEnemies().addAll(enemies);
        selectedEnemy.setKillableByNormal(false);
        selectedEnemy.setKillableByMagical(false);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("9");
        expect(generator.getRandomNumber(2)).andReturn(SELF_ATTACK_STRENGTH_ROLL);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        expect(generator.getRandomNumber(2)).andReturn(ENEMY_ATTACK_STRENGTH_ROLL);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(diceResultRenderer.render(6, SELF_ATTACK_STRENGTH_ROLL)).andReturn(SELF_ATTACK_STRENGTH_RENDER);
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{SELF_ATTACK_STRENGTH_RENDER, 17});
        logger.debug("Attack strength for self: {}", 17);
        expect(diceResultRenderer.render(6, ENEMY_ATTACK_STRENGTH_ROLL)).andReturn(ENEMY_ATTACK_STRENGTH_RENDER);
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", ENEMY_ATTACK_STRENGTH_RENDER, 15});
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

    public void testResolveRoundWhenSingleBlessedEnemyIsHitWithBlessedWeaponShouldCauseDamageAndReturnRoundResultWithWin() {
        // GIVEN
        enemies.add(selectedEnemy);
        command.getResolvedEnemies().addAll(enemies);
        selectedEnemy.setKillableByNormal(false);
        selectedEnemy.setKillableByMagical(false);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("9");
        expect(generator.getRandomNumber(2)).andReturn(SELF_ATTACK_STRENGTH_ROLL);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        expect(generator.getRandomNumber(2)).andReturn(ENEMY_ATTACK_STRENGTH_ROLL);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(diceResultRenderer.render(6, SELF_ATTACK_STRENGTH_ROLL)).andReturn(SELF_ATTACK_STRENGTH_RENDER);
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{SELF_ATTACK_STRENGTH_RENDER, 17});
        logger.debug("Attack strength for self: {}", 17);
        expect(diceResultRenderer.render(6, ENEMY_ATTACK_STRENGTH_ROLL)).andReturn(ENEMY_ATTACK_STRENGTH_RENDER);
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", ENEMY_ATTACK_STRENGTH_RENDER, 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        final FfItem weapon = new FfItem("7", "Sword", ItemType.weapon1);
        weapon.setBlessed(true);
        weapon.setStaminaDamage(3);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon).times(2);
        expect(attributeHandler.resolveValue(character, "baseStaminaDamage")).andReturn(0);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Goblin"});
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned.length, 1);
        Assert.assertEquals(returned[0], FightRoundResult.WIN);
    }

    public void testResolveRoundWhenSingleMagicalEnemyIsHitWithMagicalWeaponShouldCauseDamageAndReturnRoundResultWithWin() {
        // GIVEN
        enemies.add(selectedEnemy);
        command.getResolvedEnemies().addAll(enemies);
        selectedEnemy.setKillableByNormal(false);
        selectedEnemy.setKillableByBlessed(false);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("9");
        expect(generator.getRandomNumber(2)).andReturn(SELF_ATTACK_STRENGTH_ROLL);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        expect(generator.getRandomNumber(2)).andReturn(ENEMY_ATTACK_STRENGTH_ROLL);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(diceResultRenderer.render(6, SELF_ATTACK_STRENGTH_ROLL)).andReturn(SELF_ATTACK_STRENGTH_RENDER);
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{SELF_ATTACK_STRENGTH_RENDER, 17});
        logger.debug("Attack strength for self: {}", 17);
        expect(diceResultRenderer.render(6, ENEMY_ATTACK_STRENGTH_ROLL)).andReturn(ENEMY_ATTACK_STRENGTH_RENDER);
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", ENEMY_ATTACK_STRENGTH_RENDER, 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        final FfItem weapon = new FfItem("7", "Sword", ItemType.weapon1);
        weapon.setMagical(true);
        weapon.setStaminaDamage(3);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon).times(2);
        expect(attributeHandler.resolveValue(character, "baseStaminaDamage")).andReturn(0);
        expectText("page.ff.label.fight.single.successfulAttack", new Object[]{"Goblin"});
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(returned.length, 1);
        Assert.assertEquals(returned[0], FightRoundResult.WIN);
    }

    public void testResolveRoundWhenEdgeAbsorbingEnemyIsHitWithEdgedWeaponShouldRecordInformationAndReturnRoundResultWithWin() {
        // GIVEN
        enemies.add(selectedEnemy);
        command.getResolvedEnemies().addAll(enemies);
        selectedEnemy.setDamageAbsorptionEdged(1);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("9");
        expect(generator.getRandomNumber(2)).andReturn(SELF_ATTACK_STRENGTH_ROLL);
        expect(attributeHandler.resolveValue(character, "attackStrength")).andReturn(0);
        expect(generator.getRandomNumber(2)).andReturn(ENEMY_ATTACK_STRENGTH_ROLL);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(10);
        expect(diceResultRenderer.render(6, SELF_ATTACK_STRENGTH_ROLL)).andReturn(SELF_ATTACK_STRENGTH_RENDER);
        expectText("page.ff.label.fight.single.attackStrength.self", new Object[]{SELF_ATTACK_STRENGTH_RENDER, 17});
        logger.debug("Attack strength for self: {}", 17);
        expect(diceResultRenderer.render(6, ENEMY_ATTACK_STRENGTH_ROLL)).andReturn(ENEMY_ATTACK_STRENGTH_RENDER);
        expectText("page.ff.label.fight.single.attackStrength.enemy", new Object[]{"Goblin", ENEMY_ATTACK_STRENGTH_RENDER, 15});
        logger.debug("Attack strength for {}: {}", "Goblin", 15);
        final FfItem weapon = new FfItem("7", "Sword", ItemType.weapon1);
        weapon.setStaminaDamage(3);
        weapon.setSubType(WeaponSubType.edged);
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

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
