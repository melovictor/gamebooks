package hu.zagor.gamebooks.ff.mvc.book.section.controller;

import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.section.controller.GenericBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.section.controller.RawBookSectionController;

/**
 * Generic section selection controller for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public abstract class AbstractFfBookSectionController extends RawBookSectionController {

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookSectionController}.
     * @param sectionHandlingService the {@link SectionHandlingService} to use for handling the section changes
     */
    public AbstractFfBookSectionController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    /**
     * Method for handling pre-fight preparations.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param enemyId the ID of the enemy to fight
     */
    protected abstract void handleBeforeFight(final HttpSessionWrapper wrapper, final String enemyId);

    /**
     * Method for handling post-fight preparations.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param enemyId the ID of the enemy to fight
     */
    protected abstract void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId);

}
