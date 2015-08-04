package hu.zagor.gamebooks.books.contentstorage;

import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link CachedBookContentStorage}.
 * @author Tamas_Szekeres
 */
@Test
public class CachedBookContentStorageNegativeTest {

    private static final String ID = "10";
    private static final String TITLE = "title";
    private CachedBookContentStorage underTest;
    private IMocksControl mockControl;
    private BookContentLoader bookContentLoader;
    private Logger logger;
    private BookInformations bookInfo;
    private EnvironmentDetector environmentDetector;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        bookContentLoader = mockControl.createMock(BookContentLoader.class);
        logger = mockControl.createMock(Logger.class);
        environmentDetector = mockControl.createMock(EnvironmentDetector.class);

        bookInfo = new BookInformations(1L);
        bookInfo.setTitle(TITLE);
    }

    @BeforeMethod
    public void setUpMethod() {
        underTest = new CachedBookContentStorage(bookContentLoader);
        Whitebox.setInternalState(underTest, "logger", logger);
        Whitebox.setInternalState(underTest, "environmentDetector", environmentDetector);
        mockControl.reset();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructorWhenBookContentLoaderIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        new CachedBookContentStorage(null).getClass();
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetBookEntryWhenBookInfoIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getBookEntry(null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetBookParagraphWhenBookInfoIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getBookParagraph(null, ID);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetBookParagraphWhenParagraphIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getBookParagraph(bookInfo, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetBookParagraphWhenParagraphIdIsEmptyShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getBookParagraph(bookInfo, "");
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetBookItemWhenBookInfoIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getBookItem(null, ID);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetBookItemWhenItemIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getBookItem(bookInfo, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetBookItemWhenItemIdIsEmptyShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getBookItem(bookInfo, "");
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetBookEnemyWhenBookInfoIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getBookEnemy(null, ID);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetBookEnemyWhenEnemyIdIsNullShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getBookEnemy(bookInfo, null);
        // THEN throws exception
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetBookEnemyWhenEnemyIdIsEmptyShouldThrowException() {
        // GIVEN
        mockControl.replay();
        // WHEN
        underTest.getBookEnemy(bookInfo, "");
        // THEN throws exception
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
