package hu.zagor.gamebooks.ff.mvc.book.section.controller.domain;

/**
 * Bean for storing incoming data for a fight.
 * @author Tamas_Szekeres
 */
public class FightCommandForm {
    private String id;
    private Boolean hit;
    private Boolean def;
    private Boolean oth;
    private String special;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Boolean isHit() {
        return hit;
    }

    public void setHit(final Boolean hit) {
        this.hit = hit;
    }

    public Boolean isDef() {
        return def;
    }

    public void setDef(final Boolean def) {
        this.def = def;
    }

    public Boolean isOth() {
        return oth;
    }

    public void setOth(final Boolean oth) {
        this.oth = oth;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(final String special) {
        this.special = special;
    }

}
