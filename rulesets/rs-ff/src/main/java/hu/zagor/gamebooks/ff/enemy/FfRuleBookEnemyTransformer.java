package hu.zagor.gamebooks.ff.enemy;

import hu.zagor.gamebooks.books.contentransforming.enemy.AbstractBookEnemyTransformer;
import hu.zagor.gamebooks.character.enemy.FfEnemy;
import org.w3c.dom.Node;

/**
 * Enemy transformer for Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
public class FfRuleBookEnemyTransformer extends AbstractBookEnemyTransformer<FfEnemy> {

    /**
     * Parses a single enemy based on the {@link Node} provided.
     * @param node the {@link Node} to parse the enemy from
     */
    @Override
    protected void finishParsing(final Node node, final FfEnemy enemy) {
        enemy.setCommonName(extractAttribute(node, "commonName"));
        final int skill = extractIntegerAttribute(node, "skill");
        final int stamina = extractIntegerAttribute(node, "stamina");
        enemy.setSkill(skill);
        enemy.setStamina(stamina);
        enemy.setInitialSkill(skill);
        enemy.setInitialStamina(stamina);
        enemy.setAttackStrength(extractIntegerAttribute(node, "attackStrength", 0));
        enemy.setAttackStrengthBonus(extractIntegerAttribute(node, "attackStrengthBonus", 0));
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
        enemy.setIndifferentAlly(extractBooleanAttribute(node, "indifferentAlly", false));
    }

    /**
     * Returns an empty, appropriate {@link FfEnemy} instance.
     * @return the {@link FfEnemy} instance to populate
     */
    @Override
    protected FfEnemy createEnemyObject() {
        return new FfEnemy();
    }
}
