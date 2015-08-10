package hu.zagor.gamebooks.content.command.fight.subresolver.domain;

/**
 * Bean for storing luck test settings.
 * @author Tamas_Szekeres
 */
public class LuckTestSettings {

    private final boolean onHit;
    private final boolean onDefense;

    /**
     * Constructor that creates a new {@link LuckTestSettings} object.
     * @param onHit if luck test should be executed while we're hitting
     * @param onDefense if luck test should be executed while being hit
     */
    public LuckTestSettings(final boolean onHit, final boolean onDefense) {
        this.onHit = onHit;
        this.onDefense = onDefense;
    }

    public boolean isOnHit() {
        return onHit;
    }

    public boolean isOnDefense() {
        return onDefense;
    }

}
