package hu.zagor.gamebooks.raw.cyoa.bbtts.mvc.books.load.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.load.controller.RawBookLoadController;
import hu.zagor.gamebooks.support.bookids.english.ChooseYourOwnAdventure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the load request to the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ChooseYourOwnAdventure.BY_BALLOON_TO_THE_SAHARA)
public class Cyoa3BookLoadController extends RawBookLoadController {
    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Cyoa3BookLoadController(@Qualifier("rawSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }
}
