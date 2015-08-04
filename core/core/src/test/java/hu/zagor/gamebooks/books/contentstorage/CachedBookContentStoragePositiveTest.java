package hu.zagor.gamebooks.books.contentstorage;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.character.item.ItemType;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

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
 * Unit test for class {@link CachedBookContentStorage}.
 * @author Tamas_Szekeres
 */
@Test
public class CachedBookContentStoragePositiveTest {

    private static final String ID = "10";
    private static final String TITLE = "title";
    private CachedBookContentStorage underTest;
    private IMocksControl mockControl;
    private BookContentLoader bookContentLoader;
    private Logger logger;
    private BookInformations bookInfo;
    private BookItemStorage entryStorage;
    private Paragraph paragraph;
    private Item item;
    private Enemy enemy;
    private EnvironmentDetector environmentDetector;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        bookContentLoader = mockControl.createMock(BookContentLoader.class);
        logger = mockControl.createMock(Logger.class);
        entryStorage = mockControl.createMock(BookItemStorage.class);
        paragraph = mockControl.createMock(Paragraph.class);
        environmentDetector = mockControl.createMock(EnvironmentDetector.class);

        item = new Item("111", "item", ItemType.common);
        enemy = new Enemy();

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

    public void testGetBookEntryWhenStorageDoesNotContainCachedEntryShouldLoadBookContentAndPutInCache() {
        // GIVEN
        logger.info("Cannot find cached content for book '{}', loading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(entryStorage);
        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.getBookEntry(bookInfo);
        // THEN
        Assert.assertSame(returned, entryStorage);
    }

    public void testGetBookEntryWhenStorageDoesNotContainCachedEntryAndCannotLoadItFromFileShouldReturnNull() {
        // GIVEN
        logger.info("Cannot find cached content for book '{}', loading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(null);
        mockControl.replay();
        // WHEN
        final BookItemStorage returned = underTest.getBookEntry(bookInfo);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetBookEntryWhenStorageDoesContainCachedEntryAndNotInDevelopmentModeShouldReturnCachedInstance() {
        // GIVEN
        logger.info("Cannot find cached content for book '{}', loading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(entryStorage);
        expect(environmentDetector.isDevelopment()).andReturn(false);
        mockControl.replay();
        underTest.getBookEntry(bookInfo);
        // WHEN
        final BookItemStorage returned = underTest.getBookEntry(bookInfo);
        // THEN
        Assert.assertSame(returned, entryStorage);
    }

    public void testGetBookEntryWhenStorageDoesContainCachedEntryAndInDevelopmentModeShouldReloadCachedInstance() {
        // GIVEN
        logger.info("Cannot find cached content for book '{}', loading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(entryStorage);
        expect(environmentDetector.isDevelopment()).andReturn(true);
        logger.info("Cannot find cached content for book '{}', loading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(entryStorage);
        mockControl.replay();
        underTest.getBookEntry(bookInfo);
        // WHEN
        final BookItemStorage returned = underTest.getBookEntry(bookInfo);
        // THEN
        Assert.assertSame(returned, entryStorage);
    }

    public void testGetBookParagraphWhenEntryStorageCanBeRetrievedShouldReturnParagraph() {
        // GIVEN
        logger.info("Cannot find cached content for book '{}', loading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(entryStorage);
        expect(entryStorage.getParagraph(ID)).andReturn(paragraph);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.getBookParagraph(bookInfo, ID);
        // THEN
        Assert.assertSame(returned, paragraph);
    }

    public void testGetBookParagraphWhenEntryStorageCannotBeRetrievedShouldReturnNull() {
        // GIVEN
        logger.info("Cannot find cached content for book '{}', loading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(null);
        logger.error("Cannot load contents for book '{}'!", TITLE);
        mockControl.replay();
        // WHEN
        final Paragraph returned = underTest.getBookParagraph(bookInfo, ID);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetBookItemWhenEntryStorageCanBeRetrievedShouldReturnItem() {
        // GIVEN
        logger.info("Cannot find cached content for book '{}', loading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(entryStorage);
        expect(entryStorage.getItem(ID)).andReturn(item);
        mockControl.replay();
        // WHEN
        final Item returned = underTest.getBookItem(bookInfo, ID);
        // THEN
        Assert.assertSame(returned, item);
    }

    public void testGetBookItemWhenEntryStorageCannotBeRetrievedShouldReturnNull() {
        // GIVEN
        logger.info("Cannot find cached content for book '{}', loading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(null);
        logger.error("Cannot load items for book '{}'!", TITLE);
        mockControl.replay();
        // WHEN
        final Item returned = underTest.getBookItem(bookInfo, ID);
        // THEN
        Assert.assertNull(returned);
    }

    public void testGetBookEnemyWhenEntryStorageCanBeRetrievedShouldReturnEnemy() {
        // GIVEN
        logger.info("Cannot find cached content for book '{}', loading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(entryStorage);
        expect(entryStorage.getEnemy(ID)).andReturn(enemy);
        mockControl.replay();
        // WHEN
        final Enemy returned = underTest.getBookEnemy(bookInfo, ID);
        // THEN
        Assert.assertSame(returned, enemy);
    }

    public void testGetBookEnemyWhenEntryStorageCannotBeRetrievedShouldReturnNull() {
        // GIVEN
        logger.info("Cannot find cached content for book '{}', loading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(null);
        logger.error("Cannot load enemies for book '{}'!", TITLE);
        mockControl.replay();
        // WHEN
        final Enemy returned = underTest.getBookEnemy(bookInfo, ID);
        // THEN
        Assert.assertNull(returned);
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

}
