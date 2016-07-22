package hu.zagor.gamebooks.lw.mvc.book.section.service;

import hu.zagor.gamebooks.content.command.CommandView;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.raw.mvc.book.section.service.RawBookSectionHandlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class for handling the loading and initializing the sections for Lone Wolf books.
 * @author Tamas_Szekeres
 */
@Component
@Qualifier("lwSectionHandlingService")
public class LwBookSectionHandlingService extends RawBookSectionHandlingService {
    /**
     * Basic constructor that passes the received {@link BookContentInitializer} bean to it's parent class' constructor.
     * @param contentInitializer the {@link BookContentInitializer} bean to use for initializing the model
     */
    @Autowired
    public LwBookSectionHandlingService(final BookContentInitializer contentInitializer) {
        super(contentInitializer);
    }

    @Override
    protected String getPageTileName(final BookInformations info) {
        return "lwSection." + info.getResourceDir();
    }

    @Override
    protected String getCommandView(final CommandView commandView, final BookInformations info) {
        return commandView.getViewName() + "." + info.getResourceDir();
    }
}
