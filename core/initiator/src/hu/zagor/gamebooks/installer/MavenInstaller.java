package hu.zagor.gamebooks.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class MavenInstaller {
    private static final String rootDirectory = "c:/springsource/eclipsegit";
    private static Set<String> installed = new HashSet<>();

    public static void main(final String[] args) throws IOException, InterruptedException {
        System.setProperty("JAVA_HOME", "c:\\Program Files\\Java\\jdk1.8.0_51");

        install("/core/support");
        install("/core/core");

        install("/rulesets/rs-raw");
        install("/rulesets/rs-complex");
        installRecursive("/rulesets", 1);
        installRecursive("/books", 2);
        install("/collector/series-collector");

        install("/core/gamebooks");
    }

    private static void installRecursive(final String dir, final int level) throws IOException, InterruptedException {
        if (level == 0) {
            install(dir);
        } else {
            final File curDir = new File(rootDirectory, dir);
            for (final File next : curDir.listFiles()) {
                if (next.isDirectory()) {
                    installRecursive(dir + "/" + next.getName(), level - 1);
                }
            }
        }
    }

    private static void install(final String dir) throws IOException {
        if (installed.contains(dir)) {
            return;
        }
        installed.add(dir);

        final ProcessBuilder processBuilder = new ProcessBuilder("mvn.bat", "install");
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(new File(rootDirectory + dir));
        final Process process = processBuilder.start();

        final BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = inStreamReader.readLine()) != null) {
            System.out.println(line);
        }

    }

}
