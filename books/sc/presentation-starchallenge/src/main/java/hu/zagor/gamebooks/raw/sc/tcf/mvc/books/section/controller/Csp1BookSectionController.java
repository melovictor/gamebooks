package hu.zagor.gamebooks.raw.sc.tcf.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.hungarian.Csillagproba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 *
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Csillagproba.AZ_ELVARAZSOLT_UR_KASTELY)
public class Csp1BookSectionController extends Sc3BookSectionController {
    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Csp1BookSectionController(@Qualifier("rawSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }
}
