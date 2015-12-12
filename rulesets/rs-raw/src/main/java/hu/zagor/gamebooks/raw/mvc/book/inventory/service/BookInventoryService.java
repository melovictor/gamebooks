package hu.zagor.gamebooks.raw.mvc.book.inventory.service;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import org.springframework.ui.Model;

/**
 * Interface for a service handling inventory operations.
 * @author Tamas_Szekeres
 */
public interface BookInventoryService {
    /**
     * Handles the call for the inventory.
     * @param model the {@link Model} object
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param info the {@link BookInformations} object
     * @return the display tile name
     */
    String handleInventory(Model model, HttpSessionWrapper wrapper, BookInformations info);

}
