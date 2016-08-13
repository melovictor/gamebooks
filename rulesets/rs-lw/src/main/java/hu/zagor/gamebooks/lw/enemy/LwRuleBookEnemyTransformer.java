package hu.zagor.gamebooks.lw.enemy;

import hu.zagor.gamebooks.books.contentransforming.enemy.AbstractBookEnemyTransformer;
import hu.zagor.gamebooks.lw.character.enemy.LwEnemy;
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
        enemy.setKillableByNormal(extractBooleanAttribute(node, "killableByNormal", true));
        enemy.setFleeAtEndurance(extractIntegerAttribute(node, "fleeAtEndurance", 0));
        enemy.setUndead(extractBooleanAttribute(node, "undead", true));
    }

    @Override
    protected LwEnemy createEnemyObject() {
        return new LwEnemy();
    }

}
