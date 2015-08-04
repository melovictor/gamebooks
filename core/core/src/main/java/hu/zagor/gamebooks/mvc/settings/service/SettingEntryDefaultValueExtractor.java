package hu.zagor.gamebooks.mvc.settings.service;

import hu.zagor.gamebooks.mvc.settings.domain.SettingEntry;

import java.util.Map;

/**
 * Interface for extracting default values from a specific type of setting entry.
 * @author Tamas_Szekeres
 */
public interface SettingEntryDefaultValueExtractor {

    /**
     * Extracts the default value from a specific entry.
     * @param entryObject the entry
     * @param defaultSettings the map of the default settings
     */
    void extract(SettingEntry entryObject, Map<String, String> defaultSettings);

}
