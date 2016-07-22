package hu.zagor.gamebooks.lw.character;

/**
 * Bean for storing the character's different moneys.
 * @author Tamas_Szekeres
 */
public class Money {
    private static final int MAX_SPACE = 50;
    private int goldCrowns;

    public int getGoldCrowns() {
        return goldCrowns;
    }

    public void setGoldCrowns(final int goldCrowns) {
        this.goldCrowns = goldCrowns;
    }

    /**
     * Adds new Gold Crown pieces to the money pouch.
     * @param amount the amount intended to be taken
     * @return the amount actually taken
     */
    public int addGoldCrowns(final int amount) {
        final int availableSpace = MAX_SPACE - goldCrowns;
        final int taken = Math.min(availableSpace, amount);
        goldCrowns += taken;
        return taken;
    }

}
