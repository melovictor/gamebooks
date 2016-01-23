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
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.List;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
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
    @UnderTest private Ff18FightCommandBasicSubResolver underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private FightCommand command;
    private ResolvationData resolvationData;
    private ParagraphData rootData;
    @Mock private FfCharacter character;
    @Mock private Map<String, Enemy> enemies;
    private FfBookInformations info;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfUserInteractionHandler interactionHandler;
    @Inject private FightCommandBasicSubResolver superResolver;
    @Mock private List<ParagraphData> resolvedList;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfItem weapon;
    @Mock private BattleStatistics battleStat;
    @Inject private RandomNumberGenerator generator;
    @Inject private DiceResultRenderer renderer;
    @Mock private FightCommandMessageList messageList;
    @Mock private FfEnemy enemy;
    @Mock private List<Item> itemList;

    @BeforeClass
    public void setUpClass() throws SecurityException {
        info = new FfBookInformations(1L);
        characterHandler.setInteractionHandler(interactionHandler);
        info.setCharacterHandler(characterHandler);
        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).withEnemies(enemies)
            .build();
        characterHandler.setItemHandler(itemHandler);
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
        expect(itemHandler.removeItem(character, "4006", 1)).andReturn(itemList);
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
        expect(itemHandler.removeItem(character, "4006", 1)).andReturn(itemList);
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
        expect(itemHandler.removeItem(character, "4007", 1)).andReturn(itemList);
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
        expect(itemHandler.removeItem(character, "4007", 1)).andReturn(itemList);
        expect(itemHandler.hasItem(character, "4007")).andReturn(false);
        expect(itemHandler.removeItem(character, "4003", 1)).andReturn(itemList);
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
