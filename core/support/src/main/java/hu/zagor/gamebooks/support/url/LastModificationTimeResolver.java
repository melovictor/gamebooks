package hu.zagor.gamebooks.support.url;

import java.io.IOException;
import org.springframework.core.io.Resource;

public interface LastModificationTimeResolver {

    long getLastModified(Resource requestedResource) throws IOException;

}
