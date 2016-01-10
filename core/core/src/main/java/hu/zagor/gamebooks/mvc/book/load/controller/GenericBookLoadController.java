package hu.zagor.gamebooks.mvc.book.load.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.ContinuationData;
import hu.zagor.gamebooks.mvc.book.controller.AbstractSectionDisplayingController;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Class for handling the loading of the state of a given book.
 * @author Tamas_Szekeres
 */
public abstract class GenericBookLoadController extends AbstractSectionDisplayingController {

    @Autowired private GameStateHandler gameStateHandler;
    @LogInject private Logger logger;

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
     * Handles the loading of the previous character to start the current adventure with it.
     * @param request the http request
     * @param response the {@link HttpServletResponse}, cannot be null
     * @throws IOException occurs if the redirection fails
     */
    @RequestMapping(value = PageAddresses.BOOK_LOAD_PREVIOUS)
    public void handleLoadPrevious(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        logger.debug("GenericBookLoadController.loadPrevious");

        final ContinuationData continuationData = getInfo().getContinuationData();
        if (continuationData == null) {
            throw new UnsupportedOperationException("Continuation is not supported when continuation information is not provided.");
        }

        final HttpSessionWrapper wrapper = getWrapper(request);

        final PlayerUser player = wrapper.getPlayer();
        final int playerId = player.getId();
        final SavedGameContainer container = gameStateHandler.generateSavedGameContainer(playerId, continuationData.getPreviousBookId());
        gameStateHandler.loadGame(container);

        final Paragraph paragraph = (Paragraph) container.getElement(ControllerAddresses.PARAGRAPH_STORE_KEY);
        if (!continuationData.getPreviousBookLastSectionId().equals(paragraph.getId())) {
            throw new UnsupportedOperationException("Continuation is not supported when the character from the previous book is not staying on the finishing section.");
        }

        doLoadPrevious(request, response, container);
        response.sendRedirect(continuationData.getContinuationPageName());
    }

    /**
     * Abstract method for the rulesets and books to do custom load handling.
     * @param model the model, cannot be null
     * @param request the http request, cannot be null
     * @param savedGameContainer the container whose content will be saved, cannot be null
     * @return the view
     */
    protected abstract String doLoad(final Model model, final HttpServletRequest request, final SavedGameContainer savedGameContainer);

    /**
     * Abstract method for the rulesets and books to do custom load handling for continuing a previous book.
     * @param request the http request, cannot be null
     * @param response the {@link HttpServletResponse}, cannot be null
     * @param savedGameContainer the container whose content will be saved, cannot be null
     */
    protected abstract void doLoadPrevious(final HttpServletRequest request, HttpServletResponse response, final SavedGameContainer savedGameContainer);

    public void setGameStateHandler(final GameStateHandler gameStateHandler) {
        this.gameStateHandler = gameStateHandler;
    }

    /**
     * Method for setting up the {@link CharacterHandler} bean for the current run.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param characterHandler the {@link CharacterHandler} to set up
     */
    protected abstract void setUpCharacterHandler(final HttpSessionWrapper wrapper, final CharacterHandler characterHandler);
}
