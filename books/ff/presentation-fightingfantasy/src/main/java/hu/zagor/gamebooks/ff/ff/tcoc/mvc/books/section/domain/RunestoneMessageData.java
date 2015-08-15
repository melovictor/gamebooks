package hu.zagor.gamebooks.ff.ff.tcoc.mvc.books.section.domain;

/**
 * Bean for storing informaiton based on which Runestone messages are rendered out.
 * @author Tamas_Szekeres
 */
public class RunestoneMessageData {

    private String messageKey;
    private String playerA;
    private String playerB;
    private boolean won;
    private boolean finished;
    private int playerNumber;
    private int numberOfPlayers;

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(final String messageKey) {
        this.messageKey = messageKey;
    }

    public String getPlayerA() {
        return playerA;
    }

    public void setPlayerA(final String playerA) {
        this.playerA = playerA;
    }

    public String getPlayerB() {
        return playerB;
    }

    public void setPlayerB(final String playerB) {
        this.playerB = playerB;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(final boolean won) {
        this.won = won;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(final boolean finished) {
        this.finished = finished;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(final int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(final int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

}
