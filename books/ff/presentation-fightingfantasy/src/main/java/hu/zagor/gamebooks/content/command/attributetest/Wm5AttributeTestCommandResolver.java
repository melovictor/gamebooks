package hu.zagor.gamebooks.content.command.attributetest;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import java.util.List;
import java.util.Locale;

/**
 * Class for handling attribute test variants specific for the Warlock 5 adventure.
 * @author Tamas_Szekeres
 */
public class Wm5AttributeTestCommandResolver extends AttributeTestCommandResolver {

    @Override
    FfParagraphData getResultParagraphData(final AttributeTestCommand command, final Locale locale, final ResolvationData resolvationData, final List<String> messages) {
        final Character character = resolvationData.getCharacter();
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();

        if (characterHandler.getParagraphHandler().visitedParagraph(character, "197") && "luck".equals(command.getAgainst())) {
            command.setAdd(command.getAdd() + 1);
        }

        return super.getResultParagraphData(command, locale, resolvationData, messages);
    }
}
