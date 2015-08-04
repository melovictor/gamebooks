package hu.zagor.gamebooks.mvc.settings.domain;

/**
 * Bean for storing a single-choice radio setting.
 * @author Tamas_Szekeres
 */
public class CheckboxSettingEntry extends SettingEntry {

    private String settingKey;
    private boolean defaultState;

    /**
     * Default constructor.
     */
    public CheckboxSettingEntry() {
        super("checkbox");
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(final String settingKey) {
        this.settingKey = settingKey;
    }

    public boolean isDefaultState() {
        return defaultState;
    }

    public void setDefaultState(final boolean defaultState) {
        this.defaultState = defaultState;
    }

}
