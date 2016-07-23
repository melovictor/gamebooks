package hu.zagor.gamebooks.content.command.fight.roundresolver;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.LwEnemy;
import hu.zagor.gamebooks.character.handler.LwCharacterHandler;
import hu.zagor.gamebooks.character.handler.attribute.LwAttributeHandler;
import hu.zagor.gamebooks.content.command.fight.LwFightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightCommandMessageList;
import hu.zagor.gamebooks.content.command.fight.domain.FightFleeData;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.lw.character.LwCharacter;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DefaultLwFightRoundResolver implements LwFightRoundResolver {

    @Override
    public void resolveRound(final LwFightCommand command, final ResolvationData resolvationData) {
        final List<LwEnemy> enemies = command.getResolvedEnemies();
        final FightCommandMessageList messages = command.getMessages();
        final LwCharacterHandler characterHandler = (LwCharacterHandler) resolvationData.getCharacterHandler();
        final LwCharacter character = (LwCharacter) resolvationData.getCharacter();
        final LwAttributeHandler attributeHandler = characterHandler.getAttributeHandler();

        final LwEnemy enemy = enemies.get(0);
        final FightDataDto dto = new FightDataDto(enemy, messages, resolvationData);

        final int selfCommandRatio = attributeHandler.resolveValue(character, "commandSkill") - enemy.getCombatSkill();

        // final int[] selfAttackStrengthValues = getSelfAttackStrength(character, command, attributeHandler);
        // final int[] enemyAttackStrengthValues = getEnemyAttackStrength(enemy, command);
        // final int selfAttackStrength = attributeHandler.resolveValue(character, "skill") + selfAttackStrengthValues[0];
        // final int enemyAttackStrength = enemy.getSkill() + enemyAttackStrengthValues[0];
        // storeHeroAttackStrength(command, enemy, selfAttackStrength, selfAttackStrengthValues);
        // storeEnemyAttackStrength(command, enemy, enemyAttackStrength, enemyAttackStrengthValues);
        // recordHeroAttachStrength(messages, selfAttackStrengthValues, selfAttackStrength, character);
        // recordEnemyAttachStrength(dto, enemyAttackStrengthValues, enemyAttackStrength);
        // if (enemyAttackStrength == selfAttackStrength) {
        // doTieFight(command, enemyIdx, dto);
        // } else if (enemyAttackStrength > selfAttackStrength) {
        // doLoseFight(command, enemyIdx, dto);
        // } else {
        // doWinFight(command, enemyIdx, dto);
        // }

    }

    @Override
    public void resolveFlee(final LwFightCommand command, final ResolvationData resolvationData) {
        final LwEnemy enemy = command.getResolvedEnemies().get(0);
        final FightCommandMessageList messages = command.getMessages();
        final FightFleeData fleeData = command.getFleeData();
        getFleeTextResourceList(messages, fleeData);
        if (fleeData.isSufferDamage()) {
            fleeFromEnemy((LwCharacter) resolvationData.getCharacter(), enemy, messages);
        }
    }

    private void fleeFromEnemy(final LwCharacter character, final LwEnemy enemy, final FightCommandMessageList messages) {
        messages.addKey("page.ff.label.fight.single.flee", new Object[]{enemy.getName()});
        doLoseFightRound(character, messages, enemy);
    }

    private void doLoseFightRound(final LwCharacter character, final FightCommandMessageList messages, final LwEnemy enemy) {
        // TODO: damage self
        resolveDefeatMessage(messages, enemy);
    }

    private void resolveDefeatMessage(final FightCommandMessageList messages, final LwEnemy enemy) {
        messages.addKey("page.lw.label.fight.failedDefense", enemy.getName());
    }

    private void getFleeTextResourceList(final FightCommandMessageList messages, final FightFleeData fightFleeData) {
        final String text = fightFleeData == null ? null : fightFleeData.getText();
        if (text == null) {
            messages.addKey("page.lw.label.fight.flee");
        } else {
            messages.add(text + ".");
        }
    }

}
