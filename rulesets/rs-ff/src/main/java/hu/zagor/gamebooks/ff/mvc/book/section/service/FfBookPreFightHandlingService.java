package hu.zagor.gamebooks.ff.mvc.book.section.service;

import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.domain.FfBookInformations;

/**
 * Interface for handling pre-fight item usage in FF books.
 * @author Tamas_Szekeres
 */
public interface FfBookPreFightHandlingService {

    /**
     * Handles the pre-fight item usage.
     * @param info the {@link BookInformations} object
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param itemId the id of the item to handle
     * @return the {@link FfItem} object that has been used during the battle and is kept afterwards as well, or null if it has been discarded
     */
    FfItem handlePreFightItemUsage(FfBookInformations info, HttpSessionWrapper wrapper, String itemId);

}
