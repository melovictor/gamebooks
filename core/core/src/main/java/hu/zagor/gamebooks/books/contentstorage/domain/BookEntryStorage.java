package hu.zagor.gamebooks.books.contentstorage.domain;

import hu.zagor.gamebooks.books.contentstorage.CachedBookContentStorage;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.TrueCloneable;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.springframework.util.Assert;

/**
 * Bean to store books in the {@link CachedBookContentStorage}. It records the last access time to allow removal of unnecessary data.
 * @author Tamas_Szekeres
 */
public class BookEntryStorage implements BookItemStorage {

    @LogInject
    private Logger logger;
    private long lastAccess = System.currentTimeMillis();

    private final long bookId;
    private final Map<String, Paragraph> paragraphs;
    private final Map<String, Item> items;
    private final Map<String, Enemy> enemies;

    /**
     * Basic constructor that creates a new entry for the given {@link Map} containing the data for the book.
     * @param bookId the id of the book for which the current entries belong, cannot be null
     * @param paragraphs the map containing the data for the book, cannot be null
     * @param items the map containing the items of the book, can be null
     * @param enemies the map containing the enemies of the book, can be null
     */
    public BookEntryStorage(final long bookId, final Map<String, Paragraph> paragraphs, final Map<String, Item> items, final Map<String, Enemy> enemies) {
        Assert.notNull(bookId, "Parameter 'bookId' cannot be null!");
        Assert.notNull(paragraphs, "Parameter 'paragraphs' cannot be null!");
        Assert.notEmpty(paragraphs, "Parameter 'paragraphs' cannot be empty!");
        this.bookId = bookId;
        this.paragraphs = paragraphs;
        this.items = items;
        this.enemies = enemies;
    }

    @Override
    public Paragraph getParagraph(final String paragraphId) {
        Assert.notNull(paragraphId, "The parameter 'paragraphId' cannot be null!");
        return getObject(paragraphId, "paragraphId", "paragraph", paragraphs);
    }

    @Override
    public Item getItem(final String itemId) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        return getObject(itemId, "itemId", "item", items);
    }

    @Override
    public Enemy getEnemy(final String enemyId) {
        Assert.notNull(enemyId, "The parameter 'enemyId' cannot be null!");
        return getObject(enemyId, "enemyId", "enemy", enemies);
    }

    @SuppressWarnings("unchecked")
    private <T extends TrueCloneable> T getObject(final String id, final String idName, final String name, final Map<String, T> collection) {
        Assert.notNull(id, "Parameter '" + idName + "' cannot be null!");
        lastAccess = System.currentTimeMillis();
        T result = null;
        if (collection != null) {
            if (collection.containsKey(id)) {
                try {
                    result = (T) collection.get(id).clone();
                } catch (final CloneNotSupportedException e) {
                    logger.error("Failed to clone {} '{}' from book '{}'!", name, id, String.valueOf(bookId));
                }
            } else {
                logger.error("Cannot find {} with id '{}' in book '{}'!", name, id, String.valueOf(bookId));
            }
        }
        return result;
    }

    public long getLastAccess() {
        return lastAccess;
    }

    @Override
    public Map<String, Enemy> getEnemies() {
        final Map<String, Enemy> cloned = new HashMap<>();

        if (enemies != null) {
            for (final Entry<String, Enemy> entry : enemies.entrySet()) {
                try {
                    cloned.put(entry.getKey(), entry.getValue().clone());
                } catch (final CloneNotSupportedException e) {
                    logger.error("Failed to clone {} '{}' from book '{}'!", entry.getValue().getName(), entry.getKey(), String.valueOf(bookId));
                }
            }
        }

        return cloned;
    }

    @Override
    public String resolveParagraphId(final String paragraphId) {
        Assert.notNull(paragraphId, "The parameter 'paragraphId' cannot be null!");
        final Paragraph paragraph = paragraphs.get(paragraphId);
        String displayId = paragraphId;
        if (paragraph != null) {
            displayId = paragraph.getDisplayId();
        }
        return displayId;
    }

}
