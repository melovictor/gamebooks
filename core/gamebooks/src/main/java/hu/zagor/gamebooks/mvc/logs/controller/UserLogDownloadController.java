package hu.zagor.gamebooks.mvc.logs.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.directory.DirectoryProvider;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
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

    /**
     * Displays the selected log file content.
     * @param response the {@link HttpServletResponse} bean
     * @param file the name of the log file to load
     * @param extension the extension of the file's name
     * @throws IOException when an {@link IOException} occurs
     */
    @RequestMapping(value = PageAddresses.LOGS + "/{file}/{extension}")
    public void getLogFile(final HttpServletResponse response, @PathVariable("file") final String file, @PathVariable("extension") final String extension)
        throws IOException {
        final File fileLocation = (File) getBeanFactory().getBean("file", directoryProvider.getLogFileDirectory(), file);
        if ("zip".equals(extension)) {
            response.setHeader("Content-Disposition", "inline; filename=" + fileLocation.getName());
        } else {
            response.setContentType("text/plain; charset=utf-8");
        }
        IOUtils.copy(new FileInputStream(fileLocation), response.getOutputStream());
    }

    /**
     * Displays the selected log file content.
     * @param response the {@link HttpServletResponse} bean
     * @param userId the name of the user's directory
     * @param bookId the id of the book to which the saved game belongs
     * @throws IOException when an {@link IOException} occurs
     */
    @RequestMapping(value = PageAddresses.SAVED_GAMES + "/{userId}/{bookId}")
    public void getSaveFile(final HttpServletResponse response, @PathVariable("userId") final String userId, @PathVariable("bookId") final String bookId)
        throws IOException {
        final File fileLocation = (File) getBeanFactory().getBean("file", directoryProvider.getSaveGameDirectory(), userId + "/" + bookId);
        response.setContentType("text/xml; charset=utf-8");
        IOUtils.copy(new FileInputStream(fileLocation), response.getOutputStream());
    }

}
