package hu.zagor.gamebooks.support.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

/**
 * Class for wrapping static calls to {@link IOUtils}.
 * @author Tamas_Szekeres
 */
@Component
public class IoUtilsWrapper {

    public int copy(final InputStream resourceInputStream, final OutputStream response) throws IOException {
        return IOUtils.copy(resourceInputStream, response);
    }

}
