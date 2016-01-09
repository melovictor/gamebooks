package hu.zagor.gamebooks.controller.image;

import hu.zagor.gamebooks.books.random.RandomNumberGenerator;
import hu.zagor.gamebooks.controller.ImageHandler;
import hu.zagor.gamebooks.controller.domain.ImageLocation;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.player.PlayerSettings;
import hu.zagor.gamebooks.support.logging.LogInject;
import hu.zagor.gamebooks.support.stream.IoUtilsWrapper;
import hu.zagor.gamebooks.support.url.LastModificationTimeResolver;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * <p> Implementation of the {@link ImageHandler} interface that tries to get images from the classpath. </p> <p> This implementation first tries to resolve the requested image
 * from the media repository of the gamebook. If it fails it tries to do this from the language repository (which has a two-lettered postfix with the locale). </p> <p> In the
 * repositories it first tries to resolve it as a colored image (with the bc prefix). If it fails it tries to resolve it as a black and white image (with the bb prefix). If it also
 * fails it tries to resolve as a simple image (without any prefixes). If all three methods fail, the bean won't put any data into the {@link OutputStream}.
 * @author Tamas_Szekeres
 */
public class ClasspathImageHandler implements ImageHandler, BeanFactoryAware {

    private static final int NOT_MODIFIED = 304;
    @LogInject private Logger logger;
    private Map<ImageLookupStrategyType, ImageLookupStrategy> lookupStrategies;
    @Autowired @Qualifier("d6") private RandomNumberGenerator generator;
    @Autowired private IoUtilsWrapper ioUtilsWrapper;
    private BeanFactory beanFactory;
    @Autowired private LastModificationTimeResolver lastModificationTimeResolver;

    @Override
    public void handleImage(final HttpServletRequest request, final HttpServletResponse response, final ImageLocation imageLocation, final boolean randomImage)
        throws IOException {
        Assert.notNull(request, "The parameter 'request' cannot be null!");
        Assert.notNull(response, "The parameter 'response' cannot be null!");
        Assert.notNull(imageLocation, "The parameter 'imageLocation' cannot be null!");

        final HttpSessionWrapper wrapper = getWrapper(request);
        final PlayerSettings playerSettings = wrapper.getPlayer().getSettings();
        final ImageLookupStrategyType strategyType = ImageLookupStrategyType.fromConfig(playerSettings.getImageTypeOrder());

        final Resource requestedResource = getImage(imageLocation, strategyType, randomImage);

        if (requestedResource != null) {
            final long lastStoredState = request.getDateHeader("If-Modified-Since") * 1000;

            final long imageLastModified = lastModificationTimeResolver.getLastModified(requestedResource);
            if (lastStoredState >= imageLastModified && !randomImage) {
                response.setStatus(NOT_MODIFIED);
            } else {
                if (!randomImage) {
                    response.addDateHeader("Last-Modified", imageLastModified);
                }
                response.addHeader("Content-Type", "image/" + FilenameUtils.getExtension(requestedResource.getFilename()));
                response.setContentLength((int) requestedResource.contentLength());

                final ServletOutputStream outputStream = response.getOutputStream();
                try (InputStream resourceInputStream = requestedResource.getInputStream()) {
                    ioUtilsWrapper.copy(resourceInputStream, outputStream);
                }
            }

        } else {
            logger.warn("Couldn't find requested image.");
        }
    }

    private Resource getImage(final ImageLocation imageLocation, final ImageLookupStrategyType strategyType, final boolean randomImage) throws IOException {
        Resource resource = null;
        final Locale locale = imageLocation.getLocale();
        logger.debug("Looking for image '{}' for book '{}' for locale '{}_{}'.", imageLocation.getFile(), imageLocation.getDir(), locale.getLanguage(),
            locale.getCountry());
        final Resource[] resources = getImageResources(imageLocation, strategyType);
        if (resources.length > 0) {
            final int index = randomImage ? generator.getRandomNumber(1, resources.length, -1)[0] : 0;
            resource = resources[index];
        }
        return resource;
    }

    private Resource[] getImageResources(final ImageLocation imageLocation, final ImageLookupStrategyType strategyType) throws IOException {
        final ImageLookupStrategy lookupStrategy = lookupStrategies.get(strategyType);
        Resource[] resources = new Resource[]{};
        final String file = imageLocation.getFile();
        if (isNotCover(file)) {
            final String message = "Looking in media module.";
            final String dir = imageLocation.getDir();
            resources = tryGetResource(lookupStrategy, file, message, dir);
        }
        if (resources.length == 0) {
            final String message = "Looking in language_country module.";
            final String dirLocale = imageLocation.getFullDirLocale();
            resources = tryGetResource(lookupStrategy, file, message, dirLocale);
        }
        if (resources.length == 0) {
            final String message = "Looking in language module.";
            final String dirLocale = imageLocation.getDirLocale();
            resources = tryGetResource(lookupStrategy, file, message, dirLocale);
        }
        return resources;
    }

    private Resource[] tryGetResource(final ImageLookupStrategy lookupStrategy, final String file, final String message, final String dir) throws IOException {
        logger.debug(message);
        return lookupStrategy.getImageResourcesFromDir(dir, file);
    }

    private boolean isNotCover(final String file) {
        return !"cover".equals(file);
    }

    @Override
    public ImageLocation createImageLocation(final String dir, final String file, final Locale locale) {
        return new ImageLocation(dir, file, locale);
    }

    public void setLookupStrategies(final Map<ImageLookupStrategyType, ImageLookupStrategy> lookupStrategies) {
        this.lookupStrategies = lookupStrategies;
    }

    private HttpSessionWrapper getWrapper(final HttpServletRequest request) {
        return (HttpSessionWrapper) beanFactory.getBean("httpSessionWrapper", request);
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
