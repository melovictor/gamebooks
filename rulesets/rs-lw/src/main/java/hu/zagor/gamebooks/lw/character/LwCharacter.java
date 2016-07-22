package hu.zagor.gamebooks.lw.character;

import hu.zagor.gamebooks.character.Character;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The player character for the LW ruleset.
 * @author Tamas_Szekeres
 */
@Component("lwCharacter")
@Scope("prototype")
public class LwCharacter extends Character {
    private int combatSkill;
    private int endurance;
    private int initialEndurance;
    private int enduranceLostInCombat;

    private Rank rank;
    private Money money = new Money();
    private KaiDisciplines kaiDisciplines = new KaiDisciplines();

    public int getCombatSkill() {
        return combatSkill;
    }

    public void setCombatSkill(final int combatSkill) {
        this.combatSkill = combatSkill;
    }

    public int getEndurance() {
        return endurance;
    }

    public void setEndurance(final int endurance) {
        this.endurance = endurance;
    }

    public int getInitialEndurance() {
        return initialEndurance;
    }

    public void setInitialEndurance(final int initialEndurance) {
        this.initialEndurance = initialEndurance;
    }

    public int getEnduranceLostInCombat() {
        return enduranceLostInCombat;
    }

    public void setEnduranceLostInCombat(final int enduranceLostInCombat) {
        this.enduranceLostInCombat = enduranceLostInCombat;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(final Rank rank) {
        this.rank = rank;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(final Money money) {
        this.money = money;
    }

    public KaiDisciplines getKaiDisciplines() {
        return kaiDisciplines;
    }

    public void setKaiDisciplines(final KaiDisciplines kaiDisciplines) {
        this.kaiDisciplines = kaiDisciplines;
    }

}
