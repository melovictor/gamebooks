package hu.zagor.gamebooks.mvc.settings.domain;

import java.util.Comparator;

/**
 * Comparator for {@link SettingGroup} objects.
 * @author Tamas_Szekeres
 */
public class SettingGroupComparator implements Comparator<SettingGroup> {

    @Override
    public int compare(final SettingGroup o1, final SettingGroup o2) {
        return Integer.compare(o1.getGroupPositionIndex(), o2.getGroupPositionIndex());
    }

}
