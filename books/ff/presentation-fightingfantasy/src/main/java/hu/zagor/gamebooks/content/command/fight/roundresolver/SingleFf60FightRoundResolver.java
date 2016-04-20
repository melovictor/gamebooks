package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.CharacterHandler;
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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Single fight round resolver for FF60.
 * @author Tamas_Szekeres
 */
public class SingleFf60FightRoundResolver implements FightRoundResolver {
    private static final String OGRE_ID = "4";
    @Autowired @Qualifier("singleFightRoundResolver") private FightRoundResolver decorated;
    @Autowired @Qualifier("randomCommandResolver") private SilentCapableResolver<RandomCommand> randomCommandResolver;
    private RandomCommand ogreRandomCommand;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final CharacterHandler characterHandler = resolvationData.getCharacterHandler();
        final Character character = resolvationData.getCharacter();

        final FfEnemy enemy = getEnemy(resolvationData);

        final FightRoundResult[] results = decorated.resolveRound(command, resolvationData, beforeRoundResult);

        if (enemyIsOgre(enemy)) {
            final CharacterItemHandler itemHandler = characterHandler.getItemHandler();
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

        return results;
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
