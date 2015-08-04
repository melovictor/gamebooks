package hu.zagor.gamebooks.ff.mvc.book.section.service;

import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.raw.mvc.book.section.service.RawBookSectionHandlingService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class for handling the loading and initializing the sections for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfBookSectionHandlingService extends RawBookSectionHandlingService {

    /**
     * Basic constructor that passes the received {@link BookContentInitializer} bean to it's parent class'
     * constructor.
     * @param contentInitializer the {@link BookContentInitializer} bean to use for initializing the model
     */
    @Autowired
    public FfBookSectionHandlingService(final BookContentInitializer contentInitializer) {
        super(contentInitializer);
    }

    @Override
    protected String getPageTileName(final BookInformations info) {
        return "ffSection." + info.getResourceDir();
    }

    @Override
    protected String getCommandView(final CommandView commandView, final BookInformations info) {
        return commandView.getViewName() + "." + info.getResourceDir();
    }
}
