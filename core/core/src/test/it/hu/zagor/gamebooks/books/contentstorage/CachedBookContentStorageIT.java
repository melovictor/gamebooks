package hu.zagor.gamebooks.books.contentstorage;

import static org.easymock.EasyMock.expect;
import hu.zagor.gamebooks.books.contentstorage.domain.BookEntryStorage;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Integration test for class {@link CachedBookContentStorage}.
 * @author Tamas_Szekeres
 */
@Test
public class CachedBookContentStorageIT {

    private static final String TITLE = "title";
    private CachedBookContentStorage underTest;
    private IMocksControl mockControl;
    private BookContentLoader bookContentLoader;
    private Logger logger;
    private BookInformations bookInfo;
    private BookItemStorage entryStorage;
    private EnvironmentDetector environmentDetector;

    @BeforeClass
    public void setUpClass() {
        mockControl = EasyMock.createStrictControl();
        bookContentLoader = mockControl.createMock(BookContentLoader.class);
        logger = mockControl.createMock(Logger.class);
        entryStorage = mockControl.createMock(BookItemStorage.class);
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

    public void testGetBookEntryWhenStorageDoesContainCachedEntryButSoftReferenceIsDestroyedShouldReloadCachedInstance() {
        // GIVEN
        final SoftReference<BookItemStorage> softReference = injectInitialMapToUnderTest();
        expect(environmentDetector.isDevelopment()).andReturn(false);
        logger.info("Soft reference has been destroyed for book '{}', reloading it from file.", TITLE);
        expect(bookContentLoader.loadBookContent(bookInfo)).andReturn(entryStorage);
        boolean softReferenceCleared = false;
        mockControl.replay();
        // WHEN
        BookItemStorage returned = null;
        try {
            final List<BookItemStorage> store = new ArrayList<>();
            while (!softReferenceCleared) {
                store.add(new HugeBookEntryStorage());
                softReferenceCleared = checkSoftReferenceEmptied(softReference);
            }
        } catch (final OutOfMemoryError exception) {
            throw new IllegalStateException("Reached an out of memory exception before the soft reference was freed.");
        }
        returned = underTest.getBookEntry(bookInfo);
        // THEN
        Assert.assertSame(returned, entryStorage);
    }

    private boolean checkSoftReferenceEmptied(final SoftReference<BookItemStorage> softReference) {
        return softReference.get() == null;
    }

    private SoftReference<BookItemStorage> injectInitialMapToUnderTest() {
        final HugeBookEntryStorage basicStorage = new HugeBookEntryStorage();
        final Map<Long, SoftReference<BookItemStorage>> storage = new HashMap<>();
        final SoftReference<BookItemStorage> softReference = new SoftReference<BookItemStorage>(basicStorage);
        storage.put(bookInfo.getId(), softReference);
        Whitebox.setInternalState(underTest, "storage", storage);
        return softReference;
    }

    @AfterMethod
    public void tearDownMethod() {
        mockControl.verify();
    }

    private Map<String, Paragraph> getParagraphs() {
        final Map<String, Paragraph> paragraphs = new HashMap<>();
        paragraphs.put("1", null);
        return paragraphs;
    }

    private class HugeBookEntryStorage extends BookEntryStorage {

        private final byte[] hugeField = new byte[1024 * 1024 * 300];

        public HugeBookEntryStorage() {
            super(11L, getParagraphs(), null, null);
            getHugeField();
        }

        public byte[] getHugeField() {
            return hugeField;
        }
    }
}
