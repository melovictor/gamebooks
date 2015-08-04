package hu.zagor.gamebooks.mvc.book.load.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.controller.AbstractSectionDisplayingController;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.logging.LogInject;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Class for handling the loading of the state of a given book.
 * @author Tamas_Szekeres
 */
public abstract class GenericBookLoadController extends AbstractSectionDisplayingController {

    @Autowired
    private GameStateHandler gameStateHandler;
    @LogInject
    private Logger logger;

    /**
     * Handles the loading of an old game state.
     * @param model the model
     * @param request the http request
     * @return the appropriate view
     */
    @RequestMapping(value = PageAddresses.BOOK_LOAD)
    public String handleLoad(final Model model, final HttpServletRequest request) {
        logger.debug("GenericBookLoadController.load");
        final HttpSessionWrapper wrapper = getWrapper(request);

        final PlayerUser player = wrapper.getPlayer();
        final int playerId = player.getId();
        final Long bookId = getInfo().getId();
        final SavedGameContainer container = gameStateHandler.generateSavedGameContainer(playerId, bookId);
        gameStateHandler.loadGame(container);
        return doLoad(model, request, container);
    }

    /**
     * Abstract method for the rulesets and books to do custom load handling.
     * @param model the model, cannot be null
     * @param request the http request, cannot be null
     * @param savedGameContainer the container whose content will be saved, cannot be null
     * @return the view
     */
    protected abstract String doLoad(final Model model, final HttpServletRequest request, final SavedGameContainer savedGameContainer);

    public void setGameStateHandler(final GameStateHandler gameStateHandler) {
        this.gameStateHandler = gameStateHandler;
    }

}
