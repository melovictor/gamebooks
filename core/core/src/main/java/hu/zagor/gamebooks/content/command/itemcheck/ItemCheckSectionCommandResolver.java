package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;

/**
 * Class for resolving a section-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckSectionCommandResolver implements ItemCheckStubCommandResolver {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        ParagraphData toResolve;

        if (resolvationData.getCharacterHandler().getParagraphHandler().visitedParagraph(resolvationData.getCharacter(), parent.getId())) {
            toResolve = parent.getHave();
        } else {
            toResolve = parent.getDontHave();
        }
        return toResolve;
    }
}
