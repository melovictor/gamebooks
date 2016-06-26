package hu.zagor.gamebooks.controller.content;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.books.contentstorage.BookContentStorage;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.exception.InvalidGatheredItemException;
import hu.zagor.gamebooks.exception.InvalidStepChoiceException;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.support.logging.LogInject;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

/**
 * Default implementation of the {@link BookContentInitializer} interface.
 * @author Tamas_Szekeres
 */
public class DefaultBookContentInitializer implements BookContentInitializer, BeanFactoryAware {

    private static final String INFO_NOT_NULL = "The parameter 'info' cannot be null!";
    private static final String PLAYER_NOT_NULL = "The parameter 'player' cannot be null!";
    @LogInject private Logger logger;
    private final BookContentStorage storage;
    private final GameStateHandler gameStateHandler;
    private BeanFactory beanFactory;

    /**
     * Basic constructor.
     * @param storage the book entry storage, cannot be null
     * @param gameStateHandler the game loader/saver, cannot be null
     */
    public DefaultBookContentInitializer(final BookContentStorage storage, final GameStateHandler gameStateHandler) {
        Assert.notNull(storage, "The parameter 'storage' cannot be null!");
        Assert.notNull(gameStateHandler, "The parameter 'gameStateHandler' cannot be null!");
        this.storage = storage;
        this.gameStateHandler = gameStateHandler;
    }

    @Override
    public void initModel(final Model model, final PlayerUser player, final BookInformations info) {
        Assert.notNull(model, "The parameter 'model' cannot be null!");
        Assert.notNull(player, PLAYER_NOT_NULL);
        Assert.notNull(info, INFO_NOT_NULL);

        final String series = info.getSeries();
        final String title = info.getTitle();

        model.addAttribute(ControllerAddresses.USER_STORE_KEY, player);
        model.addAttribute("pageTitle", series + " &ndash; " + title);

        final int playerId = player.getId();
        final Long bookId = info.getId();
        final SavedGameContainer container = (SavedGameContainer) beanFactory.getBean("savedGameContainer", playerId, bookId);
        final boolean hasSavedGame = gameStateHandler.checkSavedGame(container);

        model.addAttribute("haveSavedGame", hasSavedGame);

        final BookContentSpecification contentSpecification = info.getContentSpecification();

        model.addAttribute("hasInventory", contentSpecification.isInventoryAvailable());
        model.addAttribute("hasMap", contentSpecification.isMapAvailable());
    }

    @Override
    public Paragraph loadSection(final String paragraphId, final PlayerUser player, final Paragraph previousParagraph, final BookInformations info) {
        Assert.notNull(paragraphId, "The parameter 'paragraphId' cannot be null!");
        Assert.notNull(player, PLAYER_NOT_NULL);
        Assert.notNull(info, INFO_NOT_NULL);

        final Paragraph paragraph = storage.getBookParagraph(info, paragraphId);
        if (paragraph == null) {
            throw new IllegalStateException("We received a null paragraph from the book storage for paragraph ID '" + paragraphId + "'. Something is very wrong!");
        }
        markParagraphImages(paragraph, player.getSettings().getImageTypeOrder(), info);
        try {
            checkNewParagraphValidity(previousParagraph, paragraphId);
        } catch (final InvalidStepChoiceException exception) {
            logger.debug("Player tried to navigate to illegal section {}.", paragraphId);
            if (!player.isTester()) {
                throw exception;
            }
        }

        return paragraph;
    }

    /**
     * Marks the image sources with the query of either bw or col so the browser knows when it has to reload it.
     * @param paragraph the paragraph object
     * @param imageType the image type name
     * @param info the {@link BookInformations} object
     */
    protected void markParagraphImages(final Paragraph paragraph, final String imageType, final BookInformations info) {
        final ParagraphData data = paragraph.getData();
        String text = data.getText();
        text = text.replaceAll("(<img[^>]*?src=\"[^\"]*)", "$1?" + imageType);
        text = text.replaceAll("<p class=\"inlineImage\" data-img=\"",
            "<p class=\"inlineImage\" data-book=\"" + info.getResourceDir() + "\" data-type=\"" + imageType.charAt(0) + "\" data-img=\"");
        data.setText(text);
    }

    @Override
    public void validateItem(final GatheredLostItem glItem, final PlayerUser player, final Paragraph paragraph, final BookInformations info) {
        Assert.notNull(glItem, "Parameter 'glItem' can not be null!");
        Assert.notNull(player, PLAYER_NOT_NULL);
        Assert.notNull(paragraph, "The parameter 'paragraph' cannot be null!");
        Assert.notNull(info, INFO_NOT_NULL);

        try {
            checkGatheredItemValidity(paragraph, glItem);
        } catch (final InvalidGatheredItemException exception) {
            logger.debug("Player tried to collect item {}", glItem.getId());
            throw exception;
        }
    }

    private void checkNewParagraphValidity(final Paragraph previousParagraph, final String paragraphId) {
        if (validityCheckRequired(previousParagraph, paragraphId) && stepIsInvalid(previousParagraph, paragraphId)) {
            throw new InvalidStepChoiceException(previousParagraph.getId(), paragraphId);
        }
    }

    private boolean stepIsInvalid(final Paragraph previousParagraph, final String paragraphId) {
        return !previousParagraph.isValidMove(paragraphId);
    }

    private boolean validityCheckRequired(final Paragraph previousParagraph, final String paragraphId) {
        return previousParagraph != null && !isWelcomePage(paragraphId) && !isGeneratingPage(paragraphId) && !isBackgroundPage(paragraphId);
    }

    private boolean isBackgroundPage(final String paragraphId) {
        return BookParagraphConstants.BACKGROUND.getValue().equals(paragraphId);
    }

    private boolean isGeneratingPage(final String paragraphId) {
        return BookParagraphConstants.GENERATE.getValue().equals(paragraphId);
    }

    private boolean isWelcomePage(final String paragraphId) {
        return BookParagraphConstants.BACK_COVER.getValue().equals(paragraphId);
    }

    private void checkGatheredItemValidity(final Paragraph paragraph, final GatheredLostItem glItem) {
        if (!paragraph.isValidItem(glItem)) {
            throw new InvalidGatheredItemException(glItem.getId(), paragraph.getId());
        }
    }

    @Override
    public BookItemStorage getItemStorage(final BookInformations info) {
        Assert.notNull(info, INFO_NOT_NULL);
        return storage.getBookEntry(info);
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
