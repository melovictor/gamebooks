package hu.zagor.gamebooks.support.imagetype;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class ContentBasedImageTypeDetector implements ImageTypeDetector {

    @Override
    public String probeContentType(final InputStream resourceInputStream, final OutputStream outputStream) throws IOException {
        String type = null;
        if (resourceInputStream != null && outputStream != null) {
            final String content = readContent(resourceInputStream, outputStream);
            if (content.contains("JFIF") || content.contains("Exif")) {
                type = "image/jpeg";
            } else if (content.contains("GIF89a")) {
                type = "image/gif";
            } else if (content.contains("PNG")) {
                type = "image/png";
            }
        }
        return type;
    }

    private String readContent(final InputStream resourceInputStream, final OutputStream outputStream) throws IOException {
        final byte[] buffer = new byte[10];
        IOUtils.read(resourceInputStream, buffer);
        outputStream.write(buffer);
        return new String(buffer);
    }

}
