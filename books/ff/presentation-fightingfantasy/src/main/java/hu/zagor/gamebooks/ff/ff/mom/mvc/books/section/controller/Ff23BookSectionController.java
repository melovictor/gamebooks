package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain.HuntRoundResult;
import hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.service.HuntService;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.MASKS_OF_MAYHEM)
public class Ff23BookSectionController extends FfBookSectionController {

    @Autowired
    private HuntService huntService;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff23BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleCustomSections(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        if ("240".equals(paragraph.getId())) {
            wrapper.getParagraph().clearValidMoves();
        }
    }

    /**
     * Handler method for a single round of the Tiger hunting.
     * @param request the {@link HttpServletRequest} object
     * @return the result of the round
     */
    @RequestMapping(value = "hunt", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public HuntRoundResult handleHuntRound(final HttpServletRequest request) {
        return huntService.playRound(getWrapper(request), getInfo());
    }

}
