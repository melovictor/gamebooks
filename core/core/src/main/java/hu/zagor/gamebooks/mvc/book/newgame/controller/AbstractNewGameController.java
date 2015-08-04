package hu.zagor.gamebooks.mvc.book.newgame.controller;

import hu.zagor.gamebooks.character.Character;
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

}
