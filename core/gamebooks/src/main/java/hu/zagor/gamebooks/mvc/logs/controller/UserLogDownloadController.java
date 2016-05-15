package hu.zagor.gamebooks.mvc.logs.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.directory.DirectoryProvider;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;
import hu.zagor.gamebooks.support.stream.IoUtilsWrapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for downloadng user log and save files.
 * @author Tamas_Szekeres
 */
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UserLogDownloadController extends AbstractRequestWrappingController {
    @Autowired private DirectoryProvider directoryProvider;
    @Autowired private IoUtilsWrapper ioUtilsWrapper;

    /**
     * Displays or deletes the selected log file content.
     * @param response the {@link HttpServletResponse} bean
     * @param file the name of the log file to load
     * @param extension the extension of the file's name
     * @param action the action to execute with the selected log file
     * @throws IOException when an {@link IOException} occurs
     */
    @RequestMapping(value = PageAddresses.LOGS + "/{file}/{extension}/{action}")
    public void handleLogFile(final HttpServletResponse response, @PathVariable("file") final String file, @PathVariable("extension") final String extension,
        @PathVariable("action") final String action) throws IOException {
        final File fileLocation = (File) getBeanFactory().getBean("file", directoryProvider.getLogFileDirectory(), file);
        if ("delete".equals(action)) {
            fileLocation.delete();
            goBackToLogDisplay(response);
        } else {
            if ("zip".equals(extension)) {
                response.setHeader("Content-Disposition", "inline; filename=" + fileLocation.getName());
            } else {
                response.setContentType("text/plain; charset=utf-8");
            }
            flushFile(response, fileLocation);
        }
    }

    /**
     * Displays or deletes the selected log file content.
     * @param response the {@link HttpServletResponse} bean
     * @param userId the name of the user's directory
     * @param bookId the id of the book to which the saved game belongs
     * @param action the action to execute with the selected saved game file
     * @throws IOException when an {@link IOException} occurs
     */
    @RequestMapping(value = PageAddresses.SAVED_GAMES + "/{userId}/{bookId}/{action}")
    public void handleSaveFile(final HttpServletResponse response, @PathVariable("userId") final String userId, @PathVariable("bookId") final String bookId,
        @PathVariable("action") final String action) throws IOException {
        final File fileLocation = (File) getBeanFactory().getBean("file", directoryProvider.getSaveGameDirectory(), userId + "/" + bookId);
        if ("delete".equals(action)) {
            fileLocation.delete();
            goBackToLogDisplay(response);
        } else {
            response.setContentType("text/xml; charset=utf-8");
            flushFile(response, fileLocation);
        }
    }

    private void goBackToLogDisplay(final HttpServletResponse response) throws IOException {
        response.sendRedirect("../../../logs");
    }

    private void flushFile(final HttpServletResponse response, final File fileLocation) throws IOException, FileNotFoundException {
        try (FileInputStream in = new FileInputStream(fileLocation)) {
            ioUtilsWrapper.copy(in, response.getOutputStream());
        }
    }

}
