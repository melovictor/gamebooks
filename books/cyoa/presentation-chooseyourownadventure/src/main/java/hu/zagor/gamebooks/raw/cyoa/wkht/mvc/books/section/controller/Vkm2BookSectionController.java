package hu.zagor.gamebooks.raw.cyoa.wkht.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.section.controller.RawBookSectionController;
import hu.zagor.gamebooks.support.bookids.hungarian.ValasszKalandotMagadnak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + ValasszKalandotMagadnak.KI_OLTE_MEG_HARLOWE_THROMBEY_T)
public class Vkm2BookSectionController extends RawBookSectionController {

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Vkm2BookSectionController(@Qualifier("rawSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }
}
