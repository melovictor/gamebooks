package hu.zagor.gamebooks.mvc.settings.domain;

import java.util.List;

/**
 * Bean for storing a group of settings.
 * @author Tamas_Szekeres
 */
public class SettingGroup {

    private String groupNameKey;
    private int groupPositionIndex;
    private List<SettingEntry> settingEntries;
    private boolean adminOnly;

    public String getGroupNameKey() {
        return groupNameKey;
    }

    public void setGroupNameKey(final String groupNameKey) {
        this.groupNameKey = groupNameKey;
    }

    public int getGroupPositionIndex() {
        return groupPositionIndex;
    }

    public void setGroupPositionIndex(final int groupPositionIndex) {
        this.groupPositionIndex = groupPositionIndex;
    }

    public List<SettingEntry> getSettingEntries() {
        return settingEntries;
    }

    public void setSettingEntries(final List<SettingEntry> settingEntries) {
        this.settingEntries = settingEntries;
    }

    public boolean isAdminOnly() {
        return adminOnly;
    }

    public void setAdminOnly(final boolean adminOnly) {
        this.adminOnly = adminOnly;
    }

}
