package hu.zagor.gamebooks.ff.sor.tssb.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.hungarian.KalandJatekVarazslat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + KalandJatekVarazslat.A_VARAZSLATOK_KONYVE)
public class Kjv0BookSectionController extends Sor0BookSectionController {
    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Kjv0BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }
}
