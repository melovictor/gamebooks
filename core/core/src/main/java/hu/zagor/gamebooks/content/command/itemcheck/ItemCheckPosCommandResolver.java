package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;

/**
 * Class for resolving a choice position-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckPosCommandResolver extends ItemCheckAttributeCommandResolver {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        final int chosenPosition = resolvationData.getPosition();
        final int expectedPosition = Integer.parseInt(parent.getId());
        return chosenPosition == expectedPosition ? parent.getHave() : parent.getDontHave();
    }
}
