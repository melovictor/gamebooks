package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import org.springframework.stereotype.Component;

/**
 * Handler for Ramatu's full fledged battle in FF60.
 * @author Tamas_Szekeres
 */
@Component
public class RamatuHandler extends Ff60BeforeAfterRoundEnemyHandler {

    private static final int BLOOD_FIRE_EXTRA_DAMAGE = -2;
    private static final int ROUND_MESSAGE_LOCATION_WITH_ROLL = 4;
    private static final int ROUND_MESSAGE_LOCATION_WITHOUT_ROLL = 3;
    private static final int RANDOM_ROLL_LOCATION = 3;
    private static final int RAMATU_NORMAL_DAMAGE_ROUND = 4;

    @Override
    public boolean shouldExecutePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        return command.getRoundNumber() < RAMATU_NORMAL_DAMAGE_ROUND && results[0] == FightRoundResult.LOSE;
    }

    @Override
    public void executePostHandler(final FfFightCommand command, final ResolvationData resolvationData, final FightRoundResult[] results,
        final EnemyPrePostFightDataContainer data) {
        switch (command.getRoundNumber()) {
        case 1:
            executeMagicArrows(command, resolvationData);
            break;
        case 2:
            executeCurseOfQuezkari(command, resolvationData);
            break;
        default:
            executeBloodFire(command, resolvationData);
        }
    }

    private void executeBloodFire(final FfFightCommand command, final ResolvationData resolvationData) {
        final FightCommandMessageList messages = command.getMessages();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        messages.addKey(ROUND_MESSAGE_LOCATION_WITHOUT_ROLL, "page.ff60.fight.ramatu.round3");
        character.changeStamina(BLOOD_FIRE_EXTRA_DAMAGE);
    }

    private void executeCurseOfQuezkari(final FfFightCommand command, final ResolvationData resolvationData) {
        final FightCommandMessageList messages = command.getMessages();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        messages.addKey(ROUND_MESSAGE_LOCATION_WITHOUT_ROLL, "page.ff60.fight.ramatu.round2");
        character.changeLuck(-1);
        character.changeSkill(-1);
        character.changeStamina(-1);
    }

    private void executeMagicArrows(final FfFightCommand command, final ResolvationData resolvationData) {
        final FightCommandMessageList messages = command.getMessages();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final int[] damage = getGenerator().getRandomNumber(1);
        character.changeStamina(2 - damage[0]);
        final String diceImage = getRenderer().render(getGenerator().getDefaultDiceSide(), damage);
        messages.addKey(RANDOM_ROLL_LOCATION, "page.ff.label.random.after", diceImage, damage[0]);
        messages.addKey(ROUND_MESSAGE_LOCATION_WITH_ROLL, "page.ff60.fight.ramatu.round1", damage[0]);
    }

    @Override
    protected String getEnemyId() {
        return "93";
    }

}
