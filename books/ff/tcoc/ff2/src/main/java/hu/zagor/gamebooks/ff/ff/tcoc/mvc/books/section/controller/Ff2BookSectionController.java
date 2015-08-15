package hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.domain.SixPickBets;
import hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.domain.SixPickResult;
import hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.service.RunestonesGame;
import hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.service.SixPickGame;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.THE_CITADEL_OF_CHAOS)
public class Ff2BookSectionController extends FfBookSectionController {

    @Autowired
    private SixPickGame sixPick;
    @Autowired
    private RunestonesGame runeStones;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff2BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    /**
     * Plays a single round of Six pick.
     * @param request the {@link HttpServletRequest} object
     * @param bets the bets the user did
     * @return the response to the page
     */
    @RequestMapping(value = "sixPick", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public SixPickResult playSixPickRound(final HttpServletRequest request, @RequestBody final SixPickBets bets) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final Paragraph paragraph = wrapper.getParagraph();
        final String sectionId = paragraph.getId();

        SixPickResult playRound = null;
        if ("171".equals(sectionId)) {
            playRound = sixPick.playRound((FfCharacter) wrapper.getCharacter(), paragraph, bets);
        }
        return playRound;
    }

    @Override
    protected void handleCustomSections(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph) {
        final String paragraphId = paragraph.getId();
        if ("278round".equals(paragraphId)) {
            runeStones.playRound((FfCharacter) wrapper.getCharacter(), getInfo().getCharacterHandler(), paragraph);
        }
    }
}
