package hu.zagor.gamebooks.mvc.logs.service.filter;

import java.io.File;
import java.io.FilenameFilter;

import org.springframework.stereotype.Component;

/**
 * Filter for grabbing only the log files from the directory, with the exception of the base log.
 * @author Tamas_Szekeres
 */
@Component
public class LogFilenameFilter implements FilenameFilter {

    @Override
    public boolean accept(final File dir, final String name) {
        return name.endsWith(".log") && !"log-base.log".equals(name);
    }

}
