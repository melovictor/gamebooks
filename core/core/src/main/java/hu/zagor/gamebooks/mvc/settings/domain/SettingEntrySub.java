package hu.zagor.gamebooks.mvc.settings.domain;

/**
 * Class representing a sub-entry in a radio or checkbox group.
 * @author Tamas_Szekeres
 */
public class SettingEntrySub {

    private String titleKey;
    private String optionKey;
    private boolean defaultEntry;

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(final String titleKey) {
        this.titleKey = titleKey;
    }

    public String getOptionKey() {
        return optionKey;
    }

    public void setOptionKey(final String optionKey) {
        this.optionKey = optionKey;
    }

    public boolean isDefaultEntry() {
        return defaultEntry;
    }

    public void setDefaultEntry(final boolean defaultEntry) {
        this.defaultEntry = defaultEntry;
    }

}
