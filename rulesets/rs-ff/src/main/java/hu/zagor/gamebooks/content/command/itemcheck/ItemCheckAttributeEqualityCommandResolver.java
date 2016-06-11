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
public class ItemCheckAttributeEqualityCommandResolver extends ItemCheckFfAttributeCommandResolver {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        ParagraphData toResolve;

        String attributeName;
        String acceptedValues;
        final CheckType checkType = parent.getCheckType();
        if (checkType == CheckType.enumAttribute) {
            attributeName = parent.getId();
            acceptedValues = String.valueOf(parent.getAmount());
        } else {
            attributeName = checkType.toString();
            acceptedValues = parent.getId();
        }

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
