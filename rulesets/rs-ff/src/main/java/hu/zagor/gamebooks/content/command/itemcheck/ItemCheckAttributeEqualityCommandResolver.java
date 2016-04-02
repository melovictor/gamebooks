package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.Arrays;
import java.util.List;

/**
 * Class for resolving an attribute-type {@link ItemCheckCommand} where we expect equality, instead of greater-than-or-equal-to.
 * @author Tamas_Szekeres
 */
public class ItemCheckAttributeEqualityCommandResolver extends ItemCheckAttributeCommandResolver {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        ParagraphData toResolve;

        final String attributeName = parent.getCheckType().toString();
        final String acceptedValues = parent.getId();
        final List<String> acceptedList = Arrays.asList(acceptedValues.split(","));
        final int attributeValue = getAttributeValue((FfCharacter) resolvationData.getCharacter(), characterHandler, attributeName);
        if (acceptedList.contains(String.valueOf(attributeValue))) {
            toResolve = parent.getHave();
        } else {
            toResolve = parent.getDontHave();
        }
        return toResolve;
    }

}
