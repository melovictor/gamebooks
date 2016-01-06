package hu.zagor.gamebooks.ff.mvc.book.image.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.controller.ImageHandler;
import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Image controller for the Fighting Fantasy ruleset supporting random small images.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/{bookId}/resources/{dir}")
public class FfBookImageController extends AbstractRequestWrappingController {

    @Autowired private ImageHandler imageHandler;

    /**
     * Handles requests for random small images.
     * @param request the {@link HttpServletRequest}
     * @param response the {@link HttpServletResponse}
     * @param dir the book directory in which the small images must be located
     * @param locale the locale currently in use
     * @throws IOException thrown when an error occurs during the lookup of the image
     */
    @RequestMapping(value = "image.small")
    public void handleSmallImage(final HttpServletRequest request, final HttpServletResponse response, @PathVariable("dir") final String dir, final Locale locale)
        throws IOException {

        Assert.notNull(dir, "The parameter 'dir' cannot be null!");
        Assert.isTrue(dir.length() > 0, "The parameter 'dir' cannot be empty!");

        final ImageLocation imageLocation = imageHandler.createImageLocation(dir, "small*", locale);

        imageHandler.handleImage(request, response, imageLocation, true);
    }
}
