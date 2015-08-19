package hu.zagor.gamebooks.ff.enemy;

import hu.zagor.gamebooks.books.contentransforming.AbstractTransformer;
import hu.zagor.gamebooks.books.contentransforming.enemy.BookEnemyTransformer;
import hu.zagor.gamebooks.books.contentransforming.section.XmlTransformationException;
import hu.zagor.gamebooks.character.enemy.Enemy;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Enemy transformer for Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
public class FfRuleBookEnemyTransformer extends AbstractTransformer implements BookEnemyTransformer {

    @LogInject
    private Logger logger;

    @Override
    public Map<String, Enemy> transformEnemies(final Document document) throws XmlTransformationException {
        final Map<String, Enemy> enemies = new HashMap<>();

        final NodeList childNodes = document.getChildNodes();
        if (childNodes.getLength() > 0) {
            parseEnemies(childNodes.item(0), enemies);
        } else {
            logger.error("Couldn't find 'enemies' element in xml file!");
            throw new XmlTransformationException("Missing element 'enemies'.");
        }

        return enemies;
    }

    private void parseEnemies(final Node node, final Map<String, Enemy> enemies) {
        final NodeList childNodes = node.getChildNodes();
        final int count = childNodes.getLength();
        for (int i = 0; i < count; i++) {
            final Node enemyNode = childNodes.item(i);
            if (isEnemy(enemyNode)) {
                final Enemy enemy = parseEnemy(enemyNode);
                final String id = enemy.getId();
                if (enemies.containsKey(id)) {
                    throw new IllegalStateException("Book contains at least two instances of the enemy id '" + id + "'!");
                }
                enemies.put(id, enemy);
            }
        }
    }

    private boolean isEnemy(final Node childNode) {
        return "enemy".equals(childNode.getNodeName());
    }

    private FfEnemy parseEnemy(final Node node) {
        final FfEnemy enemy = new FfEnemy();

        enemy.setId(extractAttribute(node, "id"));
        enemy.setName(extractAttribute(node, "name"));
        enemy.setCommonName(extractAttribute(node, "commonName"));
        final int skill = extractIntegerAttribute(node, "skill");
        final int stamina = extractIntegerAttribute(node, "stamina");
        enemy.setSkill(skill);
        enemy.setStamina(stamina);
        enemy.setInitialSkill(skill);
        enemy.setInitialStamina(stamina);
        enemy.setAttackStrength(extractIntegerAttribute(node, "attackStrength", 0));
        enemy.setStaminaDamage(extractIntegerAttribute(node, "staminaDamage", 2));
        enemy.setStaminaDamageWhenHit(extractIntegerAttribute(node, "staminaDamageWhenHit", 0));
        enemy.setStaminaAutoDamage(extractIntegerAttribute(node, "staminaAutoDamage", 0));
        enemy.setStaminaDamageWhileInactive(extractIntegerAttribute(node, "staminaDamageWhileInactive", 0));
        enemy.setSkillDamage(extractIntegerAttribute(node, "skillDamage", 0));
        enemy.setDamageAbsorption(extractIntegerAttribute(node, "damageAbsorption", 0));
        enemy.setDamageAbsorptionEdged(extractIntegerAttribute(node, "damageAbsorptionEdged", 0));
        enemy.setKillableByBlessed(extractBooleanAttribute(node, "killableByBlessed", true));
        enemy.setKillableByMagical(extractBooleanAttribute(node, "killableByMagical", true));
        enemy.setKillableByNormal(extractBooleanAttribute(node, "killableByNormal", true));
        enemy.setResurrectable(extractBooleanAttribute(node, "resurrectable", false));
        enemy.setFleeAtStamina(extractIntegerAttribute(node, "fleeAtStamina", 0));
        enemy.setFleeAtRound(extractIntegerAttribute(node, "fleeAtRound", Integer.MAX_VALUE));
        enemy.setAlterId(extractAttribute(node, "same"));
        enemy.setStartAtRound(extractIntegerAttribute(node, "startAtRound", 1));

        return enemy;
    }
}
