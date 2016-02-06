package hu.zagor.gamebooks.content.command.attributetest;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import java.util.List;
import java.util.Locale;

/**
 * Main bean for resolving an attribute (skill, luck, stamina, etc.) test command.
 * @author Tamas_Szekeres
 */
public class SorAttributeTestCommandResolver extends AttributeTestCommandResolver {
    private static final int HIGH_LUCK = 15;
    private static final String LUCK_TALISMAN_HIGHER_CHANCE = "3023";
    private static final String LUCK_TALISMAN_NO_FAILURE_PENALTY = "3051";

    @Override
    FfParagraphData getResultParagraphData(final AttributeTestCommand command, final Locale locale, final ResolvationData resolvationData, final List<String> messages) {
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        final SorCharacter character = (SorCharacter) resolvationData.getCharacter();
        if ("luck".equals(command.getAgainst())) {
            if (itemHandler.hasEquippedItem(character, LUCK_TALISMAN_HIGHER_CHANCE)) {
                command.setAdd(command.getAdd() - 1);
            }
            if (character.isLuckCookieActive()) {
                command.setAgainstNumeric(0);
            }
        }
        final FfParagraphData resultParagraphData = super.getResultParagraphData(command, locale, resolvationData, messages);
        if (itemHandler.hasEquippedItem(character, LUCK_TALISMAN_NO_FAILURE_PENALTY) && !command.isTestSuccess()) {
            character.changeLuck(1);
        }

        return resultParagraphData;
    }

    @Override
    protected int resolveAgainst(final AttributeTestCommand command, final FfCharacter characterObject, final FfCharacterHandler characterHandler) {
        final SorCharacter character = (SorCharacter) characterObject;
        int resolveAgainst;
        if (character.isLuckCookieActive()) {
            resolveAgainst = HIGH_LUCK;
            command.setAgainst("supportedLuck");
        } else {
            resolveAgainst = super.resolveAgainst(command, character, characterHandler);
        }
        return resolveAgainst;
    }

}
