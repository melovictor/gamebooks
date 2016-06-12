package hu.zagor.gamebooks.ff.ff.sots.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.handler.userinteraction.FfUserInteractionHandler;
import hu.zagor.gamebooks.content.command.fight.FightCommand;
import hu.zagor.gamebooks.content.command.fight.domain.FightRoundResult;
import hu.zagor.gamebooks.content.command.fight.roundresolver.AllAtOnceFightRoundResolver;
import hu.zagor.gamebooks.content.command.fight.roundresolver.domain.FightDataDto;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Customized all at once fight round resolver for FF20 that handles dual sword fight.
 * @author Tamas_Szekeres
 */
@Component
public class AllAtOnceBaseFf20FightRoundResolver extends AllAtOnceFightRoundResolver {
    private static final int NITOKENDZSUTSZU_ATTACK_ROLL_LIMIT = 9;

    @Override
    protected FightRoundResult fightSingleEnemy(final FightCommand command, final ResolvationData resolvationData, final FfEnemy enemy) {
        final FightRoundResult fightSingleEnemy = super.fightSingleEnemy(command, resolvationData, enemy);

        final FfCharacter character = (FfCharacter) resolvationData.getCharacter();
        final FfUserInteractionHandler userInteractionHandler = (FfUserInteractionHandler) resolvationData.getCharacterHandler().getInteractionHandler();
        final String enemyId = userInteractionHandler.peekLastFightCommand(character, "enemyId");
        final boolean isSelectedEnemy = enemy.getId().equals(enemyId);
        if (isSelectedEnemy && twoSwordFight(resolvationData, enemy, command)) {
            userInteractionHandler.setInteractionState(character, "nito", "1");
            super.fightSingleEnemy(command, resolvationData, enemy);
            userInteractionHandler.getInteractionState(character, "nito");
        }

        return fightSingleEnemy;
    }

    private boolean twoSwordFight(final ResolvationData resolvationData, final FfEnemy selectedEnemy, final FightCommand command) {
        final Character character = resolvationData.getCharacter();
        final CharacterItemHandler itemHandler = resolvationData.getCharacterHandler().getItemHandler();
        final Map<String, Integer> attackStrengths = command.getAttackStrengths();
        final int heroRoll = attackStrengths.get("h_d1_" + selectedEnemy.getId()) + attackStrengths.get("h_d2_" + selectedEnemy.getId());

        return selectedEnemy.getStamina() > 0 && (itemHandler.hasItem(character, "1001") || itemHandler.hasItem(character, "1003"))
            && itemHandler.hasItem(character, "1002") && heroRoll >= NITOKENDZSUTSZU_ATTACK_ROLL_LIMIT;
    }

    @Override
    protected void damageSelf(final FightDataDto dto) {
        final String nitoRound = dto.getCharacterHandler().getInteractionHandler().peekInteractionState(dto.getCharacter(), "nito");
        if (nitoRound == null) {
            super.damageSelf(dto);
        } else {
            dto.getMessages().addKey("page.ff.label.fight.single.failedAttack", dto.getEnemy().getCommonName());
        }
    }
}
