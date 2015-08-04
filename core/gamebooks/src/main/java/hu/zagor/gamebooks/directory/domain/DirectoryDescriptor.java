package hu.zagor.gamebooks.directory.domain;

/**
 * Bean for storing directory pathes.
 * @author Tamas_Szekeres
 */
public class DirectoryDescriptor {

    private final String saveGameDirectory;
    private final String logFilesDirectory;

    /**
     * Basic constructor that expects two directory pathes.
     * @param saveGameDirectory the path to the saved games
     * @param logFilesDirectory the path to the log files
     */
    public DirectoryDescriptor(final String saveGameDirectory, final String logFilesDirectory) {
        this.saveGameDirectory = saveGameDirectory;
        this.logFilesDirectory = logFilesDirectory;
    }

    public String getSaveGameDirectory() {
        return saveGameDirectory;
    }

    public String getLogFilesDirectory() {
        return logFilesDirectory;
    }

}
