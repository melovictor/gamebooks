package hu.zagor.gamebooks.controller.image;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Lookup strategy that first looks for black and white images, and if it cannot find any, continues the
 * search for coloured images.
 * @author Tamas_Szekeres
 */
public class ColorFirstImageLookupStrategy extends AbstractImageLookupStrategy {

    @Override
    public Resource[] getImageResourcesFromDir(final String dir, final String file) throws IOException {
        Assert.notNull(dir, "The parameter 'dir' cannot be null!");
        Assert.notNull(file, "The parameter 'file' cannot be null!");

        Resource[] resources = new Resource[]{};
        if (isNotCover(file)) {
            resources = getColorImage(dir, file);
            if (resources.length == 0) {
                resources = getBwImage(dir, file);
            }
        }
        if (resources.length == 0) {
            resources = getCommonImage(dir, file);
        }
        return resources;
    }

}
