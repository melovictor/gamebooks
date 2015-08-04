package hu.zagor.gamebooks.domain;

import hu.zagor.gamebooks.books.contentransforming.enemy.BookEnemyTransformer;
import hu.zagor.gamebooks.books.contentransforming.item.BookItemTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.BookParagraphTransformer;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.item.Item;
import hu.zagor.gamebooks.content.Paragraph;

import org.springframework.util.Assert;
import org.w3c.dom.Document;

/**
 * Bean for storing the transformers that are capable of transforming the {@link Document} beans into
 * meaningful domain objects.
 * @author Tamas_Szekeres
 */
public class BookContentTransformers {

    private final BookParagraphTransformer paragraphTransformer;
    private final BookItemTransformer itemTransformer;
    private final BookEnemyTransformer enemyTransformer;

    /**
     * Basic constructor.
     * @param paragraphTransformer the transformer that creates the {@link Paragraph} objects, cannot be null
     * @param itemTransformer the transformer that creates the {@link Item} objects, can be null
     * @param enemyTransformer the transformer that creates the {@link Enemy} objects, can be null
     */
    public BookContentTransformers(final BookParagraphTransformer paragraphTransformer, final BookItemTransformer itemTransformer,
            final BookEnemyTransformer enemyTransformer) {
        Assert.notNull(paragraphTransformer, "Parameter 'paragraphTransformer' can not be null!");
        this.paragraphTransformer = paragraphTransformer;
        this.itemTransformer = itemTransformer;
        this.enemyTransformer = enemyTransformer;
    }

    public BookParagraphTransformer getParagraphTransformer() {
        return paragraphTransformer;
    }

    public BookItemTransformer getItemTransformer() {
        return itemTransformer;
    }

    public BookEnemyTransformer getEnemyTransformer() {
        return enemyTransformer;
    }

}
