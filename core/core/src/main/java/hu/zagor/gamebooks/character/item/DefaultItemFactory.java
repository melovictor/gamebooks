package hu.zagor.gamebooks.character.item;

import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.character.ItemFactory;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.exception.InvalidItemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link ItemFactory} interface.
 * @author Tamas_Szekeres
 */
public class DefaultItemFactory implements ItemFactory {

    private final BookInformations info;
    @Autowired
    private BookContentInitializer contentInitializer;

    /**
     * Basic constructor.
     * @param info the {@link BookInformations} bean to use later for getting the item storage
     */
    public DefaultItemFactory(final BookInformations info) {
        Assert.notNull(info, "The parameter 'info' cannot be null!");
        this.info = info;
    }

    @Override
    public Item resolveItem(final String itemId) {
        Assert.notNull(itemId, "The parameter 'itemId' cannot be null!");
        Assert.isTrue(itemId.length() > 0, "The parameter 'itemId' cannot be empty!");
        final BookItemStorage storage = contentInitializer.getItemStorage(info);
        final Item item = storage.getItem(itemId);
        if (item == null) {
            throw new InvalidItemException(itemId);
        }
        return item;
    }

}
