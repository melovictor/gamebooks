package hu.zagor.gamebooks.raw.mvc.book.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.contentstorage.domain.BookParagraphConstants;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.handler.userinteraction.DefaultUserInteractionHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.exception.InvalidStepChoiceException;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.player.PlayerUser;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;
import hu.zagor.gamebooks.raw.mvc.book.controller.CharacterPageDisplayingController;
import hu.zagor.gamebooks.raw.mvc.book.section.domain.UserInputResponseForm;
import hu.zagor.gamebooks.recording.NavigationRecorder;
import hu.zagor.gamebooks.recording.UserInteractionRecorder;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Generic section selection controller for books with no rule system.
 * @author Tamas_Szekeres
 */
public class RawBookSectionController extends GenericBookSectionController implements CharacterPageDisplayingController {

    @Autowired private NavigationRecorder navigationRecorder;
    @Autowired private UserInteractionRecorder interactionRecorder;

    private final SectionHandlingService sectionHandlingService;

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookSectionController}.
     * @param sectionHandlingService the {@link SectionHandlingService} to use for handling the section changes
     */
    public RawBookSectionController(final SectionHandlingService sectionHandlingService) {
        Assert.notNull(sectionHandlingService, "The parameter 'sectionHandlingService' cannot be null!");
        this.sectionHandlingService = sectionHandlingService;
    }

    /**
     * Handles the initial display of the open book page. After this point, it gives control to the ruleset/bookinstance controller.
     * @param model the data model
     * @param request the http request
     * @return the book page's name
     */
    @RequestMapping(value = PageAddresses.BOOK_WELCOME)
    public String handleWelcome(final Model model, final HttpServletRequest request) {
        getLogger().debug("Handling welcome page for book.");
        final HttpSessionWrapper wrapper = getWrapper(request);
        final PlayerUser player = wrapper.getPlayer();
        final BookInformations info = getInfo();
        sectionHandlingService.initModel(model, player, info);
        final Paragraph paragraph = loadSection(BookParagraphConstants.BACK_COVER.getValue(), request);
        paragraph.calculateValidEvents();

        model.addAttribute("haveSavedGame", getGameStateHandler().checkSavedGame(player.getId(), info.getId()));
        model.addAttribute("isWelcomeScreen", true);
        model.addAttribute("haveRules", getBeanFactory().containsBean(info.getHelpBeanId()));

        final String series = info.getSeries();
        final String title = info.getTitle();
        model.addAttribute("pageTitle", series + " &ndash; " + title);
        addResources(model);

        return sectionHandlingService.checkParagraph(model, paragraph, "rawWelcome", info);
    }

    /**
     * Handles the display of the book's section page.
     * @param model the data model
     * @param request the request
     * @param sectionIdentifier the requested section's identifier
     * @return the book page's name
     */
    @RequestMapping(value = "{position}", method = RequestMethod.GET)
    public String handleSection(final Model model, final HttpServletRequest request, @PathVariable("position") final String sectionIdentifier) {
        getLogger().debug("Handling choice request '{}' for book.", sectionIdentifier);

        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph previousParagraph = wrapper.getParagraph();

        final Paragraph paragraph;
        Integer position = null;
        if (sectionIdentifier == null) {
            paragraph = previousParagraph;
            clearAnchorPoints(paragraph);
        } else {
            final Choice choice = obtainChoice(sectionIdentifier, previousParagraph, wrapper.getPlayer());
            position = choice.getPosition();
            final String paragraphId = choice.getId();
            getLogger().debug("Handling paragraph {} for book.", paragraphId);
            paragraph = loadSection(paragraphId, request);
            getInfo().getCharacterHandler().getParagraphHandler().addParagraph(wrapper.getCharacter(), paragraph.getId());
        }

        handleCustomSectionsPre(model, wrapper, sectionIdentifier, paragraph);
        final String bookPage = doHandleSection(model, wrapper, paragraph, position);
        handleCustomSectionsPost(model, wrapper, sectionIdentifier, paragraph);
        wrapper.setModel(model);
        navigationRecorder.recordNavigation(wrapper, sectionIdentifier, previousParagraph, paragraph);
        addResources(model);

        return bookPage;
    }

    /**
     * Adds all required static resource files to the batch.
     * @param model the {@link Model} object
     */
    protected void addResources(final Model model) {
        addJsResource(model, "raw");
        addCssResource(model, "raw");
    }

    /**
     * Resolve the display names for the choices.
     * @param paragraph the {@link Paragraph} object whose choices must be considered
     */
    protected void resolveChoiceDisplayNames(final Paragraph paragraph) {
        final ChoiceSet choices = paragraph.getData().getChoices();
        for (final Choice choice : choices) {
            final String paragraphId = choice.getId();
            final String paragraphDisplayId = resolveParagraphDisplayId(paragraphId);
            choice.setDisplay(paragraphDisplayId);
        }
    }

    private String resolveParagraphDisplayId(final String paragraphId) {
        return sectionHandlingService.resolveParagraphId(getInfo(), paragraphId);
    }

    /**
     * Clears the previous anchors so the new ones are not affected at all.
     * @param paragraph the main {@link Paragraph}
     */
    protected void clearAnchorPoints(final Paragraph paragraph) {
        final String text = paragraph.getData().getText();
        paragraph.getData().setText(text.replaceAll("<a id=[\"'][^\"']*[\"']><\\/a>", ""));
    }

    private Choice obtainChoice(final String sectionIdentifier, final Paragraph previousParagraph, final PlayerUser player) {
        Choice choice;

        if (sectionIdentifier.startsWith("s-")) {
            final String[] sectionPieces = sectionIdentifier.substring(2).split("\\|");
            final String sectionId = sectionPieces[0];
            final Integer position = sectionPieces.length == 2 ? Integer.parseInt(sectionPieces[1]) : null;
            final ChoiceSet choices = previousParagraph.getData().getChoices();
            choice = position == null ? choices.getChoiceById(sectionId) : choices.getChoiceByPosition(position);
            if (choice == null) {
                if (!player.isTester()) {
                    getLogger().debug("Player tried to navigate to illegal section {}.", sectionId);
                    throw new InvalidStepChoiceException(previousParagraph.getId(), sectionId);
                }
                choice = new Choice(sectionId, null, -1, null);
            }
        } else {
            final int position = Integer.valueOf(sectionIdentifier);
            choice = previousParagraph.getData().getChoices().getChoiceByPosition(position);
            if (choice == null) {
                getLogger().debug("Player tried to navigate to illegal position {}.", position);
                throw new InvalidStepChoiceException(previousParagraph.getId(), position);
            }
        }
        return choice;
    }

    @Override
    protected void handleCustomSectionsPre(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
    }

    @Override
    protected void handleCustomSectionsPost(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
    }

    private String doHandleSection(final Model model, final HttpSessionWrapper wrapper, final Paragraph paragraph, final Integer position) {
        final PlayerUser player = wrapper.getPlayer();
        setUpSectionDisplayOptions(paragraph, model, player);
        model.addAttribute("bookInfo", getInfo());
        return processSectionChange(model, wrapper, paragraph, position);
    }

    /**
     * Handles the user's response when receiving user input.
     * @param form the bean containing the user's response
     * @param model the data model
     * @param request the request
     * @return the book page's name
     * @throws UnsupportedEncodingException occurs when the webapp is installed on a very strange system
     */
    @RequestMapping(value = PageAddresses.BOOK_USER_RESPONSE)
    public String handleUserInput(final UserInputResponseForm form, final Model model, final HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        getLogger().debug("Received answer from user: '{}'.", form.getResponseText());
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        final Character character = wrapper.getCharacter();
        character.setCommandView(null);
        final DefaultUserInteractionHandler interactionHandler = (DefaultUserInteractionHandler) getInfo().getCharacterHandler().getInteractionHandler();
        interactionHandler.setUserInput(character, form.getResponseText());
        interactionHandler.setUserInputTime(character, form.getElapsedTime());
        interactionRecorder.recordUserResponse(wrapper, form.getResponseText(), form.getForcedTime() == 0 ? form.getElapsedTime() : form.getForcedTime());
        addResources(model);
        return processSectionChange(model, wrapper, paragraph, null);
    }

    private String processSectionChange(final Model model, final HttpSessionWrapper wrapper, final Paragraph paragraph, final Integer position) {
        final BookInformations info = getInfo();
        final ResolvationData resolvationData = DefaultResolvationDataBuilder.builder().withParagraph(paragraph).withBookInformations(info).usingWrapper(wrapper)
            .withPosition(position).build();

        info.getParagraphResolver().resolve(resolvationData, paragraph);
        paragraph.calculateValidEvents();
        model.addAttribute("charEquipments", getCharacterPageData(wrapper.getCharacter()));
        final String bookPageName = sectionHandlingService.handleSection(model, wrapper, paragraph, getInfo());
        resolveSingleChoice(paragraph.getData());
        resolveChoiceDisplayNames(paragraph);
        return bookPageName;
    }

    private void resolveSingleChoice(final ParagraphData data) {
        final ChoiceSet choices = data.getChoices();
        String replacementText = "";
        if (choices.size() == 1) {
            final Choice choice = choices.iterator().next();
            final String singleChoiceText = choice.getSingleChoiceText();
            if (singleChoiceText != null) {
                replacementText = singleChoiceText;
            }
        }
        if (replacementText.isEmpty()) {
            replacementText = "$1";
        }
        String text = data.getText();
        text = text.replaceAll("<alt>([^<]*)<\\/alt>", replacementText);
        data.setText(text);
    }

    /**
     * Handler for random entry points.
     * @param model the data model
     * @param request the request
     * @return the book page's name
     */
    @RequestMapping(value = PageAddresses.RANDOM)
    public String handleRandom(final Model model, final HttpServletRequest request) {
        interactionRecorder.recordRandomRoll(getWrapper(request));
        return handleSection(model, request, null);
    }

    @Override
    public RawCharacterPageData getCharacterPageData(final Character character) {
        return (RawCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character);
    }

    public UserInteractionRecorder getInteractionRecorder() {
        return interactionRecorder;
    }
}
