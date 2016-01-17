package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import java.util.Collection;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * All at once fight round resolver for Sor2.
 * @author Tamas_Szekeres
 */
@Component("allAtOncesor2FightRoundResolver")
public class AllAtOnceSor2FightRoundResolver extends AllAtOnceFightRoundResolver {
    private static final int SELF_AMONG_THE_MIRRORS = 6;
    private static final int HEAD_MINIMAL_ATTACK_STRENGTH = 5;
    private static final int BODY_PARTS_ENEMY_BASE_ID = 12;
    @Resource(name = "sor2BodyPartsEnemyIds") private Collection<String> bodyPartsEnemyIds;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfUserInteractionHandler interactionHandler = characterHandler.getInteractionHandler();
        final String enemyId = interactionHandler.peekLastFightCommand(character, "enemyId");
        if (bodyPartsEnemyIds.contains(enemyId)) {
            setUpNewTarget(command.getMessages(), characterHandler, resolvationData);
        }

        return super.resolveRound(command, resolvationData, beforeRoundResult);
    }

    private void setUpNewTarget(final FightCommandMessageList messages, final FfCharacterHandler characterHandler, final ResolvationData resolvationData) {
        final int[] randomNumber = getGenerator().getRandomNumber(1);
        final int enemyNumber = randomNumber[0];
        messages.addKey("page.ff.label.random.after", getDiceResultRenderer().render(getGenerator().getDefaultDiceSide(), randomNumber), enemyNumber);
        messages.addKey("page.sor2.fight.bodyparts.enemy" + enemyNumber);
        final SorCharacter character = (SorCharacter) resolvationData.getCharacter();
        final String enemyId = String.valueOf(BODY_PARTS_ENEMY_BASE_ID + enemyNumber);
        characterHandler.getInteractionHandler().setFightCommand(character, LastFightCommand.ENEMY_ID, enemyId);
        if (hasSixActive(character, characterHandler.getItemHandler())) {
            handleAutoMissingOfHead(resolvationData, messages);
        }
    }

    private void handleAutoMissingOfHead(final ResolvationData resolvationData, final FightCommandMessageList messages) {
        final FfEnemy enemy = (FfEnemy) resolvationData.getEnemies().get("13");
        final int[] randomNumber = getGenerator().getRandomNumber(1);
        final int roll = randomNumber[0];
        messages.addKey("page.ff.label.random.after", getDiceResultRenderer().render(getGenerator().getDefaultDiceSide(), randomNumber), roll);
        if (roll == SELF_AMONG_THE_MIRRORS) {
            enemy.setAttackStrength(0);
            messages.addKey("page.sor2.fight.bodyparts.enemy1.hitsUs");
        } else {
            enemy.setAttackStrength(HEAD_MINIMAL_ATTACK_STRENGTH);
            messages.addKey("page.sor2.fight.bodyparts.enemy1.hitsMirrorImage");
        }
    }

    private boolean hasSixActive(final SorCharacter character, final FfCharacterItemHandler itemHandler) {
        return itemHandler.hasItem(character, "4020");
    }
}
