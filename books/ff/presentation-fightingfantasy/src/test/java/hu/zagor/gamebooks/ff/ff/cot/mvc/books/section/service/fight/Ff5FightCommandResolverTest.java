package hu.zagor.gamebooks.ff.ff.cot.mvc.books.section.service.fight;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.CommandResolveResult;
import hu.zagor.gamebooks.content.command.fight.FfFightCommandResolver;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
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
 * Unit test for class {@link Ff5FightCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class Ff5FightCommandResolverTest {

    private Ff5FightCommandResolver underTest;
    private IMocksControl mockControl;
    private FfFightCommandResolver superResolver;
    private Command commandObject;
    private ResolvationData resolvationData;
    private CommandResolveResult resolveResult;
    private FfBookInformations info;
    private Map<String, Enemy> enemies;
    private FfCharacter character;
    private ParagraphData rootData;
    private FfCharacterHandler characterHandler;
    private FfCharacterItemHandler itemHandler;
    private FfAttributeHandler attributeHandler;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new Ff5FightCommandResolver();
        superResolver = mockControl.createMock(FfFightCommandResolver.class);
        Whitebox.setInternalState(underTest, "superResolver", superResolver);
        commandObject = mockControl.createMock(Command.class);
        character = mockControl.createMock(FfCharacter.class);
        resolveResult = mockControl.createMock(CommandResolveResult.class);
        info = new FfBookInformations(1L);
        characterHandler = new FfCharacterHandler();
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
        attributeHandler = mockControl.createMock(FfAttributeHandler.class);
        characterHandler.setAttributeHandler(attributeHandler);
        info.setCharacterHandler(characterHandler);
        final Paragraph paragraph = new Paragraph("3", null, 11);
        paragraph.setData(rootData);
        resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).withCharacter(character).withEnemies(enemies)
            .build();
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testResolveWhenNotFinishedShouldCallSuperAndFinish() {
        // GIVEN
        expect(superResolver.resolve(commandObject, resolvationData)).andReturn(resolveResult);
        expect(resolveResult.isFinished()).andReturn(false);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(commandObject, resolvationData);
        // THEN
        Assert.assertSame(returned, resolveResult);
    }

    public void testResolveWhenFinishedButDoNotHaveScorpionBroochShouldDoNothing() {
        // GIVEN
        expect(superResolver.resolve(commandObject, resolvationData)).andReturn(resolveResult);
        expect(resolveResult.isFinished()).andReturn(true);
        expect(itemHandler.hasEquippedItem(character, "3006")).andReturn(false);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(commandObject, resolvationData);
        // THEN
        Assert.assertSame(returned, resolveResult);
    }

    public void testResolveWhenFinishedAndHaveScorpionBroochButAlreadyDeadShouldDoNothing() {
        // GIVEN
        expect(superResolver.resolve(commandObject, resolvationData)).andReturn(resolveResult);
        expect(resolveResult.isFinished()).andReturn(true);
        expect(itemHandler.hasEquippedItem(character, "3006")).andReturn(true);
        expect(attributeHandler.isAlive(character)).andReturn(false);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(commandObject, resolvationData);
        // THEN
        Assert.assertSame(returned, resolveResult);
    }

    public void testResolveWhenFinishedAndHaveScorpionBroochAndStillAliveShouldHealCharacter() {
        // GIVEN
        expect(superResolver.resolve(commandObject, resolvationData)).andReturn(resolveResult);
        expect(resolveResult.isFinished()).andReturn(true);
        expect(itemHandler.hasEquippedItem(character, "3006")).andReturn(true);
        expect(attributeHandler.isAlive(character)).andReturn(true);
        character.changeStamina(1);
        mockControl.replay();
        // WHEN
        final CommandResolveResult returned = underTest.resolve(commandObject, resolvationData);
        // THEN
        Assert.assertSame(returned, resolveResult);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
