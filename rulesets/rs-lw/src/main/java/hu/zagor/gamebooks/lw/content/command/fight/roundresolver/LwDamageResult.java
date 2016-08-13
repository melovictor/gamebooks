package hu.zagor.gamebooks.lw.content.command.fight.roundresolver;

import hu.zagor.gamebooks.content.TrueCloneable;

/**
 * Bean for storing the damage LW and his enemy suffers.
 * @author Tamas_Szekeres
 */
public class LwDamageResult implements TrueCloneable {
    private int lwSuffers;
    private int enemySuffers;
    private boolean enemyKilled;
    private boolean lwKilled;

    public int getLwSuffers() {
        return lwSuffers;
    }

    public void setLwSuffers(final int lwSuffers) {
        this.lwSuffers = lwSuffers;
    }

    public int getEnemySuffers() {
        return enemySuffers;
    }

    public void setEnemySuffers(final int enemySuffers) {
        this.enemySuffers = enemySuffers;
    }

    public boolean isEnemyKilled() {
        return enemyKilled;
    }

    public void setEnemyKilled(final boolean enemyKilled) {
        this.enemyKilled = enemyKilled;
    }

    public boolean isLwKilled() {
        return lwKilled;
    }

    public void setLwKilled(final boolean lwKilled) {
        this.lwKilled = lwKilled;
    }

    @Override
    public LwDamageResult clone() throws CloneNotSupportedException {
        return (LwDamageResult) super.clone();
    }
}
