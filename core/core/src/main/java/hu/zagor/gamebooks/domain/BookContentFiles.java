package hu.zagor.gamebooks.domain;

import org.springframework.util.Assert;

/**
 * Bean for storing the names of the files containing the actual data with absolute path in the classpaht.
 * @author Tamas_Szekeres
 *
 */
public class BookContentFiles {
    private final String paragraphs;
    private final String enemies;
    private final String items;

    /**
     * Basic constructor that sets the locations of the files.
     * @param paragraphs the file containing the paragraph informations, cannot be null
     * @param enemies the file containing the enemy informations
     * @param items the file containing the item informations
     */
    public BookContentFiles(final String paragraphs, final String enemies, final String items) {
        Assert.notNull(paragraphs, "Parameter 'paragraphs' can not be null!");
        this.paragraphs = paragraphs;
        this.enemies = enemies;
        this.items = items;
    }

    public String getParagraphs() {
        return paragraphs;
    }

    public String getEnemies() {
        return enemies;
    }

    public String getItems() {
        return items;
    }

}
