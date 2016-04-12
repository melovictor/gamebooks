package hu.zagor.gamebooks.content.command.itemcheck;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link ItemCheckTotalItemsCommandResolver}.
 * @author Tamas_Szekeres
 */
@Test
public class ItemCheckTotalItemsCommandResolverTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private ItemCheckTotalItemsCommandResolver underTest;
    @Mock private ItemCheckCommand parent;
    @Instance private ResolvationData resolvationData;
    @Mock private Character character;
    @Mock private ParagraphData parData;

    @BeforeClass
    public void setUpClass() {
        resolvationData.setCharacter(character);
    }

    public void testCountActualItemsWhenHasNoItemsShouldReturnZero() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.countActualItems(new ArrayList<Item>());
        // THEN
        Assert.assertEquals(returned, 0);
    }

    public void testCountActualItemsWhenOnlyHasFistShouldReturnZero() {
        // GIVEN
        final ArrayList<Item> equipment = new ArrayList<Item>();
        equipment.add(new Item("defWpn", "Fist", ItemType.weapon1));
        mockControl.replay();
        // WHEN
        final int returned = underTest.countActualItems(equipment);
        // THEN
        Assert.assertEquals(returned, 0);
    }

    public void testCountActualItemsWhenOnlyHasShadowItemShouldReturnZero() {
        // GIVEN
        final ArrayList<Item> equipment = new ArrayList<Item>();
        equipment.add(new Item("defWpn", "Fist", ItemType.weapon1));
        equipment.add(new Item("4001", "talked with the butler", ItemType.shadow));
        mockControl.replay();
        // WHEN
        final int returned = underTest.countActualItems(equipment);
        // THEN
        Assert.assertEquals(returned, 0);
    }

    public void testCountActualItemsWhenOnlyHasInfoItemShouldReturnZero() {
        // GIVEN
        final ArrayList<Item> equipment = new ArrayList<Item>();
        equipment.add(new Item("defWpn", "Fist", ItemType.weapon1));
        equipment.add(new Item("4001", "talked with the butler", ItemType.shadow));
        equipment.add(new Item("4002", "know about the secret door", ItemType.info));
        mockControl.replay();
        // WHEN
        final int returned = underTest.countActualItems(equipment);
        // THEN
        Assert.assertEquals(returned, 0);
    }

    public void testCountActualItemsWhenOnlyHasCurseSicknessItemShouldReturnZero() {
        // GIVEN
        final ArrayList<Item> equipment = new ArrayList<Item>();
        equipment.add(new Item("defWpn", "Fist", ItemType.weapon1));
        equipment.add(new Item("4001", "talked with the butler", ItemType.shadow));
        equipment.add(new Item("4002", "know about the secret door", ItemType.info));
        equipment.add(new Item("4002", "Yellow plague", ItemType.curseSickness));
        mockControl.replay();
        // WHEN
        final int returned = underTest.countActualItems(equipment);
        // THEN
        Assert.assertEquals(returned, 0);
    }

    public void testCountActualItemsWhenHasCommonItemShouldReturnOne() {
        // GIVEN
        final ArrayList<Item> equipment = new ArrayList<Item>();
        equipment.add(new Item("defWpn", "Fist", ItemType.weapon1));
        equipment.add(new Item("4001", "talked with the butler", ItemType.shadow));
        equipment.add(new Item("4002", "know about the secret door", ItemType.info));
        equipment.add(new Item("4002", "Yellow plague", ItemType.curseSickness));
        equipment.add(new Item("3001", "Parchment", ItemType.common));
        mockControl.replay();
        // WHEN
        final int returned = underTest.countActualItems(equipment);
        // THEN
        Assert.assertEquals(returned, 1);
    }

    public void testCountActualItemsWhenHasNonFistWeaponAndCommonItemShouldReturnTwo() {
        // GIVEN
        mockControl.replay();
        // WHEN
        final int returned = underTest.countActualItems(getFullEquipment());
        // THEN
        Assert.assertEquals(returned, 2);
    }

    public void testResolveWhenHasMoreItemsThanRequestedShouldReturnHave() {
        // GIVEN
        expect(character.getEquipment()).andReturn(getFullEquipment());
        expect(parent.getId()).andReturn("1");
        expect(parent.getHave()).andReturn(parData);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, parData);
    }

    public void testResolveWhenHasSameAmountOfItemsThanRequestedShouldReturnHave() {
        // GIVEN
        expect(character.getEquipment()).andReturn(getFullEquipment());
        expect(parent.getId()).andReturn("2");
        expect(parent.getHave()).andReturn(parData);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, parData);
    }

    public void testResolveWhenDoesNotHaveEnoughItemsShouldReturnDontHave() {
        // GIVEN
        expect(character.getEquipment()).andReturn(getFullEquipment());
        expect(parent.getId()).andReturn("3");
        expect(parent.getDontHave()).andReturn(parData);
        mockControl.replay();
        // WHEN
        final ParagraphData returned = underTest.resolve(parent, resolvationData);
        // THEN
        Assert.assertSame(returned, parData);
    }

    private ArrayList<Item> getFullEquipment() {
        final ArrayList<Item> equipment = new ArrayList<Item>();
        equipment.add(new Item("defWpn", "Fist", ItemType.weapon1));
        equipment.add(new Item("4001", "talked with the butler", ItemType.shadow));
        equipment.add(new Item("4002", "know about the secret door", ItemType.info));
        equipment.add(new Item("4002", "Yellow plague", ItemType.curseSickness));
        equipment.add(new Item("3001", "Parchment", ItemType.common));
        equipment.add(new Item("1001", "Sword", ItemType.weapon1));
        return equipment;
    }

}
