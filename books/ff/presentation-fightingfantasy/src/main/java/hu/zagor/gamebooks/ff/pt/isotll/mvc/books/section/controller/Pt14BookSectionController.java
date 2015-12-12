package hu.zagor.gamebooks.ff.pt.isotll.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.choice.Choice;
import hu.zagor.gamebooks.content.choice.ChoiceSet;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Proteus;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Lazy
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Proteus.IN_SEARCH_OF_THE_LOST_LAND)
public class Pt14BookSectionController extends FfBookSectionController {

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Pt14BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleCustomSectionsPre(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        final FfCharacterHandler characterHandler = getInfo().getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        if (sectionIdentifier != null && characterHandler.getItemHandler().hasItem(character, "3015")) {
            attributeHandler.handleModification(character, "stamina", -1);
        }
        if ("4".equals(paragraph.getId()) || "184".equals(paragraph.getId())) {
            final ChoiceSet choices = paragraph.getData().getChoices();
            final Choice choice = choices.getChoiceByPosition(2);
            choices.remove(choice);
            final String originalSection = character.getUserInteraction().get("fenrisBack");
            final Choice newChoice = (Choice) getBeanFactory().getBean("choice", originalSection, choice.getText(), choice.getPosition(), null);
            choices.add(newChoice);
            if ("184".equals(paragraph.getId())) {
                final int skill = attributeHandler.resolveValue(character, "skill");
                attributeHandler.handleModification(character, "skill", -skill / 2);
            }
            paragraph.addValidMove(originalSection);
        }
    }

    /**
     * Handles the discarding of the Fenris wolf.
     * @param model the {@link Model} object
     * @param request the {@link HttpServletRequest} object
     * @param source the id of the section where we originated from
     * @return the book page's name
     */
    @RequestMapping(value = "dropFenris")
    public String dropFenris(final Model model, final HttpServletRequest request, @RequestParam("source") final String source) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        wrapper.getParagraph().addValidMove("4");
        wrapper.getCharacter().getUserInteraction().put("fenrisBack", source);
        return super.handleSection(model, request, "s-4");
    }

}
