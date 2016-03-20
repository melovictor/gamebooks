package hu.zagor.gamebooks.initiator.collector;

public class CollectorData {
    private String bookDirName;
    private String bookSeriesFullName;
    private String basicRuleset;

    public String getBookDirName() {
        return bookDirName;
    }

    public void setBookDirName(final String bookDirName) {
        this.bookDirName = bookDirName;
    }

    public String getBookSeriesFullName() {
        return bookSeriesFullName;
    }

    public void setBookSeriesFullName(final String bookSeriesFullName) {
        this.bookSeriesFullName = bookSeriesFullName;
    }

    public String getBasicRuleset() {
        return basicRuleset;
    }

    public void setBasicRuleset(final String basicRuleset) {
        this.basicRuleset = basicRuleset;
    }
}
