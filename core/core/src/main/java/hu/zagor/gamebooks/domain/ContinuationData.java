package hu.zagor.gamebooks.domain;

/**
 * Bean storing data about continuing the adventure with the same hero from a previous book.
 * @author Tamas_Szekeres
 */
public class ContinuationData {
    private long previousBookId;
    private long nextBookId;
    private BookInformations nextBookInfo;
    private String currentBookLastSectionId;
    private String previousBookLastSectionId;
    private String continuationPageName;

    public long getPreviousBookId() {
        return previousBookId;
    }

    public void setPreviousBookId(final long previousBookId) {
        this.previousBookId = previousBookId;
    }

    public String getPreviousBookLastSectionId() {
        return previousBookLastSectionId;
    }

    public void setPreviousBookLastSectionId(final String previousBookLastSectionId) {
        this.previousBookLastSectionId = previousBookLastSectionId;
    }

    public String getContinuationPageName() {
        return continuationPageName;
    }

    public void setContinuationPageName(final String continuationPageName) {
        this.continuationPageName = continuationPageName;
    }

    public String getCurrentBookLastSectionId() {
        return currentBookLastSectionId;
    }

    public void setCurrentBookLastSectionId(final String currentBookLastSectionId) {
        this.currentBookLastSectionId = currentBookLastSectionId;
    }

    public long getNextBookId() {
        return nextBookId;
    }

    public void setNextBookId(final long nextBookId) {
        this.nextBookId = nextBookId;
    }

    public BookInformations getNextBookInfo() {
        return nextBookInfo;
    }

    public void setNextBookInfo(final BookInformations nextBookInfo) {
        this.nextBookInfo = nextBookInfo;
    }

}
