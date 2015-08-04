package hu.zagor.gamebooks.mvc.book.save.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Class for handling the saving of the state of a given book.
 * @author Tamas_Szekeres
 */
public abstract class GenericBookSaveController extends AbstractRequestWrappingController {

    @Autowired
    private GameStateHandler gameStateHandler;

    /**
     * Handles the saving of the current game state.
     * @param request the http request
     */
    @RequestMapping(value = PageAddresses.BOOK_SAVE)
    @ResponseBody
    public void handleSave(final HttpServletRequest request) {
        Assert.notNull(request, "The parameter 'request' cannot be null!");

        final HttpSessionWrapper wrapper = getWrapper(request);

        final SavedGameContainer container = gameStateHandler.generateSavedGameContainer(wrapper.getPlayer().getId(), getInfo().getId());
        doSave(request, container);
        gameStateHandler.saveGame(container);
    }

    /**
     * Abstract method for the rulesets and books to do custom save handling.
     * @param request the http request, cannot be null
     * @param savedGameContainer the container whose content will be saved, cannot be null
     */
    protected abstract void doSave(final HttpServletRequest request, final SavedGameContainer savedGameContainer);

    public void setGameStateHandler(final GameStateHandler gameStateHandler) {
        this.gameStateHandler = gameStateHandler;
    }

}
