package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.LastFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightBeforeRoundResult;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfAllyCharacter;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Sub fight round resolver for the eventuality where there are two fighting pairs paralelly.
 * @author Tamas_Szekeres
 */
@Component
public class HighestMainSupportedDualSor2FightRoundResolver extends SingleSupportedFightRoundResolver {
    @Autowired @Qualifier("sorHeroAttackStrengthRoller") private HeroAttackStrengthRoller heroAttackStrengthRoller;

    @Override
    int[] getSelfAttackStrength(final FfCharacter character, final FfFightCommand command, final FfAttributeHandler attributeHandler) {
        return heroAttackStrengthRoller.getSelfAttackStrength(character, command, attributeHandler);
    }

    @Override
    public FightRoundResult[] resolveRound(final FfFightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final SorCharacter character = (SorCharacter) resolvationData.getCharacter();

        final FfAllyCharacter ally = command.getResolvedAllies().get(0);
        final FfEnemy allyEnemy = command.getResolvedEnemies().get(0);
        final FfEnemy selfEnemy = command.getResolvedEnemies().get(1);
        characterHandler.getInteractionHandler().setFightCommand(character, LastFightCommand.ENEMY_ID, selfEnemy.getId());

        final FightRoundResult[] result = super.resolveRound(command, resolvationData, beforeRoundResult);

        final int[] allyAttackStrengthValues = getSelfAttackStrength(ally, command, attributeHandler);
        final int[] allyEnemyAttackStrengthValues = getEnemyAttackStrength(allyEnemy, command);

        final int allyAttackStrength = allyAttackStrengthValues[0] + attributeHandler.resolveValue(ally, "skill");
        final int allyEnemyAttackStrength = allyEnemyAttackStrengthValues[0] + allyEnemy.getSkill();

        final FightCommandMessageList messages = command.getMessages();
        recordHeroAttachStrength(messages, allyAttackStrengthValues, allyAttackStrength, ally);
        recordEnemyAttachStrength(new FightDataDto(allyEnemy, messages, null, null), allyEnemyAttackStrengthValues, allyEnemyAttackStrength);

        if (allyAttackStrength > allyEnemyAttackStrength) {
            // ally hits
            allyEnemy.setStamina(allyEnemy.getStamina() - 2);
            messages.addKey("page.sor2.fight.allyHitsEnemy", ally.getName(), allyEnemy.getCommonName());
        } else if (allyAttackStrength == allyEnemyAttackStrength) {
            // ally enemy ties
            messages.addKey("page.sor2.fight.EnemyTies", ally.getName(), allyEnemy.getCommonName());
        } else {
            // enemy hits
            ally.setStamina(ally.getStamina() - 2);
            messages.addKey("page.sor2.fight.enemyHitsAlly", ally.getName(), allyEnemy.getCommonName());
        }

        return result;
    }

}
