package hu.zagor.gamebooks.raw.mvc.book.controller;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.raw.character.RawCharacterPageData;

/**
 * Interface for controllers that must provide a {@link CharacterPageDisplayingController} object to the
 * display.
 * @author Tamas_Szekeres
 */
public interface CharacterPageDisplayingController {

    /**
     * Provides the transfer object bringing the character's properties to the frontend.
     * @param character the character
     * @param handler the {@link CharacterHandler} object to use to grab the information from the
     *        {@link Character}
     * @return the transfer object
     */
    RawCharacterPageData getCharacterPageData(final Character character, final CharacterHandler handler);
}
