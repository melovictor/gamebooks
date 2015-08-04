package hu.zagor.gamebooks.ff.character;

import hu.zagor.gamebooks.character.enemy.FfEnemy;

/**
 * Player character for Fighting Fantasy ruleset.
 * @author Tamas_Szekeres
 */
public class FfAllyCharacter extends FfCharacter {

    private String name;
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
        setBackpackSize(Integer.MAX_VALUE);
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
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public void changeStamina(final int change) {
        ally.setStamina(ally.getStamina() + change);
    }

}
