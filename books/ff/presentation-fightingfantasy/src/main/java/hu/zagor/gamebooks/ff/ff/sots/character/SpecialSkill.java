package hu.zagor.gamebooks.ff.ff.sots.character;

/**
 * List of possible special skills for the player to choose from.
 * @author Tamas_Szekeres
 */
public enum SpecialSkill {
    KJUDZSUTSZU("Kjudzsutszu, íjászat"),
    DZSADZSUTSZU("Dzsadzsutszu, a kardrántás művészete"),
    KARUMIDZSUTSZU("Karumidzsutszu, ugrások harc közben"),
    NITOKENDZSUTSZU("Nitokendzsutszu, harc két karddal");

    private String description;

    SpecialSkill(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
