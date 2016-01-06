package hu.zagor.gamebooks.controller;

import hu.zagor.gamebooks.controller.domain.ImageLocation;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for a class that is capable of resolving image requests for the webapplication.
 * @author Tamas_Szekeres
 */
public interface ImageHandler {

    /**
     * Tries to get the image specified by the parameters and write it into the provided output stream.
     * @param request the {@link HttpServletRequest} object, cannot be null
     * @param response the {@link HttpServletResponse} object, cannot be null
     * @param imageLocation bean storing data about the image's location, cannot be null
     * @param randomImage true if a random image must be supplied in case of multiple hits, false if the first one
     * @throws IOException occurs if an I/O exception happens
     */
    void handleImage(final HttpServletRequest request, final HttpServletResponse response, final ImageLocation imageLocation, boolean randomImage) throws IOException;

    /**
     * Creates a new {@link ImageLocation} bean from the input parameters.
     * @param dir the directory of the file
     * @param file the name of the file
     * @param locale the locale for which we want to have the {@link ImageLocation} bean
     * @return the created {@link ImageLocation} bean
     */
    ImageLocation createImageLocation(String dir, String file, Locale locale);

}
