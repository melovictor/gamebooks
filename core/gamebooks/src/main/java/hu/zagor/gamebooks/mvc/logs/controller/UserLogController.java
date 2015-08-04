package hu.zagor.gamebooks.mvc.logs.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.directory.DirectoryProvider;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;
import hu.zagor.gamebooks.mvc.logs.domain.LogFileContainer;
import hu.zagor.gamebooks.mvc.logs.domain.LogFileData;
import hu.zagor.gamebooks.support.scanner.Scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for displaying and downloading user log files.
 * @author Tamas_Szekeres
 */
@Controller
public class UserLogController extends AbstractRequestWrappingController {

    private static final int FULL_FILE_SIZE = 3;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("YYYY. MMMM dd. HH:mm:ss.S");
    private static final Pattern USERNAME = Pattern.compile("User '(.*)' logged in successfully.");
    private static final Map<String, String> USER_NAMES = new HashMap<>();

    @Autowired
    private DirectoryProvider directoryProvider;

    /**
     * Displays the opening log page with all the available log files.
     * @param model the {@link Model}
     * @param request the {@link HttpServletRequest} bean
     * @return the target page's identifier
     */
    @RequestMapping(value = PageAddresses.LOGS)
    public String listDirectories(final Model model, final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        String target;
        if (!wrapper.getPlayer().isAdmin()) {
            target = "redirect:" + PageAddresses.BOOK_LIST;
        } else {
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

            target = "logs";
        }
        model.addAttribute("pageTitle", "page.title");

        return target;
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
        if (filePieces.length == FULL_FILE_SIZE) {
            final LogFileData logFileData = new LogFileData();
            final String userId = filePieces[1];
            String userName = USER_NAMES.get(userId);
            if (userName == null) {
                userName = provideUserName(userId, file);
            }
            logFileData.setUserId(userId);
            logFileData.setUserName(userName);
            logFileData.setTimestamp(filePieces[2]);
            final Date loginDate = new Date();
            loginDate.setTime(Long.parseLong(logFileData.getTimestamp()));
            logFileData.setLoginDateTime(SDF.format(loginDate));
            container.add(logFileData);
        }
    }

    private String provideUserName(final String userId, final File file) {
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
        }
        IOUtils.copy(new FileInputStream(fileLocation), response.getOutputStream());
    }

}
