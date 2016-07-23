package hu.zagor.gamebooks.content.command.fight.domain;

import hu.zagor.gamebooks.content.TrueCloneable;

/**
 * Bean for storing information about fleeing from the battle.
 * @author Tamas_Szekeres
 */
public class FightFleeData implements TrueCloneable {

    private int afterRound;
    private String text;
    private boolean sufferDamage = true;

    public int getAfterRound() {
        return afterRound;
    }

    public void setAfterRound(final int afterRound) {
        this.afterRound = afterRound;
    }

    @Override
    public FightFleeData clone() throws CloneNotSupportedException {
        return (FightFleeData) super.clone();
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public boolean isSufferDamage() {
        return sufferDamage;
    }

    public void setSufferDamage(final boolean sufferDamage) {
        this.sufferDamage = sufferDamage;
    }

}
