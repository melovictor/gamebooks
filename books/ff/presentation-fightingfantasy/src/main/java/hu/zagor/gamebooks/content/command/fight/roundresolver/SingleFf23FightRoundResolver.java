package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.renderer.DiceResultRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Single fight round resolver for FF23.
 * @author Tamas_Szekeres
 */
@Component("singleff23FightRoundResolver")
public class SingleFf23FightRoundResolver implements FfFightRoundResolver {

    private static final int DAMAGE_WITHOUT_BRANCH = 2;
    private static final int DAMAGE_WITH_BRANCH = 3;
    private static final int ROLL_PICK_BRANCH_LOWER_LIMIT = 4;
    private static final int ROLL_BURN_SELF_UPPER_LIMIT = 3;
    @Autowired
    @Qualifier("singleFightRoundResolver")
    private FfFightRoundResolver superResolver;
    @Autowired
    @Qualifier("d6")
    private RandomNumberGenerator generator;
    @Autowired
    private DiceResultRenderer renderer;

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {

        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfUserInteractionHandler interactionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();

        final String enemyId = interactionHandler.peekLastFightCommand(character, "enemyId");
        if ("46".equals(enemyId) || "47".equals(enemyId) || "48".equals(enemyId) || "49".equals(enemyId) || "50".equals(enemyId)) {
            final String lastEnemyId = interactionHandler.peekLastFightCommand(character, "lastEnemyId");
            if (lastEnemyId == null || !lastEnemyId.equals(enemyId)) {
                final FightCommandMessageList messages = command.getMessages();
                interactionHandler.setFightCommand(character, "lastEnemyId", enemyId);

                final int[] randomNumber = generator.getRandomNumber(1);
                messages.switchToPreRoundMessages();
                messages.addKey("page.ff.label.random.after", randomNumber[0], renderer.render(generator.getDefaultDiceSide(), randomNumber));
                int damage = DAMAGE_WITHOUT_BRANCH;
                if (randomNumber[0] < ROLL_BURN_SELF_UPPER_LIMIT) {
                    messages.addKey("page.ff23.branch.burnSelf");
                    character.changeStamina(-1);
                } else if (randomNumber[0] > ROLL_PICK_BRANCH_LOWER_LIMIT) {
                    messages.addKey("page.ff23.branch.burns");
                    damage = DAMAGE_WITH_BRANCH;
                } else {
                    messages.addKey("page.ff23.branch.extinguished");
                }
                final FfItem item = (FfItem) resolvationData.getCharacterHandler().getItemHandler().getItem(character, "1001");
                item.setStaminaDamage(damage);
            }
        }

        return superResolver.resolveRound(command, resolvationData, beforeRoundResult);
    }

    @Override
    public void resolveFlee(final FfFightCommand command, final ResolvationData resolvationData) {
        superResolver.resolveFlee(command, resolvationData);
    }

}
