package hu.zagor.gamebooks.support.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

public class Writer {

    private OutputStreamWriter writer;
    private final FileOutputStream outputStream;
    private ObjectOutputStream objectOutputStream;

    public Writer(final File location, final String encoding) throws IOException {
        outputStream = new FileOutputStream(location);
        writer = new OutputStreamWriter(outputStream, encoding);
    }

    public Writer(final File location) throws IOException {
        outputStream = new FileOutputStream(location);
        objectOutputStream = new ObjectOutputStream(outputStream);
    }

    public void write(final String string) throws IOException {
        writer.write(string);
    }

    public void write(final Object object) throws IOException {
        objectOutputStream.writeObject(object);
    }

    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (final IOException e) {
            }
        }
        if (objectOutputStream != null) {
            try {
                objectOutputStream.close();
            } catch (final IOException e) {
            }
        }
        try {
            outputStream.close();
        } catch (final IOException e) {
        }
    }
}
