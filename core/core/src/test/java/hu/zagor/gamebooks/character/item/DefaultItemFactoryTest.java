package hu.zagor.gamebooks.character.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.exception.InvalidItemException;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultItemFactory}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultItemFactoryTest {

    private static final String ITEM_ID = "3001";
    private DefaultItemFactory underTest;
    private IMocksControl mockControl;
    private BookItemStorage bookItemStorage;
    private Item item;
    private BookInformations info;
    private BookContentInitializer contentInitializer;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();

        info = new BookInformations(21L);
        bookItemStorage = mockControl.createMock(BookItemStorage.class);
        item = mockControl.createMock(Item.class);
        contentInitializer = mockControl.createMock(BookContentInitializer.class);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new DefaultItemFactory(info);
        Whitebox.setInternalState(underTest, "contentInitializer", contentInitializer);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenStorageIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new DefaultItemFactory(null).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testResolveItemWhenItemIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.resolveItem(null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testResolveItemWhenItemIdIsEmptyShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.resolveItem("");
        // THEN throws exception
    }

    public void testResolveItemWhenItemIdIsSetProperlyShouldCallStorage() {
        // GIVEN
        expect(contentInitializer.getItemStorage(info)).andReturn(bookItemStorage);
        expect(bookItemStorage.getItem(ITEM_ID)).andReturn(item);
        mockControl.replay();
        // WHEN
        final Item returned = underTest.resolveItem(ITEM_ID);
        // THEN
        Assert.assertSame(returned, item);
    }

    @Test(expectedExceptions = InvalidItemException.class)
    public void testResolveItemWhenResolvedItemIsNullShouldThrowException() {
        // GIVEN
        expect(contentInitializer.getItemStorage(info)).andReturn(bookItemStorage);
        expect(bookItemStorage.getItem(ITEM_ID)).andReturn(null);
        mockControl.replay();
        // WHEN
        underTest.resolveItem(ITEM_ID);
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
