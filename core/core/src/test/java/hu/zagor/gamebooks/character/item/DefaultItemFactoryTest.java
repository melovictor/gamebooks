package hu.zagor.gamebooks.character.item;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.exception.InvalidItemException;
import hu.zagor.gamebooks.support.mock.annotation.Inject;
import hu.zagor.gamebooks.support.mock.annotation.MockControl;
import hu.zagor.gamebooks.support.mock.annotation.UnderTest;
import org.easymock.IMocksControl;
import org.easymock.Mock;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultItemFactory}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultItemFactoryTest {

    private static final String ITEM_ID = "3001";
    private DefaultItemFactory underTest;
    @MockControl private IMocksControl mockControl;
    @Mock private BookItemStorage bookItemStorage;
    @Mock private Item item;
    private BookInformations info;
    @Inject private BookContentInitializer contentInitializer;
    @Inject private Logger logger;

    @UnderTest
    public DefaultItemFactory underTest() {
        info = new BookInformations(21L);
        info.setTitle("Book title");
        return new DefaultItemFactory(info);
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
        logger.debug("Loading item {} from storage for book {} ({}).", ITEM_ID, "Book title", 21L);
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
        logger.debug("Loading item {} from storage for book {} ({}).", ITEM_ID, "Book title", 21L);
        expect(contentInitializer.getItemStorage(info)).andReturn(bookItemStorage);
        expect(bookItemStorage.getItem(ITEM_ID)).andReturn(null);
        mockControl.replay();
        // WHEN
        underTest.resolveItem(ITEM_ID);
        // THEN throws exception
    }

}
