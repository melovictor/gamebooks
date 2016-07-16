package hu.zagor.gamebooks.lw.lw.fftd.mvc.books.load.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.lw.mvc.book.load.controller.LwBookLoadController;
import hu.zagor.gamebooks.support.bookids.english.LoneWolf;

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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + LoneWolf.FLIGHT_FROM_THE_DARK)
public class Lw1BookLoadController extends LwBookLoadController {
    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Lw1BookLoadController(@Qualifier("lwSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }
}
