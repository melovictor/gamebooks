package hu.zagor.gamebooks.initiator.book;

public class BookBaseData {
    private boolean forceOverride;

    private String mainLanguage;
    private String seriesCode;
    private String titleCode;
    private String position;
    private String ruleset;
    private boolean hasEnemies;
    private boolean hasItems;
    private boolean hasMap;
    private boolean hasInventory;
    private String defaultSkillTestType;
    private String collectorCode;
    private String collectorName;
    private boolean mediaProject;
    private boolean charPageRequired;

    public String getSeriesCode() {
        return seriesCode;
    }

    public String getSeriesCodeCapital() {
        return capitalize(seriesCode);
    }

    public void setSeriesCode(final String seriesCode) {
        this.seriesCode = seriesCode;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(final String titleCode) {
        this.titleCode = titleCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(final int position) {
        this.position = String.valueOf(position);
    }

    public String getRuleset() {
        return ruleset;
    }

    public String getRulesetCapital() {
        return capitalize(ruleset);
    }

    public void setRuleset(final String ruleset) {
        this.ruleset = ruleset;
    }

    public void setHasEnemies(final boolean hasEnemies) {
        this.hasEnemies = hasEnemies;
    }

    public void setHasItems(final boolean hasItems) {
        this.hasItems = hasItems;
    }

    public boolean hasEnemies() {
        return hasEnemies;
    }

    public boolean hasItems() {
        return hasItems;
    }

    private String capitalize(final String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public boolean hasMap() {
        return hasMap;
    }

    public void setHasMap(final boolean hasMap) {
        this.hasMap = hasMap;
    }

    public boolean hasInventory() {
        return hasInventory;
    }

    public void setHasInventory(final boolean hasInventory) {
        this.hasInventory = hasInventory;
    }

    public String getDefaultSkillTestType() {
        return defaultSkillTestType;
    }

    public void setDefaultSkillTestType(final String defaultSkillTestType) {
        this.defaultSkillTestType = defaultSkillTestType;
    }

    public String getCollectorCode() {
        return collectorCode;
    }

    public void setCollectorCode(final String collectorCode) {
        this.collectorCode = collectorCode;
    }

    public void setMediaProject(final boolean mediaProject) {
        this.mediaProject = mediaProject;
    }

    public boolean hasMediaProject() {
        return mediaProject;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(final String collectorName) {
        this.collectorName = collectorName;
    }

    public String getBookInfoPrefix() {
        return ("ff".equals(seriesCode) || "sor".equals(seriesCode) || "kjp".equals(seriesCode)) ? "Ff" : "";
    }

    public boolean isCharPageRequired() {
        return charPageRequired;
    }

    public void setCharPageRequired(final boolean charPageRequired) {
        this.charPageRequired = charPageRequired;
    }

    public boolean isForceOverride() {
        return forceOverride;
    }

    @Deprecated
    public void setForceOverride(final boolean forceOverride) {
        this.forceOverride = forceOverride;
    }

    public String getMainLanguage() {
        return mainLanguage;
    }

    public void setMainLanguage(final String mainLanguage) {
        this.mainLanguage = mainLanguage;
    }

}
