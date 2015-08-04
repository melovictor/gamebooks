package hu.zagor.gamebooks.controller;

import hu.zagor.gamebooks.books.contentstorage.domain.BookItemStorage;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.gathering.GatheredLostItem;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.exception.InvalidGatheredItemException;
import hu.zagor.gamebooks.player.PlayerUser;

import org.springframework.ui.Model;

/**
 * Interface for class that is responsible for grabbing and initializing book content.
 * @author Tamas_Szekeres
 */
public interface BookContentInitializer {

    /**
     * Loads the paragraph content belonging to the given id.
     * @param paragraphId the id of the paragraph to load, cannot be null
     * @param player the {@link PlayerUser} bean, cannot be null
     * @param previousParagraph the previous {@link Paragraph} bean, can be null
     * @param info the {@link BookInformations} bean, cannot be null
     * @return the {@link Paragraph} bean
     */
    Paragraph loadSection(final String paragraphId, final PlayerUser player, final Paragraph previousParagraph, final BookInformations info);

    /**
     * Validates that the item belonging to the given id and amount can, at this point, be gathered.
     * @param glItem the item and it's amount to load, cannot be null
     * @param player the {@link PlayerUser} bean, cannot be null
     * @param paragraph the {@link Paragraph} bean, cannot be null
     * @param info the {@link BookInformations} bean, cannot be null
     * @throws InvalidGatheredItemException occurs if the given item cannot be gathered at this point
     */
    void validateItem(final GatheredLostItem glItem, final PlayerUser player, final Paragraph paragraph, final BookInformations info);

    /**
     * Method for adding the series' and book's title to the model.
     * @param model the model, cannot be null
     * @param player the {@link PlayerUser} bean, cannot be null
     * @param info the relevant {@link BookInformations} bean, cannot be null
     */
    void initModel(final Model model, final PlayerUser player, final BookInformations info);

    /**
     * Returns the {@link BookItemStorage} containing the items.
     * @param info the {@link BookInformations} bean to identify the appropriate {@link BookItemStorage}
     * @return the {@link BookItemStorage}
     */
    BookItemStorage getItemStorage(BookInformations info);

}
