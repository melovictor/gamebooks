package hu.zagor.gamebooks.ff.sor.kcot.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.exception.InvalidStepChoiceException;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.SorBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import java.util.Map;
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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.KHARE_CITYPORT_OF_TRAPS)
public class Sor2BookSectionController extends SorBookSectionController {
    private static final String MET_FLANKER_MARKER = "4013";
    @Resource(name = "flankerVisitTargets") private Map<String, String> flankerVisitTargets;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Sor2BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId) {
        final Character character = wrapper.getCharacter();
        if (character.getCommandView() == null) {
            final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
            itemHandler.removeItem(character, "4015", 1);
            itemHandler.removeItem(character, "4016", 1);
        }
    }

    /**
     * Handles the jumps for visiting Flanker, the assassin.
     * @param model the {@link Model} object
     * @param request the {@link HttpServletRequest} object
     * @return the book page's name
     */
    @RequestMapping(value = "visitFlanker", method = RequestMethod.GET)
    public String handleFlankerJumps(final Model model, final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);

        final Paragraph oldParagraph = wrapper.getParagraph();
        final String currentSectionId = oldParagraph.getId();

        final Character character = wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        if (!itemHandler.hasItem(character, MET_FLANKER_MARKER)) {
            throw new InvalidStepChoiceException("You cannot meet Flanker if you never met him among the Shamutanti Hills.", currentSectionId);
        }
        final String targetSectionId = flankerVisitTargets.get(currentSectionId);

        if (targetSectionId == null) {
            throw new InvalidStepChoiceException("You cannot meet Flanker at your current position.", currentSectionId);
        }

        final ChoiceSet choices = oldParagraph.getData().getChoices();
        final Choice flanker = new Choice(targetSectionId, "", 99, null);
        choices.add(flanker);
        oldParagraph.addValidMove(targetSectionId);

        return handleSection(model, request, "s-" + targetSectionId);
    }
}
