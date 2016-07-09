package hu.zagor.gamebooks.ff.ff.aod.mvc.books.section.service.fight;

/**
 * Bean storign information about force change for a specific outcome based on the size of the fighting forces.
 * @author Tamas_Szekeres
 */
public class BattleRoundResult {
    private int selfStrong;
    private int selfWeak;
    private int egal;

    public int getSelfStrong() {
        return selfStrong;
    }

    public void setSelfStrong(final int selfStrong) {
        this.selfStrong = selfStrong;
    }

    public int getSelfWeak() {
        return selfWeak;
    }

    public void setSelfWeak(final int selfWeak) {
        this.selfWeak = selfWeak;
    }

    public int getEgal() {
        return egal;
    }

    public void setEgal(final int egal) {
        this.egal = egal;
    }

}
