package hu.zagor.gamebooks.player.settings;

import hu.zagor.gamebooks.mvc.settings.domain.SettingGroup;

import java.util.Map;
import java.util.SortedSet;

/**
 * Class for handling the default values of the different user settings.
 * @author Tamas_Szekeres
 */
public interface DefaultSettingsHandler {

    /**
     * Gets all the settings ordered properly.
     * @return the ordered setting groups
     */
    SortedSet<SettingGroup> getOrderedSettings();

    /**
     * Obtains all the default values for the currently available settings.
     * @return the default values for the existing settings
     */
    Map<String, String> getDefaultSettings();

}
