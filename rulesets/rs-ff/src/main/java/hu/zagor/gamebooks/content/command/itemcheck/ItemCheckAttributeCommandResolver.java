package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.ff.character.FfCharacter;

/**
 * Class for resolving an attribute-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckAttributeCommandResolver implements ItemCheckStubCommandResolver {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        ParagraphData toResolve;

        final String attribute = parent.getCheckType().toString();
        if (getAttributeValue((FfCharacter) resolvationData.getCharacter(), characterHandler, attribute) >= Integer.parseInt(parent.getId())) {
            toResolve = parent.getHave();
        } else {
            toResolve = parent.getDontHave();
        }
        return toResolve;
    }

    /**
     * Returns the resolved value for a specific attribute.
     * @param character the {@link Character} that is being examined
     * @param characterHandler the {@link FfCharacterHandler} object that can resolve the attribute's value
     * @param attribute the name of the attribute
     * @return the numeric value of the attribute
     */
    protected int getAttributeValue(final FfCharacter character, final FfCharacterHandler characterHandler, final String attribute) {
        return characterHandler.getAttributeHandler().resolveValue(character, attribute);
    }

}
