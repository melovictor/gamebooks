package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.domain.builder.DefaultResolvationDataBuilder;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.FfCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.FfAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
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
 * Sub fight round resolver for the eventuality where there is a single enemy and a single ally with us, and the highest of the two of us will be the main fighter.
 * @author Tamas_Szekeres
 */
@Component
public class HighestMainSupportedSingleSor2FightRoundResolver extends SingleSupportedFightRoundResolver {
    @Autowired @Qualifier("sorHeroAttackStrengthRoller") private HeroAttackStrengthRoller heroAttackStrengthRoller;

    @Override
    int[] getSelfAttackStrength(final FfCharacter character, final FightCommand command, final FfAttributeHandler attributeHandler) {
        return heroAttackStrengthRoller.getSelfAttackStrength(character, command, attributeHandler);
    }

    @Override
    public FightRoundResult[] resolveRound(final FightCommand command, final ResolvationData resolvationData, final FightBeforeRoundResult beforeRoundResult) {
        final FightRoundResult[] result = new FightRoundResult[1];
        final FfCharacterHandler characterHandler = (FfCharacterHandler) resolvationData.getCharacterHandler();
        final FfAttributeHandler attributeHandler = characterHandler.getAttributeHandler();
        final SorCharacter character = (SorCharacter) resolvationData.getCharacter();
        final FfAllyCharacter ally = command.getResolvedAllies().get(0);
        final FfEnemy enemy = command.getResolvedEnemies().get(0);

        final int[] selfAttackStrengthValues = getSelfAttackStrength(character, command, attributeHandler);
        final int[] allyAttackStrengthValues = getSelfAttackStrength(ally, command, attributeHandler);
        final int[] enemyAttackStrengthValues = getEnemyAttackStrength(enemy, command);

        final int selfAttackStrength = selfAttackStrengthValues[0] + attributeHandler.resolveValue(character, "skill");
        final int allyAttackStrength = allyAttackStrengthValues[0] + attributeHandler.resolveValue(ally, "skill");
        final int enemyAttackStrength = enemyAttackStrengthValues[0] + enemy.getSkill();

        final FightCommandMessageList messages = command.getMessages();
        recordHeroAttachStrength(messages, selfAttackStrengthValues, selfAttackStrength, character);
        recordHeroAttachStrength(messages, allyAttackStrengthValues, allyAttackStrength, ally);
        recordEnemyAttachStrength(new FightDataDto(enemy, messages, null, null), enemyAttackStrengthValues, enemyAttackStrength);

        if (selfAttackStrength >= allyAttackStrength) {

            result[0] = mainAttacks(command, resolvationData, selfAttackStrength, enemyAttackStrength);
            allySubAttacks(command, resolvationData, allyAttackStrength, enemyAttackStrength);

        } else {

            final ResolvationData allyResolvationData = DefaultResolvationDataBuilder.builder().usingResolvationData(resolvationData).withCharacter(ally).build();
            result[0] = mainAttacks(command, allyResolvationData, allyAttackStrength, enemyAttackStrength);
            heroSubAttacks(command, resolvationData, selfAttackStrength, enemyAttackStrength);

        }

        return result;
    }

    private void heroSubAttacks(final FightCommand command, final ResolvationData resolvationData, final int selfAttackStrength, final int enemyAttackStrength) {
        final FightRoundResult[] result = new FightRoundResult[1];
        final FightCommandMessageList messages = command.getMessages();
        final FfEnemy enemy = command.getResolvedEnemies().get(0);
        final FightDataDto dto = new FightDataDto(enemy, messages, resolvationData, null);
        if (selfAttackStrength > enemyAttackStrength) {
            // we hit enemy
            doWinFight(command, result, 0, dto);
        } else {
            // enemy blocked us
            doTieFight(command, result, 0, dto);
        }
    }

    private void allySubAttacks(final FightCommand command, final ResolvationData resolvationData, final int allyAttackStrength, final int enemyAttackStrength) {
        final FfAllyCharacter ally = command.getResolvedAllies().get(0);
        final ResolvationData allyResolvationData = DefaultResolvationDataBuilder.builder().usingResolvationData(resolvationData).withCharacter(ally).build();
        heroSubAttacks(command, allyResolvationData, allyAttackStrength, enemyAttackStrength);
    }

    private FightRoundResult mainAttacks(final FightCommand command, final ResolvationData resolvationData, final int selfAttackStrength, final int enemyAttackStrength) {
        final FightRoundResult[] result = new FightRoundResult[1];
        final FightCommandMessageList messages = command.getMessages();
        final FfEnemy enemy = command.getResolvedEnemies().get(0);
        final FightDataDto dto = new FightDataDto(enemy, messages, resolvationData, null);
        if (selfAttackStrength > enemyAttackStrength) {
            // we hit enemy
            doWinFight(command, result, 0, dto);
        } else if (selfAttackStrength == enemyAttackStrength) {
            // we enemy blocked
            doTieFight(command, result, 0, dto);
        } else {
            // enemy hit us
            doLoseFight(command, result, 0, dto);
        }
        return result[0];
    }

}
