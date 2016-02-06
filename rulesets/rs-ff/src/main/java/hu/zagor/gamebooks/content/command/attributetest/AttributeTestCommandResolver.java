package hu.zagor.gamebooks.content.command.attributetest;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.SilentCapableResolver;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.content.dice.DiceConfiguration;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.section.FfRuleBookParagraphResolver;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import hu.zagor.gamebooks.support.locale.LocaleProvider;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.util.Assert;

/**
 * Main bean for resolving an attribute (skill, luck, stamina, etc.) test command.
 * @author Tamas_Szekeres
 */
public class AttributeTestCommandResolver extends TypeAwareCommandResolver<AttributeTestCommand>
    implements BeanFactoryAware, SilentCapableResolver<AttributeTestCommand> {

    @Autowired @Qualifier("d6RandomGenerator") private RandomNumberGenerator generator;
    @Autowired private HierarchicalMessageSource messageSource;
    @Autowired private LocaleProvider localeProvider;
    @Autowired private DiceResultRenderer diceRenderer;
    private BeanFactory beanFactory;
    @LogInject private Logger logger;

    @Override
    public List<ParagraphData> resolveSilently(final Command commandObject, final ResolvationData resolvationData, final List<String> messages, final Locale locale) {
        final AttributeTestCommand command = (AttributeTestCommand) commandObject;

        Assert.state(!command.getSuccess().isEmpty() || !command.getFailure().isEmpty() || command.getFailureEven() != null || command.getFailureOdd() != null,
            "At least one of the two outcomes (success, failure, failureEven, failureOdd) must be specified!");
        Assert.notNull(resolvationData, "The parameter 'resolvationData' cannot be null!");

        final BookInformations info = resolvationData.getInfo();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();

        Assert.notNull(info, "The parameter 'info' cannot be null!");
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(characterHandler, "The parameter 'characterHandler' cannot be null!");

        provideSuccessType(command, resolvationData);
        command.setCompact(true);

        final List<ParagraphData> responseList = new ArrayList<>();
        final FfParagraphData resultParagraphData = getResultParagraphData(command, locale, resolvationData, messages);
        try {
            if (resultParagraphData != null) {
                responseList.add(resultParagraphData.clone());
            }
        } catch (final CloneNotSupportedException e) {
            logger.error("Failed to clone object '{}'.", resultParagraphData);
        }

        if ("luck".equals(command.getAgainst())) {
            characterHandler.getAttributeHandler().handleModification(character, "luck", -1);
        }

        return responseList;
    }

    @Override
    protected List<ParagraphData> doResolve(final AttributeTestCommand command, final ResolvationData resolvationData) {
        Assert.state(!command.getSuccess().isEmpty(), "A success outcome must be specified!");
        Assert.state(!command.getFailure().isEmpty() || (command.getFailureEven() != null && command.getFailureOdd() != null),
            "Either a failure or a failureEven and failureOdd outcome must be specified!");
        Assert.notNull(resolvationData, "The parameter 'resolvationData' cannot be null!");

        final ParagraphData rootDataElement = resolvationData.getParagraph().getData();
        final BookInformations info = resolvationData.getInfo();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();

        Assert.notNull(info, "The parameter 'info' cannot be null!");
        Assert.notNull(rootDataElement, "The parameter 'rootDataElement' cannot be null!");
        Assert.notNull(character, "The parameter 'character' cannot be null!");
        Assert.notNull(characterHandler, "The parameter 'characterHandler' cannot be null!");

        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) info.getCharacterHandler().getInteractionHandler();
        final Locale locale = localeProvider.getLocale();

        provideSuccessType(command, resolvationData);
        provideLabelMessage(command, locale);

        List<ParagraphData> responseList = null;
        if (interactionHandler.hasAttributeTestResult(character)) {
            responseList = new ArrayList<>();
            final List<String> messages = new ArrayList<String>();
            responseList.add(getResultParagraphData(command, locale, resolvationData, messages));
            appendText(responseList.get(0), messages.get(0), true);

            if ("luck".equals(command.getAgainst())) {
                characterHandler.getAttributeHandler().handleModification(character, "luck", -1);
            }
        } else {
            appendText(rootDataElement, command.getLabel(), false);
            interactionHandler.setAttributeTestResult(character);
        }
        return responseList;
    }

    private void provideLabelMessage(final AttributeTestCommand command, final Locale locale) {
        if (command.getLabel() == null) {
            command.setLabel(messageSource.getMessage("page.ff.label.test." + command.getAgainst(), null, locale));
        }
    }

    private void provideSuccessType(final AttributeTestCommand command, final ResolvationData resolvationData) {
        if (command.getSuccessType() == null) {
            final FfRuleBookParagraphResolver paragraphResolver = (FfRuleBookParagraphResolver) resolvationData.getInfo().getParagraphResolver();
            final AttributeTestSuccessType successType = paragraphResolver.getAttributeTestDefaultSuccessType(command.getAgainst());
            if (successType == null) {
                throw new IllegalStateException(
                    "No success type was specified for the current test (" + command.getAgainst() + ") and no default success type was found either.");
            }
            command.setSuccessType(successType);
        }
    }

    FfParagraphData getResultParagraphData(final AttributeTestCommand command, final Locale locale, final ResolvationData resolvationData, final List<String> messages) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();

        final DiceConfiguration diceConfiguration = beanFactory.getBean(command.getConfigurationName(), DiceConfiguration.class);
        final int[] results = generator.getRandomNumber(diceConfiguration, command.getAdd());
        final int result = results[0];
        final String diceRenders = diceRenderer.render(diceConfiguration, results);
        command.setResultString(diceRenders);
        command.setResult(result);
        int againstNumeric = resolveAgainst(command, character, characterHandler);
        if (command.getSuccessType() != AttributeTestSuccessType.lower) {
            againstNumeric += 1;
        }
        command.setAgainstNumeric(againstNumeric);
        FfParagraphData resultData;
        boolean isSuccessful;
        if (result < againstNumeric ^ command.getSuccessType() == AttributeTestSuccessType.higher) {
            isSuccessful = true;
            resultData = selectWithRoll(command.getSuccess(), result);
        } else {
            isSuccessful = false;
            resultData = selectWithRoll(command.getFailure(), result);
            if (resultData == null) {
                if (result % 2 == 0) {
                    resultData = command.getFailureEven();
                } else {
                    resultData = command.getFailureOdd();
                }
            }
        }
        command.setTestSuccess(isSuccessful);
        messages.add(getResultMessage(command, locale, isSuccessful));
        return resultData;
    }

    private FfParagraphData selectWithRoll(final List<SuccessFailureDataContainer> containerList, final int result) {
        SuccessFailureDataContainer withoutRoll = null;
        SuccessFailureDataContainer withRoll = null;
        for (final SuccessFailureDataContainer container : containerList) {
            if (container.getRolled() == null) {
                withoutRoll = container;
            } else if (container.getRolled() == result) {
                withRoll = container;
            }
        }
        final SuccessFailureDataContainer specificContainer = withRoll == null ? withoutRoll : withRoll;
        return specificContainer == null ? null : specificContainer.getData();
    }

    private String getResultMessage(final AttributeTestCommand command, final Locale locale, final boolean isSuccessful) {
        String textResult;
        if (command.isCompact()) {
            textResult = getComactTextResult(command, locale, isSuccessful);
        } else {
            textResult = getTextResult(command, locale, isSuccessful);
        }
        return textResult;
    }

    private String getComactTextResult(final AttributeTestCommand command, final Locale locale, final boolean isSuccessful) {
        final String resultMessage = messageSource.getMessage("page.ff.label.test." + (isSuccessful ? "success" : "failure"), null, locale);
        String against = command.getCompactAgainst();
        if (against == null) {
            against = command.getAgainst();
        }
        return messageSource.getMessage("page.ff.label.test." + against + ".compact", new Object[]{command.getResultString(), command.getResult(), resultMessage},
            locale);
    }

    private String getTextResult(final AttributeTestCommand attributeTestCommand, final Locale locale, final boolean isSuccessful) {
        final String resultMessage = messageSource.getMessage("page.ff.label.test." + (isSuccessful ? "success" : "failure"), null, locale);
        return messageSource.getMessage("page.ff.label.test.after", new Object[]{attributeTestCommand.getResultString(), attributeTestCommand.getResult(), resultMessage},
            locale);
    }

    /**
     * Returns the value against which we will execute the test.
     * @param command the {@link AttributeTestCommand} object
     * @param character the {@link FfCharacter} object
     * @param characterHandler the {@link FfCharacterHandler} object
     * @return the target number
     */
    protected int resolveAgainst(final AttributeTestCommand command, final FfCharacter character, final FfCharacterHandler characterHandler) {
        return characterHandler.getAttributeHandler().resolveValue(character, command.getAgainst());
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
