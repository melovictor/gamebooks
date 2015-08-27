package hu.zagor.gamebooks.content.command.itemcheck;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.domain.BookInformations;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ItemCheckItemCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckItemCommandTest {

    private static final String ID = "3001";
    private ItemCheckItemCommand underTest;
    private IMocksControl mockControl;
    private Character character;
    private ItemCheckCommand parent;
    private ParagraphData data;
    private CharacterHandler characterHandler;
    private CharacterItemHandler itemHandler;
    private BookInformations info;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        character = new Character();
        parent = mockControl.createMock(ItemCheckCommand.class);
        data = mockControl.createMock(ParagraphData.class);
        characterHandler = new CharacterHandler();
        itemHandler = mockControl.createMock(CharacterItemHandler.class);
        characterHandler.setItemHandler(itemHandler);
        info = new BookInformations(1L);
        info.setCharacterHandler(characterHandler);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new ItemCheckItemCommand();
        mockControl.reset();
    }

    public void testResolveWhenCharacterHasEquippedItemShouldReturnCommandsHaveEquippedBlock() {
        // GIVEN
        expect(parent.getId()).andReturn(ID);
        expect(parent.getAmount()).andReturn(1);
        expect(itemHandler.hasEquippedItem(character, ID)).andReturn(true);
        expect(parent.getHaveEquipped()).andReturn(data);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, getResolvationData());
        // THEN
        Assert.assertSame(returned, data);
    }

    public void testResolveWhenCharacterHasItemShouldReturnCommandsHaveBlock() {
        // GIVEN
        expect(parent.getId()).andReturn(ID);
        expect(parent.getAmount()).andReturn(1);
        expect(itemHandler.hasEquippedItem(character, ID)).andReturn(false);
        expect(itemHandler.hasItem(character, ID, 1)).andReturn(true);
        expect(parent.getHave()).andReturn(data);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, getResolvationData());
        // THEN
        Assert.assertSame(returned, data);
    }

    public void testResolveWhenCharacterHasProperAmountOfItemsShouldSkipHaveEquippedAndReturnCommandsHaveBlock() {
        // GIVEN
        expect(parent.getId()).andReturn(ID);
        expect(parent.getAmount()).andReturn(3);
        expect(itemHandler.hasItem(character, ID, 3)).andReturn(true);
        expect(parent.getHave()).andReturn(data);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, getResolvationData());
        // THEN
        Assert.assertSame(returned, data);
    }

    public void testResolveWhenCharacterDontHaveItemShouldReturnCommandsDontHaveBlock() {
        // GIVEN
        expect(parent.getId()).andReturn(ID);
        expect(parent.getAmount()).andReturn(1);
        expect(itemHandler.hasEquippedItem(character, ID)).andReturn(false);
        expect(itemHandler.hasItem(character, ID, 1)).andReturn(false);
        expect(parent.getDontHave()).andReturn(data);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, getResolvationData());
        // THEN
        Assert.assertSame(returned, data);
    }

    private ResolvationData getResolvationData() {
        return DefaultResolvationDataBuilder.builder().withRootData(null).withBookInformations(info).withCharacter(character).build();
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
