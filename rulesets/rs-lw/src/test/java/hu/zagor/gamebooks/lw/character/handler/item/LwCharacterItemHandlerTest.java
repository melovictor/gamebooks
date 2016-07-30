package hu.zagor.gamebooks.lw.character.handler.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.bookinfo.BookInformationFetcher;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import hu.zagor.gamebooks.lw.character.item.LwItem;
import hu.zagor.gamebooks.lw.character.item.Placement;
import hu.zagor.gamebooks.lw.domain.LwBookInformations;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.Instance;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link LwCharacterItemHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class LwCharacterItemHandlerTest {

    @MockControl private IMocksControl mockControl;
    @UnderTest private LwCharacterItemHandler underTest;
    @Mock private LwItem item;
    @Mock private LwCharacter character;
    @Inject private ItemFactory itemFactory;
    @Inject private BeanFactory beanFactory;
    @Inject private BookInformationFetcher bookInformationFetcher;
    @Mock private LwBookInformations info;
    private LwItem backpack;
    @Instance(inject = true) private Map<Placement, Integer> maxPlacementValues;

    @BeforeClass
    public void setUpClass() {
        backpack = new LwItem("40000", "Backpack", ItemType.backpack);
        backpack.setPlacement(Placement.special);
        maxPlacementValues.put(Placement.special, 9999);
        maxPlacementValues.put(Placement.backpack, 8);
    }

    public void testAddItemWhenItemGoesIntoBackpackButHasNoBackpackShouldReturnZero() {
        // GIVEN
        final String itemId = "30006";

        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
        expect(itemFactory.resolveItem(itemId)).andReturn(item);
        expect(item.getPlacement()).andReturn(Placement.backpack);
        expect(character.getEquipment()).andReturn(new ArrayList<Item>());
        mockControl.replay();
        // WHEN
        final int returned = underTest.addItem(character, itemId, 1);
        // THEN
        Assert.assertEquals(returned, 0);
    }

    public void testAddItemWhenTryingToPickUpSecondBackpackShouldReturnZero() {
        // GIVEN
        final String itemId = "40000";

        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
        expect(itemFactory.resolveItem(itemId)).andReturn(item);
        expect(item.getPlacement()).andReturn(Placement.special).times(2);
        expect(character.getEquipment()).andReturn(Arrays.asList((Item) backpack));
        expect(item.getId()).andReturn(itemId);
        expect(item.getAmount()).andReturn(1);
        expect(item.getBackpackSize()).andReturn(1.0);
        expect(item.getPlacement()).andReturn(Placement.special).times(2);
        expect(item.getItemType()).andReturn(ItemType.backpack);

        mockControl.replay();
        // WHEN
        final int returned = underTest.addItem(character, itemId, 1);
        // THEN
        Assert.assertEquals(returned, 0);
    }

}
