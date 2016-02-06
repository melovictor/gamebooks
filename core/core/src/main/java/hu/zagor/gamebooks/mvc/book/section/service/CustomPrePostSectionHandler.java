package hu.zagor.gamebooks.mvc.book.section.service;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import org.springframework.ui.Model;

/**
 * Interface for a handler class that does a specific section handling, either pre or post.
 * @author Tamas_Szekeres
 */
public interface CustomPrePostSectionHandler {

    /**
     * Executes the special events for the current section.
     * @param model the {@link Model} object
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param info the {@link BookInformations} object
     * @param changedSection true if there was a section change, false if we stayed at the same
     */
    void handle(Model model, HttpSessionWrapper wrapper, BookInformations info, boolean changedSection);

}
