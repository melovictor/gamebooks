package hu.zagor.gamebooks.ff.ff.sots.character;

/**
 * List of possible special skills for the player to choose from.
 * @author Tamas_Szekeres
 */
public enum SpecialSkill {
    KJUDZSUTSZU("Kjudzsutszu, íjászat"), // 0
    DZSADZSUTSZU("Dzsadzsutszu, a kardrántás művészete"), // 1
    KARUMIDZSUTSZU("Karumidzsutszu, ugrások harc közben"), // 2
    NITOKENDZSUTSZU("Nitokendzsutszu, harc két karddal"); // 3

    private String description;

    SpecialSkill(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
