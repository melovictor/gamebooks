package hu.zagor.gamebooks.ff.character;

import hu.zagor.gamebooks.character.enemy.FfEnemy;

/**
 * Player character for Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
public class FfAllyCharacter extends FfCharacter {

    private final FfEnemy ally;

    /**
     * Default constructor for the deserializer.
     */
    public FfAllyCharacter() {
        ally = null;
    }

    /**
     * Basic constructor to merge an {@link FfEnemy} ally with an {@link FfCharacter}.
     * @param ally the ally to merge with
     */
    public FfAllyCharacter(final FfEnemy ally) {
        this.ally = ally;
        setSkill(ally.getSkill());
        setInitialSkill(ally.getSkill());
        setStamina(ally.getStamina());
        setInitialStamina(ally.getStamina());
        setBackpackSize(Integer.MAX_VALUE);
    }

    public String getId() {
        return ally.getId();
    }

    @Override
    public int getSkill() {
        return ally.getSkill();
    }

    @Override
    public int getStamina() {
        return ally.getStamina();
    }

    @Override
    public void setStamina(final int stamina) {
        super.setStamina(stamina);
        ally.setStamina(stamina);
    }

    @Override
    public int getInitialSkill() {
        return getSkill();
    }

    @Override
    public int getInitialStamina() {
        return getStamina();
    }

    @Override
    public String getName() {
        return ally.getName();
    }

    @Override
    public void changeStamina(final int change) {
        super.changeStamina(change);
        ally.setStamina(ally.getStamina() + change);
    }

    public int getAttackStrengthDices() {
        return ally.getAttackStrengthDices();
    }

    public FfEnemy getAlly() {
        return ally;
    }

}
