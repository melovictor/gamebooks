package hu.zagor.gamebooks.dispatcher;

import hu.zagor.gamebooks.support.logging.LogInject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

/**
 * Class to handle the modify events for the jsp directories during development time.
 * @author Tamas_Szekeres
 */
public class WatchServiceEventHandler implements Runnable {

    @LogInject
    private Logger logger;

    private WatchService watcher;
    private Map<WatchKey, String> watchKeyLocationMap;
    private final List<File> outputDirs = new ArrayList<File>();
    private WatchServiceIoHandler ioHandler;

    @Override
    public void run() {
        boolean shouldBreak = false;
        while (!shouldBreak) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (final InterruptedException e) {
                logger.error("WatchService has been interrupted!", e);
                shouldBreak = true;
                continue;
            }
            processEvent(key);
            final boolean valid = key.reset();
            if (!valid) {
                logger.error("WatchService key became invalid after reset!");
                shouldBreak = true;
            }
        }
    }

    private void processEvent(final WatchKey key) {
        for (final WatchEvent<?> event : key.pollEvents()) {
            final WatchEvent.Kind<?> kind = event.kind();
            if (kind == StandardWatchEventKinds.OVERFLOW) {
                continue;
            }

            final Path filePath = (Path) event.context();
            final String filename = filePath.getName(filePath.getNameCount() - 1).toString();
            logger.debug("File '" + filename + "' has changed, updating!");

            final String fileLocation = watchKeyLocationMap.get(key) + "/" + filename;
            final File file = new File(fileLocation);

            final String fileRelativePath = fileLocation.substring(fileLocation.indexOf("WEB-INF"));

            try {
                for (final File out : outputDirs) {
                    ioHandler.doCopyFiles(file, new File(out, fileRelativePath));
                    // ioHandler.doCopyFiles(file, new File(currentDir.getParent() + "/gamebooks/" +
                    // fileRelativePath));
                }
            } catch (final IOException e) {
                logger.error("Updating the file '" + filename + "' failed due to IO error!", e);
                continue;
            }
        }
    }

    public void setIoHandler(final WatchServiceIoHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    public void setWatcher(final WatchService watcher) {
        this.watcher = watcher;
    }

    /**
     * Add a new output directory for the updates.
     * @param dir the dir to add
     */
    public void addOutputDir(final File dir) {
        this.outputDirs.add(dir);
    }

    public void setWatchKeyLocationMap(final Map<WatchKey, String> watchKeyLocationMap) {
        this.watchKeyLocationMap = watchKeyLocationMap;
    }

}
