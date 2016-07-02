package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.ff.b.character.Ff60Character;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import org.springframework.stereotype.Component;

/**
 * Handler for Ninetail in FF60.
 * @author Tamas_Szekeres
 */
@Component
public class NinetailHandler extends Ff60BeforeAfterRoundEnemyHandler {
    private static final int NINETAIL_HIT_MAX = 6;
    private static final int NINETAIL_HIT_MIN = 5;
    private static final int CLAW_MESSAGE_POSITION = 4;
    private static final int WEAPON_ROLL_POSITION = 3;

    @Override
    public boolean shouldExecutePreHandler(final FightCommand command, final EnemyPrePostFightDataContainer data) {
        return true;
    }

    @Override
    public void executePreHandler(final FightCommand command, final EnemyPrePostFightDataContainer data) {
        int[] preFightRoll;
        preFightRoll = determineFutureTailAttackRoll();
        if (tailWillHitUs(preFightRoll)) {
            command.getResolvedEnemies().get(0).setStaminaDamage(0);
            command.setLuckOnDefense(false);
        } else {
            command.getResolvedEnemies().get(0).setStaminaDamage(2);
        }
        data.setPreFightRoll(preFightRoll);
    }

    @Override
    public boolean shouldExecutePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return results[0] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        final FightCommandMessageList messages = command.getMessages();
        if (tailHitsUs(command, data.getPreFightRoll())) {
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

    private boolean wantToTestLuck(final ResolvationData resolvationData) {
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final String luck = interactionHandler.peekLastFightCommand(resolvationData.getCharacter(), LastFightCommand.LUCK_ON_OTHER);
        return Boolean.parseBoolean(luck);
    }

    private boolean tailHitsUs(final FightCommand command, final int[] ninetailAttackRoll) {
        final FightCommandMessageList messages = command.getMessages();
        messages.addKey(WEAPON_ROLL_POSITION, "page.ff.label.random.after", getRenderer().render(getGenerator().getDefaultDiceSide(), ninetailAttackRoll),
            ninetailAttackRoll[0]);
        return tailWillHitUs(ninetailAttackRoll);
    }

    private void reportTailHit(final FightCommand command, final ResolvationData resolvationData) {
        final int[] randomNumber = getGenerator().getRandomNumber(1);

        final FightCommandMessageList messages = command.getMessages();
        final int damage = randomNumber[0];
        messages.addKey("page.ff.label.random.after", getRenderer().render(getGenerator().getDefaultDiceSide(), randomNumber), damage);

        final Ff60Character character = (Ff60Character) resolvationData.getCharacter();
        character.changeStamina(-damage);
        character.setNineTailDamage(character.getNineTailDamage() + damage);

        command.getMessages().addKey("page.ff60.fight.ninetail.hit", damage);
    }

    private void reportTailMissed(final FightCommand command) {
        command.getMessages().addKey("page.ff60.fight.ninetail.missed");
    }

    private boolean luckTestSucceeds(final FightCommand command, final ResolvationData resolvationData) {
        final int[] randomNumber = getGenerator().getRandomNumber(2);

        final Ff60Character character = (Ff60Character) resolvationData.getCharacter();
        final int luck = resolvationData.getCharacterHandler().getAttributeHandler().resolveValue(character, "luck");

        final boolean testSuccessful = randomNumber[0] <= luck;
        character.changeLuck(-1);

        final FightCommandMessageList messages = command.getMessages();
        final String resultText = messages.resolveKey("page.ff.label.test." + (testSuccessful ? "success" : "failure"));
        messages.addKey("page.ff.label.test.luck.compact", getRenderer().render(getGenerator().getDefaultDiceSide(), randomNumber), randomNumber[0], resultText);

        return testSuccessful;
    }

    @Override
    public String getEnemyId() {
        return "25";
    }

    private boolean tailWillHitUs(final int[] ninetailAttackRoll) {
        return ninetailAttackRoll[0] == NINETAIL_HIT_MIN || ninetailAttackRoll[0] == NINETAIL_HIT_MAX;
    }

    private int[] determineFutureTailAttackRoll() {
        return getGenerator().getRandomNumber(1);

    }

}
