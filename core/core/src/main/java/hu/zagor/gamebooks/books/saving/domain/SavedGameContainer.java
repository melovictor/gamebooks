package hu.zagor.gamebooks.books.saving.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Bean for storing saved games. This bean contains the userId, the book's id and any other data the book
 * deems as important for saving.
 * @author Tamas_Szekeres
 */
@Component("savedGameContainer")
@Scope("prototype")
public class SavedGameContainer {

    private final Integer userId;
    private final Long bookId;

    private final Map<String, Object> savedElements = new HashMap<>();

    /**
     * Basic constructor to create a bean with a proper userId and book id.
     * @param userId the id of the user to whom this saved state belongs, cannot be null
     * @param bookId the id of the book for which this saved state belongs, cannot be null
     */
    public SavedGameContainer(final Integer userId, final Long bookId) {
        Assert.notNull(userId, "The parameter 'userId' cannot be null!");
        Assert.notNull(bookId, "The parameter 'bookId' cannot be null!");
        this.userId = userId;
        this.bookId = bookId;
    }

    /**
     * Adds a new element that has to be saved in order to be able to successfully restore the saved game.
     * @param elementKey a key with which the book can reclaim this, cannot be null
     * @param elementValue a (hopefully {@link Serializable}) element to be saved, cannot be null
     */
    public void addElement(final String elementKey, final Object elementValue) {
        Assert.notNull(elementKey, "The parameter 'elementKey' cannot be null!");
        Assert.notNull(elementValue, "The parameter 'elementValue' cannot be null!");
        savedElements.put(elementKey, elementValue);
    }

    /**
     * Retrieves a previously saved element using the given key.
     * @param elementKey the key of the element, cannot be null
     * @return the previously saved element, or null if an element with the given name hasn't been saved
     * previously
     */
    public Object getElement(final String elementKey) {
        Assert.notNull(elementKey, "The parameter 'elementKey' cannot be null!");
        return savedElements.get(elementKey);
    }

    public Integer getUserId() {
        return userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public Map<String, Object> getSavedElements() {
        return savedElements;
    }

    /**
     * Adds the newly deserialized elements into the current stash.
     * @param savedElements the deserialized elements to use
     */
    public void addElements(final Map<String, Object> savedElements) {
        this.savedElements.putAll(savedElements);
    }

}
