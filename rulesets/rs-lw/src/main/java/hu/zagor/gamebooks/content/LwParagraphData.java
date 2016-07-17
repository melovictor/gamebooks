package hu.zagor.gamebooks.content;

public class LwParagraphData extends ParagraphData {
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

}
