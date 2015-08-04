package hu.zagor.gamebooks.content.command.itemcheck;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.ParagraphData;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ItemCheckItemsCommand}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckItemsCommandTest {

    private ItemCheckItemsCommand underTest;
    private IMocksControl mockControl;
    private ItemCheckCommand parent;
    private Character character;
    private CharacterHandler characterHandler;
    private ParagraphData have;
    private ParagraphData dontHave;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        underTest = new ItemCheckItemsCommand();
        character = mockControl.createMock(Character.class);
        characterHandler = new CharacterHandler();
        parent = new ItemCheckCommand();
        have = new ParagraphData();
        dontHave = new ParagraphData();
        parent.setHave(have);
        parent.setDontHave(dontHave);
        parent.setCheckType(CheckType.items);
        parent.setId("1001 and 1002");
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
    }

    public void testResolveWhenHasAllRequiredItemShouldReturnHave() {
        // GIVEN
        final List<Item> equipment = new ArrayList<Item>();
        equipment.add(new Item("1001", "Sword", ItemType.weapon1));
        equipment.add(new Item("1002", "Dagger", ItemType.weapon1));
        expect(character.getEquipment()).andReturn(equipment);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, character, characterHandler);
        // THEN
        Assert.assertEquals(returned, have);
    }

    public void testResolveWhenDoesNotHaveAllRequiredItemsShouldReturnDontHave() {
        // GIVEN
        final List<Item> equipment = new ArrayList<Item>();
        equipment.add(new Item("1001", "Sword", ItemType.weapon1));
        expect(character.getEquipment()).andReturn(equipment);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, character, characterHandler);
        // THEN
        Assert.assertEquals(returned, dontHave);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
