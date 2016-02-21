package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * Class for resolving an attribute-type {@link ItemCheckCommand} where we expect equality, instead of greater-than-or-equal-to.
 * @author Tamas_Szekeres
 */
public class ItemCheckAttributeEqualityCommand extends ItemCheckAttributeCommand {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        ParagraphData toResolve;

        final String attribute = parent.getCheckType().toString();
        if (getAttributeValue((FfCharacter) resolvationData.getCharacter(), characterHandler, attribute) == Integer.parseInt(parent.getId())) {
            toResolve = parent.getHave();
        } else {
            toResolve = parent.getDontHave();
        }
        return toResolve;
    }

}
