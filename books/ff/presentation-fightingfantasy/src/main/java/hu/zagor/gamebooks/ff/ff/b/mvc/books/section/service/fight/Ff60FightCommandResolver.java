package hu.zagor.gamebooks.ff.ff.b.mvc.books.section.service.fight;

import hu.zagor.gamebooks.character.domain.ResolvationData;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.content.ParagraphData;
import hu.zagor.gamebooks.content.command.fight.FfFightCommand;
import hu.zagor.gamebooks.content.command.fight.FfFightCommandResolver;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

/**
 * Book-specific fight command resolver for FF60.
 * @author Tamas_Szekeres
 */
public class Ff60FightCommandResolver extends FfFightCommandResolver {
    private static final String NIGHTDEATH = "1004";
    @Resource(name = "ff60AttackEffectivenessVerification") private Map<String, Set<String>> effectivenessVerification;

    @Override
    protected List<ParagraphData> doResolve(final FfFightCommand command, final ResolvationData resolvationData) {
        final CharacterItemHandler itemHandler = resolvationData.getInfo().getCharacterHandler().getItemHandler();
        final boolean hasChainmail = itemHandler.hasEquippedItem(resolvationData.getCharacter(), "3018");
        prepareEnemies(command, resolvationData.getEnemies(), hasChainmail ? -1 : 0);
        prepareWeapons(command, resolvationData);

        return super.doResolve(command, resolvationData);
    }

    private void prepareWeapons(final FfFightCommand command, final ResolvationData resolvationData) {
        final FfItem nightdeath = (FfItem) resolvationData.getInfo().getCharacterHandler().getItemHandler().getItem(resolvationData.getCharacter(), NIGHTDEATH);
        if (nightdeath != null) {
            final Set<String> set = effectivenessVerification.get(NIGHTDEATH);
            final String enemyId = command.getEnemies().get(0);
            if (set.contains(enemyId)) {
                nightdeath.setSkill(1);
            } else {
                nightdeath.setSkill(0);
            }
        }
    }

    private void prepareEnemies(final FfFightCommand command, final Map<String, Enemy> enemies, final int attackStrengthBonus) {
        for (final String enemyId : command.getEnemies()) {
            final FfEnemy enemy = (FfEnemy) enemies.get(enemyId);
            enemy.setAttackStrengthBonus(attackStrengthBonus);
        }
    }
}
