package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.domain.LastFightCommand;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Implementation for Sor4.
 * @author Tamas_Szekeres
 */
@Component("singleAllysor4FightRoundResolver")
public class SingleAllySor4FightRoundResolver extends SingleAllySorFightRoundResolver {
    private static final int ENEMY_ALLY_RED_EYE_SHIFT = 108 - 19;
    @Resource(name = "sor4RedEyeAllies") private Set<String> redEyeAllies;

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final Character character = resolvationData.getCharacter();
        final boolean redEyeAlly = isRedEyeAlly(character);
        if (redEyeAlly) {
            selectProperEnemy(resolvationData);
        }

        final FightRoundResult[] resolveRound = super.resolveRound(command, resolvationData, beforeRoundResult);

        if (redEyeAlly) {
            final FfAllyCharacter allyCharacter = (FfAllyCharacter) character;
            final String enemyId = getEnemyId(allyCharacter);
            final FfEnemy enemy = (FfEnemy) resolvationData.getEnemies().get(enemyId);
            if (enemy.getStamina() <= 0) {
                allyCharacter.setStamina(0);
            }
        }

        return resolveRound;
    }

    private void selectProperEnemy(final ResolvationData resolvationData) {
        final FfAllyCharacter character = (FfAllyCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final String enemyId = getEnemyId(character);

        characterHandler.getInteractionHandler().setFightCommand(character, LastFightCommand.ENEMY_ID, enemyId);
    }

    private String getEnemyId(final FfAllyCharacter character) {
        final int allyId = Integer.valueOf(getAllyId(character));
        return String.valueOf(allyId - ENEMY_ALLY_RED_EYE_SHIFT);
    }

    private boolean isRedEyeAlly(final Character character) {
        boolean isRedEye = false;
        if (character instanceof FfAllyCharacter) {
            isRedEye = redEyeAllies.contains(getAllyId(character));
        }
        return isRedEye;
    }

    private String getAllyId(final Character character) {
        final FfAllyCharacter allyCharacter = (FfAllyCharacter) character;
        final FfEnemy allyEnemy = allyCharacter.getAlly();
        return allyEnemy.getId();
    }

    @Override
    void doLoseFight(final FightCommand command, final FightRoundResult[] result, final int enemyIdx, final FightDataDto dto) {
        final FfAllyCharacter firstAlly = command.getFirstAlly();
        final FfCharacter character = dto.getCharacter();

        if (firstAlly == character || isRedEyeAlly(character)) {
            result[enemyIdx] = FightRoundResult.LOSE;
            damageSelf(dto);
        } else {
            dto.getMessages().addKey("page.ff.label.fight.single.failedAttack.ally", new Object[]{dto.getEnemy().getName(), character.getName()});
        }
    }

}
