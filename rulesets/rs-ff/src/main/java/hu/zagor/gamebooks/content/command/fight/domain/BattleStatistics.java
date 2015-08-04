package hu.zagor.gamebooks.content.command.fight.domain;

import hu.zagor.gamebooks.content.TrueCloneable;

/**
 * Bean for storing statistical data about the battle.
 * @author Tamas_Szekeres
 */
public class BattleStatistics implements TrueCloneable {

    private int totalWin;
    private int totalLose;
    private int totalTie;
    private int subsequentWin;
    private int subsequentLose;
    private int subsequentTie;
    private int subsequentNotWon;

    public int getTotalWin() {
        return totalWin;
    }

    public int getTotalLose() {
        return totalLose;
    }

    public int getTotalTie() {
        return totalTie;
    }

    public int getSubsequentWin() {
        return subsequentWin;
    }

    public int getSubsequentLose() {
        return subsequentLose;
    }

    public int getSubsequentTie() {
        return subsequentTie;
    }

    public int getTotalNotWon() {
        return totalLose + totalTie;
    }

    public int getSubsequentNotWon() {
        return subsequentNotWon;
    }

    /**
     * Updates the statistics based on the result of the last round.
     * @param result the result of the last round
     */
    public void updateStats(final FightRoundResult result) {
        if (result != null) {
            switch (result) {
            case WIN:
                subsequentWin++;
                totalWin++;
                subsequentLose = 0;
                subsequentTie = 0;
                subsequentNotWon = 0;
                break;
            case LOSE:
                subsequentLose++;
                totalLose++;
                subsequentWin = 0;
                subsequentTie = 0;
                subsequentNotWon++;
                break;
            case TIE:
                subsequentTie++;
                totalTie++;
                subsequentLose = 0;
                subsequentWin = 0;
                subsequentNotWon++;
                break;
            case IDLE:
                break;
            default:
                throw new IllegalArgumentException("The battle result '" + result + "' is not supported!");
            }
        }
    }

    @Override
    public BattleStatistics clone() throws CloneNotSupportedException {
        return (BattleStatistics) super.clone();
    }
}
