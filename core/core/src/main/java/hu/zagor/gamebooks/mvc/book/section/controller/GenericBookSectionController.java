package hu.zagor.gamebooks.mvc.book.section.controller;

import hu.zagor.gamebooks.books.saving.GameStateHandler;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.BookContentInitializer;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.controller.AbstractSectionDisplayingController;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.util.Map;
import javax.annotation.PostConstruct;
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
    private Map<String, CustomPrePostSectionHandler> prePostHandlers;

    @PostConstruct
    @Override
    @SuppressWarnings("unchecked")
    public void init() {
        super.init();
        final String customHandlerName = fetchBookIdByReflection() + "PrePostSectionHandler";
        if (getApplicationContext().containsBean(customHandlerName)) {
            prePostHandlers = (Map<String, CustomPrePostSectionHandler>) getApplicationContext().getBean(customHandlerName);
        }
    }

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
     * @param changedSection true if there was a section change, false if we stayed at the same
     */
    protected void handleCustomSectionsPre(final Model model, final HttpSessionWrapper wrapper, final boolean changedSection) {
        defaultHandleCustomSections(model, wrapper, changedSection, "pre");
    }

    /**
     * Does custom section handling in the books after the actual handler flow.
     * @param model the {@link Model}
     * @param wrapper the {@link HttpSessionWrapper}
     * @param changedSection true if there was a section change, false if we stayed at the same
     */
    protected void handleCustomSectionsPost(final Model model, final HttpSessionWrapper wrapper, final boolean changedSection) {
        defaultHandleCustomSections(model, wrapper, changedSection, "post");
    }

    private void defaultHandleCustomSections(final Model model, final HttpSessionWrapper wrapper, final boolean changedSection, final String postfix) {
        if (prePostHandlers != null) {
            final CustomPrePostSectionHandler customSectionHandler = prePostHandlers.get(wrapper.getParagraph().getId() + postfix);
            if (customSectionHandler != null) {
                customSectionHandler.handle(model, wrapper, getInfo(), changedSection);
            }
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public GameStateHandler getGameStateHandler() {
        return gameStateHandler;
    }
}
