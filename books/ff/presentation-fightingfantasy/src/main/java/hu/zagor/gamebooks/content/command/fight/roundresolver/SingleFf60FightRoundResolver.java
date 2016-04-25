package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.SilentCapableResolver;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.random.RandomCommand;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Single fight round resolver for FF60.
 * @author Tamas_Szekeres
 */
public class SingleFf60FightRoundResolver implements FightRoundResolver {
    private static final int HOOK_MAN_EXTRA_HIT = 4;
    private static final String OGRE_ID = "4";
    @Autowired @Qualifier("singleFightRoundResolver") private FightRoundResolver decorated;
    @Autowired @Qualifier("randomCommandResolver") private SilentCapableResolver<RandomCommand> randomCommandResolver;
    private RandomCommand ogreRandomCommand;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private DiceResultRenderer renderer;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FfEnemy enemy = getEnemy(resolvationData);

        final FightRoundResult[] results = decorated.resolveRound(command, resolvationData, beforeRoundResult);

        if (enemyIsOgre(enemy)) {
            handleOgre(command, resolvationData, results);
        } else if (enemyIsHookMan(enemy) && results[0] == FightRoundResult.LOSE) {
            handleHookMan(command, resolvationData);
        }

        return results;
    }

    private void handleHookMan(final FightCommand command, final ResolvationData resolvationData) {
        final FightCommandMessageList messages = command.getMessages();
        messages.switchToPostRoundMessages();
        final int[] randomNumber = generator.getRandomNumber(1);
        final String dice = renderer.render(generator.getDefaultDiceSide(), randomNumber);
        messages.addKey("page.ff.label.random.after", dice, randomNumber[0]);
        if (randomNumber[0] > HOOK_MAN_EXTRA_HIT) {
            final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
            character.changeStamina(-1);
        }
    }

    private boolean enemyIsHookMan(final FfEnemy enemy) {
        return "19".equals(enemy.getId());
    }

    private void handleOgre(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results) {
        final Character character = resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        itemHandler.removeItem(character, "4001", 2);
        if (results[0] == FightRoundResult.LOSE) {
            final FightCommandMessageList messages = command.getMessages();
            try {
                messages.switchToPostRoundMessages();
                final List<ParagraphData> randomResult = randomCommandResolver.resolveSilently(ogreRandomCommand.clone(), resolvationData, messages,
                    messages.getLocale());
                if (!randomResult.isEmpty()) {
                    itemHandler.addItem(character, "4001", 2);
                    messages.addKey("page.ff60.fight.ogre.push");
                }
            } catch (final CloneNotSupportedException ex) {
                throw new IllegalStateException("Failed to clone random command.", ex);
            }
        }
    }

    private boolean enemyIsOgre(final FfEnemy enemy) {
        return OGRE_ID.equals(enemy.getId());
    }

    private FfEnemy getEnemy(final ResolvationData resolvationData) {
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final String enemyId = interactionHandler.peekLastFightCommand(character, LastFightCommand.ENEMY_ID);
        return (FfEnemy) resolvationData.getEnemies().get(enemyId);
    }

    @Override
    public void resolveFlee(final FightCommand command, final ResolvationData resolvationData) {
        decorated.resolveFlee(command, resolvationData);
    }

    public void setOgreRandomCommand(final RandomCommand ogreRandomCommand) {
        this.ogreRandomCommand = ogreRandomCommand;
    }

}
