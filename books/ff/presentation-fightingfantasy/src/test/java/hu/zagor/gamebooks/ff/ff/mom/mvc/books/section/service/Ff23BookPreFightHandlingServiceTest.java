package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.commandlist.CommandList;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link Ff23BookPreFightHandlingService}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff23BookPreFightHandlingServiceTest {

    private static final String HORN_ID = "3016";
    @MockControl private IMocksControl mockControl;
    @UnderTest private Ff23BookPreFightHandlingService underTest;
    @Mock private FfBookInformations info;
    @Mock private HttpSessionWrapper wrapper;
    @Mock private Paragraph paragraph;
    @Mock private ParagraphData data;
    @Mock private CommandList commands;
    @Mock private FightCommand command;
    @Instance private Map<String, Enemy> enemies;
    @Mock private FfEnemy enemy;
    @Instance(inject = true) private List<String> hornResistantEnemies;
    @Instance private FfCharacterHandler characterHandler;
    @Mock private FfCharacterItemHandler itemHandler;
    @Mock private FfCharacter character;
    @Mock private FfItem horn;

    @BeforeClass
    public void setUpClass() {
        characterHandler.setItemHandler(itemHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        enemies.clear();
        hornResistantEnemies.clear();
    }

    public void testHandlePreFightItemUsageWhenItemIsNotHeversHornShouldDoNothing() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, "3001");
        // THEN
        Assert.assertNull(returned);
    }

    public void testHandlePreFightItemUsageWhenItemIsHeversHornAndEnemyIsNotResistantToItShouldRecudeSkill() {
        // GIVEN
        enemies.put("1", enemy);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commands);
        expect(commands.get(0)).andReturn(command);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(command.getEnemies()).andReturn(Arrays.asList(new String[]{"1"}));
        expect(enemy.getSkill()).andReturn(6);
        enemy.setSkill(5);
        expect(info.getCharacterHandler()).andReturn(characterHandler);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.getItem(character, HORN_ID)).andReturn(horn);
        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, HORN_ID);
        // THEN
        Assert.assertSame(returned, horn);
    }

    public void testHandlePreFightItemUsageWhenItemIsHeversHornAndEnemyIsResistantToItShouldNotRecudeSkill() {
        // GIVEN
        hornResistantEnemies.add("1");
        enemies.put("1", enemy);
        expect(wrapper.getParagraph()).andReturn(paragraph);
        expect(paragraph.getData()).andReturn(data);
        expect(data.getCommands()).andReturn(commands);
        expect(commands.get(0)).andReturn(command);
        expect(wrapper.getEnemies()).andReturn(enemies);
        expect(command.getEnemies()).andReturn(Arrays.asList(new String[]{"1"}));
        expect(info.getCharacterHandler()).andReturn(characterHandler);
        expect(wrapper.getCharacter()).andReturn(character);
        expect(itemHandler.getItem(character, HORN_ID)).andReturn(horn);
        mockControl.replay();
        // WHEN
        final FfItem returned = underTest.handlePreFightItemUsage(info, wrapper, HORN_ID);
        // THEN
        Assert.assertSame(returned, horn);
    }

}
