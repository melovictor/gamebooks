package hu.zagor.gamebooks.mvc.settings.service;

import hu.zagor.gamebooks.mvc.settings.domain.SettingGroup;

import java.util.Map;

/**
 * Class for extracting the default values from the setting entries.
 * @author Tamas_Szekeres
 */
public interface SettingDefaultValueExtractor {

    /**
     * Extracts the default values from inside a setting group.
     * @param group the {@link SettingGroup} to get the values from
     * @param defaultSettings the map into which the data has to be injected
     */
    void extract(SettingGroup group, Map<String, String> defaultSettings);

}
