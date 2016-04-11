package hu.zagor.gamebooks.mvc.book.save.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.bookinfo.BookInformationFetcher;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Class for handling the saving of the state of a given book.
 * @author Tamas_Szekeres
 */
@Controller
public class GenericBookSaveController extends AbstractRequestWrappingController {

    @Autowired private GameStateHandler gameStateHandler;
    @Autowired private BookInformationFetcher fetcher;

    /**
     * Handles the saving of the current game state.
     * @param request the http request
     * @param bookId the id of the book
     */
    @RequestMapping(value = PageAddresses.BOOK_PAGE + "/{bookId}/s/" + PageAddresses.BOOK_SAVE)
    @ResponseBody
    public final void handleSave(final HttpServletRequest request, @PathVariable("bookId") final String bookId) {
        Assert.notNull(request, "The parameter 'request' cannot be null!");
        Assert.notNull(bookId, "The parameter 'bookId' cannot be null!");

        final HttpSessionWrapper wrapper = getWrapper(request);

        final SavedGameContainer container = gameStateHandler.generateSavedGameContainer(wrapper.getPlayer().getId(), fetcher.getInfoById(bookId).getId());
        doSave(wrapper, container);
        gameStateHandler.saveGame(container);
    }

    private void doSave(final HttpSessionWrapper wrapper, final SavedGameContainer container) {
        container.addElement(ControllerAddresses.PARAGRAPH_STORE_KEY, wrapper.getParagraph());
        container.addElement(ControllerAddresses.ENEMY_STORE_KEY, wrapper.getEnemies());
        container.addElement(ControllerAddresses.CHARACTER_STORE_KEY, wrapper.getCharacter());
    }

    public void setGameStateHandler(final GameStateHandler gameStateHandler) {
        this.gameStateHandler = gameStateHandler;
    }

}
