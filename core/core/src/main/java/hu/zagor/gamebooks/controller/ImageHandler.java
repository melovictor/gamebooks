package hu.zagor.gamebooks.controller;

import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.controller.image.ImageLookupStrategyType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Interface for a class that is capable of resolving image requests for the webapplication.
 * @author Tamas_Szekeres
 */
public interface ImageHandler {

    /**
     * Tries to get the image specified by the parameters and write it into the provided output stream.
     * @param response the output stream into which the image must be written, cannot be null
     * @param imageLocation bean storing data about the image's location, cannot be null
     * @param strategyType the type of the lookup strategy to use. It can be null, in which case the default
     * lookup strategy – first looking for colored images, then black and white ones – will be used
     * @param randomImage true if a random image must be supplied in case of multiple hits, false if the first
     * one
     * @throws IOException occurs if an I/O exception happens
     */
    void handleImage(final OutputStream response, final ImageLocation imageLocation, final ImageLookupStrategyType strategyType, boolean randomImage)
        throws IOException;

    /**
     * Creates a new {@link ImageLocation} bean from the input parameters.
     * @param dir the directory of the file
     * @param file the name of the file
     * @param locale the locale for which we want to have the {@link ImageLocation} bean
     * @return the created {@link ImageLocation} bean
     */
    ImageLocation createImageLocation(String dir, String file, Locale locale);
}
