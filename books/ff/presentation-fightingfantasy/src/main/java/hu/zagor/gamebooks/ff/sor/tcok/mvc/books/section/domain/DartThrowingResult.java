package hu.zagor.gamebooks.ff.sor.tcok.mvc.books.section.domain;

/**
 * Bean for returning all the information about the throwing round.
 * @author Tamas_Szekeres
 */
public class DartThrowingResult {
    private int x;
    private int y;
    private boolean winner;
    private int newTotal;
    private String winnerName;
    private int gold;

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(final boolean winner) {
        this.winner = winner;
    }

    public int getNewTotal() {
        return newTotal;
    }

    public void setNewTotal(final int newTotal) {
        this.newTotal = newTotal;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(final String winnerName) {
        this.winnerName = winnerName;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(final int gold) {
        this.gold = gold;
    }

}
