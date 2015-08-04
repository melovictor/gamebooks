package hu.zagor.gamebooks.controller.image;

import java.io.IOException;

import org.springframework.core.io.Resource;

/**
 * Interface for image lookup strategies.
 * @author Tamas_Szekeres
 *
 */
public interface ImageLookupStrategy {
    /**
     * Does the actual lookup using a given strategy.
     * @param dir the name of the directory to look for, cannot be null
     * @param file the name of the file, cannot be null
     * @return the found resources, can be empty array
     * @throws IOException if an io error occurs
     */
    Resource[] getImageResourcesFromDir(final String dir, final String file) throws IOException;
}
