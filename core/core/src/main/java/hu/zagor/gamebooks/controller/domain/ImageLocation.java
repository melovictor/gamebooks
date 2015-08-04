package hu.zagor.gamebooks.controller.domain;

import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Bean for storing data for looking up images.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class ImageLocation {

    private final String dir;
    private final String file;
    private final Locale locale;

    /**
     * Basic constructor that creates a new {@link ImageLocation} bean with all the necessary data included.
     * @param dir the directory in which the image is located, cannot be null
     * @param file the base name of the file, cannot be null
     * @param locale the locale in which we're searching for the image, cannot be null
     */
    public ImageLocation(final String dir, final String file, final Locale locale) {
        Assert.notNull(dir, "The parameter 'dir' cannot be null!");
        Assert.notNull(file, "The parameter 'file' cannot be null!");
        Assert.notNull(locale, "The parameter 'locale' cannot be null!");
        this.dir = dir;
        this.file = file;
        this.locale = locale;
    }

    public String getDir() {
        return dir;
    }

    public String getDirLocale() {
        return dir + locale.getLanguage();
    }

    public String getFile() {
        return file;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getFullDirLocale() {
        return dir + locale.getLanguage() + "_" + locale.getCountry();
    }

}
