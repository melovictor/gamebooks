package hu.zagor.gamebooks.content.command.fight.domain;

/**
 * Simple class for returning details about a cast spell.
 * @author Tamas_Szekeres
 */
public class SpellResult {

    private String spellId;
    private boolean roundSkippable;

    public String getSpellId() {
        return spellId;
    }

    public void setSpellId(final String spellId) {
        this.spellId = spellId;
    }

    public boolean isSpellActive() {
        return spellId != null;
    }

    public boolean isRoundSkippable() {
        return roundSkippable;
    }

    public void setRoundSkippable(final boolean roundSkippable) {
        this.roundSkippable = roundSkippable;
    }
}
