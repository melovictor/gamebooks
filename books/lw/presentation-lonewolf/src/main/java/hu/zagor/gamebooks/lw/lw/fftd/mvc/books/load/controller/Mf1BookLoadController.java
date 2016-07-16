package hu.zagor.gamebooks.lw.lw.fftd.mvc.books.load.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.hungarian.MaganyosFarkas;

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
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + MaganyosFarkas.MENEKULES_A_SOTETSEGBOL)
public class Mf1BookLoadController extends Lw1BookLoadController {
    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Mf1BookLoadController(@Qualifier("lwSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }
}
