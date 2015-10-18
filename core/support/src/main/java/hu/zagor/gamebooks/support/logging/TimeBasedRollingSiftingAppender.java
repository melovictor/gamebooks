package hu.zagor.gamebooks.support.logging;

import ch.qos.logback.classic.sift.SiftingAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.joran.spi.DefaultClass;
import ch.qos.logback.core.sift.Discriminator;

public class TimeBasedRollingSiftingAppender extends SiftingAppender {

    @Override
    @DefaultClass(MDCBasedDiscriminatorWithTimeBasedDefault.class)
    public void setDiscriminator(final Discriminator<ILoggingEvent> discriminator) {
        super.setDiscriminator(discriminator);
    }

}
