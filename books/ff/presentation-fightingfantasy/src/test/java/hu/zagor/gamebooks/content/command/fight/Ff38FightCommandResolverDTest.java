package hu.zagor.gamebooks.content.command.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff38FightCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff38FightCommandResolverDTest {

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
    private FfEnemy wolf;
    private FfEnemy heydrich;
    private FfParagraphData win;
    private Map<String, FightCommandSubResolver> subResolvers;
    private FightCommandSubResolver resolver;
    private List<ParagraphData> paragraphList;
    private FfAttributeHandler attributeHandler;
    private FfItem weapon;
    private FfEnemy katarina;
    private Iterator<String> iterator;

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
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        info.setCharacterHandler(characterHandler);
        enemies = new HashMap<>();
        rootData = new ParagraphData();
        rootData.setText("");
        resolvationData = DefaultResolvationDataBuilder.builder().withRootData(rootData).withBookInformations(info).withCharacter(character).withEnemies(enemies).build();
        messages = mockControl.createMock(FightCommandMessageList.class);
        underTest.setBoneEnemies(Arrays.asList("39"));
        underTest.setUndeadEnemies(Arrays.asList("38", "40"));
        wolf = new FfEnemy();
        heydrich = new FfEnemy();
        katarina = new FfEnemy();
        enemies.put("7", wolf);
        enemies.put("40", heydrich);
        enemies.put("41", katarina);
        paragraphList = new ArrayList<>();
        weapon = mockControl.createMock(FfItem.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        command = new FightCommand();
        Whitebox.setInternalState(command, "messages", messages);
        wolf.setStamina(5);
        heydrich.setStamina(21);
        katarina.setStamina(10);
        command.getEnemies().clear();
        final FightOutcome winOutcome = new FightOutcome();
        winOutcome.setParagraphData(win);
        command.setWin(Arrays.asList(winOutcome));
        command.setResolver("single");
        command.setOngoing(true);
        iterator = Arrays.asList("a", "b", "c").iterator();
    }

    public void testResolveWhenHaveRegenerationRingButNotEndOfBattleYetShouldNotHeal() {
        // GIVEN
        final String enemyId = "7";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoision();
        noSpell(enemyId);
        normalFight();
        noWhiteWine();
        noAutoDamage();
        expect(itemHandler.hasEquippedItem(character, "3020")).andReturn(true);
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
    }

    public void testResolveWhenHaveRegenerationRingAndBattleIsOverButWeDiedShouldNotHeal() {
        // GIVEN
        final String enemyId = "7";
        command.getEnemies().add(enemyId);
        command.setOngoing(false);
        messages.clear();
        noSlowPoision();
        noSpell(enemyId);
        normalFight();
        noWhiteWine();
        noSithCurse();
        noAutoDamage();
        expect(itemHandler.hasEquippedItem(character, "3020")).andReturn(true);
        expect(attributeHandler.isAlive(character)).andReturn(false);
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
    }

    public void testResolveWhenFightingAgainstHeydrichButNotWithNightstarShouldDoBattleNormally() {
        // GIVEN
        final String enemyId = "40";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoision();
        noSpell(enemyId);
        expect(itemHandler.getItem(character, "1003")).andReturn(null);
        normalFight();
        expect(itemHandler.getItem(character, "1003")).andReturn(null);
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

    public void testResolveWhenFightingAgainstHeydrichWithNightstarShouldAlterWeaponAttachStrengthBonus() {
        // GIVEN
        final String enemyId = "40";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoision();
        noSpell(enemyId);
        expect(itemHandler.getItem(character, "1003")).andReturn(weapon);
        expect(weapon.getAttackStrength()).andReturn(1);
        weapon.setAttackStrength(2);
        normalFight();
        expect(itemHandler.getItem(character, "1003")).andReturn(weapon);
        expect(weapon.getAttackStrength()).andReturn(2);
        weapon.setAttackStrength(1);
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

    public void testResolveWhenFightingAgainstKatarinaAndHaveLotsOfStaminaShouldDoBattleNormally() {
        // GIVEN
        final String enemyId = "41";
        command.getEnemies().add(enemyId);
        messages.clear();
        noSlowPoision();
        noSpell(enemyId);
        normalFight();
        noWhiteWine();
        noAutoDamage();
        noRegeneratingRing();
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(10);
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
    }

    public void testResolveWhenAutoBleedingIsActiveAndNotYetEndOfBleedingsShouldLoseExtraStamina() {
        // GIVEN
        final String enemyId = "7";
        command.getEnemies().add(enemyId);
        messages.clear();
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("fight");
        noSpell(enemyId);
        normalFight();
        noWhiteWine();
        expect(itemHandler.hasItem(character, "4005")).andReturn(false);
        expect(itemHandler.hasEquippedItem(character, "3020")).andReturn(false);
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
    }

    private void noSlowPoision() {
        expect(interactionHandler.peekLastFightCommand(character)).andReturn(null);
        expect(itemHandler.hasItem(character, "4101")).andReturn(false);
    }

    private void noSpell(final String enemyId) {
        expect(interactionHandler.peekLastFightCommand(character)).andReturn("attack");
        messages.switchToPreFightMessages();
        expect(interactionHandler.peekLastFightCommand(character, "enemyId")).andReturn(enemyId);
        expect(itemHandler.hasItem(character, "4013")).andReturn(false);
        expect(itemHandler.hasItem(character, "4014")).andReturn(false);
        expect(itemHandler.hasItem(character, "4015")).andReturn(false);
        messages.switchToRoundMessages();
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
