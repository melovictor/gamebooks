package hu.zagor.gamebooks.mvc.login.service;

import java.io.File;
import java.util.Collection;
import org.apache.commons.io.FileUtils;

/**
 * Implementation to be used on the development environment, always returns the string "dev".
 * @author Tamas_Szekeres
 */
public class AppLocationBasedVersionNumberProvider extends FileBasedVersionNumberProvider {

    @Override
    protected File getFile() {
        File newestFile = null;
        final String rootDirectory = this.getClass().getClassLoader().getResource("").getPath();
        final Collection<File> allFiles = FileUtils.listFiles(new File(rootDirectory), null, true);
        for (final File file : allFiles) {
            if (newestFile == null || newestFile.lastModified() < file.lastModified()) {
                newestFile = file;
            }
        }
        return newestFile;
    }
}
