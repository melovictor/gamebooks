package hu.zagor.gamebooks.character.handler.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.bookinfo.BookInformationFetcher;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link FfCharacterItemHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class FfCharacterItemHandlerAddItemBTest {
    @UnderTest private FfCharacterItemHandler underTest;
    @MockControl private IMocksControl mockControl;
    private FfCharacter character;
    @Inject private ItemFactory itemFactory;
    @Inject private Logger logger;
    private FfItem eSword;
    private FfItem eBroadsword;
    private FfItem dShield;
    @Inject private BeanFactory beanFactory;
    @Inject private BookInformationFetcher bookInformationFetcher;
    @Mock private BookInformations info;

    @BeforeMethod
    public void setUpMethod() {
        character = new FfCharacter();
        character.setBackpackSize(99);

        eSword = new FfItem("1001", "Sword", ItemType.weapon1);
        eSword.getEquipInfo().setEquipped(true);
        eBroadsword = new FfItem("1005", "Broadsword", ItemType.weapon2);
        eBroadsword.getEquipInfo().setEquipped(true);
        dShield = new FfItem("3007", "Shield", ItemType.shield);
    }

    public void testAddItemWhenTwoHandedEquippedSwordAddedWhenOneHandedSwordIsEquippedWithNonEquippedShieldShouldRemoveOneHandedSword() {
        // GIVEN
        character.getEquipment().add(eSword);
        character.getEquipment().add(dShield);
        logger.debug("Resolving item {} for addition.", eBroadsword.getId());
        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
        expect(itemFactory.resolveItem(eBroadsword.getId())).andReturn(eBroadsword);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, eBroadsword.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 3);
        Assert.assertEquals(character.getEquipment().get(0), eSword);
        Assert.assertFalse(character.getEquipment().get(0).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(1), dShield);
        Assert.assertFalse(character.getEquipment().get(1).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(2), eBroadsword);
        Assert.assertTrue(character.getEquipment().get(2).getEquipInfo().isEquipped());
    }

    public void testAddItemWhenOneHandedEquippedSwordAddedWhenTwoHandedSwordIsEquippedShouldRemoveTwoHandedSword() {
        // GIVEN
        character.getEquipment().add(eBroadsword);
        logger.debug("Resolving item {} for addition.", eSword.getId());
        expect(bookInformationFetcher.getInfoByRequest()).andReturn(info);
        expect(beanFactory.getBean("defaultItemFactory", info)).andReturn(itemFactory);
        expect(itemFactory.resolveItem(eSword.getId())).andReturn(eSword);
        mockControl.replay();
        // WHEN
        underTest.addItem(character, eSword.getId(), 1);
        // THEN
        Assert.assertEquals(character.getEquipment().size(), 2);
        Assert.assertEquals(character.getEquipment().get(0), eBroadsword);
        Assert.assertFalse(character.getEquipment().get(0).getEquipInfo().isEquipped());
        Assert.assertEquals(character.getEquipment().get(1), eSword);
        Assert.assertTrue(character.getEquipment().get(1).getEquipInfo().isEquipped());
    }

}
