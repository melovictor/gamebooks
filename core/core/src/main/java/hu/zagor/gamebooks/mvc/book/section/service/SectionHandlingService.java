package hu.zagor.gamebooks.mvc.book.section.service;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.player.PlayerUser;

import org.springframework.ui.Model;

/**
 * Interface for a service that is capable of handling section changes for the different rulesets in the books.
 * @author Tamas_Szekeres
 */
public interface SectionHandlingService {

    /**
     * Method for handling the section selection for the raw ruleset.
     * @param model the model, cannot be null
     * @param wrapper the {@link HttpSessionWrapper} bean, cannot be null
     * @param paragraph the {@link Paragraph} bean, can be null
     * @param info the {@link BookInformations} bean, cannot be null
     * @return the name of the view that has to be displayed
     */
    String handleSection(final Model model, final HttpSessionWrapper wrapper, final Paragraph paragraph, final BookInformations info);

    /**
     * Method for adding the series' and book's title to the model.
     * @param model the model, cannot be null
     * @param player the {@link PlayerUser} bean, cannot be null
     * @param info the appropriate {@link BookInformations} bean, cannot be null
     */
    void initModel(final Model model, final PlayerUser player, final BookInformations info);

    /**
     * After everything is set up properly, this method can decide whether the loading of the given section was successful or not, and can decide what page to display for the user.
     * @param model the model, cannot be null
     * @param paragraph the {@link Paragraph} that has been loaded, can be null
     * @param tile the name of the tile to send the user in case of a success, cannot be null
     * @param info the {@link BookInformations} bean, cannot be null
     * @return the target tile (either the appropriate content's view name or the error page)
     */
    String checkParagraph(Model model, Paragraph paragraph, String tile, BookInformations info);

    /**
     * Method to resolve a paragraph's id into its display id.
     * @param info the {@link BookInformations} to identify the proper storage
     * @param paragraphId the id of the paragraph
     * @return the display id of the paragraph
     */
    String resolveParagraphId(BookInformations info, String paragraphId);

}
