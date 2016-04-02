package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.partialMockBuilder;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.BattleStatistics;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.context.MessageSource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link SingleSor3FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class SingleSor3FightRoundResolverTest {
    @MockControl private IMocksControl mockControl;
    private SingleSor3FightRoundResolver underTest;
    @Mock private FightCommand command;
    private FightRoundResult[] winResult;
    @Mock private FfEnemy enemy;
    @Mock private FightCommandMessageList message;
    @Mock private FfItem weapon;
    @Mock private FfAttributeHandler attributeHandler;
    @Mock private SorCharacter character;
    @Instance private FfCharacterHandler characterHandler;
    @Instance private ResolvationData resolvationData;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FightBeforeRoundResult beforeRoundResult;
    private final List<String> genericEnemyList = Arrays.asList("1");
    @Mock private FfUserInteractionHandler interactionHandler;
    @Mock private Map<String, Enemy> enemies;
    @Instance(inject = true) private Set<String> snattaCat;
    @Mock private List<String> paragraphs;
    private final List<String> badduEnemyList = Arrays.asList("8");
    private final List<String> centaurEnemyListA = Arrays.asList("5", "6", "7");
    private final List<String> centaurEnemyListB = Arrays.asList("9", "6", "7");
    @Mock private FfEnemy enemyB;
    @Mock private BattleStatistics stat;
    @Inject private MessageSource source;
    @Inject private LocaleProvider provider;
    private final FightRoundResult[] loseResult = new FightRoundResult[]{FightRoundResult.LOSE};
    private final List<String> airSerpentEnemyList = Arrays.asList("44");
    @Mock private FightFleeData fleeData;

    @BeforeClass
    public void setUpClass() {
        resolvationData.setCharacter(character);
        final BookInformations info = new BookInformations(3);
        info.setCharacterHandler(characterHandler);
        resolvationData.setInfo(info);
        resolvationData.setEnemies(enemies);
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        snattaCat.add("35");
        snattaCat.add("36");
        snattaCat.add("37");
        snattaCat.add("38");
        winResult = new FightRoundResult[]{FightRoundResult.WIN};
    }

    @UnderTest
    public SingleSor3FightRoundResolver underTest() {
        return partialMockBuilder(SingleSor3FightRoundResolver.class).withConstructor().addMockedMethod("superResolveRound").createMock(mockControl);
    }

    public void testResolveRoundWhenGenericEnemyShouldCallSuper() {
        // GIVEN
        final String enemyId = "1";
        final List<String> enemyList = genericEnemyList;
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList).times(2);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(command.getEnemies()).andReturn(enemyList);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenNonAngeredBadduBugEnemyShouldCallSuper() {
        // GIVEN
        final List<String> enemyList = badduEnemyList;
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList).times(2);
        expect(character.getParagraphs()).andReturn(paragraphs);
        expect(paragraphs.contains("488")).andReturn(false);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenAngeredBadduBugEnemyShouldCallSuperAndIncreaseBugAttackStrength() {
        // GIVEN
        final List<String> enemyList = badduEnemyList;
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList).times(2);
        expect(character.getParagraphs()).andReturn(paragraphs);
        expect(paragraphs.contains("488")).andReturn(true);
        expect(enemies.get("8")).andReturn(enemy);
        enemy.setAttackStrengthBonus(4);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenLastSnattacatDiesShouldDoNothing() {
        // GIVEN
        final String enemyId = "37";
        final List<String> enemyList = Arrays.asList("37");
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList).times(2);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
        expect(command.getResolvedEnemies()).andReturn(Arrays.asList(enemy));
        expect(command.getEnemies()).andReturn(enemyList);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenNotLastSnattacatDiesShouldEnsureAddedAttackStrengthMallus() {
        // GIVEN
        final String enemyId = "36";
        final List<String> enemyList = Arrays.asList("36", "37", "38", "39");
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList).times(2);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
        expect(command.getResolvedEnemies()).andReturn(Arrays.asList(enemy, enemy, enemy));
        expect(itemHandler.addItem(character, "4008", 2)).andReturn(2);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenSnattacatIsNotDeadShouldDoNothing() {
        // GIVEN
        final String enemyId = "37";
        final List<String> enemyList = Arrays.asList("37");
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList).times(2);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(5);
        expect(command.getEnemies()).andReturn(enemyList);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenCentaurEnemiesAllAliveShouldDoNothing() {
        // GIVEN
        final String enemyId = "5";
        final List<String> enemyList = centaurEnemyListA;
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemies.get("5")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(3);
        expect(enemies.get("5")).andReturn(enemy);
        expect(enemies.get("6")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(3);
        expect(enemies.get("6")).andReturn(enemy);
        expect(enemies.get("7")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(3);
        expect(enemies.get("7")).andReturn(enemy);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(command.getEnemies()).andReturn(enemyList);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenCentaurEnemiesTwoAliveShouldDoNothing() {
        // GIVEN
        final String enemyId = "5";
        final List<String> enemyList = centaurEnemyListA;
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemies.get("5")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(3);
        expect(enemies.get("5")).andReturn(enemy);
        expect(enemies.get("6")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(3);
        expect(enemies.get("6")).andReturn(enemy);
        expect(enemies.get("7")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(command.getEnemies()).andReturn(enemyList);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenCentaurEnemiesOneAliveAndOnlyHitNowShouldDoNothing() {
        // GIVEN
        final String enemyId = "7";
        final List<String> enemyList = centaurEnemyListB;
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemies.get("9")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
        expect(enemies.get("6")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
        expect(enemies.get(enemyId)).andReturn(enemyB);
        expect(enemyB.getStamina()).andReturn(3);
        expect(enemies.get(enemyId)).andReturn(enemyB);
        expect(enemyB.getId()).andReturn(enemyId);
        expect(command.getBattleStatistics(enemyId)).andReturn(stat);
        expect(stat.getTotalWin()).andReturn(0);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(command.getEnemies()).andReturn(enemyList);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenCentaurEnemiesOneAliveButSomehowNoAliveEnemyObjectIsPresentShouldDoNothing() {
        // GIVEN
        final String enemyId = "7";
        final List<String> enemyList = centaurEnemyListB;
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemies.get("9")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
        expect(enemies.get("6")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
        expect(enemies.get(enemyId)).andReturn(enemyB);
        expect(enemyB.getStamina()).andReturn(3);
        expect(enemies.get(enemyId)).andReturn(null);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(command.getEnemies()).andReturn(enemyList);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenCentaurEnemiesOneAliveAndHitTwiceInTotalShouldAllowSurrender() {
        // GIVEN
        final String enemyId = "7";
        final List<String> enemyList = centaurEnemyListB;
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemies.get("9")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
        expect(enemies.get("6")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
        expect(enemies.get(enemyId)).andReturn(enemyB);
        expect(enemyB.getStamina()).andReturn(3);
        expect(enemies.get(enemyId)).andReturn(enemyB);
        expect(enemyB.getId()).andReturn(enemyId);
        expect(command.getBattleStatistics(enemyId)).andReturn(stat);
        expect(stat.getTotalWin()).andReturn(1);
        command.setFleeAllowed(true);
        final Capture<FightFleeData> fleeData = newCapture();
        expect(provider.getLocale()).andReturn(Locale.ENGLISH);
        expect(source.getMessage("page.sor3.fight.centaur.flee", null, Locale.ENGLISH)).andReturn("Surrender");
        command.setFleeData(capture(fleeData));
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
        final FightFleeData fightFleeData = fleeData.getValue();
        Assert.assertEquals(fightFleeData.getAfterRound(), 1);
        Assert.assertFalse(fightFleeData.isSufferDamage());
        Assert.assertEquals(fightFleeData.getText(), "Surrender");
    }

    public void testResolveRoundWhenCentaurEnemiesOneAliveAndHitTwiceInTotalEvenWhenLastRoundWasLostShouldAllowSurrender() {
        // GIVEN
        final String enemyId = "7";
        final List<String> enemyList = centaurEnemyListB;
        expect(command.getEnemies()).andReturn(enemyList);
        resolveRound(loseResult);
        expect(command.getEnemies()).andReturn(enemyList);
        expect(enemies.get("9")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
        expect(enemies.get("6")).andReturn(enemy);
        expect(enemy.getStamina()).andReturn(0);
        expect(enemies.get(enemyId)).andReturn(enemyB);
        expect(enemyB.getStamina()).andReturn(3);
        expect(enemies.get(enemyId)).andReturn(enemyB);
        expect(enemyB.getId()).andReturn(enemyId);
        expect(command.getBattleStatistics(enemyId)).andReturn(stat);
        expect(stat.getTotalWin()).andReturn(3);
        command.setFleeAllowed(true);
        final Capture<FightFleeData> fleeData = newCapture();
        expect(provider.getLocale()).andReturn(Locale.ENGLISH);
        expect(source.getMessage("page.sor3.fight.centaur.flee", null, Locale.ENGLISH)).andReturn("Surrender");
        command.setFleeData(capture(fleeData));
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, loseResult);
        final FightFleeData fightFleeData = fleeData.getValue();
        Assert.assertEquals(fightFleeData.getAfterRound(), 1);
        Assert.assertFalse(fightFleeData.isSufferDamage());
        Assert.assertEquals(fightFleeData.getText(), "Surrender");
    }

    public void testResolveRoundWhenAttackingAirSerpentTheFirstTimeShouldResetDamageAndDamageAbsorptionThenStoreLastRoundResult() {
        // GIVEN
        final String enemyId = "44";
        final List<String> enemyList = airSerpentEnemyList;
        expect(command.getEnemies()).andReturn(enemyList);
        expect(interactionHandler.peekInteractionState(character, "lastRound")).andReturn(null);
        expect(enemies.get(enemyId)).andReturn(enemy);
        enemy.setStaminaDamage(1);
        enemy.setDamageAbsorption(10);

        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList).times(2);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(command.getEnemies()).andReturn(enemyList);

        expect(interactionHandler.getInteractionState(character, "lastRound")).andReturn(null);
        interactionHandler.setInteractionState(character, "lastRound", "WIN");

        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenAttackingAirSerpentTwiceWinningShouldResetDamageAndRemoveDamageAbsorptionThenClearLastRoundResult() {
        // GIVEN
        final String enemyId = "44";
        final List<String> enemyList = airSerpentEnemyList;
        expect(command.getEnemies()).andReturn(enemyList);
        expect(interactionHandler.peekInteractionState(character, "lastRound")).andReturn("WIN");
        expect(enemies.get(enemyId)).andReturn(enemy);
        enemy.setStaminaDamage(1);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(2);
        enemy.setDamageAbsorption(-1);

        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList).times(2);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(command.getEnemies()).andReturn(enemyList);

        expect(interactionHandler.getInteractionState(character, "lastRound")).andReturn("WIN");

        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenAttackingAirSerpentWonLastRoundButLosesNowShouldResetDamageAndRemoveDamageAbsorptionThenClearLastRoundResult() {
        // GIVEN
        final String enemyId = "44";
        final List<String> enemyList = airSerpentEnemyList;
        expect(command.getEnemies()).andReturn(enemyList);
        expect(interactionHandler.peekInteractionState(character, "lastRound")).andReturn("WIN");
        expect(enemies.get(enemyId)).andReturn(enemy);
        enemy.setStaminaDamage(1);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(2);
        enemy.setDamageAbsorption(-1);

        resolveRound(loseResult);
        expect(command.getEnemies()).andReturn(enemyList).times(2);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(command.getEnemies()).andReturn(enemyList);

        expect(interactionHandler.getInteractionState(character, "lastRound")).andReturn("WIN");
        interactionHandler.setInteractionState(character, "lastRound", "LOSE");

        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, loseResult);
    }

    public void testResolveRoundWhenAttackingAirSerpentWithLastTimeLosingAndNowWinningShouldIncreaseDamageAndResetDamageAbsorptionThenSetLastRoundResult() {
        // GIVEN
        final String enemyId = "44";
        final List<String> enemyList = airSerpentEnemyList;
        expect(command.getEnemies()).andReturn(enemyList);
        expect(interactionHandler.peekInteractionState(character, "lastRound")).andReturn("LOSE");
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getStaminaDamage()).andReturn(4);
        enemy.setStaminaDamage(8);
        enemy.setDamageAbsorption(10);

        resolveRound(winResult);
        expect(command.getEnemies()).andReturn(enemyList).times(2);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(command.getEnemies()).andReturn(enemyList);

        expect(interactionHandler.getInteractionState(character, "lastRound")).andReturn("LOSE");
        interactionHandler.setInteractionState(character, "lastRound", "WIN");

        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, winResult);
    }

    public void testResolveRoundWhenAttackingAirSerpentWithTwiceLosingShouldIncreaseDamageAndResetDamageAbsorptionThenSetLastRoundResult() {
        // GIVEN
        final String enemyId = "44";
        final List<String> enemyList = airSerpentEnemyList;
        expect(command.getEnemies()).andReturn(enemyList);
        expect(interactionHandler.peekInteractionState(character, "lastRound")).andReturn("LOSE");
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(enemy.getStaminaDamage()).andReturn(4);
        enemy.setStaminaDamage(8);
        enemy.setDamageAbsorption(10);

        resolveRound(loseResult);
        expect(command.getEnemies()).andReturn(enemyList).times(2);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn(enemyId);
        expect(enemies.get(enemyId)).andReturn(enemy);
        expect(command.getEnemies()).andReturn(enemyList);

        expect(interactionHandler.getInteractionState(character, "lastRound")).andReturn("LOSE");
        interactionHandler.setInteractionState(character, "lastRound", "LOSE");

        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertSame(returned, loseResult);
    }

    public void testResolveFleeShouldCallSuper() {
        // GIVEN
        expect(command.getResolvedEnemies()).andReturn(Arrays.asList(enemy, enemyB));
        expect(command.getMessages()).andReturn(message);
        expect(command.getFleeData()).andReturn(fleeData);
        expect(fleeData.getText()).andReturn("Flee");
        expect(message.add("Flee.")).andReturn(true);
        expect(fleeData.isSufferDamage()).andReturn(false);

        mockControl.replay();
        // WHEN
        underTest.resolveFlee(command, resolvationData);
        // THEN
    }

    private void resolveRound(final FightRoundResult[] roundResult) {
        expect(underTest.superResolveRound(command, resolvationData, beforeRoundResult)).andReturn(roundResult);
    }

}
