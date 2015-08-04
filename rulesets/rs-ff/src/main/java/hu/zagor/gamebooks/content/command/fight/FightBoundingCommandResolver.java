package hu.zagor.gamebooks.content.command.fight;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.character.handler.luck.BattleLuckTestParameters;
import hu.zagor.gamebooks.content.FfParagraphData;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.Command;
import hu.zagor.gamebooks.content.command.SilentCapableResolver;
import hu.zagor.gamebooks.content.command.TypeAwareCommandResolver;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.modifyattribute.ModifyAttribute;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;

/**
 * Resolver for {@link FightRoundBoundingCommand}.
 * @author Tamas_Szekeres
 */
public class FightBoundingCommandResolver extends TypeAwareCommandResolver<FightRoundBoundingCommand> {

    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private DiceResultRenderer diceRenderer;
    @Autowired
    private HierarchicalMessageSource messageSource;

    private Map<Class<? extends Command>, SilentCapableResolver<? extends Command>> boundingResolvers;

    @Override
    protected List<ParagraphData> doResolve(final FightRoundBoundingCommand fightBoundingCommand, final ResolvationData resolvationData) {
        List<ParagraphData> result = null;

        if (fightBoundingCommand.isActive()) {
            final FightCommandMessageList messages = fightBoundingCommand.getMessages();
            final Locale locale = messages.getLocale();

            for (final Command command : fightBoundingCommand.getCommands()) {
                final SilentCapableResolver<? extends Command> resolver = boundingResolvers.get(command.getClass());
                if (resolver == null) {
                    throw new IllegalStateException("Cannot find resolver for command '" + command.getClass() + "'!");
                }
                result = resolver.resolveSilently(command, resolvationData, messages, locale);
            }
            if (fightBoundingCommand.isLuckAllowed()) {
                handleLuckTest(resolvationData, result, messages);
            }
        }

        return result;
    }

    private void handleLuckTest(final ResolvationData resolvationData, final List<ParagraphData> result, final FightCommandMessageList messages) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final BattleLuckTestParameters battleLuckTestParameters = characterHandler.getBattleLuckTestParameters();

        if ("true".equals(characterHandler.getInteractionHandler().getLastFightCommand(character, "luckOnOther")) && !result.isEmpty()) {
            final FfParagraphData data = (FfParagraphData) result.get(0);
            for (final ModifyAttribute modification : data.getModifyAttributes()) {
                if ("stamina".equals(modification.getAttribute()) && modification.getAmount() < 0) {

                    final int luck = attributeHandler.resolveValue(character, "luck");
                    final int[] rolled = generator.getRandomNumber(2);

                    String testResultKey;
                    if (rolled[0] <= luck) {
                        testResultKey = "success";
                        modification.setAmount(modification.getAmount() + battleLuckTestParameters.getLuckyDefenseDeduction());
                        if (modification.getAmount() > 0) {
                            modification.setAmount(0);
                        }
                    } else {
                        testResultKey = "failure";
                        modification.setAmount(modification.getAmount() - battleLuckTestParameters.getUnluckyDefenseAddition());
                    }

                    final String testResult = messageSource.getMessage("page.ff.label.test." + testResultKey, null, messages.getLocale());
                    messages.addKey("page.ff.label.test.luck.compact", new Object[]{diceRenderer.render(generator.getDefaultDiceSide(), rolled), rolled[0], testResult});
                    messages.addKey("page.ff.label.fight.luck.defense." + testResultKey + ".unknown");
                    character.changeLuck(-1);
                }
            }
        }
    }

    public void setBoundingResolvers(final Map<Class<? extends Command>, SilentCapableResolver<? extends Command>> boundingResolvers) {
        this.boundingResolvers = boundingResolvers;
    }

}
