package hu.zagor.gamebooks.initiator.book;

public class BookLangData {

    private String seriesCode;
    private String position;
    private String actualPosition;
    private String bookId;
    private String bookIdDomain;
    private String lang;
    private String fullLang;
    private String title;

    private boolean hasItems;
    private boolean hasEnemies;

    private boolean generatable;
    private boolean hidden;

    public String getSeriesCode() {
        return seriesCode;
    }

    public void setSeriesCode(final String seriesCode) {
        this.seriesCode = seriesCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(final int position) {
        this.position = String.valueOf(position);
        this.actualPosition = this.position;
    }

    public void setPosition(final String position) {
        this.position = position.replace(".", "p");
        this.actualPosition = position;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(final String bookId) {
        this.bookId = bookId;
        this.bookIdDomain = bookId.substring(0, bookId.indexOf("."));
    }

    public String getLang() {
        return lang;
    }

    public String getCompactLang() {
        return lang.replace("_", "").toLowerCase();
    }

    public void setLang(final String lang) {
        this.lang = lang;
        switch (lang) {
        case "en":
            this.fullLang = "english";
            break;
        case "hu":
            this.fullLang = "hungarian";
            break;
        case "pt_BR":
            this.fullLang = "brazilian";
            break;
        default:
            throw new IllegalArgumentException("Unknown language '" + lang + "'!");
        }
    }

    public String getFullLang() {
        return fullLang;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContentFileName() {
        return seriesCode + position + "content.xml";
    }

    public String getEnemiesFileName() {
        String enemyFileName = "";
        if (hasEnemies) {
            enemyFileName = seriesCode + position + "enemies.xml";
        }
        return enemyFileName;
    }

    public String getItemsFileName() {
        String itemsFileName = "";
        if (hasItems) {
            itemsFileName = seriesCode + position + "items.xml";
        }
        return itemsFileName;
    }

    public String getBookIdDomain() {
        return bookIdDomain;
    }

    public void init(final BookBaseData baseData) {
        hasItems = baseData.hasItems();
        hasEnemies = baseData.hasEnemies();

        if (seriesCode == null) {
            seriesCode = baseData.getSeriesCode();
        }
        if (position == null) {
            position = baseData.getPosition();
            actualPosition = baseData.getPosition();
        }
    }

    public String getSeriesCodeCapital() {
        return capitalize(seriesCode);
    }

    private String capitalize(final String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public String getActualPosition() {
        return actualPosition;
    }

    public boolean isGeneratable() {
        return generatable;
    }

    public void setGeneratable(final boolean generatable) {
        this.generatable = generatable;
    }

    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

}
