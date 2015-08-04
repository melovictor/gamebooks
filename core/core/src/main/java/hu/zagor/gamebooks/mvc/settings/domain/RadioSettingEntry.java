package hu.zagor.gamebooks.mvc.settings.domain;

import java.util.List;

/**
 * Bean for storing a single-choice radio setting.
 * @author Tamas_Szekeres
 */
public class RadioSettingEntry extends SettingEntry {

    private String settingKey;
    private List<SettingEntrySub> subEntries;

    /**
     * Default constructor.
     */
    public RadioSettingEntry() {
        super("radio");
    }

    public List<SettingEntrySub> getSubEntries() {
        return subEntries;
    }

    public void setSubEntries(final List<SettingEntrySub> subEntries) {
        this.subEntries = subEntries;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(final String settingKey) {
        this.settingKey = settingKey;
    }

}
