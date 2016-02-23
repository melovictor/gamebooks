package hu.zagor.gamebooks.content.command.fight.roundresolver;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.ff.sa.character.Ff12Character;
import hu.zagor.gamebooks.ff.ff.sa.enemy.Ff12Enemy;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.powermock.reflect.Whitebox;
import org.springframework.context.MessageSource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ShootingFf12FightRoundResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class ShootingFf12FightRoundResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private ShootingFf12FightRoundResolver underTest;
    @Instance private FightCommand command;
    private ResolvationData resolvationData;
    @Instance private FightBeforeRoundResult beforeRoundResult;
    @Instance private Ff12Enemy enemy;
    @Mock private FightCommandMessageList messages;
    @Mock private Paragraph paragraph;
    private BookInformations info;
    @Instance private Ff12Character character;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfAttributeHandler attributeHandler;

    @Inject private RandomNumberGenerator generator;
    @Inject private DiceResultRenderer renderer;
    @Inject private LocaleProvider localeProvider;
    @Inject private MessageSource source;
    @Mock private Map<String, Enemy> enemies;
    @Mock private FfUserInteractionHandler interactionHandler;

    @BeforeClass
    public void setUpClass() {
        info = new BookInformations(1L);
        info.setCharacterHandler(characterHandler);
        characterHandler.setItemHandler(itemHandler);
        characterHandler.setAttributeHandler(attributeHandler);
        characterHandler.setInteractionHandler(interactionHandler);
        Whitebox.setInternalState(command, "messages", messages);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).build();
        resolvationData.setEnemies(enemies);
    }

    @BeforeMethod
    public void setUpMethod() {
        command.getResolvedEnemies().clear();

        enemy.setCommonName("Orc");
        enemy.setId("5");
        enemy.setStamina(5);
        enemy.setSkill(7);
        enemy.setAttackPerRound(1);

        mockControl.reset();
    }

    public void testResolveRoundWhenNormalFightRoundWithSingleEnemyAndEverybodyMissingShouldPlayRoundDownProperly() {
        // GIVEN
        command.getResolvedEnemies().add(enemy);
        final int[] heroTarget = new int[]{11, 6, 5};
        final int[] enemyTarget = new int[]{9, 4, 5};

        expect(itemHandler.removeItem(character, "4002", 1)).andReturn(new ArrayList<Item>());
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(attributeHandler.resolveValue(character, "skill")).andReturn(8);
        expect(generator.getRandomNumber(2)).andReturn(heroTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, heroTarget)).andReturn("Roll result: 11.");
        expect(messages.addKey("page.ff12.fight.targetHero", "Orc", "Roll result: 11.", 11)).andReturn(true);
        expect(messages.addKey("page.ff12.fight.missedEnemy", "Orc")).andReturn(true);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(generator.getRandomNumber(2)).andReturn(enemyTarget);
        expect(generator.getDefaultDiceSide()).andReturn(6);
        expect(renderer.render(6, enemyTarget)).andReturn("Roll result: 9.");
        expect(messages.addKey("page.ff12.fight.targetEnemy", "Orc", "Roll result: 9.", 9)).andReturn(true);
        expect(messages.addKey("page.ff12.fight.missedHero", "Orc")).andReturn(true);
        character.changeStamina(0);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);
        expect(interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID)).andReturn("5");
        expect(enemies.get("5")).andReturn(enemy);

        mockControl.replay();
        // WHEN
        underTest.resolveRound(command, resolvationData, beforeRoundResult);
        // THEN
        Assert.assertEquals(enemy.getActiveWeapon(), null);
        Assert.assertEquals(enemy.getStamina(), 5);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
