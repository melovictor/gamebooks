package hu.zagor.gamebooks.initiator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {

    private static Console SINGLETON;
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static {
        SINGLETON = new Console();
    }

    public static Console getConsole() {
        return SINGLETON;
    }

    public void print(final String string) {
        System.out.print(string);
    }

    public void println(final String string) {
        System.out.println(string);
    }

    public String readLine() {
        try {
            return br.readLine();
        } catch (final IOException e) {
            return null;
        }
    }

}
