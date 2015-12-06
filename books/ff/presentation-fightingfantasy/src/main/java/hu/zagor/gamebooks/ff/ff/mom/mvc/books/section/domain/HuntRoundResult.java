package hu.zagor.gamebooks.ff.ff.mom.mvc.books.section.domain;

/**
 * Bean for returning the results of the current hunt round.
 * @author Tamas_Szekeres
 */
public class HuntRoundResult {

    private String dogPosition;
    private String tigerPosition;
    private boolean huntFinished;
    private String roundMessage;
    private String nextSectionPos;

    public String getDogPosition() {
        return dogPosition;
    }

    public void setDogPosition(final String dogPosition) {
        this.dogPosition = dogPosition;
    }

    public String getTigerPosition() {
        return tigerPosition;
    }

    public void setTigerPosition(final String tigerPosition) {
        this.tigerPosition = tigerPosition;
    }

    public boolean isHuntFinished() {
        return huntFinished;
    }

    public void setHuntFinished(final boolean huntFinished) {
        this.huntFinished = huntFinished;
    }

    public String getRoundMessage() {
        return roundMessage;
    }

    public void setRoundMessage(final String roundMessage) {
        this.roundMessage = roundMessage;
    }

    public String getNextSectionPos() {
        return nextSectionPos;
    }

    public void setNextSectionPos(final String nextSectionId) {
        this.nextSectionPos = nextSectionId;
    }

}
