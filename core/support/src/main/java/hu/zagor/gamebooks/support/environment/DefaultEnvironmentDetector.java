package hu.zagor.gamebooks.support.environment;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

/**
 * Default implementation of the {@link EnvironmentDetector} interface.
 * @author Tamas_Szekeres
 */
@Component
public class DefaultEnvironmentDetector implements EnvironmentDetector, ServletContextAware {

    private boolean seleniumTesting;
    private boolean recordState;
    private ServletContext servletContext;

    @Override
    public boolean isDevelopment() {
        final String realPath = servletContext.getRealPath("");
        return (realPath.startsWith("C:\\") || realPath.startsWith("D:\\")) && !seleniumTesting;
    }

    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public boolean isRecordState() {
        return recordState;
    }

    @Override
    public void setRecordState(final boolean recordState) {
        this.recordState = recordState;
    }

    @Override
    public boolean isSeleniumTesting() {
        return seleniumTesting;
    }

    @Override
    public void setSeleniumTesting(final boolean seleniumTesting) {
        this.seleniumTesting = seleniumTesting;
    }

}
