package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Resolver for a single fight round where there are multiple enemies, all of which attack at the same time.
 * @author Tamas_Szekeres
 */
@Component("allAtOnceFightRoundResolver")
public class AllAtOnceFightRoundResolver extends SingleFightRoundResolver {

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final List<FfEnemy> enemies = getRoundRelevantEnemies(command);
        final FightRoundResult[] result = new FightRoundResult[enemies.size()];

        for (int i = 0; i < enemies.size(); i++) {
            final FfEnemy enemy = enemies.get(i);
            if (enemy.getStamina() > 0) {
                result[i] = fightSingleEnemy(command, resolvationData, enemy);
            }
        }
        return result;
    }

    /**
     * Executes a battle round with a single enemy.
     * @param command the {@link FfFightCommand} object
     * @param resolvationData the {@link ResolvationData} object
     * @param enemy the {@link FfEnemy} the hero is fighting at the moment
     * @return the result of this specific round
     */
    protected FightRoundResult fightSingleEnemy(final FfFightCommand command, final ResolvationData resolvationData, final FfEnemy enemy) {
        FightRoundResult roundResult;
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final String enemyId = characterHandler.getInteractionHandler().peekLastFightCommand(character, "enemyId");
        final FightCommandMessageList messages = command.getMessages();
        final FightDataDto dto = new FightDataDto(enemy, messages, resolvationData, command.getUsableWeaponTypes());
        final boolean isSelectedEnemy = enemy.getId().equals(enemyId);
        final int[] selfAttackStrengthValues = getSelfAttackStrength(character, command, attributeHandler);
        final int[] enemyAttackStrengthValues = getEnemyAttackStrength(enemy, command);
        final int selfAttackStrength = attributeHandler.resolveValue(character, "skill") + selfAttackStrengthValues[0];
        final int enemyAttackStrength = enemy.getSkill() + enemyAttackStrengthValues[0];
        storeHeroAttackStrength(command, enemy, selfAttackStrength, selfAttackStrengthValues);
        storeEnemyAttackStrength(command, enemy, enemyAttackStrength, enemyAttackStrengthValues);
        recordHeroAttachStrength(messages, selfAttackStrengthValues, selfAttackStrength, character);
        recordEnemyAttachStrength(dto, enemyAttackStrengthValues, enemyAttackStrength);
        if (enemyAttackStrength > selfAttackStrength) {
            roundResult = FightRoundResult.LOSE;
            damageSelf(dto);
            handleDefeatLuckTest(command, dto);
        } else if (enemyAttackStrength < selfAttackStrength && isSelectedEnemy) {
            roundResult = FightRoundResult.WIN;
            damageEnemy(command, dto);
        } else {
            roundResult = FightRoundResult.TIE;
            resolveTieMessage(dto);
        }

        autoDamageSelf(dto);
        return roundResult;
    }

    List<FfEnemy> getRoundRelevantEnemies(final FfFightCommand command) {
        return command.getResolvedEnemies();
    }

}
