package hu.zagor.gamebooks.mvc.book.load.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
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
    public final String handleLoad(final Model model, final HttpServletRequest request) {
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
     * Handles the loading of the character from a saved game of the previous book to start the current adventure with it.
     * @param request the http request
     * @param response the {@link HttpServletResponse}, cannot be null
     * @throws IOException occurs if the redirection fails
     */
    @RequestMapping(value = PageAddresses.BOOK_LOAD_PREVIOUS)
    public final void handleLoadPrevious(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        logger.debug("GenericBookLoadController.loadPrevious");

        final ContinuationData continuationData = getInfo().getContinuationData();
        if (continuationData == null) {
            throw new IllegalStateException("Continuation is not supported when continuation information is not provided.");
        }

        final HttpSessionWrapper wrapper = getWrapper(request);

        final PlayerUser player = wrapper.getPlayer();
        final int playerId = player.getId();
        final SavedGameContainer container = gameStateHandler.generateSavedGameContainer(playerId, continuationData.getPreviousBookId());
        gameStateHandler.loadGame(container);

        final Paragraph paragraph = (Paragraph) container.getElement(ControllerAddresses.PARAGRAPH_STORE_KEY);
        if (!continuationData.getPreviousBookLastSectionId().equals(paragraph.getId())) {
            throw new IllegalStateException("Continuation is not supported when the character from the previous book is not staying on the finishing section.");
        }

        doLoadPrevious(wrapper, container);
        prepareNextChoice(wrapper, continuationData);
        final Character character = (Character) container.getElement(ControllerAddresses.CHARACTER_STORE_KEY);
        character.getParagraphs().clear();
        response.sendRedirect(continuationData.getContinuationPageName());
    }

    /**
     * Handles the loading of the previous character of the previous book from memory to start the current adventure with it.
     * @param request the http request
     * @param response the {@link HttpServletResponse}, cannot be null
     * @throws IOException occurs if the redirection fails
     */
    @RequestMapping(value = PageAddresses.BOOK_CONTINUE_PREVIOUS)
    public void continueWithPrevious(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        logger.debug("GenericBookLoadController.continueWithPrevious");

        final ContinuationData continuationData = getInfo().getContinuationData();
        if (continuationData == null) {
            throw new IllegalStateException("Continuation is not supported when continuation information is not provided.");
        }

        final Paragraph paragraph = (Paragraph) request.getSession().getAttribute(ControllerAddresses.PARAGRAPH_STORE_KEY + continuationData.getPreviousBookId());
        if (!continuationData.getPreviousBookLastSectionId().equals(paragraph.getId())) {
            throw new IllegalStateException("Continuation is not supported when the character from the previous book is not staying on the finishing section.");
        }

        final Character character = (Character) request.getSession().getAttribute(ControllerAddresses.CHARACTER_STORE_KEY + continuationData.getPreviousBookId());
        if (character == null) {
            throw new IllegalStateException("Couldn't find character for previous book with which continuation would have been possible.");
        }
        final HttpSessionWrapper wrapper = getWrapper(request);
        wrapper.setCharacter(character);
        character.getParagraphs().clear();

        doContinuePrevious(wrapper);
        prepareNextChoice(wrapper, continuationData);
        response.sendRedirect(continuationData.getContinuationPageName());
    }

    private void prepareNextChoice(final HttpSessionWrapper wrapper, final ContinuationData continuationData) {
        Paragraph paragraph = wrapper.getParagraph();
        if (paragraph == null) {
            paragraph = getDummyParagraph();
            wrapper.setParagraph(paragraph);
        }
        final ChoiceSet choices = paragraph.getData().getChoices();
        choices.add(new Choice(continuationData.getContinuationPageName().substring(2), null, -1, null));
        paragraph.calculateValidEvents();
    }

    private Paragraph getDummyParagraph() {
        final Paragraph dummy = getBeanFactory().getBean(Paragraph.class);
        dummy.setData(getBeanFactory().getBean("paragraphData", ParagraphData.class));
        return dummy;
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
     * Abstract method for the rulesets and books to do custom load handling for continuing a previous book from a saved game.
     * @param wrapper the {@link HttpSessionWrapper}, cannot be null
     * @param savedGameContainer the container whose content will be saved, cannot be null
     */
    protected abstract void doLoadPrevious(HttpSessionWrapper wrapper, final SavedGameContainer savedGameContainer);

    /**
     * Abstract method for the rulesets and books to do custom load handling for continuing a previous book from memory.
     * @param wrapper the {@link HttpSessionWrapper}, cannot be null
     */
    protected abstract void doContinuePrevious(HttpSessionWrapper wrapper);

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
