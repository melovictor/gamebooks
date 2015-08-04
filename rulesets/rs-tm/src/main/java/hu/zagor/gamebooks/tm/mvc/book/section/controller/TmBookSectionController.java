package hu.zagor.gamebooks.tm.mvc.book.section.controller;

import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.section.controller.RawBookSectionController;

import org.springframework.ui.Model;

/**
 * Generic section selection controller for Time Machine books.
 * @author Tamas_Szekeres
 */
public class TmBookSectionController extends RawBookSectionController {

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookSectionController}.
     * @param sectionHandlingService the {@link SectionHandlingService} to use for handling the section changes
     */
    public TmBookSectionController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void addResources(final Model model) {
        super.addResources(model);
        addCssResource(model, "tm");
    }
}
