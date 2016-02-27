package hu.zagor.gamebooks.support.imagetype;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ImageTypeDetector {

    String probeContentType(InputStream resourceInputStream, OutputStream outputStream) throws IOException;

}
