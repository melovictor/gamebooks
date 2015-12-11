package hu.zagor.gamebooks.mvc.logs.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.books.bookinfo.BookInformationFetcher;
import hu.zagor.gamebooks.directory.DirectoryProvider;
import hu.zagor.gamebooks.domain.BookInformations;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;
import hu.zagor.gamebooks.mvc.logs.domain.LogFileContainer;
import hu.zagor.gamebooks.mvc.logs.domain.LogFileData;
import hu.zagor.gamebooks.support.scanner.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for displaying and downloading user log files.
 * @author Tamas_Szekeres
 */
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UserLogController extends AbstractRequestWrappingController {

    private static final int BOOK_ID_BOOK = 3;
    private static final int TIMESTAMP_IDX = 2;
    private static final int USER_ID_IDX = 1;

    private static final int BASE_FILE_SIZE = 2;
    private static final int GENERIC_FILE_SIZE = 3;
    private static final int BOOK_FILE_SIZE = 4;

    private static final Pattern USERNAME = Pattern.compile("User '(.*)' logged in successfully.");
    private static final Map<String, String> USER_NAMES = new HashMap<>();

    private static final DateTimeFormatter SHORT_FORMAT = DateTimeFormat.forPattern("YYYY. MMMM d.");
    private static final DateTimeFormatter LONG_FORMAT = DateTimeFormat.forPattern("YYYY. MMMM d. HH:mm:ss");

    @Autowired private DirectoryProvider directoryProvider;
    @Autowired private BookInformationFetcher infoFetcher;

    /**
     * Displays the opening log page with all the available log files.
     * @param model the {@link Model}
     * @return the target page's identifier
     */
    @RequestMapping(value = PageAddresses.LOGS)
    public String listDirectories(final Model model) {
        final LogFileContainer container = new LogFileContainer();
        final Set<String> archivedContainer = new TreeSet<String>(Collections.reverseOrder());
        final String logDir = directoryProvider.getLogFileDirectory();
        final File logFiles = (File) getBeanFactory().getBean("file", logDir);
        if (logFiles.exists()) {
            for (final File file : logFiles.listFiles()) {
                processFile(container, archivedContainer, file);
            }
        }
        model.addAttribute("logFiles", container);
        model.addAttribute("archivedLogFiles", archivedContainer);

        model.addAttribute("pageTitle", "page.title");

        return PageAddresses.LOGS;
    }

    private void processFile(final LogFileContainer container, final Set<String> archivedContainer, final File file) {
        if (file.isFile() && file.canRead()) {
            if (file.getName().endsWith(".log")) {
                processLogFile(container, file);
            } else if (file.getName().endsWith(".zip")) {
                processArchiveFile(archivedContainer, file);
            }
        }
    }

    private void processArchiveFile(final Set<String> archivedContainer, final File file) {
        final String fileName = file.getName().replace(".zip", "").replace("archive.", "");
        archivedContainer.add(fileName);
    }

    private void processLogFile(final LogFileContainer container, final File file) {
        final String fileName = file.getName().replace(".log", "");
        final String[] filePieces = fileName.split("-");

        final LogFileData logFileData = new LogFileData();
        String formattedTime = null;

        if (filePieces.length >= GENERIC_FILE_SIZE) {
            final String userId = filePieces[USER_ID_IDX];
            logFileData.setUserId(userId);
            logFileData.setTimestamp(filePieces[TIMESTAMP_IDX]);
            logFileData.setUserName(getUsername(logFileData, userId));
            setBookInformation(filePieces, logFileData);
            formattedTime = LONG_FORMAT.withZone(DateTimeZone.forID("Europe/Budapest")).print(Long.parseLong(logFileData.getTimestamp()));
            container.add(logFileData);
        } else if (filePieces.length == BASE_FILE_SIZE) {
            final String timestamp = filePieces[1].replace("base", "0");
            logFileData.setTimestamp(timestamp.substring(1));
            formattedTime = SHORT_FORMAT.print(Long.parseLong(timestamp));
            container.addBase(logFileData);
        }
        logFileData.setSize(file.length());
        logFileData.setLoginDateTime(formattedTime);

    }

    private void setBookInformation(final String[] filePieces, final LogFileData logFileData) {
        if (filePieces.length == BOOK_FILE_SIZE) {
            logFileData.setBookId(filePieces[BOOK_ID_BOOK]);
            final BookInformations info = infoFetcher.getInfoById(logFileData.getBookId());
            logFileData.setBookName(info.getTitle());
        }
    }

    private String getUsername(final LogFileData logFileData, final String userId) {
        String userName = USER_NAMES.get(userId);
        if (userName == null) {
            userName = provideUserName(userId, logFileData);
        }
        return userName;
    }

    private String provideUserName(final String userId, final LogFileData logFileData) {
        final File file = new File(directoryProvider.getLogFileDirectory(), "log-" + logFileData.getUserId() + "-" + logFileData.getTimestamp() + ".log");
        final String userName = getUserNameFromLog(file);
        USER_NAMES.put(userId, userName);
        return userName;
    }

    private String getUserNameFromLog(final File file) {
        String username = null;
        final Scanner scanner = (Scanner) getBeanFactory().getBean("scanner", file, "UTF-8");
        final String line = scanner.nextLine();
        final Matcher matcher = USERNAME.matcher(line);
        if (matcher.find()) {
            username = matcher.group(1);
        }
        return username;
    }

    /**
     * Displays the selected log file content.
     * @param response the {@link HttpServletResponse} bean
     * @param file the name of the log file to load
     * @param extension the extension of the file's name
     * @throws IOException when an {@link IOException} occurs
     */
    @RequestMapping(value = PageAddresses.LOGS + "/{file}/{extension}")
    public void getFile(final HttpServletResponse response, @PathVariable("file") final String file, @PathVariable("extension") final String extension)
        throws IOException {
        final File fileLocation = (File) getBeanFactory().getBean("file", directoryProvider.getLogFileDirectory(), file);
        if ("zip".equals(extension)) {
            response.setHeader("Content-Disposition", "inline; filename=" + fileLocation.getName());
        } else {
            response.setContentType("text/plain; charset=utf-8");
        }
        IOUtils.copy(new FileInputStream(fileLocation), response.getOutputStream());
    }

}
