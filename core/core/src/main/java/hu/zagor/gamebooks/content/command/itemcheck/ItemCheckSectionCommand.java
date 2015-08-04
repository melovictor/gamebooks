package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.ParagraphData;

/**
 * Class for resolving a section-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckSectionCommand implements ItemCheckStubCommand {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final Character character, final CharacterHandler characterHandler) {
        ParagraphData toResolve;

        if (characterHandler.getParagraphHandler().visitedParagraph(character, parent.getId())) {
            toResolve = parent.getHave();
        } else {
            toResolve = parent.getDontHave();
        }
        return toResolve;
    }
}
