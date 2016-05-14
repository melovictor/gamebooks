package hu.zagor.gamebooks.mvc.book.inventory.domain;

/**
 * Bean for sending back response after market buy/sell event.
 * @author Tamas_Szekeres
 */
public class BuySellResponse {
    private boolean successfulTransaction;
    private int gold;
    private boolean giveUpMode;
    private boolean giveUpFinished;
    private String text;

    public boolean isSuccessfulTransaction() {
        return successfulTransaction;
    }

    public void setSuccessfulTransaction(final boolean successfulTransaction) {
        this.successfulTransaction = successfulTransaction;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(final int gold) {
        this.gold = gold;
    }

    public boolean isGiveUpMode() {
        return giveUpMode;
    }

    public void setGiveUpMode(final boolean giveUpMode) {
        this.giveUpMode = giveUpMode;
    }

    public boolean isGiveUpFinished() {
        return giveUpFinished;
    }

    public void setGiveUpFinished(final boolean giveUpFinished) {
        this.giveUpFinished = giveUpFinished;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}
