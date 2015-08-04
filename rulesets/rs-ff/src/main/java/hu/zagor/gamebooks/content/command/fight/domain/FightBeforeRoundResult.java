package hu.zagor.gamebooks.content.command.fight.domain;

/**
 * Class for transmitting before round information.
 * @author Tamas_Szekeres
 */
public class FightBeforeRoundResult {

    private boolean interrupted;
    private boolean loseBattle;
    private String beforeRoundText;

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(final boolean interrupted) {
        this.interrupted = interrupted;
    }

    public String getBeforeRoundText() {
        return beforeRoundText;
    }

    public void setBeforeRoundText(final String beforeRoundText) {
        this.beforeRoundText = beforeRoundText;
    }

    public boolean isLoseBattle() {
        return loseBattle;
    }

    public void setLoseBattle(final boolean loseBattle) {
        this.loseBattle = loseBattle;
    }

}
