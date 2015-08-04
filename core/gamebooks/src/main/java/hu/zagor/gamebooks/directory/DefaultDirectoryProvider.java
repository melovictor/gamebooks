package hu.zagor.gamebooks.directory;

import hu.zagor.gamebooks.directory.domain.DirectoryDescriptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Default implementation of the {@link DirectoryProvider} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultDirectoryProvider implements DirectoryProvider {

    @Autowired
    private DirectoryDescriptor directoryDescriptor;

    @Override
    public String getSaveGameDirectory() {
        return directoryDescriptor.getSaveGameDirectory();
    }

    @Override
    public String getLogFileDirectory() {
        return directoryDescriptor.getLogFilesDirectory();
    }

}
