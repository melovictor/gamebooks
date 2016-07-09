package hu.zagor.gamebooks.ff.ff.aod.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.ARMIES_OF_DEATH)
public class Ff36BookSectionController extends FfBookSectionController {
    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff36BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    /**
     * Handler for finishing the army attack and lose selected fighters in the squadrons.
     * @param model the {@link Model}
     * @param request the {@link HttpServletRequest}
     * @param container the bean containing the user's input
     * @return the name of the view to display
     */
    @RequestMapping("finishAttack")
    public String finishAttack(final Model model, final HttpServletRequest request, @ModelAttribute final ArmyLossesContainer container) {
        final FfCharacter character = (FfCharacter) getWrapper(request).getCharacter();
        final FfUserInteractionHandler interactionHandler = getInfo().getCharacterHandler().getInteractionHandler();
        interactionHandler.setFightCommand(character, "finalizing");
        // TODO: extend when new squadron is added
        interactionHandler.setFightCommand(character, "loseArmy",
            "warriors=" + container.getWarriors() + ";elves=" + container.getElves() + ";dwarves=" + container.getDwarves() + ";knights=" + container.getKnights()
                + ";wilders=" + container.getWilders() + ";northerns=" + container.getNortherns() + ";marauders=" + container.getMarauders() + ";whiteKnights="
                + container.getWhiteKnights());

        return this.handleSection(model, request, null);
    }
}
