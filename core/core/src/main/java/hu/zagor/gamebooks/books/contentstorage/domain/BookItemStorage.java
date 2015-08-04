package hu.zagor.gamebooks.books.contentstorage.domain;

import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;

import java.util.Map;

/**
 * Interface for accessing the items of a {@link BookEntryStorage} bean.
 * @author Tamas_Szekeres
 */
public interface BookItemStorage {

    /**
     * Returns a copy of the paragraph of the current book specified by the parameter.
     * @param paragraphId the id of the paragraph to return, cannot be null
     * @return a copy of the {@link Paragraph} bean, or null if the specified paragraphId doesn't exists in the current {@link BookEntryStorage} bean
     */
    Paragraph getParagraph(final String paragraphId);

    /**
     * Returns a copy of the item of the current book specified by the parameter.
     * @param itemId the id of the item to return, cannot be null
     * @return a copy of the {@link Item} bean, or null if the specified itemId doesn't exists in the current {@link BookEntryStorage} bean
     */
    Item getItem(String itemId);

    /**
     * Returns a copy of the enemy of the current book specified by the parameter.
     * @param enemyId the id of the enemy to return, cannot be null
     * @return a copy of the {@link Enemy} bean, or null if the specified enemyId doesn't exists in the current {@link BookEntryStorage} bean
     */
    Enemy getEnemy(String enemyId);

    /**
     * Returns a copy of all the enemies from the current book.
     * @return a copy of the map of enemies.
     */
    Map<String, Enemy> getEnemies();

    /**
     * Resolves the id of a paragraph into a display id.
     * @param paragraphId the paragraph id to resolve
     * @return the display id
     */
    String resolveParagraphId(String paragraphId);

}
