package hu.zagor.gamebooks.mvc.settings.domain;

/**
 * Bean for storing a single settings.
 * @author Tamas_Szekeres
 */
public abstract class SettingEntry {

    private String settingNameKey;
    private final String settingType;

    /**
     * Basic constructor that expects the setting type to be specified for this specific setting in order to
     * know how the option has to be rendered on screen.
     * @param settingType the setting type
     */
    public SettingEntry(final String settingType) {
        this.settingType = settingType;
    }

    public String getSettingNameKey() {
        return settingNameKey;
    }

    public void setSettingNameKey(final String settingNameKey) {
        this.settingNameKey = settingNameKey;
    }

    public String getSettingType() {
        return settingType;
    }

}
