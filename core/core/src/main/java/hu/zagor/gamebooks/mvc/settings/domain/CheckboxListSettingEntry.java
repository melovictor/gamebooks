package hu.zagor.gamebooks.mvc.settings.domain;

import java.util.List;

/**
 * Bean for storing a single-choice radio setting.
 * @author Tamas_Szekeres
 */
public class CheckboxListSettingEntry extends SettingEntry {

    private List<SettingEntrySub> subEntries;

    /**
     * Default constructor.
     */
    public CheckboxListSettingEntry() {
        super("checkboxlist");
    }

    public List<SettingEntrySub> getSubEntries() {
        return subEntries;
    }

    public void setSubEntries(final List<SettingEntrySub> subEntries) {
        this.subEntries = subEntries;
    }

}
