package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * Class for resolving an attribute-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckGoldPiecesCommand extends ItemCheckAttributeCommand {

    @Override
    protected int getAttributeValue(final FfCharacter character, final FfCharacterHandler characterHandler, final String attribute) {
        return character.getGold();
    }
}
