package hu.zagor.gamebooks.content.command.attributetest;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import java.util.List;
import java.util.Locale;

/**
 * Class for handling attribute test variants specific for the Ff60 adventure.
 * @author Tamas_Szekeres
 */
public class Ff60AttributeTestCommandResolver extends AttributeTestCommandResolver {
    @Override
    FfParagraphData getResultParagraphData(final AttributeTestCommand command, final Locale locale, final ResolvationData resolvationData, final List<String> messages) {
        final Character character = resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        FfParagraphData resultParagraphData;

        if ("skill".equals(command.getAgainst()) && itemHandler.hasItem(character, "4016")) {
            itemHandler.removeItem(character, "4016", 1);
            resultParagraphData = command.getSuccess().get(0).getData();
        } else {
            if (itemHandler.hasItem(character, "3014") && "luck".equals(command.getAgainst())) {
                command.setAdd(command.getAdd() - 1);
            } else if (itemHandler.hasEquippedItem(character, "3018") && "skill".equals(command.getAgainst())) {
                command.setAdd(command.getAdd() + 1);
            }

            resultParagraphData = super.getResultParagraphData(command, locale, resolvationData, messages);
        }
        return resultParagraphData;
    }
}
