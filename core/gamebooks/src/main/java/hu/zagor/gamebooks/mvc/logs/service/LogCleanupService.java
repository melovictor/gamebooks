package hu.zagor.gamebooks.mvc.logs.service;

import hu.zagor.gamebooks.directory.DirectoryProvider;
import hu.zagor.gamebooks.mvc.logs.service.filter.LogFilenameFilter;
import hu.zagor.gamebooks.support.logging.LogInject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * A service that ensures that old logs are compressed so they take less space on the server.
 * @author Tamas_Szekeres
 */
@Component
@Lazy(false)
public class LogCleanupService {

    @LogInject private Logger logger;
    @Autowired private LogFilenameFilter logFilenameFilter;
    @Autowired private DirectoryProvider directoryProvider;

    /**
     * Method that runs every time at 02:10 to zip up all the logs that are older than a week.
     */
    @Scheduled(cron = "0 10 2 * * *")
    public void clearUpLogs() {
        final Map<String, Set<File>> logs = new HashMap<>();
        final File[] logFiles = getLogFileLis();
        assignLogsToSlots(logs, logFiles);
        packLogs(logs);
        deleteLogs(logs);
    }

    private void deleteLogs(final Map<String, Set<File>> logs) {
        for (final Entry<String, Set<File>> mapEntry : logs.entrySet()) {
            for (final File file : mapEntry.getValue()) {
                file.delete();
            }
        }
    }

    private void packLogs(final Map<String, Set<File>> logs) {
        for (final Entry<String, Set<File>> mapEntry : logs.entrySet()) {
            final File zipFile = new File(directoryProvider.getLogFileDirectory() + "/archive." + mapEntry.getKey() + ".zip");
            try (final FileOutputStream uncompressedFileOutputStream = new FileOutputStream(zipFile);
                ZipOutputStream compressetZipFileOutputStream = new ZipOutputStream(uncompressedFileOutputStream)) {
                for (final File logFile : mapEntry.getValue()) {
                    addToZip(compressetZipFileOutputStream, logFile);
                }
            } catch (final IOException ex) {
                logger.error("Unexpected error occurred while compressing the log files.", ex);
            }
        }
    }

    private void addToZip(final ZipOutputStream zipFileOutputStream, final File logFile) throws IOException {
        final FileInputStream fis = new FileInputStream(logFile);
        final ZipEntry zipEntry = new ZipEntry(logFile.getName());
        zipFileOutputStream.putNextEntry(zipEntry);
        IOUtils.copy(fis, zipFileOutputStream);
        zipFileOutputStream.closeEntry();
        fis.close();
    }

    private void assignLogsToSlots(final Map<String, Set<File>> logs, final File[] logFiles) {
        final DateTime cutoffDate = DateTime.now().minusWeeks(1).withTimeAtStartOfDay();
        for (final File logFile : logFiles) {
            assignLogToSlot(logs, logFile, cutoffDate);
        }
    }

    private File[] getLogFileLis() {
        final String logFileDirectory = directoryProvider.getLogFileDirectory();
        final File file = new File(logFileDirectory);
        final File[] logFiles = file.listFiles(logFilenameFilter);
        return logFiles;
    }

    private void assignLogToSlot(final Map<String, Set<File>> logs, final File logFile, final DateTime cutoffDate) {
        final long lastModified = logFile.lastModified();
        final DateTime lastModifiedDateTime = new DateTime(lastModified);
        if (cutoffDate.isAfter(lastModifiedDateTime)) {
            final String dateKey = lastModifiedDateTime.toString("YYYY-MM-dd");
            provideSlot(logs, dateKey);
            logs.get(dateKey).add(logFile);
        }
    }

    private void provideSlot(final Map<String, Set<File>> logs, final String dateKey) {
        if (!logs.containsKey(dateKey)) {
            logs.put(dateKey, new HashSet<File>());
        }
    }
}
