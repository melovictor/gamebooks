package hu.zagor.gamebooks.initiator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public abstract class AbstractGenerator {

    protected void createDir(final File baseDir, final String subdirName) {
        new File(baseDir, subdirName).mkdirs();
    }

    protected void createFile(final File baseDir, final String subdirName, final String fileName, final String fileContent) throws IOException {
        createFile(new File(baseDir, subdirName), fileName, fileContent);
    }

    protected void createFile(final File baseDir, final String fileName, final String fileContent) throws IOException {
        createDir(baseDir, "");
        final File file = new File(baseDir, fileName);
        file.createNewFile();

        try (FileOutputStream outStream = new FileOutputStream(file); OutputStreamWriter streamWriter = new OutputStreamWriter(outStream, "UTF-8")) {
            streamWriter.write(fileContent);
        }
    }
}
