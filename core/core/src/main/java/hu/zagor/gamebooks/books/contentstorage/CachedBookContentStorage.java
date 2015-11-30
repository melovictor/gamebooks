package hu.zagor.gamebooks.books.contentstorage;

import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.support.environment.EnvironmentDetector;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Simple bean for storing the parsed contents of the books.
 * @author Tamas_Szekeres
 */
public class CachedBookContentStorage implements BookContentStorage {

    private static final String ITEMID_NOT_EMPTY = "Parameter 'itemId' can not be empty!";
    private static final String ITEMID_NOT_NULL = "Parameter 'itemId' can not be null!";
    private static final String INFO_NOT_NULL = "Parameter 'info' can not be null!";
    private static final String BOOKINFO_NOT_NULL = "Parameter 'bookInfo' can not be null!";
    @LogInject private Logger logger;
    private final Map<Long, SoftReference<BookItemStorage>> storage = new HashMap<>();
    private final BookContentLoader bookContentLoader;

    @Autowired private EnvironmentDetector environmentDetector;

    /**
     * Basic constructor that creates a new {@link CachedBookContentStorage} with the given {@link BookContentLoader} bean to actually load data from the resource files.
     * @param bookContentLoader the bean for loading data from xml, cannot be null
     */
    public CachedBookContentStorage(final BookContentLoader bookContentLoader) {
        Assert.notNull(bookContentLoader, "Parameter 'bookContentLoader' can not be null!");
        this.bookContentLoader = bookContentLoader;
    }

    @Override
    public Paragraph getBookParagraph(final BookInformations bookInfo, final String paragraphId) {
        Assert.notNull(bookInfo, BOOKINFO_NOT_NULL);
        Assert.notNull(paragraphId, "Parameter 'paragraphId' can not be null!");
        Assert.isTrue(paragraphId.length() > 0, "Parameter 'paragraphId' can not be empty!");

        Paragraph paragraph = null;
        final BookItemStorage storageEntry = doGetBookEntry(bookInfo);
        if (storageEntry != null) {
            paragraph = storageEntry.getParagraph(paragraphId);
        } else {
            logger.error("Cannot load contents for book '{}'!", bookInfo.getTitle());
        }
        return paragraph;
    }

    @Override
    public Item getBookItem(final BookInformations info, final String itemId) {
        Assert.notNull(info, INFO_NOT_NULL);
        Assert.notNull(itemId, ITEMID_NOT_NULL);
        Assert.isTrue(itemId.length() > 0, ITEMID_NOT_EMPTY);

        Item item = null;
        final BookItemStorage storageEntry = getBookEntry(info);
        if (storageEntry != null) {
            item = storageEntry.getItem(itemId);
            logger.debug("Found item {}: '{}' in book {}.", itemId, item.getName(), info.getId());
        } else {
            logger.error("Cannot load items for book '{}'!", info.getTitle());
        }
        return item;
    }

    @Override
    public Enemy getBookEnemy(final BookInformations info, final String enemyId) {
        Assert.notNull(info, INFO_NOT_NULL);
        Assert.notNull(enemyId, ITEMID_NOT_NULL);
        Assert.isTrue(enemyId.length() > 0, ITEMID_NOT_EMPTY);

        Enemy enemy = null;
        final BookItemStorage storageEntry = getBookEntry(info);
        if (storageEntry != null) {
            enemy = storageEntry.getEnemy(enemyId);
        } else {
            logger.error("Cannot load enemies for book '{}'!", info.getTitle());
        }
        return enemy;
    }

    @Override
    public BookItemStorage getBookEntry(final BookInformations bookInfo) {
        Assert.notNull(bookInfo, BOOKINFO_NOT_NULL);
        return doGetBookEntry(bookInfo);
    }

    private BookItemStorage doGetBookEntry(final BookInformations bookInfo) {
        final SoftReference<BookItemStorage> storageReference = storage.get(bookInfo.getId());
        BookItemStorage storageEntry = null;
        if (storageReference == null || environmentDetector.isDevelopment()) {
            final String message = "Cannot find cached content for book '{}', loading it from file.";
            storageEntry = reloadBookEntry(bookInfo, message);
        } else {
            storageEntry = storageReference.get();
            if (storageEntry == null) {
                final String message = "Soft reference has been destroyed for book '{}', reloading it from file.";
                storageEntry = reloadBookEntry(bookInfo, message);
            }
        }
        return storageEntry;
    }

    private BookItemStorage reloadBookEntry(final BookInformations bookInfo, final String message) {
        BookItemStorage storageEntry;
        logger.info(message, bookInfo.getTitle());
        storageEntry = loadBook(bookInfo);
        return storageEntry;
    }

    private BookItemStorage loadBook(final BookInformations bookInfo) {
        final BookItemStorage loadedContent = bookContentLoader.loadBookContent(bookInfo);
        if (loadedContent != null) {
            storage.put(bookInfo.getId(), new SoftReference<BookItemStorage>(loadedContent));
        }
        return loadedContent;
    }

}
