package hu.zagor.gamebooks.ff.ff.b.character;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
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
    private final boolean skillSpell;
    private final boolean luckSpell;

    /**
     * Bean for storing data to display on the character page for Fighting Fantasy 60 book.
     * @param character the character
     * @param handler the {@link FfCharacterHandler} to use for obtaining the characters' properties
     */
    public Ff60CharacterPageData(final FfCharacter character, final FfCharacterHandler handler) {
        super(character, handler);
        final Ff60Character chr = (Ff60Character) character;
        time = chr.getTime();
        final FfCharacterItemHandler itemHandler = handler.getItemHandler();
        luckSpell = itemHandler.hasItem(character, "4007");
        skillSpell = itemHandler.hasItem(character, "4008");
    }

    public int getTime() {
        return time;
    }

    public boolean isSkillSpell() {
        return skillSpell;
    }

    public boolean isLuckSpell() {
        return luckSpell;
    }

}
