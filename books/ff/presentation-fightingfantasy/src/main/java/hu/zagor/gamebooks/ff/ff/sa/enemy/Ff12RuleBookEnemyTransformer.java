package hu.zagor.gamebooks.ff.ff.sa.enemy;

import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.ff.enemy.FfRuleBookEnemyTransformer;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

/**
 * Enemy transformert for FF12.
 * @author Tamas_Szekeres
 */
@Component
public class Ff12RuleBookEnemyTransformer extends FfRuleBookEnemyTransformer {

    @Override
    protected void finishParsing(final Node node, final FfEnemy enemy) {
        super.finishParsing(node, enemy);

        final Ff12Enemy parsedEnemy = (Ff12Enemy) enemy;
        parsedEnemy.setWeapon(extractAttribute(node, "weapon", "1002"));
        parsedEnemy.setAttackPerRound(extractIntegerAttribute(node, "attackPerRound", 1));

    }

    @Override
    protected FfEnemy createEnemyObject() {
        return new Ff12Enemy();
    }
}
