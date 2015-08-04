package hu.zagor.gamebooks.tm.mvc.book.load.controller;

import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.mvc.book.load.controller.GenericBookLoadController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.load.controller.RawBookLoadController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

/**
 * Generic load controller for Time Machine books.
 * @author Tamas_Szekeres
 */
public class TmBookLoadController extends RawBookLoadController {

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookLoadController}.
     * @param sectionHandlingService he {@link SectionHandlingService} that will handle the section changing
     */
    public TmBookLoadController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected String doLoad(final Model model, final HttpServletRequest request, final SavedGameContainer savedGameContainer) {
        addCssResource(model, "tm");
        return super.doLoad(model, request, savedGameContainer);
    }
}
