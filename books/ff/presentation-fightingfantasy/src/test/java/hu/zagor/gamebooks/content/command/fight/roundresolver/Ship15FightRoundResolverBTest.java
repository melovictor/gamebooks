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
public class Ship15FightRoundResolverBTest {

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
    private FfEnemy shipB;
    private FightCommandMessageList messages;
    private int[] failedAttackResults;
    private List<String> enemyList;

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
        command = mockControl.createMock(FfFightCommand.class);
        shipA = new FfEnemy();
        shipB = new FfEnemy();
        resolvedEnemies = new ArrayList<>();
        resolvedEnemies.add(shipA);
        resolvedEnemies.add(shipB);
        enemyList = new ArrayList<>();
        enemyList.add("10");
        enemyList.add("11");

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

        shipB.setId("11");
        shipB.setName("Second Ship");
        shipB.setStamina(3);
        shipB.setSkill(6);
    }

    public void testResolveRoundWhenAttackingTwoShipsAndMissileAttackIsRequestedWithTheFirstShipInTargetAndNooneHitsAnyoneShouldReportMissesAndSecondShipShouldBeDestroyed() {
        // GIVEN
        expectMissilePreparation(true);
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies);
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getInteractionHandler()).andReturn(interactionHandler);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("10");
        expect(command.getEnemies()).andReturn(enemyList);
        expect(command.getMessages()).andReturn(messages);
        expect(messages.addKey("page.ff.fight.ff15.ship.killBySmartMissile", "Second Ship")).andReturn(true);
        expectLaserFight();
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
        Assert.assertEquals(ship.getSmartMissile(), 1);
        Assert.assertEquals(shipB.getStamina(), 0);
    }

    public void testResolveRoundWhenAttackingTwoShipsAndMissileAttackIsNotRequestedWithTheFirstShipInTargetAndNooneHitsAnyoneShouldReportMisses() {
        // GIVEN
        expectMissilePreparation(false);
        expectLaserFight();
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
        expect(generator.getRandomNumber(2)).andReturn(failedAttackResults);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, failedAttackResults)).andReturn("d5 d6");
        expect(messages.addKey("page.ff.fight.ff15.enemyAttackStrength", "Second Ship", "d5 d6", 11)).andReturn(true);
        expect(messages.addKey("page.ff.fight.ff15.ship.laserMissedUs", "Second Ship")).andReturn(true);
        mockControl.replay();
        // WHEN
        final FightRoundResult[] returned = underTest.resolveRound(command, resolvationData, null);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(shipA.getStamina(), 3);
        Assert.assertEquals(ship.getShield(), 5);
        Assert.assertEquals(ship.getSmartMissile(), 2);
        Assert.assertEquals(shipB.getStamina(), 3);
    }

    private void expectLaserFight() {
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getInteractionHandler()).andReturn(interactionHandler);
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn("10");
        expect(character.getShipAttributes()).andReturn(ship);
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies);
        expect(command.getMessages()).andReturn(messages);
    }

    private void expectMissilePreparation(final boolean willFireMissile) {
        expect(resolvationData.getCharacter()).andReturn(character);
        expect(character.getShipAttributes()).andReturn(ship);
        expect(resolvationData.getCharacterHandler()).andReturn(characterHandler);
        expect(characterHandler.getInteractionHandler()).andReturn(interactionHandler);
        expect(interactionHandler.getLastFightCommand(character, "missile")).andReturn(String.valueOf(willFireMissile));
        expect(command.getResolvedEnemies()).andReturn(resolvedEnemies);
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
