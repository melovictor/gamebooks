package hu.zagor.gamebooks.lw.enemy;

import hu.zagor.gamebooks.books.contentransforming.enemy.AbstractBookEnemyTransformer;
import hu.zagor.gamebooks.character.enemy.LwEnemy;
import org.w3c.dom.Node;

/**
 * Enemy transformer for Lone Wolf ruleset.
 * @author Tamas_Szekeres
 */
public class LwRuleBookEnemyTransformer extends AbstractBookEnemyTransformer<LwEnemy> {

    @Override
    protected void finishParsing(final Node node, final LwEnemy enemy) {
        enemy.setCombatSkill(extractIntegerAttribute(node, "combatSkill"));
        enemy.setEndurance(extractIntegerAttribute(node, "endurance"));
        enemy.setMindforce(extractBooleanAttribute(node, "mindforce", false));
        enemy.setMindblast(extractBooleanAttribute(node, "mindblast", false));
        enemy.setMindshield(extractBooleanAttribute(node, "mindshield", false));
    }

    @Override
    protected LwEnemy createEnemyObject() {
        return new LwEnemy();
    }

}
