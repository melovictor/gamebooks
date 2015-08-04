package hu.zagor.gamebooks.raw.mvc.book.save.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.save.controller.GenericBookSaveController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;

/**
 * Generic save controller for books with no rule system.
 * @author Tamas_Szekeres
 */
public class RawBookSaveController extends GenericBookSaveController {

    @Override
    protected void doSave(final HttpServletRequest request, final SavedGameContainer container) {
        Assert.notNull(request, "The parameter 'request' cannot be null!");
        Assert.notNull(container, "The parameter 'container' cannot be null!");

        final HttpSessionWrapper wrapper = getWrapper(request);
        container.addElement(ControllerAddresses.PARAGRAPH_STORE_KEY, wrapper.getParagraph());
        container.addElement(ControllerAddresses.ENEMY_STORE_KEY, wrapper.getEnemies());
        container.addElement(ControllerAddresses.CHARACTER_STORE_KEY, wrapper.getCharacter());
    }

}
