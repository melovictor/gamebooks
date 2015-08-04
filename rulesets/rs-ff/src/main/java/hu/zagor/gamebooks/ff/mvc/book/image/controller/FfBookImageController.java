package hu.zagor.gamebooks.ff.mvc.book.image.controller;

import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.controller.image.ImageLookupStrategyType;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.mvc.book.image.controller.GenericBookImageController;
import hu.zagor.gamebooks.player.PlayerSettings;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Image controller for the Fighting Fantasy ruleset supporting random small images.
 * @author Tamas_Szekeres
 */
public class FfBookImageController extends GenericBookImageController {

    /**
     * Handles requests for random small images.
     * @param request the {@link HttpServletRequest}
     * @param response the {@link HttpServletResponse}
     * @param dir the book directory in which the small images must be located
     * @param locale the locale currently in use
     * @throws IOException thrown when an error occurs during the lookup of the image
     */
    @RequestMapping(value = "resources/{dir}/image.small")
    public void handleSmallImage(final HttpServletRequest request, final HttpServletResponse response, @PathVariable("dir") final String dir, final Locale locale)
        throws IOException {

        Assert.notNull(dir, "The parameter 'dir' cannot be null!");
        Assert.isTrue(dir.length() > 0, "The parameter 'dir' cannot be empty!");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final ServletOutputStream outputStream = response.getOutputStream();
        final ImageLocation imageLocation = getImageHandler().createImageLocation(dir, "small*", locale);

        final PlayerSettings playerSettings = wrapper.getPlayer().getSettings();
        final ImageLookupStrategyType imageLookupStrategyType = ImageLookupStrategyType.fromConfig(playerSettings.getImageTypeOrder());

        getImageHandler().handleImage(outputStream, imageLocation, imageLookupStrategyType, true);

    }
}
