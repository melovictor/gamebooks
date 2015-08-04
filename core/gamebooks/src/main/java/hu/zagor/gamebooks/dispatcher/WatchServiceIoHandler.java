package hu.zagor.gamebooks.dispatcher;

import hu.zagor.gamebooks.support.logging.LogInject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * Class for handling the stream copy tasks.
 * @author Tamas_Szekeres
 */
public class WatchServiceIoHandler {

    @LogInject
    private Logger logger;

    /**
     * Copies the source file into the target file.
     * @param sourceFile the input file
     * @param targetFile the output file
     * @throws IOException occurs when there is a problem during the copy process
     */
    public void doCopyFiles(final File sourceFile, final File targetFile) throws IOException {
        targetFile.getParentFile().mkdirs();

        logger.debug("Source location: " + sourceFile.getCanonicalPath());
        logger.debug("Target location: " + targetFile.getCanonicalPath());

        final InputStream inputStream = new FileInputStream(sourceFile);
        final FileOutputStream outputStream = new FileOutputStream(targetFile);
        copyAndClose(inputStream, outputStream);
    }

    /**
     * Copies the input stream into the output stream and closes both.
     * @param in the input stream
     * @param out the output stream
     * @throws IOException occurs when there is a problem during the copy process
     */
    public void copyAndClose(final InputStream in, final OutputStream out) throws IOException {
        try {
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
}
