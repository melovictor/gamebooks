package hu.zagor.gamebooks.content.command.fight.subresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Class for resolving the {@link FightCommand} object in the basic mode.
 * @author Tamas_Szekeres
 */
@Component
public class FightCommandBasicSubResolver extends AbstractFightCommandSubResolver {

    @Override
    protected void prepareLuckTest(final FightCommand command, final FfCharacter character, final FfUserInteractionHandler interactionHandler) {
        command.setLuckOnHit(Boolean.valueOf(interactionHandler.getLastFightCommand(character, "luckOnHit")));
        command.setLuckOnDefense(Boolean.valueOf(interactionHandler.getLastFightCommand(character, "luckOnDefense")));
    }

    @Override
    protected FfCharacter resolveCharacter(final FightCommand command, final ResolvationData resolvationData) {
        return (FfCharacter) resolvationData.getCharacter();
    }

    @Override
    protected boolean aliveAfterResolvation(final ResolvationData resolvationData, final List<ParagraphData> resolveList) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();

        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();

        for (final ParagraphData data : resolveList) {
            final FfParagraphData ffData = (FfParagraphData) data;
            resolveAttributeChanges(ffData, character, attributeHandler);
        }

        return attributeHandler.isAlive(character);

    }

    private void resolveAttributeChanges(final FfParagraphData ffData, final FfCharacter character, final FfAttributeHandler attributeHandler) {
        final List<ModifyAttribute> modifyAttributes = ffData.getModifyAttributes();
        for (final ModifyAttribute modify : modifyAttributes) {
            attributeHandler.handleModification(character, modify);
        }
        modifyAttributes.clear();
    }
}
