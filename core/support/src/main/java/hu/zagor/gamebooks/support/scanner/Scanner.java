package hu.zagor.gamebooks.support.scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Wrapper class for the {@link java.util.Scanner}.
 * @author Tamas_Szekeres
 */
public class Scanner {

    private java.util.Scanner scanner;

    private FileInputStream inputStream;
    private ObjectInputStream objectInputStream;

    public Scanner(final File paramFile, final String paramString) throws FileNotFoundException {
        scanner = new java.util.Scanner(paramFile, paramString);
    }

    public Scanner(final File location) throws IOException {
        inputStream = new FileInputStream(location);
        objectInputStream = new ObjectInputStream(inputStream);
    }

    public String next() {
        return scanner.next();
    }

    public String nextLine() {
        return scanner.nextLine();
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public Object nextObject() throws IOException, ClassNotFoundException {
        return objectInputStream.readObject();
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
        }
        if (objectInputStream != null) {
            try {
                objectInputStream.close();
            } catch (final IOException e) {
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (final IOException e) {
            }
        }
    }

    public void forceFullFileLoading() {
        scanner.useDelimiter("\\Z");
    }

}
