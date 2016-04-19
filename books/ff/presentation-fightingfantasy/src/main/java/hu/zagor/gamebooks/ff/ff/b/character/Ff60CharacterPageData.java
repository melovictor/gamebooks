package hu.zagor.gamebooks.ff.ff.b.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data object for FF60.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Ff60CharacterPageData extends FfCharacterPageData {
    private final int time;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy 60 book.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff60CharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character, handler);
        final Ff60Character chr = (Ff60Character) character;
        time = chr.getTime();
    }

    public int getTime() {
        return time;
    }

}
