package hu.zagor.gamebooks.character.enemy;

import hu.zagor.gamebooks.books.saving.domain.IgnoreField;

/**
 * Enemy object for the Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
@IgnoreField("forced")
public class FfEnemy extends Enemy {

    private String commonName;
    private int skill;
    private int stamina;
    private int initialSkill;
    private int initialStamina;

    private int staminaDamage;
    private int staminaDamageWhenHit;
    private int staminaAutoDamage;
    private int staminaDamageWhileInactive;
    private int skillDamage;
    private int attackStrength;
    private int attackStrengthBonus;

    private int damageAbsorption;
    private int damageAbsorptionEdged;

    private boolean killableByNormal = true;
    private boolean killableByMagical = true;
    private boolean killableByBlessed = true;
    private boolean resurrectable;

    private int fleeAtStamina;
    private int fleeAtRound = Integer.MAX_VALUE;

    private String alterId;
    private FfEnemy alterEgo;
    private int startAtRound;

    public int getSkill() {
        return skill;
    }

    /**
     * Sets the skill for both the current enemy and for a possible alter ego.
     * @param skill the skill to set
     */
    public void setSkill(final int skill) {
        this.skill = skill;
        if (alterEgo != null && alterEgo.skill != skill) {
            alterEgo.setSkill(skill);
        }

    }

    public int getStamina() {
        return stamina;
    }

    /**
     * Sets the stamina for both the current enemy and for a possible alter ego.
     * @param stamina the stamina to set
     */
    public void setStamina(final int stamina) {
        this.stamina = stamina;
        if (alterEgo != null && alterEgo.stamina != stamina) {
            alterEgo.setStamina(stamina);
        }
    }

    public int getStaminaDamage() {
        return staminaDamage;
    }

    public void setStaminaDamage(final int staminaDamage) {
        this.staminaDamage = staminaDamage;
    }

    public int getInitialSkill() {
        return initialSkill;
    }

    public void setInitialSkill(final int initialSkill) {
        this.initialSkill = initialSkill;
    }

    public int getInitialStamina() {
        return initialStamina;
    }

    public void setInitialStamina(final int initialStamina) {
        this.initialStamina = initialStamina;
    }

    public int getSkillDamage() {
        return skillDamage;
    }

    public void setSkillDamage(final int skillDamage) {
        this.skillDamage = skillDamage;
    }

    public int getDamageAbsorption() {
        return damageAbsorption;
    }

    public void setDamageAbsorption(final int damageAbsorption) {
        this.damageAbsorption = damageAbsorption;
    }

    public boolean isKillableByNormal() {
        return killableByNormal;
    }

    public void setKillableByNormal(final boolean killableByNormal) {
        this.killableByNormal = killableByNormal;
    }

    public boolean isKillableByMagical() {
        return killableByMagical;
    }

    public void setKillableByMagical(final boolean killableByMagical) {
        this.killableByMagical = killableByMagical;
    }

    public boolean isKillableByBlessed() {
        return killableByBlessed;
    }

    public void setKillableByBlessed(final boolean killableByBlessed) {
        this.killableByBlessed = killableByBlessed;
    }

    public boolean isResurrectable() {
        return resurrectable;
    }

    public void setResurrectable(final boolean resurrectable) {
        this.resurrectable = resurrectable;
    }

    public int getStaminaDamageWhileInactive() {
        return staminaDamageWhileInactive;
    }

    public void setStaminaDamageWhileInactive(final int staminaDamageWhileInactive) {
        this.staminaDamageWhileInactive = staminaDamageWhileInactive;
    }

    public int getDamageAbsorptionEdged() {
        return damageAbsorptionEdged;
    }

    public void setDamageAbsorptionEdged(final int damageAbsorptionEdged) {
        this.damageAbsorptionEdged = damageAbsorptionEdged;
    }

    public int getStaminaAutoDamage() {
        return staminaAutoDamage;
    }

    public void setStaminaAutoDamage(final int staminaAutoDamage) {
        this.staminaAutoDamage = staminaAutoDamage;
    }

    public String getCommonName() {
        return commonName == null ? getName() : commonName;
    }

    public void setCommonName(final String commonName) {
        this.commonName = commonName;
    }

    public int getFleeAtStamina() {
        return fleeAtStamina;
    }

    public void setFleeAtStamina(final int fleeAtStamina) {
        this.fleeAtStamina = fleeAtStamina;
    }

    public int getFleeAtRound() {
        return fleeAtRound;
    }

    public void setFleeAtRound(final int fleeAtRound) {
        this.fleeAtRound = fleeAtRound;
    }

    public String getAlterId() {
        return alterId;
    }

    public void setAlterId(final String alterId) {
        this.alterId = alterId;
    }

    public FfEnemy getAlterEgo() {
        return alterEgo;
    }

    public void setAlterEgo(final FfEnemy alterEgo) {
        this.alterEgo = alterEgo;
    }

    public int getStaminaDamageWhenHit() {
        return staminaDamageWhenHit;
    }

    public void setStaminaDamageWhenHit(final int staminaDamageWhenHit) {
        this.staminaDamageWhenHit = staminaDamageWhenHit;
    }

    public int getStartAtRound() {
        return startAtRound;
    }

    public void setStartAtRound(final int startAtRound) {
        this.startAtRound = startAtRound;
    }

    public int getAttackStrength() {
        return attackStrength;
    }

    public void setAttackStrength(final int attackStrength) {
        this.attackStrength = attackStrength;
    }

    public int getAttackStrengthBonus() {
        return attackStrengthBonus;
    }

    public void setAttackStrengthBonus(final int attackStrengthBonus) {
        this.attackStrengthBonus = attackStrengthBonus;
    }

}
