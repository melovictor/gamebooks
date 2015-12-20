package hu.zagor.gamebooks.content.command.fight;

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
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.subresolver.FightCommandSubResolver;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.ff.votv.character.Ff38Character;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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
 * Unit test for class {@link Ff38FightCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff38FightCommandResolverBTest {

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
    private FfItem weapon;
    private Map<String, FightCommandSubResolver> subResolvers;
    private FightCommandSubResolver resolver;
    private List<ParagraphData> paragraphList;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ff38FightCommandResolver();
        generator = mockControl.createMock(RandomNumberGenerator.class);
        Whitebox.setInternalState(underTest, "generator", generator);
        subResolvers = new HashMap<>();
        resolver = mockControl.createMock(FightCommandSubResolver.class);
        subResolvers.put("single", resolver);
        underTest.setSubResolvers(subResolvers);

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
        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).withEnemies(enemies)
            .build();
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
        weapon = mockControl.createMock(FfItem.class);
        paragraphList = new ArrayList<>();
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
        command.setResolver("single");
        command.setOngoing(true);
    }

    public void testResolveWhenCastingBashOnBoneEnemyAndWeHaveSithCurseStillActiveForLongShouldKillEnemyAndLoseOneLayerOfTheCurse() {
        // GIVEN
        final String enemyId = "39";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoison();
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
        expect(itemHandler.hasItem(character, "4004")).andReturn(true);
        itemHandler.removeItem(character, "4004", 1);
        expect(itemHandler.hasItem(character, "4004")).andReturn(true);
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

    public void testResolveWhenCastingBashOnBoneEnemyAndWeHaveSithCurseStillActiveForJustThisLastFightShouldKillEnemyAndLoseOneTheCurse() {
        // GIVEN
        final String enemyId = "39";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoison();
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
        expect(itemHandler.hasItem(character, "4004")).andReturn(true);
        itemHandler.removeItem(character, "4004", 1);
        expect(itemHandler.hasItem(character, "4004")).andReturn(false);
        itemHandler.removeItem(character, "4105", 1);
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

    public void testResolveWhenCastingStrongHitShouldIncreaseAndDecreaseWeaponDamage() {
        // GIVEN
        final String enemyId = "7";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoison();
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("attack");
        messages.switchToPreFightMessages();
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4013")).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(2);
        weapon.setStaminaDamage(6);
        expect(messages.addKey("page.ff.fight.spell.strongHit")).andReturn(true);
        messages.switchToRoundMessages();
        normalFight();
        itemHandler.removeItem(character, "4013", 1);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(6);
        weapon.setStaminaDamage(2);
        noWhiteWine();
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
    }

    private void noSlowPoison() {
        expect(interactionHandler.peekLastFightCommand(character)).andReturn(null);
        expect(itemHandler.hasItem(character, "4101")).andReturn(false);
    }

    public void testResolveWhenCastingStrongHitAndEnemyDiesShouldIncreaseAndDecreaseWeaponDamage() {
        // GIVEN
        final String enemyId = "7";
        command.getEnemies().add(enemyId);
        command.setOngoing(false);
        messages.clear();
        noSlowPoison();
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("attack");
        messages.switchToPreFightMessages();
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4013")).andReturn(true);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(2);
        weapon.setStaminaDamage(6);
        expect(messages.addKey("page.ff.fight.spell.strongHit")).andReturn(true);
        messages.switchToRoundMessages();
        normalFight();
        itemHandler.removeItem(character, "4013", 1);
        expect(itemHandler.getEquippedWeapon(character)).andReturn(weapon);
        expect(weapon.getStaminaDamage()).andReturn(6);
        weapon.setStaminaDamage(2);
        noWhiteWine();
        noSithCurse();
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
    }

    private void normalFight() {
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        expect(resolver.doResolve(command, resolvationData)).andReturn(paragraphList);
    }

    private void noSithCurse() {
        expect(itemHandler.hasItem(character, "4004")).andReturn(false);
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
