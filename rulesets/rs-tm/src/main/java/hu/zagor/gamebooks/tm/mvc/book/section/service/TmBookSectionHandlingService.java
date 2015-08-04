package hu.zagor.gamebooks.tm.mvc.book.section.service;

import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.raw.mvc.book.section.service.RawBookSectionHandlingService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class for handling the loading and initializing the sections for Time Machine books.
 * @author Tamas_Szekeres
 */
public class TmBookSectionHandlingService extends RawBookSectionHandlingService {

    /**
     * Basic constructor that passes the received {@link BookContentInitializer} bean to it's parent class'
     * constructor.
     * @param contentInitializer the {@link BookContentInitializer} bean to use for initializing the model
     */
    @Autowired
    public TmBookSectionHandlingService(final BookContentInitializer contentInitializer) {
        super(contentInitializer);
    }

    @Override
    protected String getPageTileName(final BookInformations info) {
        return "tmSection";
    }
}
