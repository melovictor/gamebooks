package hu.zagor.gamebooks.support.logging;

import java.util.Map;

import org.joda.time.DateTime;

import ch.qos.logback.classic.sift.MDCBasedDiscriminator;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class MDCBasedDiscriminatorWithTimeBasedDefault extends MDCBasedDiscriminator {

    @Override
    public String getDiscriminatingValue(final ILoggingEvent event) {
        String value;

        final Map<String, String> mdcMap = event.getMDCPropertyMap();
        if (mdcMap == null) {
            value = getTime();
        } else {
            final String mdcValue = mdcMap.get(getKey());
            if (mdcValue == null) {
                value = getTime();
            } else {
                value = mdcValue;
            }
        }
        return value;
    }

    private String getTime() {
        return getDefaultValue() + String.valueOf(DateTime.now().withTimeAtStartOfDay().getMillis());
    }
}
