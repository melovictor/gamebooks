package hu.zagor.gamebooks.ff.sor.tcok.mvc.books.section.controller;

import hu.zagor.gamebooks.ControllerAddresses;
import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.saving.xml.XmlGameStateLoader;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.SorBookSectionController;
import hu.zagor.gamebooks.ff.sor.tcok.mvc.books.section.domain.DartThrowingResult;
import hu.zagor.gamebooks.ff.sor.tcok.mvc.books.section.service.DartThrowingService;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_CROWN_OF_KINGS)
public class Sor4BookSectionController extends SorBookSectionController {
    private static final int WHYDE_RECOGNITION = 210;
    private static final int CURRENT_BOOK = 4;
    @Autowired private DartThrowingService dartThrowingService;
    @Autowired private XmlGameStateLoader gameStateLoader;
    @Resource(name = "sor4TimeTravelSources") private Set<String> timeTravelSources;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Sor4BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId) {
        super.handleAfterFight(wrapper, enemyId);
        final Character character = wrapper.getCharacter();
        if (character.getCommandView() == null) {
            final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
            itemHandler.removeItem(character, "4008", 1);
        }
    }

    /**
     * Handler for the Ten-Up game handling.
     * @param request the {@link HttpServletRequest} object
     * @return the new data for the system
     */
    @RequestMapping("throwNextDart")
    @ResponseBody
    public DartThrowingResult handleDartThrowing(final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        return dartThrowingService.throwDart(wrapper, getInfo());
    }

    /**
     * Handler for jumping in time either with the ZED spell, or with the help of the Genie.
     * @param request the {@link HttpServletRequest} object
     * @param response the {@link HttpServletResponse} object
     * @param bookNumber the number of the book we need to jump back to
     * @param sectionNumber the section number we need to jump back to
     * @throws IOException if an input or output exception occurs
     */
    @RequestMapping("spellJump/{book}/{section}")
    public void handleSpellJumping(final HttpServletRequest request, final HttpServletResponse response, @PathVariable("book") final int bookNumber,
        @PathVariable("section") final int sectionNumber) throws IOException {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        if (!timeTravelSources.contains(paragraph.getId())) {
            throw new IllegalStateException("You cannot time-travel from this location!");
        }

        final SorCharacter currentCharacter = (SorCharacter) wrapper.getCharacter();

        final String targetSection = fetchCorrectSaveLocation(bookNumber, sectionNumber);
        final SorCharacter reloadedCharacter = reloadCharacter(currentCharacter, targetSection, "2-1", "2-254", "3-1", "3-48", "4-1");

        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        if (bookNumber == CURRENT_BOOK) {
            if (currentCharacter.getParagraphs().contains("334")) {
                itemHandler.removeItem(reloadedCharacter, "3084", 1);
            }
            if (itemHandler.hasItem(currentCharacter, "4098")) {
                itemHandler.addItem(reloadedCharacter, "4098", 1);
            }
            wrapper.setCharacter(reloadedCharacter);
            enableJumpToSection(wrapper.getParagraph(), sectionNumber);
            response.sendRedirect("../../s-" + sectionNumber);
        } else {
            final long targetBookId = getInfo().getId() - 4 + bookNumber;
            request.getSession().setAttribute(ControllerAddresses.CHARACTER_STORE_KEY + targetBookId, reloadedCharacter);
            enableJumpToSection(wrapper.getParagraph(), sectionNumber);
            response.sendRedirect("../../../" + targetBookId + "/spellJump-" + sectionNumber);
        }

    }

    private void enableJumpToSection(final Paragraph paragraph, final int sectionNumber) {
        final ChoiceSet choices = paragraph.getData().getChoices();
        choices.clear();
        choices.add(new Choice(String.valueOf(sectionNumber), null, 0, null));
        paragraph.calculateValidEvents();
    }

    private String fetchCorrectSaveLocation(final int bookNumber, final int sectionNumber) {
        String saveLocation = bookNumber + "-" + sectionNumber;
        if (bookNumber == CURRENT_BOOK && sectionNumber == WHYDE_RECOGNITION) {
            saveLocation = "4-321";
        }
        return saveLocation;
    }

    private SorCharacter reloadCharacter(final SorCharacter currentCharacter, final String... locationCandidates) {
        final String toReload = fetchCharacterString(currentCharacter, locationCandidates);
        return convertToCharacter(toReload);
    }

    private String fetchCharacterString(final SorCharacter currentCharacter, final String... locationCandidates) {
        final Map<String, String> characterSaveLocations = currentCharacter.getCharacterSaveLocations();
        String toReload;
        int i = 0;
        do {
            toReload = characterSaveLocations.get(locationCandidates[i++]);
        } while (toReload == null);
        return toReload;
    }

    private SorCharacter convertToCharacter(final String toReload) {
        return (SorCharacter) gameStateLoader.load(toReload);
    }

}
