package hu.zagor.gamebooks.directory;

/**
 * Interface for providing directory information for the components.
 * @author Tamas_Szekeres
 */
public interface DirectoryProvider {

    /**
     * Returns the absolute save game directory location.
     * @return the save game directory
     */
    String getSaveGameDirectory();

    /**
     * Returns the absolute log file directory location.
     * @return the log file directory
     */
    String getLogFileDirectory();
}
