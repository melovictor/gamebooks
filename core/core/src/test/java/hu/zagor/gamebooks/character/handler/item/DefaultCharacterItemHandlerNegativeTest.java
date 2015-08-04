package hu.zagor.gamebooks.character.handler.item;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.ItemFactory;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link AbstractCharacterItemHandler}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultCharacterItemHandlerNegativeTest {

    private static final String ITEM_ID = "3001";
    private CharacterItemHandler underTest;
    private IMocksControl mockControl;
    private Character character;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        mockControl.createMock(ItemFactory.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new DefaultCharacterItemHandler();
        character = new Character();
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetItemFactoryWhenItemFactoryIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.setItemFactory(null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddEquipmentWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addItem(null, ITEM_ID, 5);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddEquipmentWhenAmountIsZeroShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, 0);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddEquipmentWhenAmountIsNegativeShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addItem(character, ITEM_ID, -5);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddEquipmentWhenItemIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.addItem(character, (String) null, 5);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveEquipmentWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.removeItem(null, ITEM_ID, 3);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveEquipmentWhenAmountIsZeroShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.removeItem(character, ITEM_ID, 0);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveEquipmentWhenAmountIsNegativeShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.removeItem(character, ITEM_ID, -5);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveEquipmentWhenItemIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.removeItem(character, (String) null, 5);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHasEquippedItemWhenIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.hasEquippedItem(character, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHasItemWhenIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.hasItem(character, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHasItemWhenAmountIsZeroShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.hasItem(character, "1111", 0);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHasItemWhenAmountIsNegativeShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.hasItem(character, "1111", -1);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHasEquippedItemWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.hasEquippedItem(null, ITEM_ID);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHasItemWhenCharacterIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.hasItem(null, ITEM_ID);
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
