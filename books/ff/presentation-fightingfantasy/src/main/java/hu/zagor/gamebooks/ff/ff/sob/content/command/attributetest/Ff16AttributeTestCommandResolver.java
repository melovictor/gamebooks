package hu.zagor.gamebooks.ff.ff.sob.content.command.attributetest;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommand;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestCommandResolver;
import hu.zagor.gamebooks.content.command.attributetest.AttributeTestSuccessType;
import java.util.Arrays;
import java.util.List;

/**
 * FF16-specific attribute test resolver.
 * @author Tamas_Szekeres
 */
public class Ff16AttributeTestCommandResolver extends AttributeTestCommandResolver {

    private static final int GHOST_HELPING = 2;
    private static final int WIND_HELPING = 4;

    @Override
    protected List<ParagraphData> doResolve(final AttributeTestCommand command, final ResolvationData resolvationData) {
        List<ParagraphData> doResolve;

        if (crewStrengthTest(command)) {
            if (autoLoseRoll(resolvationData)) {
                doResolve = Arrays.asList((ParagraphData) command.getFailure().get(0).getData());
            } else {
                configureCrewStrengthParameters(command, resolvationData);
                doResolve = super.doResolve(command, resolvationData);
            }
        } else {
            doResolve = super.doResolve(command, resolvationData);
        }
        return doResolve;
    }

    private boolean autoLoseRoll(final ResolvationData resolvationData) {
        final Character character = resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        return itemHandler.hasItem(character, "4016");
    }

    private void configureCrewStrengthParameters(final AttributeTestCommand command, final ResolvationData resolvationData) {
        command.setSuccessType(AttributeTestSuccessType.lower);

        final Character character = resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();

        if (itemHandler.hasItem(character, "4017")) {
            command.setConfigurationName("dice2d6");
        } else {
            command.setConfigurationName("dice3d6");
        }

        int addition = 0;
        if (itemHandler.hasItem(character, "4014")) {
            addition -= GHOST_HELPING;
        }
        if (itemHandler.hasItem(character, "4015")) {
            addition -= WIND_HELPING;
        }
        command.setAdd(addition);
    }

    private boolean crewStrengthTest(final AttributeTestCommand command) {
        return "crewStrength".equals(command.getAgainst());
    }

}
