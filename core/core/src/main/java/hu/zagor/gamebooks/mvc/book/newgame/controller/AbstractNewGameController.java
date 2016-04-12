package hu.zagor.gamebooks.mvc.book.newgame.controller;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.controller.AbstractSectionDisplayingController;
import java.util.Locale;

/**
 * Abstract controller for handling new game requests with abstract method to get preinitialized character.
 * @author Tamas_Szekeres
 */
public abstract class AbstractNewGameController extends AbstractSectionDisplayingController {

    /**
     * Method for acquiring the initialized, basic character.
     * @param locale the used locale
     * @return the new {@link Character} bean
     */
    public abstract Character getCharacter(final Locale locale);

    /**
     * Method for setting up the {@link CharacterHandler} bean for the current run.
     * @param wrapper the {@link HttpSessionWrapper} object
     * @param characterHandler the {@link CharacterHandler} to set up
     */
    protected abstract void setUpCharacterHandler(final HttpSessionWrapper wrapper, final CharacterHandler characterHandler);
}
