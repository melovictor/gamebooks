package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.attribute.AttributeHandler;
import hu.zagor.gamebooks.content.ParagraphData;

/**
 * Class for resolving an attribute-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckAttributeCommandResolver implements ItemCheckStubCommandResolver {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        final AttributeHandler attributeHandler = resolvationData.getCharacterHandler().getAttributeHandler();
        final String attributeName = parent.getId();
        final int attributeAmount = parent.getAmount();

        final ParagraphData data;

        final int resolvedValue = attributeHandler.resolveValue(resolvationData.getCharacter(), attributeName);
        if (resolvedValue < attributeAmount) {
            data = parent.getDontHave();
        } else {
            data = parent.getHave();
        }

        return data;
    }

}
