package hu.zagor.gamebooks.content.command.userinput;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.DefaultUserInteractionHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.content.command.userinput.domain.UserInputResponse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

/**
 * User input command resolver.
 * @author Tamas_Szekeres
 */
public class UserInputCommandResolver extends TypeAwareCommandResolver<UserInputCommand> {

    @Override
    public List<ParagraphData> doResolve(final UserInputCommand command, final ResolvationData resolvationData) {
        Assert.state(command.getResponses().size() >= 2, "At least two possible responses (a default and a specific one) has to be set!");
        Assert.notNull(resolvationData, "The parameter 'resolvationData' cannot be null!");

        final ParagraphData rootDataElement = resolvationData.getRootData();
        final Character character = resolvationData.getCharacter();
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();

        Assert.notNull(rootDataElement, "The parameter 'rootDataElement' cannot be null!");
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(characterHandler, "The parameter 'characterHandler' cannot be null!");

        final DefaultUserInteractionHandler interactionHandler = (DefaultUserInteractionHandler) characterHandler.getInteractionHandler();
        final String answer = interactionHandler.getUserInput(character);
        List<ParagraphData> responseList = null;

        command.setOngoing(true);
        if (userAnswerIsAvailable(answer)) {
            final int responseTime = interactionHandler.getUserInputTime(character);
            responseList = new ArrayList<>();
            responseList.addAll(getResultParagraphData(command.getResponses(), answer, responseTime));
            command.setOngoing(false);
        }
        return responseList;
    }

    private List<ParagraphData> getResultParagraphData(final List<UserInputResponse> responses, final String answer, final int responseTime) {
        final List<ParagraphData> match = new ArrayList<ParagraphData>();
        final List<ParagraphData> def = new ArrayList<ParagraphData>();

        for (final UserInputResponse response : responses) {
            if (responseInTime(response, responseTime)) {
                if (response.isFallback()) {
                    def.add(response.getData());
                } else if (response.matches(answer)) {
                    match.add(response.getData());
                }
            }
        }
        return match.isEmpty() ? def : match;
    }

    private boolean responseInTime(final UserInputResponse response, final int responseTime) {
        return response.getMinResponseTime() <= responseTime && responseTime <= response.getMaxResponseTime();
    }

    private boolean userAnswerIsAvailable(final String answer) {
        return answer != null;
    }

}
