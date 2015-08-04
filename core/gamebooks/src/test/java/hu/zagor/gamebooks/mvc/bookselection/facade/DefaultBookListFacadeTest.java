package hu.zagor.gamebooks.mvc.bookselection.facade;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.bookselection.domain.SeriesCollection;
import hu.zagor.gamebooks.mvc.bookselection.domain.transformer.SeriesCollectionTransformer;
import hu.zagor.gamebooks.player.PlayerUser;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DefaultBookListFacade}.
 * @author Tamas_Szekeres
 */
@Test
public class DefaultBookListFacadeTest {

    private DefaultBookListFacade underTest;
    private IMocksControl mockControl;
    private SeriesCollectionTransformer collectionTransformer;
    private Logger logger;
    private Locale locale;
    private PlayerUser player;
    private List<BookInformations> bookList;
    private SeriesCollection nonAdminBooks;
    private SeriesCollection adminBooks;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public void setUpClass() {
        underTest = new DefaultBookListFacade();
        mockControl = EasyMock.createStrictControl();
        logger = mockControl.createMock(Logger.class);
        player = mockControl.createMock(PlayerUser.class);
        bookList = mockControl.createMock(List.class);
        nonAdminBooks = mockControl.createMock(SeriesCollection.class);
        adminBooks = mockControl.createMock(SeriesCollection.class);

        collectionTransformer = mockControl.createMock(SeriesCollectionTransformer.class);
        Whitebox.setInternalState(underTest, "collectionTransformer", collectionTransformer);
        underTest.setAvailableBooks(bookList);
        underTest.setLogger(logger);

        locale = Locale.UK;
    }

    @BeforeMethod
    public void setUpMethod() {
        mockControl.reset();
        Whitebox.getInternalState(underTest, Map.class).clear();
    }

    public void testGetAvailableBooksWhenNormalUserComingAndHasNoCachedContentShouldReloadData() {
        // GIVEN
        expect(player.isAdmin()).andReturn(false);
        expect(collectionTransformer.createSeriesCollection(bookList, locale, player)).andReturn(nonAdminBooks);
        mockControl.replay();
        // WHEN
        final SeriesCollection returned = underTest.getAvailableBooks(locale, player);
        // THEN
        Assert.assertSame(returned, nonAdminBooks);
    }

    @SuppressWarnings("unchecked")
    public void testGetAvailableBooksWhenNormalUserComingAndHasCachedContentShouldReturnCachedData() {
        // GIVEN
        Whitebox.getInternalState(underTest, Map.class).put("en", nonAdminBooks);
        expect(player.isAdmin()).andReturn(false);
        mockControl.replay();
        // WHEN
        final SeriesCollection returned = underTest.getAvailableBooks(locale, player);
        // THEN
        Assert.assertSame(returned, nonAdminBooks);
    }

    @SuppressWarnings("unchecked")
    public void testGetAvailableBooksWhenAdminUserComingAndHasCachedContentShouldRelaodData() {
        // GIVEN
        Whitebox.getInternalState(underTest, Map.class).put("en", nonAdminBooks);
        expect(player.isAdmin()).andReturn(true);
        expect(collectionTransformer.createSeriesCollection(bookList, locale, player)).andReturn(adminBooks);
        mockControl.replay();
        // WHEN
        final SeriesCollection returned = underTest.getAvailableBooks(locale, player);
        // THEN
        Assert.assertSame(returned, adminBooks);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
