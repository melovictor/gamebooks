package hu.zagor.gamebooks.content;

/**
 * Extended paragraph data for Lone Wolf books.
 * @author Tamas_Szekeres
 */
public class LwParagraphData extends ComplexParagraphData {

    private boolean mustEat;
    private boolean canHunt;

    public boolean isMustEat() {
        return mustEat;
    }

    public void setMustEat(final boolean mustEat) {
        this.mustEat = mustEat;
    }

    public boolean isCanHunt() {
        return canHunt;
    }

    public void setCanHunt(final boolean canHunt) {
        this.canHunt = canHunt;
    }

    @Override
    public LwParagraphData clone() throws CloneNotSupportedException {
        return (LwParagraphData) super.clone();
    }
}
