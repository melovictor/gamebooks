package hu.zagor.gamebooks.content;

/**
 * Extended paragraph data for Fighting Fantasy books.
 * @author Tamas_Szekeres
 */
public class FfParagraphData extends ComplexParagraphData {

    private boolean interrupt;
    private boolean loseBattleRound;
    private boolean canEat;

    public boolean isInterrupt() {
        return interrupt;
    }

    public void setInterrupt(final boolean interrupt) {
        this.interrupt = interrupt;
    }

    public boolean isLoseBattleRound() {
        return loseBattleRound;
    }

    public void setLoseBattleRound(final boolean loseBattleRound) {
        this.loseBattleRound = loseBattleRound;
    }

    public boolean isCanEat() {
        return canEat;
    }

    public void setCanEat(final boolean canEat) {
        this.canEat = canEat;
    }

    @Override
    public FfParagraphData clone() throws CloneNotSupportedException {
        return (FfParagraphData) super.clone();
    }
}
