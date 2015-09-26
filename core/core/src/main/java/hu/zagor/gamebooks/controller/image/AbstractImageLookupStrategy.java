package hu.zagor.gamebooks.controller.image;

import hu.zagor.gamebooks.support.logging.LogInject;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

/**
 * Abstract bean that contains the functionality of the image lookups, but not the strategy itself.
 * @author Tamas_Szekeres
 */
public abstract class AbstractImageLookupStrategy implements ApplicationContextAware, ImageLookupStrategy {

    @LogInject
    private Logger logger;
    private ApplicationContext applicationContext;

    /**
     * Does the actual lookup from the application context.
     * @param dir the directory of the image, cannot be null
     * @param file the (partial) name of the image file, cannot be null
     * @param prefix prefix for the file name, cannot be null
     * @return the extracted resources, can be empty
     * @throws IOException occurs if an I/O exception happens
     */
    protected Resource[] doGetImage(final String dir, final String file, final String prefix) throws IOException {
        final String resourcePath = "classpath*:/" + dir + "/" + prefix + file + ".jpg";
        logger.trace("Looking for image as {}", resourcePath);
        return applicationContext.getResources(resourcePath);
    }

    /**
     * Check whether the file requested is a cover image or not.
     * @param file the name of the file
     * @return true if it is not a cover image, false if it is
     */
    protected boolean isNotCover(final String file) {
        return !"cover".equals(file);
    }

    /**
     * Tries to grab a color image.
     * @param dir the directory path
     * @param file the name of the image
     * @return the corresponding Resource objects
     * @throws IOException occurs if an I/O exception happens
     */
    protected Resource[] getColorImage(final String dir, final String file) throws IOException {
        logger.trace("Looking for coloured image...");
        return doGetImage(dir, file, "c-");
    }

    /**
     * Tries to grab a black and white image.
     * @param dir the directory path
     * @param file the name of the image
     * @return the corresponding Resource objects
     * @throws IOException occurs if an I/O exception happens
     */
    protected Resource[] getBwImage(final String dir, final String file) throws IOException {
        logger.trace("Looking for black and white image...");
        return doGetImage(dir, file, "b-");
    }

    /**
     * Tries to grab a common (eg. a cover) image.
     * @param dir the directory path
     * @param file the name of the image
     * @return the corresponding Resource objects
     * @throws IOException occurs if an I/O exception happens
     */
    protected Resource[] getCommonImage(final String dir, final String file) throws IOException {
        logger.trace("Looking for common image...");
        return doGetImage(dir, file, "");
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
