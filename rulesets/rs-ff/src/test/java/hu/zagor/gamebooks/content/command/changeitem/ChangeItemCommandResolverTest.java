package hu.zagor.gamebooks.content.command.changeitem;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;

import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ChangeItemCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class ChangeItemCommandResolverTest {

    private static final String ITEM_ID = "1005";
    private ChangeItemCommandResolver underTest;
    private IMocksControl mockControl;
    private ChangeItemCommand command;
    private ResolvationData resolvationData;
    private FfCharacter character;
    private BookInformations info;
    private CharacterHandler characterHandler;
    private CharacterItemHandler itemHandler;
    private FfItem item;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new ChangeItemCommandResolver();
        command = mockControl.createMock(ChangeItemCommand.class);
        resolvationData = new ResolvationData();
        character = mockControl.createMock(FfCharacter.class);
        resolvationData.setCharacter(character);
        info = new BookInformations(3L);
        characterHandler = new FfCharacterHandler();
        itemHandler = mockControl.createMock(FfCharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
        info.setCharacterHandler(characterHandler);
        resolvationData.setInfo(info);
    }

    @BeforeMethod
    public void setUpMethod() {
        item = new FfItem(ITEM_ID, "Sword", ItemType.weapon1);
        mockControl.reset();
    }

    public void testDoResolveWhenCharacterDoesNotHaveItemInQuestionShouldDoNothing() {
        // GIVEN
        expect(command.getId()).andReturn(ITEM_ID);
        expect(itemHandler.getItem(character, ITEM_ID)).andReturn(null);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned);
    }

    public void testDoResolveWhenNewValueIsSetShouldSetNewValueToField() {
        // GIVEN
        expect(command.getId()).andReturn(ITEM_ID);
        expect(itemHandler.getItem(character, ITEM_ID)).andReturn(item);
        expect(command.getAttribute()).andReturn("attackStrength");
        expect(command.getNewValue()).andReturn(3);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(item.getAttackStrength(), 3);
    }

    public void testDoResolveWhenChangedAmountIsSetShouldChangeFieldByAmount() {
        // GIVEN
        item.setAttackStrength(3);
        expect(command.getId()).andReturn(ITEM_ID);
        expect(itemHandler.getItem(character, ITEM_ID)).andReturn(item);
        expect(command.getAttribute()).andReturn("attackStrength");
        expect(command.getNewValue()).andReturn(null);
        expect(command.getChangeValue()).andReturn(-1);
        mockControl.replay();
        // WHEN
        final List<ParagraphData> returned = underTest.doResolve(command, resolvationData);
        // THEN
        Assert.assertNull(returned);
        Assert.assertEquals(item.getAttackStrength(), 2);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
