package hu.zagor.gamebooks.mvc.login.controller.service;

import hu.zagor.gamebooks.domain.BookInformations;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.joda.time.DateTime;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Abstract class that is capable of putting together a version number based on the last modification date of a file.
 * @author Tamas_Szekeres
 */
public abstract class FileBasedVersionNumberProvider implements VersionNumberProvider, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private int numberOfBooks;
    private int numberOfSeries;
    private String datePart;

    /**
     * Initializes the current version number provider.
     */
    @PostConstruct
    public void initialize() {
        calculateBookRelatedValues();
        calculateDatePart();
    }

    private void calculateBookRelatedValues() {
        final Map<String, BookInformations> bookInfos = applicationContext.getBeansOfType(BookInformations.class);

        int active = 0;
        final Set<String> series = new HashSet<>();

        for (final BookInformations info : bookInfos.values()) {
            if (!info.isHidden() && !info.isDisabled() && !info.isUnfinished()) {
                active++;
                series.add(info.getSeries());
            }
        }

        numberOfBooks = active;
        numberOfSeries = series.size();
    }

    private void calculateDatePart() {
        final DateTime dateTime = new DateTime(getFile().lastModified());

        final int year = dateTime.getYearOfCentury();
        final String month = Integer.toHexString(dateTime.getMonthOfYear());
        final String day = Integer.toString(dateTime.getDayOfMonth(), 32);
        final String hour = Integer.toString(dateTime.getHourOfDay(), 25);

        datePart = year + month + day + hour;
    }

    @Override
    public String getVersion() {
        return "0." + numberOfSeries + "." + numberOfBooks + "." + datePart + "-beta";
    }

    /**
     * Returns the {@link File} object based on which we need to assemble a version number.
     * @return the {@link File} object
     */
    protected abstract File getFile();

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
