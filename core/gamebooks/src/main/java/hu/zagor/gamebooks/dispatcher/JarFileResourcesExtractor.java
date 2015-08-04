package hu.zagor.gamebooks.dispatcher;

import hu.zagor.gamebooks.support.logging.LogInject;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Allows extraction of contents of a JAR file. All files matching a given Ant path pattern will be extracted into a specified path.
 */
public final class JarFileResourcesExtractor {

    @Value("${jfre.extractionOutputDir}")
    private String extractionOutputDirPath;
    @Value("${jfre.outputDir}")
    private String outputDirPath;
    @Value("${jfre.rulesetDir}")
    private String rulesetDirPath;
    @Value("${jfre.booksDir}")
    private String booksDirPath;

    @LogInject
    private Logger logger;
    private WatchServiceEventHandler eventHandler;
    private WatchServiceIoHandler ioHandler;

    /**
     * Extracts the JSP files from the source rule and book directories straight into the target.
     * @throws IOException if an IO error occurs when reading the jar file
     */
    @PostConstruct
    public void extractRulesetSourceFiles() throws IOException {
        Assert.isTrue(validPath(extractionOutputDirPath), "Please set up the pathes in configuration-{yourname}.properties!");
        Assert.isTrue(validPath(outputDirPath), "Please set up the pathes in configuration-{yourname}.properties!");
        Assert.isTrue(validPath(rulesetDirPath), "Please set up the pathes in configuration-{yourname}.properties!");
        Assert.isTrue(validPath(booksDirPath), "Please set up the pathes in configuration-{yourname}.properties!");

        final WatchService watcher = FileSystems.getDefault().newWatchService();
        final Map<WatchKey, String> watchKeyLocationMap = new HashMap<>();

        final File extractionOutputDir = new File(extractionOutputDirPath);
        final File outputDir = new File(outputDirPath);
        final File rulesetDir = new File(rulesetDirPath);
        final File booksDir = new File(booksDirPath);
        final List<File> outputDirs = new ArrayList<>();
        outputDirs.add(extractionOutputDir);
        outputDirs.add(outputDir);

        logger.info("Ruleset directory: {}.", rulesetDir);
        extractFromRulesDir(watcher, watchKeyLocationMap, outputDirs, rulesetDir);
        extractFromBooksDir(watcher, watchKeyLocationMap, outputDirs, booksDir);

        extractJspFiles(outputDirs, new File(rulesetDir, "../gamebooks/src/main/webapp/WEB-INF/tiles"), watcher, watchKeyLocationMap);

        eventHandler.setWatcher(watcher);
        eventHandler.addOutputDir(outputDir);
        eventHandler.addOutputDir(extractionOutputDir);
        eventHandler.setWatchKeyLocationMap(watchKeyLocationMap);
        new Thread(eventHandler).start();
    }

    private boolean validPath(final String path) {
        return !StringUtils.isEmpty(path);
    }

    private void extractFromBooksDir(final WatchService watcher, final Map<WatchKey, String> watchKeyLocationMap, final List<File> outputDirs, final File booksDir)
        throws IOException {
        for (final File seriesDir : booksDir.listFiles()) {
            if (seriesDir.isDirectory() && !"collector".equals(seriesDir.getName())) {
                extractFromSeriesDirectory(watcher, watchKeyLocationMap, outputDirs, seriesDir);
            }
        }
    }

    private void extractFromSeriesDirectory(final WatchService watcher, final Map<WatchKey, String> watchKeyLocationMap, final List<File> outputDirs, final File seriesDir)
        throws IOException {
        for (final File bookDir : seriesDir.listFiles()) {
            if (bookDir.isDirectory()) {
                extractFromBookDirectory(watcher, watchKeyLocationMap, outputDirs, bookDir);
            }
        }
    }

    private void extractFromBookDirectory(final WatchService watcher, final Map<WatchKey, String> watchKeyLocationMap, final List<File> outputDirs, final File bookDir)
        throws IOException {
        for (final File bookInstanceDir : bookDir.listFiles()) {
            if (bookInstanceDir.isDirectory()) {
                extractJspFiles(outputDirs, new File(bookInstanceDir, "target/classes/WEB-INF/tiles/ruleset"), watcher, watchKeyLocationMap);
                extractJspFiles(outputDirs, new File(bookInstanceDir, "target/classes/WEB-INF/tiles/rules"), watcher, watchKeyLocationMap);
            }
        }
    }

    private void extractFromRulesDir(final WatchService watcher, final Map<WatchKey, String> watchKeyLocationMap, final List<File> outputDirs, final File rulesetDir)
        throws IOException {
        for (final File ruleDir : rulesetDir.listFiles()) {
            if (ruleDir.isDirectory()) {
                extractJspFiles(outputDirs, new File(ruleDir, "target/classes/WEB-INF/tiles"), watcher, watchKeyLocationMap);
            }
        }
    }

    private void extractJspFiles(final List<File> outputDirs, final File jspLocation, final WatchService watcher, final Map<WatchKey, String> watchKeyLocationMap)
        throws IOException {
        boolean dirContainsJspFiles = false;
        final File[] listFiles = jspLocation.listFiles();
        if (listFiles != null) {
            for (final File file : listFiles) {
                if (file.isDirectory()) {
                    extractJspFiles(outputDirs, file, watcher, watchKeyLocationMap);
                } else if (file.isFile() && file.getName().endsWith(".jsp")) {
                    final String fileAbsolutePath = file.getAbsolutePath();
                    final String fileRelativePath = fileAbsolutePath.substring(fileAbsolutePath.indexOf("WEB-INF"));

                    ioHandler.doCopyFiles(file, new File(outputDirs.get(0), fileRelativePath));
                    ioHandler.doCopyFiles(file, new File(outputDirs.get(1), fileRelativePath));
                    dirContainsJspFiles = true;
                }
            }
        }
        if (dirContainsJspFiles) {
            final WatchKey key = jspLocation.toPath().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
            watchKeyLocationMap.put(key, jspLocation.getCanonicalPath());
        }
    }

    public void setIoHandler(final WatchServiceIoHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    public void setEventHandler(final WatchServiceEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

}
