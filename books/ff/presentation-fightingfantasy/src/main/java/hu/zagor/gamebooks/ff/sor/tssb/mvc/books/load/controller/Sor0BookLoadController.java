package hu.zagor.gamebooks.ff.sor.tssb.mvc.books.load.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.ff.mvc.book.load.controller.FfBookLoadController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the load request to the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_SORCERY_SPELL_BOOK)
public class Sor0BookLoadController extends FfBookLoadController {
    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Sor0BookLoadController(@Qualifier("rawSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }
}
