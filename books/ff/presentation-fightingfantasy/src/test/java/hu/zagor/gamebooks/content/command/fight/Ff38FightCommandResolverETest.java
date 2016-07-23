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
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.subresolver.FightCommandSubResolver;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
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
public class Ff38FightCommandResolverETest {

    private IMocksControl mockControl;
    private Ff38FightCommandResolver underTest;
    private RandomNumberGenerator generator;
    private FfFightCommand command;
    private ResolvationData resolvationData;
    private ParagraphData rootData;
    private Ff38Character character;
    private Map<String, Enemy> enemies;
    private BookInformations info;
    private FfCharacterHandler characterHandler;
    private FfUserInteractionHandler interactionHandler;
    private FfCharacterItemHandler itemHandler;
    private FightCommandMessageList messages;
    private Iterator<String> iterator;
    private FfEnemy wolf;
    private FfEnemy heydrich;
    private FfParagraphData win;
    private Map<String, FightCommandSubResolver> subResolvers;
    private FightCommandSubResolver resolver;
    private List<ParagraphData> paragraphList;
    private FfAttributeHandler attributeHandler;
    private FfEnemy katarina;
    private FfEnemy vampireBat;
    private FightRoundBoundingCommand afterBounding;
    private RandomCommand randomCommand;

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
        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).withEnemies(enemies)
            .build();
        messages = mockControl.createMock(FightCommandMessageList.class);
        underTest.setBoneEnemies(Arrays.asList("39"));
        underTest.setUndeadEnemies(Arrays.asList("38", "40"));
        wolf = new FfEnemy();
        heydrich = new FfEnemy();
        katarina = new FfEnemy();
        vampireBat = new FfEnemy();
        enemies.put("7", wolf);
        enemies.put("40", heydrich);
        enemies.put("41", katarina);
        enemies.put("34", vampireBat);
        paragraphList = new ArrayList<>();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        command = new FfFightCommand();
        Whitebox.setInternalState(command, "messages", messages);
        wolf.setStamina(5);
        heydrich.setStamina(21);
        katarina.setStamina(10);
        vampireBat.setStamina(7);
        iterator = new ArrayList<String>().iterator();
        command.getEnemies().clear();
        final FightOutcome fightOutcome = new FightOutcome();
        fightOutcome.setParagraphData(win);
        command.setWin(Arrays.asList(fightOutcome));
        command.setResolver("single");
        command.setOngoing(true);
        randomCommand = new RandomCommand();
        afterBounding = new FightRoundBoundingCommand(command);
        afterBounding.getCommands().add(randomCommand);
        command.setAfterBounding(afterBounding);
    }

    public void testResolveWhenFightingAgainstKatarinaAndHaveLittleStaminaShouldInterruptAndFlee() {
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
        expect(attributeHandler.resolveValue(character, "stamina")).andReturn(5);
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        final CommandResolveResult resolve = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertTrue(resolve.isFinished());
        Assert.assertSame(resolve.getResolveList().get(0), win);
    }

    public void testResolveWhenFightingAgainstVampireBatInInitializationStateShouldDoNothing() {
        // GIVEN
        final String enemyId = "34";
        command.getEnemies().add(enemyId);
        command.getEnemies().add("35");
        messages.clear();
        noSlowPoision();
        noSpell(enemyId);
        normalFight();
        noWhiteWine();
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertSame(command.getAfterBounding(), afterBounding);
    }

    public void testResolveWhenFightingAgainstVampireBatAndHitsFirstTimeShouldRemoveAfterBound() {
        // GIVEN
        final String enemyId = "34";
        command.getEnemies().add(enemyId);
        command.getEnemies().add("35");
        randomCommand.setDiceResult(6);
        messages.clear();
        noSlowPoision();
        noSpell(enemyId);
        normalFight();
        noWhiteWine();
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertNull(command.getAfterBounding());
    }

    public void testResolveWhenFightingAgainstVampireBatAndWeaselAlreadyHitUsShouldNotDoRandomRollAnymore() {
        // GIVEN
        final String enemyId = "34";
        command.getEnemies().add(enemyId);
        command.getEnemies().add("35");
        command.setAfterBounding(null);
        messages.clear();
        noSlowPoision();
        noSpell(enemyId);
        normalFight();
        noWhiteWine();
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertNull(command.getAfterBounding());
    }

    public void testResolveWhenFightingAgainstVampireBatAndBatDiesCloseFinishBattle() {
        // GIVEN
        final String enemyId = "34";
        vampireBat.setStamina(0);
        command.getEnemies().add(enemyId);
        command.getEnemies().add("35");
        command.setAfterBounding(null);
        messages.clear();
        noSlowPoision();
        noSpell(enemyId);
        normalFight();
        noWhiteWine();
        noSithCurse();
        noAutoDamage();
        noRegeneratingRing();
        expect(messages.isEmpty()).andReturn(false);
        expect(messages.iterator()).andReturn(iterator);
        mockControl.replay();
        // WHEN
        final CommandResolveResult resolve = underTest.resolve(command, resolvationData);
        // THEN
        Assert.assertTrue(resolve.isFinished());
        Assert.assertSame(resolve.getResolveList().get(0), win);
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
