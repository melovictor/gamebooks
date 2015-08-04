package hu.zagor.gamebooks.books.contentstorage;

import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.domain.BookInformations;

/**
 * Interface for a storage from which book paragraphs can be requested by using book ids and paragraph ids.
 * @author Tamas_Szekeres
 */
public interface BookContentStorage {

    /**
     * Gets a copy of the given paragraph from the given book.
     * @param info the {@link BookInformations} bean for the book from which the paragraph is requested,
     * cannot be null
     * @param paragraphId the id of the paragraph that is requested, cannot be null
     * @return the deep copy of the paragraph from the book, or null if either the book or the paragraph
     * cannot be found and cannot be retrieved
     */
    Paragraph getBookParagraph(BookInformations info, String paragraphId);

    /**
     * Gets a copy of the given item from the given book.
     * @param info the {@link BookInformations} bean for the book from which the item is requested, cannot be
     * null
     * @param itemId the id of the item that is requested, cannot be null
     * @return the deep copy of the item from the book, or null if either the book or the item cannot be found
     * and cannot be retrieved
     */
    Item getBookItem(BookInformations info, String itemId);

    /**
     * Gets a copy of the given enemy from the given book.
     * @param info the {@link BookInformations} bean for the book from which the enemy is requested, cannot be
     * null
     * @param enemyId the id of the enemy that is requested, cannot be null
     * @return the deep copy of the enemy from the book, or null if either the book or the enemy cannot be
     * found and cannot be retrieved
     */
    Enemy getBookEnemy(BookInformations info, String enemyId);

    /**
     * Returns the underlying {@link BookItemStorage} bean for the given book.
     * @param bookInfo the {@link BookInformations} bean for identifying the appropriate
     * {@link BookItemStorage}
     * @return the retrieved {@link BookItemStorage}
     */
    BookItemStorage getBookEntry(BookInformations bookInfo);

}
