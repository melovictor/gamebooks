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
import hu.zagor.gamebooks.ff.ff.b.character.Ff60Character;
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
    private static final int WEAPON_ROLL_POSITION = 3;
    private static final int CLAW_MESSAGE_POSITION = 4;
    private static final int NINETAIL_HIT_MAX = 6;
    private static final int NINETAIL_HIT_MIN = 5;
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

        int[] ninetailAttackRoll = null;
        if (enemyIsNintail(enemy)) {
            ninetailAttackRoll = determineFutureTailAttackRoll();
            if (tailWillHitUs(ninetailAttackRoll)) {
                command.getResolvedEnemies().get(0).setStaminaDamage(0);
                command.setLuckOnDefense(false);
            } else {
                command.getResolvedEnemies().get(0).setStaminaDamage(2);
            }
        }

        final FightRoundResult[] results = decorated.resolveRound(command, resolvationData, beforeRoundResult);

        if (enemyIsOgre(enemy)) {
            handleOgre(command, resolvationData, results);
        } else if (enemyIsHookMan(enemy) && results[0] == FightRoundResult.LOSE) {
            handleHookMan(command, resolvationData);
        } else if (enemyIsNintail(enemy) && results[0] == FightRoundResult.LOSE) {
            handleNinetail(command, resolvationData, ninetailAttackRoll);
        }

        return results;
    }

    private int[] determineFutureTailAttackRoll() {
        return generator.getRandomNumber(1);

    }

    private void handleNinetail(final FightCommand command, final ResolvationData resolvationData, final int[] ninetailAttackRoll) {
        final FightCommandMessageList messages = command.getMessages();
        if (tailHitsUs(command, ninetailAttackRoll)) {
            messages.addKey("page.ff60.fight.ninetail.withTail");
            if (wantToTestLuck(resolvationData) && luckTestSucceeds(command, resolvationData)) {
                reportTailMissed(command);
            } else {
                reportTailHit(command, resolvationData);
            }
        } else {
            messages.addKey(CLAW_MESSAGE_POSITION, "page.ff60.fight.ninetail.withClaws");
        }
    }

    private void reportTailHit(final FightCommand command, final ResolvationData resolvationData) {
        final int[] randomNumber = generator.getRandomNumber(1);

        final FightCommandMessageList messages = command.getMessages();
        final int damage = randomNumber[0];
        messages.addKey("page.ff.label.random.after", renderer.render(generator.getDefaultDiceSide(), randomNumber), damage);

        final Ff60Character character = (Ff60Character) resolvationData.getCharacter();
        character.changeStamina(-damage);
        character.setNineTailDamage(character.getNineTailDamage() + damage);

        command.getMessages().addKey("page.ff60.fight.ninetail.hit", damage);
    }

    private void reportTailMissed(final FightCommand command) {
        command.getMessages().addKey("page.ff60.fight.ninetail.missed");
    }

    private boolean luckTestSucceeds(final FightCommand command, final ResolvationData resolvationData) {
        final int[] randomNumber = generator.getRandomNumber(2);

        final Ff60Character character = (Ff60Character) resolvationData.getCharacter();
        final int luck = resolvationData.getCharacterHandler().getAttributeHandler().resolveValue(character, "luck");

        final boolean testSuccessful = randomNumber[0] <= luck;
        character.changeLuck(-1);

        final FightCommandMessageList messages = command.getMessages();
        final String resultText = messages.resolveKey("page.ff.label.test." + (testSuccessful ? "success" : "failure"));
        messages.addKey("page.ff.label.test.luck.compact", renderer.render(generator.getDefaultDiceSide(), randomNumber), randomNumber[0], resultText);

        return testSuccessful;
    }

    private boolean wantToTestLuck(final ResolvationData resolvationData) {
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final String luck = interactionHandler.peekLastFightCommand((FfCharacter) resolvationData.getCharacter(), LastFightCommand.LUCK_ON_OTHER);
        return Boolean.parseBoolean(luck);
    }

    private boolean tailHitsUs(final FightCommand command, final int[] ninetailAttackRoll) {
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey(WEAPON_ROLL_POSITION, "page.ff.label.random.after", renderer.render(generator.getDefaultDiceSide(), ninetailAttackRoll), ninetailAttackRoll[0]);
        return tailWillHitUs(ninetailAttackRoll);
    }

    private boolean tailWillHitUs(final int[] ninetailAttackRoll) {
        return ninetailAttackRoll[0] == NINETAIL_HIT_MIN || ninetailAttackRoll[0] == NINETAIL_HIT_MAX;
    }

    private boolean enemyIsNintail(final FfEnemy enemy) {
        return "25".equals(enemy.getId());
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
