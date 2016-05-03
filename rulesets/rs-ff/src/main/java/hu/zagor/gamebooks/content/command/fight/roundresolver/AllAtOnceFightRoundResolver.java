package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
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
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final List<FfEnemy> enemies = getRoundRelevantEnemies(command);
        final FightRoundResult[] result = new FightRoundResult[enemies.size()];
        final FightCommandMessageList messages = command.getMessages();
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final String enemyId = characterHandler.getInteractionHandler().peekLastFightCommand(character, "enemyId");

        for (int i = 0; i < enemies.size(); i++) {
            final FfEnemy enemy = enemies.get(i);
            if (enemy.getStamina() > 0) {
                final FightDataDto dto = new FightDataDto(enemy, messages, resolvationData, command.getUsableWeaponTypes());
                final boolean isSelectedEnemy = enemy.getId().equals(enemyId);
                final int[] selfAttackStrengthValues = getSelfAttackStrength(character, command, attributeHandler);
                final int[] enemyAttackStrengthValues = getEnemyAttackStrength(enemy, command);
                final int selfAttackStrength = attributeHandler.resolveValue(character, "skill") + selfAttackStrengthValues[0];
                final int enemyAttackStrength = enemy.getSkill() + enemyAttackStrengthValues[0];
                command.getAttackStrengths().put(enemy.getId(), enemyAttackStrength);
                recordHeroAttachStrength(messages, selfAttackStrengthValues, selfAttackStrength, character);
                recordEnemyAttachStrength(dto, enemyAttackStrengthValues, enemyAttackStrength);
                if (enemyAttackStrength > selfAttackStrength) {
                    result[i] = FightRoundResult.LOSE;
                    damageSelf(dto);
                    handleDefeatLuckTest(command, dto);
                } else if (enemyAttackStrength < selfAttackStrength && isSelectedEnemy) {
                    result[i] = FightRoundResult.WIN;
                    damageEnemy(command, dto);
                } else {
                    result[i] = FightRoundResult.TIE;
                    resolveTieMessage(dto);
                }

                autoDamageSelf(dto);
            }
        }
        return result;
    }

    List<FfEnemy> getRoundRelevantEnemies(final FightCommand command) {
        return command.getResolvedEnemies();
    }

}
