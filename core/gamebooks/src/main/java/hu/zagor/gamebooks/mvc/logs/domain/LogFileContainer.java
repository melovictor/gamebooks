package hu.zagor.gamebooks.mvc.logs.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Container for the {@link LogFileData} objects that sort them properly.
 * @author Tamas_Szekeres
 */
public class LogFileContainer {

    private static final int WINDOW = -7;

    private final Map<String, LogFileContainerB> dateBasedMap = new HashMap<>();
    private final Set<LogFileContainerB> byDate = new TreeSet<>(Collections.reverseOrder());
    private final long limit;
    private final Set<LogFileData> base = new TreeSet<>();

    /**
     * Default constructor.
     */
    public LogFileContainer() {
        final Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.DATE, WINDOW);
        calendar.set(GregorianCalendar.HOUR, 0);
        calendar.set(GregorianCalendar.MINUTE, 0);
        calendar.set(GregorianCalendar.SECOND, 0);
        limit = calendar.getTimeInMillis();
    }

    /**
     * Adds a new file to the container.
     * @param data the new file
     */
    public void add(final LogFileData data) {
        final long loginStamp = Long.parseLong(data.getTimestamp());
        if (limit < loginStamp) {
            final String date = new SimpleDateFormat("YYYY.MM.dd.").format(new Date(loginStamp));
            LogFileContainerB containerB = dateBasedMap.get(date);
            if (containerB == null) {
                containerB = new LogFileContainerB(date);
                byDate.add(containerB);
                dateBasedMap.put(date, containerB);
            }
            containerB.add(data);
        }
    }

    /**
     * Adds a new base log file to the container.
     * @param logFileData the new file
     */
    public void addBase(final LogFileData logFileData) {
        base.add(logFileData);
    }

    public Set<LogFileData> getBase() {
        return base;
    }

    public Set<LogFileContainerB> getByDate() {
        return byDate;
    }

    /**
     * A second level storage class.
     * @author Tamas_Szekeres
     */
    public class LogFileContainerB implements Comparable<LogFileContainerB> {

        private final Map<String, LogFileContainerC> userBasedMap = new HashMap<>();
        private final Set<LogFileContainerC> byUser = new TreeSet<>();
        private final String date;

        /**
         * Basic constructor.
         * @param date the sort value
         */
        public LogFileContainerB(final String date) {
            this.date = date;
        }

        /**
         * Add new element.
         * @param data element to add
         */
        public void add(final LogFileData data) {
            final String userId = data.getUserName();
            LogFileContainerC containerC = userBasedMap.get(userId);
            if (containerC == null) {
                containerC = new LogFileContainerC(userId);
                userBasedMap.put(userId, containerC);
                byUser.add(containerC);
            }
            containerC.add(data);
        }

        @Override
        public int compareTo(final LogFileContainerB o) {
            return date.compareTo(o.date);
        }

        public String getDate() {
            return date;
        }

        public Set<LogFileContainerC> getByUser() {
            return byUser;
        }

    }

    /**
     * Basically just a set.
     * @author Tamas_Szekeres
     */
    public class LogFileContainerC extends TreeSet<LogFileData> implements Comparable<LogFileContainerC> {

        private final String userId;

        /**
         * Basic constructor.
         * @param userId the sort value
         */
        public LogFileContainerC(final String userId) {
            this.userId = userId;
        }

        @Override
        public int compareTo(final LogFileContainerC o) {
            return userId.compareTo(o.userId);
        }

        public String getUserId() {
            return userId;
        }

    }

}
