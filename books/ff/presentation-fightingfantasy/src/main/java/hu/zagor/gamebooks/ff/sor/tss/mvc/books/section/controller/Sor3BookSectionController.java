package hu.zagor.gamebooks.ff.sor.tss.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.exception.InvalidStepChoiceException;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_SEVEN_SERPENTS)
public class Sor3BookSectionController extends FfBookSectionController {
    private static final String SNAKE_RING = "3049";
    @Resource(name = "snakeEncounterSections") private Set<String> snakeEncounterSections;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Sor3BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    /**
     * Handles the jumps for using the snake ring on the seven serpents.
     * @param model the {@link Model} object
     * @param request the {@link HttpServletRequest} object
     * @return the book page's name
     */
    @RequestMapping(value = "useSnakeRing", method = RequestMethod.GET)
    public String handleSnakeRingJumps(final Model model, final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);

        final Paragraph oldParagraph = wrapper.getParagraph();
        final String currentSectionId = oldParagraph.getId();

        final Character character = wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        if (!itemHandler.hasEquippedItem(character, SNAKE_RING)) {
            throw new InvalidStepChoiceException("You cannot use the snake ring if you do not have it, or are not wearing it.", currentSectionId);
        }
        final String targetSectionId = String.valueOf(Integer.parseInt(currentSectionId) - 14);

        if (!snakeEncounterSections.contains(currentSectionId)) {
            throw new InvalidStepChoiceException("You cannot use the snake ring at your current position.", currentSectionId);
        }

        final ChoiceSet choices = oldParagraph.getData().getChoices();
        final Choice snakeRing = new Choice(targetSectionId, "", 99, null);
        choices.add(snakeRing);
        oldParagraph.addValidMove(targetSectionId);

        return handleSection(model, request, "s-" + targetSectionId);
    }

}
