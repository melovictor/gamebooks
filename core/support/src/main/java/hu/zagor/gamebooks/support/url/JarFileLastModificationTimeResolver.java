package hu.zagor.gamebooks.support.url;

import java.io.IOException;
import java.net.URL;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Class that can resolve the last modification time based on a {@link Resource} whether it is pointing to a file in the filesystem or in a JAR file.
 * @author Tamas_Szekeres
 */
@Component
public class JarFileLastModificationTimeResolver implements LastModificationTimeResolver {

    @Override
    public long getLastModified(final Resource requestedResource) throws IOException {
        long date;
        final URL url = requestedResource.getURL();
        if ("jar".equals(url.getProtocol())) {
            date = url.openConnection().getLastModified();
        } else {
            date = requestedResource.getFile().lastModified();
        }
        return date;
    }

}
