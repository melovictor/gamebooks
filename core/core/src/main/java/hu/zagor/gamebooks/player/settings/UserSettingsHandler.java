package hu.zagor.gamebooks.player.settings;

import hu.zagor.gamebooks.player.PlayerUser;

/**
 * Interface to handle the loading and saving of the user settings.
 * @author Tamas_Szekeres
 */
public interface UserSettingsHandler {

    /**
     * Loads the settings from the player's own setting file.
     * @param player the {@link PlayerUser} into which the settings must be loaded
     */
    void loadSettings(PlayerUser player);

    /**
     * Saves the updated settings into the player's own setting file.
     * @param player the {@link PlayerUser} from which the settings must be saved
     */
    void saveSettings(PlayerUser player);

}
