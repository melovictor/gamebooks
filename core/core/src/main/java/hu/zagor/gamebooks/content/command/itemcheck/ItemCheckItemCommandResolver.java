package hu.zagor.gamebooks.content.command.itemcheck;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.ParagraphData;

/**
 * Class for resolving an item-type {@link ItemCheckCommand}.
 * @author Tamas_Szekeres
 */
public class ItemCheckItemCommandResolver implements ItemCheckStubCommandResolver {

    @Override
    public ParagraphData resolve(final ItemCheckCommand parent, final ResolvationData resolvationData) {
        ParagraphData toResolve;
        final CharacterItemHandler characterItemHandler = resolvationData.getCharacterHandler().getItemHandler();
        final String id = parent.getId();
        final int amount = parent.getAmount();
        final Character character = resolvationData.getCharacter();
        if (amount == 1 && characterItemHandler.hasEquippedItem(character, id)) {
            toResolve = parent.getHaveEquipped();
        } else if (characterItemHandler.hasItem(character, id, amount)) {
            toResolve = parent.getHave();
        } else {
            toResolve = parent.getDontHave();
        }
        return toResolve;
    }
}
