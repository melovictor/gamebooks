package hu.zagor.gamebooks.content.command.fight.subresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
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
public class Ff18FightCommandBasicSubResolverATest {

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
        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).withEnemies(enemies)
            .build();
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
    }

    @SuppressWarnings("unchecked")
    private void setUpClassUnchecked() {
        resolvedList = mockControl.createMock(List.class);
        enemies = mockControl.createMock(HashMap.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testDoResolveWhenNoFightCommandHasBeenIssuedShouldJustInitialize() {
        // GIVEN
        expect(interactionHandler.peekLastFightCommand(character)).andReturn(null);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightCommandHasBeenIssuedAndTargetingRobotAntennaeShouldChangeAndResetWeaponProperties() {
        // GIVEN
        final String enemyId = "26b";

        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        weapon.setStaminaDamage(0);
        weapon.setSkillDamage(1);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        weapon.setStaminaDamage(2);
        weapon.setSkillDamage(0);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1001");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightingAgainstCentralArcadianDidNotSufferDamageAndDidNotLoseCurrentRoundAndNotSufferingFromSkillMallusShouldDoNothing() {
        // GIVEN
        final String enemyId = "42";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4006")).andReturn(true);
        expect(command.getBattleStatistics("42")).andReturn(battleStat);
        expect(battleStat.getTotalLose()).andReturn(0);
        expect(itemHandler.hasItem(character, "4007")).andReturn(false);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1001");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightingAgainstCentralArcadianDidNotSufferDamageAndLostCurrentRoundButAvoidedDirtyTrickShouldDoNothing() {
        // GIVEN
        final String enemyId = "42";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4006")).andReturn(true);
        expect(command.getBattleStatistics("42")).andReturn(battleStat);
        expect(battleStat.getTotalLose()).andReturn(1);
        itemHandler.removeItem(character, "4006", 1);
        final int[] randomResult = new int[]{1, 1};
        expect(generator.getRandomNumber(1)).andReturn(randomResult);
        expect(command.getMessages()).andReturn(messageList);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, randomResult)).andReturn("Thrown result is 1.");
        expect(messageList.addKey("page.ff.label.random.after", "Thrown result is 1.", 1)).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1001");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightingAgainstCentralArcadianDidSufferDamageAlreadyAndNoSkillDeductionHappenedShouldDoNothing() {
        // GIVEN
        final String enemyId = "42";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4006")).andReturn(false);
        expect(itemHandler.hasItem(character, "4007")).andReturn(false);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1001");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightingAgainstCentralArcadianDidNotSufferDamageAndLostCurrentRoundAndDidNotAvoidDirtyTrickShouldSufferStaminaDeduction() {
        // GIVEN
        final String enemyId = "42";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4006")).andReturn(true);
        expect(command.getBattleStatistics("42")).andReturn(battleStat);
        expect(battleStat.getTotalLose()).andReturn(1);
        itemHandler.removeItem(character, "4006", 1);
        final int[] randomResult = new int[]{5, 5};
        expect(generator.getRandomNumber(1)).andReturn(randomResult);
        expect(command.getMessages()).andReturn(messageList);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, randomResult)).andReturn("Thrown result is 5.");
        expect(messageList.addKey("page.ff.label.random.after", "Thrown result is 5.", 5)).andReturn(true);
        expect(itemHandler.addItem(character, "4003", 1)).andReturn(1);
        expect(itemHandler.addItem(character, "4007", 2)).andReturn(2);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1001");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightingAgainstCentralArcadianAlreadySufferedDamageAndDirtyTrickAndHasOneMoreRoundsShouldLoseOneRoundMarker() {
        // GIVEN
        final String enemyId = "42";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4006")).andReturn(false);
        expect(itemHandler.hasItem(character, "4007")).andReturn(true);
        itemHandler.removeItem(character, "4007", 1);
        expect(itemHandler.hasItem(character, "4007")).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1001");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightingAgainstCentralArcadianAlreadySufferedDamageAndDirtyTrickAndHasOnlyCurrentRoundsShouldLoseOneRoundMarkerAndSkillDeduction() {
        // GIVEN
        final String enemyId = "42";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4006")).andReturn(false);
        expect(itemHandler.hasItem(character, "4007")).andReturn(true);
        itemHandler.removeItem(character, "4007", 1);
        expect(itemHandler.hasItem(character, "4007")).andReturn(false);
        itemHandler.removeItem(character, "4003", 1);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("1001");
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertSame(returned, resolvedList);
    }

    public void testDoResolveWhenFightingAgainstNormalEnemyWithFistButEnemyAlreadyDeadShouldDoNothing() {
        // GIVEN
        final String enemyId = "1";
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(superResolver.doResolve(command, resolvationData)).andReturn(resolvedList);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getId()).andReturn("defWpn");
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
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
