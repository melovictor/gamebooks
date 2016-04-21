package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.ParagraphData;

/**
 * Class for resolving a codeword-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckCodewordCommandResolver implements ItemCheckStubCommandResolver {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        final Character character = resolvationData.getCharacter();
        final String codeword = parent.getId();
        final ParagraphData data;
        if (character.getCodeWords().contains(codeword)) {
            data = parent.getHave();
        } else {
            data = parent.getDontHave();
        }
        return data;
    }

}
