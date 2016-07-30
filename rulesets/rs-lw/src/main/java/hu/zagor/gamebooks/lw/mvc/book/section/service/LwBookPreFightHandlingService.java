package hu.zagor.gamebooks.lw.mvc.book.section.service;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.LwBookInformations;

/**
 * Interface for handling pre-fight item usage in LW books.
 * @author Tamas_Szekeres
 */
public interface LwBookPreFightHandlingService {

    /**
     * Handles the pre-fight item usage.
     * @param info the {@link BookInformations} object
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param itemId the id of the item to handle
     */
    void handlePreFightItemUsage(LwBookInformations info, HttpSessionWrapper wrapper, String itemId);

}
