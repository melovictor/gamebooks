package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.ff.trok.character.Ff15Character;
import hu.zagor.gamebooks.ff.ff.trok.character.domain.Ff15ShipAttributes;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.ArrayList;
import java.util.List;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ship15FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class Ship15FightRoundResolverATest {

    private IMocksControl mockControl;
    private Ship15FightRoundResolver underTest;
    private RandomNumberGenerator generator;
    private DiceResultRenderer renderer;
    private ResolvationData resolvationData;
    private FfFightCommand command;
    private Ff15Character character;
    private Ff15ShipAttributes ship;
    private FfCharacterHandler characterHandler;
    private FfUserInteractionHandler interactionHandler;
    private List<FfEnemy> resolvedEnemies;
    private FfEnemy shipA;
    private FightCommandMessageList messages;
    private int[] failedAttackResults;
    private int[] succeededAttackResults;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ship15FightRoundResolver();
        generator = mockControl.createMock(RandomNumberGenerator.class);
        renderer = mockControl.createMock(DiceResultRenderer.class);
        resolvationData = mockControl.createMock(ResolvationData.class);
        character = mockControl.createMock(Ff15Character.class);
        characterHandler = mockControl.createMock(FfCharacterHandler.class);
        interactionHandler = mockControl.createMock(FfUserInteractionHandler.class);
        messages = mockControl.createMock(FightCommandMessageList.class);
        failedAttackResults = new int[]{11, 5, 6};
        succeededAttackResults = new int[]{3, 1, 2};
        command = mockControl.createMock(FfFightCommand.class);
        shipA = new FfEnemy();
        resolvedEnemies = new ArrayList<>();
        resolvedEnemies.add(shipA);

        Whitebox.setInternalState(underTest, "generator", generator);
        Whitebox.setInternalState(underTest, "diceResultRenderer", renderer);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        ship = new Ff15ShipAttributes();
        ship.setShield(5);
        ship.setSmartMissile(2);
        ship.setWeaponStrength(9);
        shipA.setId("10");
        shipA.setName("First Ship");
        shipA.setStamina(3);
        shipA.setSkill(6);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testResolveFleeShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.resolveFlee(command, resolvationData);
        // THEN throws exception
    }

    public void testResolveRoundWhenAttackingASingleShipAndNoMissileAttackIsRequestedAndNooneHitsAnyoneShouldReportMisses() {
        // GIVEN
        expectSingleEncounterWithoutMissile();
        expect(generator.getRandomNumber(2)).andReturn(failedAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, failedAttackResults)).andReturn("d5 d6");
        expect(messages.addKey("page.ff.fight.ff15.ourAttackStrength", "d5 d6", 11)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserMissedEnemy", "First Ship")).andReturn(true);
        expectEnemyCounterattack();
        expect(generator.getRandomNumber(2)).andReturn(failedAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, failedAttackResults)).andReturn("d5 d6");
        expect(messages.addKey("page.ff.fight.ff15.enemyAttackStrength", "First Ship", "d5 d6", 11)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserMissedUs", "First Ship")).andReturn(true);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, null);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(shipA.getStamina(), 3);
        Assert.assertEquals(ship.getShield(), 5);
    }

    public void testResolveRoundWhenAttackingASingleShipAndNoMissileAttackIsRequestedAndWeHitEnemyShouldReportHitOnEnemy() {
        // GIVEN
        expectSingleEncounterWithoutMissile();
        expect(generator.getRandomNumber(2)).andReturn(succeededAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, succeededAttackResults)).andReturn("d1 d2");
        expect(messages.addKey("page.ff.fight.ff15.ourAttackStrength", "d1 d2", 3)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserHitEnemy", "First Ship")).andReturn(true);
        expectEnemyCounterattack();
        expect(generator.getRandomNumber(2)).andReturn(failedAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, failedAttackResults)).andReturn("d5 d6");
        expect(messages.addKey("page.ff.fight.ff15.enemyAttackStrength", "First Ship", "d5 d6", 11)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserMissedUs", "First Ship")).andReturn(true);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, null);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(shipA.getStamina(), 2);
        Assert.assertEquals(ship.getShield(), 5);
    }

    public void testResolveRoundWhenAttackingASingleShipAndNoMissileAttackIsRequestedAndWeHitAndDestroyEnemyShouldReportHitOnEnemyAndNoReturnFireShouldBeAllowed() {
        // GIVEN
        shipA.setStamina(1);
        expectSingleEncounterWithoutMissile();
        expect(generator.getRandomNumber(2)).andReturn(succeededAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, succeededAttackResults)).andReturn("d1 d2");
        expect(messages.addKey("page.ff.fight.ff15.ourAttackStrength", "d1 d2", 3)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserHitEnemy", "First Ship")).andReturn(true);
        expectEnemyCounterattack();
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, null);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(shipA.getStamina(), 0);
        Assert.assertEquals(ship.getShield(), 5);
    }

    public void testResolveRoundWhenAttackingASingleShipAndNoMissileAttackIsRequestedAndEnemyHitsUsShouldReportHitOnUs() {
        // GIVEN
        expectSingleEncounterWithoutMissile();
        expect(generator.getRandomNumber(2)).andReturn(failedAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, failedAttackResults)).andReturn("d5 d6");
        expect(messages.addKey("page.ff.fight.ff15.ourAttackStrength", "d5 d6", 11)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserMissedEnemy", "First Ship")).andReturn(true);
        expectEnemyCounterattack();
        expect(generator.getRandomNumber(2)).andReturn(succeededAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, succeededAttackResults)).andReturn("d1 d2");
        expect(messages.addKey("page.ff.fight.ff15.enemyAttackStrength", "First Ship", "d1 d2", 3)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserHitUs", "First Ship")).andReturn(true);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, null);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(shipA.getStamina(), 3);
        Assert.assertEquals(ship.getShield(), 4);
    }

    public void testResolveRoundWhenAttackingASingleShipAndNoMissileAttackIsRequestedAndEnemyHitsUsAndWeHitEnemyShouldReportHitOnBoth() {
        // GIVEN
        expectSingleEncounterWithoutMissile();
        expect(generator.getRandomNumber(2)).andReturn(succeededAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, succeededAttackResults)).andReturn("d1 d2");
        expect(messages.addKey("page.ff.fight.ff15.ourAttackStrength", "d1 d2", 3)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserHitEnemy", "First Ship")).andReturn(true);
        expectEnemyCounterattack();
        expect(generator.getRandomNumber(2)).andReturn(succeededAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, succeededAttackResults)).andReturn("d1 d2");
        expect(messages.addKey("page.ff.fight.ff15.enemyAttackStrength", "First Ship", "d1 d2", 3)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserHitUs", "First Ship")).andReturn(true);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, null);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(shipA.getStamina(), 2);
        Assert.assertEquals(ship.getShield(), 4);
    }

    public void testResolveRoundWhenAttackingASingleShipAndMissileAttackIsRequestedButThereAreNoMissilesAndEnemyHitsUsAndWeHitEnemyShouldReportHitOnBothAndIgnoreMissileRequest() {
        // GIVEN
        ship.setSmartMissile(0);
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(character.getShipAttributes()).andReturn(ship);
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getInteractionHandler()).andReturn(interactionHandler);
        expect(interactionHandler.getLastFightCommand(character, "missile")).andReturn("true");
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies);
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getInteractionHandler()).andReturn(interactionHandler);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("10");
        expect(character.getShipAttributes()).andReturn(ship);
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies);
        expect(command.getMessages()).andReturn(messages);
        expect(generator.getRandomNumber(2)).andReturn(succeededAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, succeededAttackResults)).andReturn("d1 d2");
        expect(messages.addKey("page.ff.fight.ff15.ourAttackStrength", "d1 d2", 3)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserHitEnemy", "First Ship")).andReturn(true);
        expectEnemyCounterattack();
        expect(generator.getRandomNumber(2)).andReturn(succeededAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, succeededAttackResults)).andReturn("d1 d2");
        expect(messages.addKey("page.ff.fight.ff15.enemyAttackStrength", "First Ship", "d1 d2", 3)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserHitUs", "First Ship")).andReturn(true);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, null);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(shipA.getStamina(), 2);
        Assert.assertEquals(ship.getShield(), 4);
    }

    public void testResolveRoundWhenAttackingASingleShipAndMissileAttackIsRequestedAndThereAreMissilesShouldEnemyDestroyal() {
        // GIVEN
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(character.getShipAttributes()).andReturn(ship);
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getInteractionHandler()).andReturn(interactionHandler);
        expect(interactionHandler.getLastFightCommand(character, "missile")).andReturn("true");
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies).times(2);
        expect(command.getMessages()).andReturn(messages);
        expect(messages.addKey("page.ff.fight.ff15.ship.killBySmartMissile", "First Ship")).andReturn(true);
        expectEnemyCounterattack();
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, null);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(shipA.getStamina(), 0);
        Assert.assertEquals(ship.getSmartMissile(), 1);
        Assert.assertEquals(ship.getShield(), 5);
    }

    private void expectSingleEncounterWithoutMissile() {
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(character.getShipAttributes()).andReturn(ship);
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getInteractionHandler()).andReturn(interactionHandler);
        expect(interactionHandler.getLastFightCommand(character, "missile")).andReturn("false");
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies);
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getInteractionHandler()).andReturn(interactionHandler);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("10");
        expect(character.getShipAttributes()).andReturn(ship);
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies);
        expect(command.getMessages()).andReturn(messages);
    }

    private void expectEnemyCounterattack() {
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(character.getShipAttributes()).andReturn(ship);
        expect(command.getMessages()).andReturn(messages);
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
