package hu.zagor.gamebooks.mvc.book.section.controller;

import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.controller.AbstractSectionDisplayingController;
import hu.zagor.gamebooks.support.logging.LogInject;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

/**
 * Class for handling the selection of the next section in the book.
 * @author Tamas_Szekeres
 */
public abstract class GenericBookSectionController extends AbstractSectionDisplayingController {

    @LogInject private Logger logger;
    @Autowired private BookContentInitializer contentInitializer;
    @Autowired private GameStateHandler gameStateHandler;

    /**
     * Loads the paragraph content belonging to the given id.
     * @param paragraphId the id of the paragraph to load, cannot be null
     * @param request the http request, cannot be null
     * @return the {@link Paragraph} bean
     */
    protected Paragraph loadSection(final String paragraphId, final HttpServletRequest request) {
        Assert.notNull(paragraphId, "The parameter 'paragraphId' cannot be null!");
        Assert.notNull(request, "The parameter 'request' cannot be null!");

        final HttpSessionWrapper wrapper = getWrapper(request);
        return wrapper.setParagraph(contentInitializer.loadSection(paragraphId, wrapper.getPlayer(), wrapper.getParagraph(), getInfo()));
    }

    /**
     * Does custom section handling in the books before the actual handler flow.
     * @param model the {@link Model}
     * @param wrapper the {@link HttpSessionWrapper}
     * @param sectionIdentifier the section identifier which was requested by the user
     * @param paragraph the new {@link Paragraph} object
     */
    protected abstract void handleCustomSectionsPre(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph);

    /**
     * Does custom section handling in the books after the actual handler flow.
     * @param model the {@link Model}
     * @param wrapper the {@link HttpSessionWrapper}
     * @param sectionIdentifier the section identifier which was requested by the user
     * @param paragraph the new {@link Paragraph} object
     */
    protected abstract void handleCustomSectionsPost(final Model model, final HttpSessionWrapper wrapper, final String sectionIdentifier, final Paragraph paragraph);

    public Logger getLogger() {
        return logger;
    }

    public GameStateHandler getGameStateHandler() {
        return gameStateHandler;
    }
}
