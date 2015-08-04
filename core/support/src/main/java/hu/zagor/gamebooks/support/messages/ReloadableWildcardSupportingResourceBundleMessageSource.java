package hu.zagor.gamebooks.support.messages;

import hu.zagor.gamebooks.support.environment.EnvironmentDetector;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Message source that is based on the {@link ReloadableResourceBundleMessageSource} and supports wildcards in it's basenames.
 * @author Tamas_Szekeres
 */
public class ReloadableWildcardSupportingResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

    private static final String FILE_PREFIX = "file:";
    private static final String CLASSPATH = "classpath:/";
    private static final String CLASSPATH_MESSAGES_PROPERTIES = "classpath*:messages/**/*.properties";
    private static final Pattern JAR_FILE_EXTRACT_PATTERN = Pattern.compile("\\.jar!/([^_]*)");
    private static final Pattern FILE_EXTRACT_PATTERN = Pattern.compile("classes/([^_]*)");

    private PathMatchingResourcePatternResolver patternResolver;
    @Autowired
    private EnvironmentDetector detector;

    /**
     * Constructor that creates a new object with the necessary resolver object inserted and then initializes the basenames by itself.
     * @param patternResolver the resolver object to use for resolving the basenames
     */
    public ReloadableWildcardSupportingResourceBundleMessageSource(final PathMatchingResourcePatternResolver patternResolver) {
        this.patternResolver = patternResolver;
        setBasenames(resolveBasename());
    }

    @PostConstruct
    public void init() {
        setCacheSeconds(detector.isDevelopment() ? 0 : -1);
    }

    private String[] resolveBasename() {
        final Set<String> resolvedBundles = new HashSet<String>();

        Resource[] resources = null;
        try {
            resources = patternResolver.getResources(CLASSPATH_MESSAGES_PROPERTIES);
            for (final Resource resource : resources) {
                final String resolvedName = resolveMessageResourceName(resource.getURL().toString());
                if (resolvedName != null) {
                    logger.trace("Resolved message bundle '" + resource.getFilename() + "'.");
                    resolvedBundles.add(resolvedName);
                } else {
                    logger.trace("Failed to resolve message bundle '" + resource.getFilename() + "'.");
                }
            }
        } catch (final IOException exception) {
        }

        return resolvedBundles.toArray(new String[]{});
    }

    private String resolveMessageResourceName(final String filename) {
        String resolvedName = null;

        if (filename.startsWith(FILE_PREFIX)) {
            resolvedName = getBundleName(FILE_EXTRACT_PATTERN, filename);
        } else {
            resolvedName = getBundleName(JAR_FILE_EXTRACT_PATTERN, filename);
        }
        if (resolvedName != null) {
            resolvedName = CLASSPATH + resolvedName;
        }
        return resolvedName;
    }

    private String getBundleName(final Pattern pattern, final String filename) {
        String bundleName = null;
        final Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            bundleName = matcher.group(1);
        }
        return bundleName;
    }

    public void setPatternResolver(final PathMatchingResourcePatternResolver patternResolver) {
        this.patternResolver = patternResolver;
    }

}
