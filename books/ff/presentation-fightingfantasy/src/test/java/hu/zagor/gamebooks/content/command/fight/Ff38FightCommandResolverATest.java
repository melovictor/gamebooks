package hu.zagor.gamebooks.content.command.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.ff.votv.character.Ff38Character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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
 * Unit test for class {@link Ff38FightCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff38FightCommandResolverATest {

    private IMocksControl mockControl;
    private Ff38FightCommandResolver underTest;
    private RandomNumberGenerator generator;
    private FightCommand command;
    private ResolvationData resolvationData;
    private ParagraphData rootData;
    private Ff38Character character;
    private Map<String, Enemy> enemies;
    private BookInformations info;
    private FfCharacterHandler characterHandler;
    private FfUserInteractionHandler interactionHandler;
    private FfCharacterItemHandler itemHandler;
    private FightCommandMessageList messages;
    private FfEnemy skull;
    private Iterator<String> iterator;
    private FfEnemy wolf;
    private FfEnemy heydrich;
    private FfEnemy thassalos;
    private FfParagraphData win;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ff38FightCommandResolver();
        generator = mockControl.createMock(RandomNumberGenerator.class);
        Whitebox.setInternalState(underTest, "generator", generator);

        character = mockControl.createMock(Ff38Character.class);
        info = new FfBookInformations(3L);
        characterHandler = new FfCharacterHandler();
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
        interactionHandler = mockControl.createMock(FfUserInteractionHandler.class);
        characterHandler.setInteractionHandler(interactionHandler);
        info.setCharacterHandler(characterHandler);
        enemies = new HashMap<>();
        rootData = new ParagraphData();
        rootData.setText("");
        resolvationData = new ResolvationData(rootData, character, enemies, info);
        messages = mockControl.createMock(FightCommandMessageList.class);
        underTest.setBoneEnemies(Arrays.asList("39"));
        underTest.setUndeadEnemies(Arrays.asList("38", "40"));
        skull = new FfEnemy();
        wolf = new FfEnemy();
        heydrich = new FfEnemy();
        thassalos = new FfEnemy();
        enemies.put("38", skull);
        enemies.put("7", wolf);
        enemies.put("40", heydrich);
        enemies.put("39", thassalos);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        command = new FightCommand();
        Whitebox.setInternalState(command, "messages", messages);
        skull.setStamina(9);
        wolf.setStamina(5);
        heydrich.setStamina(21);
        thassalos.setStamina(15);
        iterator = new ArrayList<String>().iterator();
        command.getEnemies().clear();
        final FightOutcome winOutcome = new FightOutcome();
        winOutcome.setParagraphData(win);
        command.setWin(Arrays.asList(winOutcome));
    }

    public void testResolveWhenCastingJandorsArrowAndEnemyIsNormalUndeadShouldCauseDamage() {
        // GIVEN
        final String enemyId = "38";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoision();
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("attack");
        messages.switchToPreFightMessages();
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4014")).andReturn(true);
        expect(messages.addKey("page.ff.fight.spell.jandor.success")).andReturn(true);
        messages.switchToRoundMessages();
        itemHandler.removeItem(character, "4014", 1);
        noWhiteWine();
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertEquals(skull.getStamina(), 3);
    }

    public void testResolveWhenCastingJandorsArrowAndEnemyIsNotUndeadShouldNotCauseDamage() {
        // GIVEN
        final String enemyId = "7";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoision();
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("attack");
        messages.switchToPreFightMessages();
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4014")).andReturn(true);
        expect(messages.addKey("page.ff.fight.spell.jandor.failure")).andReturn(true);
        messages.switchToRoundMessages();
        itemHandler.removeItem(character, "4014", 1);
        noWhiteWine();
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertEquals(wolf.getStamina(), 5);
    }

    public void testResolveWhenCastingJandorsArrowOnHeydrichAndHeIsLuckyShouldCauseHalfDamage() {
        // GIVEN
        final String enemyId = "40";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoision();
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("attack");
        messages.switchToPreFightMessages();
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4014")).andReturn(true);
        expect(generator.getRandomNumber(1)).andReturn(new int[]{5, 5});
        expect(messages.addKey("page.ff.fight.spell.jandor.40.true")).andReturn(true);
        expect(messages.addKey("page.ff.fight.spell.jandor.success2")).andReturn(true);
        messages.switchToRoundMessages();
        itemHandler.removeItem(character, "4014", 1);
        noWhiteWine();
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertEquals(heydrich.getStamina(), 18);
    }

    public void testResolveWhenCastingJandorsArrowOnHeydrichAndHeIsUnluckyShouldCauseFullDamage() {
        // GIVEN
        final String enemyId = "40";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoision();
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("attack");
        messages.switchToPreFightMessages();
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4014")).andReturn(true);
        expect(generator.getRandomNumber(1)).andReturn(new int[]{2, 2});
        expect(messages.addKey("page.ff.fight.spell.jandor.40.false")).andReturn(true);
        expect(messages.addKey("page.ff.fight.spell.jandor.success")).andReturn(true);
        messages.switchToRoundMessages();
        itemHandler.removeItem(character, "4014", 1);
        noWhiteWine();
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertEquals(heydrich.getStamina(), 15);
    }

    public void testResolveWhenCastingBashOnBoneEnemyShouldKillEnemy() {
        // GIVEN
        final String enemyId = "39";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoision();
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("attack");
        messages.switchToPreFightMessages();
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4014")).andReturn(false);
        expect(itemHandler.hasItem(character, "4015")).andReturn(true);
        expect(messages.addKey("page.ff.fight.spell.bash.success")).andReturn(true);
        messages.switchToRoundMessages();
        itemHandler.removeItem(character, "4015", 1);
        noWhiteWine();
        expect(itemHandler.hasItem(character, "4004")).andReturn(false);
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertEquals(thassalos.getStamina(), 0);
        Assert.assertTrue(returned.isFinished());
        Assert.assertSame(returned.getResolveList().get(0), win);
    }

    public void testResolveWhenCastingBashOnNormalEnemyShouldDoNothing() {
        // GIVEN
        final String enemyId = "7";
        messages.clear();
        command.getEnemies().add(enemyId);
        noSlowPoision();
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("attack");
        messages.switchToPreFightMessages();
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4014")).andReturn(false);
        expect(itemHandler.hasItem(character, "4015")).andReturn(true);
        expect(messages.addKey("page.ff.fight.spell.bash.failure")).andReturn(true);
        messages.switchToRoundMessages();
        itemHandler.removeItem(character, "4015", 1);
        noWhiteWine();
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertEquals(wolf.getStamina(), 5);
        Assert.assertFalse(returned.isFinished());
    }

    private void noSlowPoision() {
        expect(interactionHandler.peekLastFightCommand(character)).andReturn(null);
        expect(itemHandler.hasItem(character, "4101")).andReturn(false);
    }

    private void noRegeneratingRing() {
        expect(itemHandler.hasEquippedItem(character, "3020")).andReturn(false);
    }

    private void noAutoDamage() {
        expect(itemHandler.hasItem(character, "4005")).andReturn(false);
    }

    private void noWhiteWine() {
        expect(itemHandler.hasItem(character, "4010")).andReturn(false);
        expect(itemHandler.hasItem(character, "4011")).andReturn(false);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
