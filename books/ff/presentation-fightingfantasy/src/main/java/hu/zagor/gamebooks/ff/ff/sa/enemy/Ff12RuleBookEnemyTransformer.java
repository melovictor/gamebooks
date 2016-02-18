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
    protected FfEnemy parseEnemy(final Node node) {
        final Ff12Enemy parsedEnemy = (Ff12Enemy) super.parseEnemy(node);

        parsedEnemy.setWeapon(extractAttribute(node, "weapon", "1002"));
        parsedEnemy.setAttackPerRound(extractIntegerAttribute(node, "attackPerRound", 1));

        return parsedEnemy;
    }

    @Override
    protected FfEnemy createEnemyObject() {
        return new Ff12Enemy();
    }
}
