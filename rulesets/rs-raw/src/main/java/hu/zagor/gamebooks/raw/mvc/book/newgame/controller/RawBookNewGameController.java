package hu.zagor.gamebooks.raw.mvc.book.newgame.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.books.random.ReplayingNumberGenerator;
import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookContentSpecification;
import hu.zagor.gamebooks.mvc.book.newgame.controller.AbstractNewGameController;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import hu.zagor.gamebooks.raw.mvc.book.controller.CharacterPageDisplayingController;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Generic new game handling controller for books with no rule system.
 * @author Tamas_Szekeres
 */
public class RawBookNewGameController extends AbstractNewGameController implements CharacterPageDisplayingController {

    @Autowired private BookContentInitializer contentInitializer;
    @Autowired private GameStateHandler gameStateHandler;

    /**
     * Redirects the reader to the background page.
     * @param request the {@link HttpServletRequest} object
     * @param model the model
     * @param locale the used locale
     * @return the redirection command
     */
    @RequestMapping(value = PageAddresses.BOOK_NEW)
    public final String handleNew(final HttpServletRequest request, final Model model, final Locale locale) {
        model.addAttribute("isNewScreen", true);
        return doHandleNew(request, model, locale);
    }

    /**
     * Redirects the reader to the background page.
     * @param request the {@link HttpServletRequest} object
     * @param model the model
     * @param locale the used locale
     * @return the redirection command
     */
    protected String doHandleNew(final HttpServletRequest request, final Model model, final Locale locale) {
        final BookItemStorage storage = contentInitializer.getItemStorage(getInfo());
        final CharacterHandler characterHandler = getInfo().getCharacterHandler();
        final Character c = getCharacter(locale);
        final HttpSessionWrapper wrapper = getWrapper(request);
        wrapper.setCharacter(c);
        wrapper.setEnemies(storage.getEnemies());

        setUpCharacterHandler(wrapper, characterHandler);

        final PlayerUser player = wrapper.getPlayer();
        final Paragraph paragraph = contentInitializer.loadSection(getStarterParagraph().getValue(), player, wrapper.getParagraph(), getInfo());
        wrapper.setParagraph(paragraph);
        paragraph.calculateValidEvents();

        final BookContentSpecification contentSpecification = getInfo().getContentSpecification();

        model.addAttribute("hasInventory", contentSpecification.isInventoryAvailable());
        model.addAttribute("hasMap", contentSpecification.isMapAvailable());
        model.addAttribute("haveSavedGame", gameStateHandler.checkSavedGame(player.getId(), getInfo().getId()));

        model.addAttribute("data", getCharacterPageData(c));
        model.addAttribute("bookInfo", getInfo());

        setUpSectionDisplayOptions(paragraph, model, player);

        final String series = getInfo().getSeries();
        final String title = getInfo().getTitle();
        model.addAttribute("pageTitle", series + " &ndash; " + title);
        addJsResource(model, "raw");
        addCssResource(model, "raw");
        markParagraphImages(paragraph, player.getSettings().getImageTypeOrder());

        return "rawSection";
    }

    /**
     * Redirects the reader to the background page.
     * @param request the {@link HttpServletRequest} object
     * @param model the model
     * @param locale the used locale
     * @param randomInit the numbers with which the random number generator should be initialized
     * @return the redirection command
     */
    @RequestMapping(value = PageAddresses.BOOK_NEW + "-{randomInit}")
    public final String handleNewWithRandomInit(final HttpServletRequest request, final Model model, final Locale locale,
        @PathVariable("randomInit") final String randomInit) {
        final String[] delimitedListToStringArray = StringUtils.delimitedListToStringArray(randomInit, ",");
        final List<Integer> intValues = new ArrayList<>();
        for (final String value : delimitedListToStringArray) {
            intValues.add(Integer.valueOf(value));
        }
        getBeanFactory().getBean("d6", ReplayingNumberGenerator.class).setUpThrowResultQueue(intValues);
        return handleNew(request, model, locale);
    }

    @Override
    protected void setUpCharacterHandler(final HttpSessionWrapper wrapper, final CharacterHandler characterHandler) {
    }

    @Override
    public RawCharacterPageData getCharacterPageData(final Character character) {
        return (RawCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character);
    }

    /**
     * Returns the starter {@link BookParagraphConstants}.
     * @return the starter constant
     */
    protected BookParagraphConstants getStarterParagraph() {
        return BookParagraphConstants.BACKGROUND;
    }

    @Override
    public Character getCharacter(final Locale locale) {
        return (Character) getBeanFactory().getBean(getInfo().getCharacterBeanId());
    }

}
