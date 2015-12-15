package hu.zagor.gamebooks.content.command.random;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
import hu.zagor.gamebooks.character.handler.ExpressionResolver;
import hu.zagor.gamebooks.character.handler.userinteraction.DefaultUserInteractionHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.SilentCapableResolver;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

/**
 * Main bean for resolving a random roll command.
 * @author Tamas_Szekeres
 */
public class RandomCommandResolver extends TypeAwareCommandResolver<RandomCommand> implements BeanFactoryAware, SilentCapableResolver<RandomCommand> {

    @LogInject private Logger logger;
    @Autowired @Qualifier("d6RandomGenerator") private RandomNumberGenerator generator;
    private BeanFactory beanFactory;
    @Autowired private ExpressionResolver expressionResolver;

    @Override
    public List<ParagraphData> resolveSilently(final Command commandObject, final ResolvationData resolvationData, final List<String> messages, final Locale locale) {
        final RandomCommand command = (RandomCommand) commandObject;

        Assert.notNull(resolvationData, "The parameter 'resolvationData' cannot be null!");

        final List<ParagraphData> responseList = getResultParagraphData(command, messages, locale, resolvationData.getCharacter());
        final List<ParagraphData> clonedResponseList = new ArrayList<>();
        for (final ParagraphData data : responseList) {
            if (data != null) {
                try {
                    clonedResponseList.add(data.clone());
                } catch (final CloneNotSupportedException e) {
                    logger.error("Failed to clone object '{}'.", data);
                }
            }
        }
        return clonedResponseList;
    }

    @Override
    public List<ParagraphData> doResolve(final RandomCommand command, final ResolvationData resolvationData) {
        Assert.notNull(resolvationData, "The parameter 'resolvationData' cannot be null!");

        final ParagraphData rootDataElement = resolvationData.getRootData();
        final Character character = resolvationData.getCharacter();
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();

        Assert.notNull(rootDataElement, "The parameter 'rootDataElement' cannot be null!");
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(characterHandler, "The parameter 'characterHandler' cannot be null!");

        final DefaultUserInteractionHandler interactionHandler = (DefaultUserInteractionHandler) characterHandler.getInteractionHandler();
        final Locale locale = getLocaleProvider().getLocale();
        provideLabel(command, locale);

        final List<String> messages = new ArrayList<>();
        List<ParagraphData> responseList = null;
        if (interactionHandler.hasRandomResult(character)) {
            responseList = getResultParagraphData(command, messages, locale, resolvationData.getCharacter());
            appendText(responseList.get(0), messages.get(0), true);
        } else {
            appendText(rootDataElement, command.getLabel(), false);
            interactionHandler.setRandomResult(character);
        }
        return responseList;
    }

    private void provideLabel(final RandomCommand command, final Locale locale) {
        if (command.getLabel() == null) {
            command.setLabel(getMessageSource().getMessage("page.raw.label.random." + command.getDiceConfig(), null, locale));
        }
    }

    private List<ParagraphData> getResultParagraphData(final RandomCommand command, final List<String> messages, final Locale locale, final Character character) {
        final List<ParagraphData> responseList = new ArrayList<>();

        final DiceConfiguration diceConfiguration = beanFactory.getBean(command.getDiceConfig(), DiceConfiguration.class);
        final int[] diceResults = generator.getRandomNumber(diceConfiguration);
        final int diceResult = diceResults[0];
        logger.debug("Random command generated the number '{}'.", diceResult);

        final String diceText = getDiceRenderer().render(diceConfiguration, diceResults);
        command.setDiceResultText(diceText);

        command.setDiceResult(diceResult);
        command.setDiceResults(diceResults);
        boolean foundResult = false;

        prepare(messages, command, locale);
        for (final RandomResult result : command.getResults()) {
            if (expressionResolver.resolveValue(character, result.getMin()) <= diceResult && diceResult <= expressionResolver.resolveValue(character, result.getMax())) {
                responseList.add(result.getParagraphData());
                foundResult = true;
            }
        }
        if (!foundResult) {
            if (command.getResultElse() != null) {
                responseList.add(command.getResultElse());
            }
        }
        final ParagraphData after = command.getAfter();
        if (after != null) {
            responseList.add(after);
        }

        return responseList;
    }

    private void prepare(final List<String> messages, final RandomCommand command, final Locale locale) {
        messages.add(getMessageSource().getMessage("page.raw.label.random.after", new Object[]{command.getDiceResultText(), command.getDiceResult()}, locale));
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
