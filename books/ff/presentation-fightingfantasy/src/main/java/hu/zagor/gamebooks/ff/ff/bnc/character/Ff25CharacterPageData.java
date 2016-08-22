package hu.zagor.gamebooks.ff.ff.bnc.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data object for FF25.
 * @author Tamas_Szekeres
 */
@Component("ff25CharacterPageData")
@Scope("prototype")
public class Ff25CharacterPageData extends FfCharacterPageData {
    private int willpower;
    private int initialWillpower;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy 25 book.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff25CharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character, handler);

        final FfAttributeHandler attributeHandler = handler.getAttributeHandler();
        initialWillpower = attributeHandler.resolveValue(character, "initialWillpower");
        willpower = attributeHandler.resolveValue(character, "willpower");
    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(final int willpower) {
        this.willpower = willpower;
    }

    public int getInitialWillpower() {
        return initialWillpower;
    }

    public void setInitialWillpower(final int initialWillpower) {
        this.initialWillpower = initialWillpower;
    }

}
