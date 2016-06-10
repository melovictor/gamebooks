package hu.zagor.gamebooks.ff.ff.sots.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.ff.character.FfCharacterPageData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Character page data for FF20.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class Ff20CharacterPageData extends FfCharacterPageData {
    private final int honor;
    private final SpecialSkill specialSkill;

    /**
     * Basic constructor.
     * @param character the {@link Ff20Character} object
     * @param handler the {@link FfCharacterHandler} object
     */
    public Ff20CharacterPageData(final Ff20Character character, final FfCharacterHandler handler) {
        super(character, handler);
        honor = character.getHonor();
        specialSkill = SpecialSkill.DZSADZSUTSZU;
    }

    public int getHonor() {
        return honor;
    }

    public SpecialSkill getSpecialSkill() {
        return specialSkill;
    }

}
