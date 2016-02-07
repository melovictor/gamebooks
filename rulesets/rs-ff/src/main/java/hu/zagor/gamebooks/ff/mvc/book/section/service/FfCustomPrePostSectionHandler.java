package hu.zagor.gamebooks.ff.mvc.book.section.service;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;
import hu.zagor.gamebooks.mvc.book.section.service.CustomPrePostSectionHandler;
import org.springframework.ui.Model;

/**
 * Abstract class that does the conversion from {@link BookInformations} to {@link FfBookInformations} before beginning the conversion.
 * @author Tamas_Szekeres
 */
public abstract class FfCustomPrePostSectionHandler implements CustomPrePostSectionHandler {

    @Override
    public final void handle(final Model model, final HttpSessionWrapper wrapper, final BookInformations info, final boolean changedSection) {
        handle(model, wrapper, (FfBookInformations) info, changedSection);
    }

    /**
     * Executes the special events for the current section.
     * @param model the {@link Model} object
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param info the {@link FfBookInformations} object
     * @param changedSection true if there was a section change, false if we stayed at the same
     */
    public abstract void handle(Model model, HttpSessionWrapper wrapper, FfBookInformations info, boolean changedSection);

}
