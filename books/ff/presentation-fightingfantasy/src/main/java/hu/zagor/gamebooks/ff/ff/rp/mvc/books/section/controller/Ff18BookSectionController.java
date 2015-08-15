package hu.zagor.gamebooks.ff.ff.rp.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.REBEL_PLANET)
public class Ff18BookSectionController extends FfBookSectionController {

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff18BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleCustomSections(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        final String paragraphId = paragraph.getId();
        if ("374".equals(paragraphId) || "191".equals(paragraphId) || "78".equals(paragraphId)) {
            final FfCharacter character = (FfCharacter) wrapper.getCharacter();
            final int halfStamina = character.getStamina() / 2;
            getInfo().getCharacterHandler().getAttributeHandler().handleModification(character, "stamina", halfStamina);
        }
    }
}
