package hu.zagor.gamebooks.mvc.book.image.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.controller.ImageHandler;
import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.controller.image.ImageLookupStrategyType;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.controller.AbstractRequestWrappingController;
import hu.zagor.gamebooks.player.PlayerSettings;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Class for handling the retrieval of the images in the book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/{bookId}/resources/{dir}")
public class GenericBookImageController extends AbstractRequestWrappingController {

    @Autowired
    private ImageHandler imageHandler;

    /**
     * Handles requests for images.
     * @param request the request
     * @param response the response into which we're writing the output stream
     * @param dir the name of the directory of the requested image file
     * @param file the name of the image file
     * @param locale the current locale
     * @throws IOException when an I/O error occurs
     */
    @RequestMapping(value = "{file}")
    public void handleImage(final HttpServletRequest request, final HttpServletResponse response, @PathVariable("dir") final String dir,
        @PathVariable("file") final String file, final Locale locale) throws IOException {
        Assert.notNull(dir, "The parameter 'dir' cannot be null!");
        Assert.isTrue(dir.length() > 0, "The parameter 'dir' cannot be empty!");
        Assert.notNull(file, "The parameter 'file' cannot be null!");
        Assert.isTrue(file.length() > 0, "The parameter 'file' cannot be empty!");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final ServletOutputStream outputStream = response.getOutputStream();
        final ImageLocation imageLocation = imageHandler.createImageLocation(dir, file, locale);

        final PlayerSettings playerSettings = wrapper.getPlayer().getSettings();
        final ImageLookupStrategyType imageLookupStrategyType = ImageLookupStrategyType.fromConfig(playerSettings.getImageTypeOrder());

        imageHandler.handleImage(outputStream, imageLocation, imageLookupStrategyType, false);
    }

    public void setImageHandler(final ImageHandler imageHandler) {
        this.imageHandler = imageHandler;
    }

    protected ImageHandler getImageHandler() {
        return imageHandler;
    }

}
