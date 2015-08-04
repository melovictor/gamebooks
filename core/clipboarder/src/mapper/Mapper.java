package mapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mapper {

    private static final Pattern SECTIONS = Pattern.compile("<p id=\"([a-zA-Z_0-9*]+)\"(.*?)<\\/p>");
    private static final Pattern NAVIGATION = Pattern.compile("<next.*?id=\"([^\"]*)\"");

    public static void main(final String[] args) throws IOException {
        final String root = "d:\\System\\eclipse\\books";
        final String[] series = new String[]{"cyoa", "cyoar", "ff", "solo", "tm", "sc", "cyoawd", "wm", "agirl"};
        final String content = "src\\main\\resources";

        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.DAY_OF_WEEK, -2);
        final long lastWeek = calendar.getTime().getTime();

        for (final String serie : series) {
            final File serieDir = new File(root, serie);
            for (final File bookDir : serieDir.listFiles()) {
                for (final File instDir : bookDir.listFiles()) {
                    if (!instDir.getName().endsWith("med")) {
                        final File xmlDir = new File(instDir, content);
                        for (final File contentFile : xmlDir.listFiles()) {
                            if (contentFile.getName().endsWith("content.xml") && contentFile.lastModified() > lastWeek) {
                                createMap(contentFile);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void createMap(final File xml) throws IOException {
        final String content = getContent(xml);
        final String map = getMapContent(content);
        writeMapFile(xml, map);
        generateImageFile(xml);
    }

    private static void generateImageFile(final File xml) {
        final String inputFileName = xml.getName().replace("xml", "dot");
        final String outputFileName = xml.getName().replace("xml", "png").replace("content.", ".");
        final Runtime runtime = Runtime.getRuntime();
        try {
            final String command = "\"d:/System/Visual Studio/Gamebooks/Output/graphviz/dot.exe\" -Tpng -o" + outputFileName + " " + inputFileName;
            final File workDirectory = new File("d:/System/eclipse/output/map");
            runtime.exec(command, null, workDirectory);
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    private static void writeMapFile(final File xml, final String map) throws IOException {
        final File outputFile = new File("d:\\System\\eclipse\\output\\map", xml.getName().replace("xml", "dot"));
        final FileWriter writer = new FileWriter(outputFile);
        writer.write(map);
        writer.close();
        System.out.println("Created map " + outputFile.getName());
    }

    private static String getMapContent(final String content) {
        String map = "digraph dotOut {";
        final Set<String> connections = new HashSet<>();
        final Set<String> implementedSections = new HashSet<>();
        final Set<String> referencedSections = new HashSet<>();
        final Set<String> gatherItemSections = new HashSet<>();
        final Set<String> fightSections = new HashSet<>();
        final Matcher matcher = SECTIONS.matcher(content);
        while (matcher.find()) {
            final String id = matcher.group(1);
            final String section = matcher.group(2);
            implementedSections.add(id);
            final Matcher matcher2 = NAVIGATION.matcher(section);
            while (matcher2.find()) {
                final String next = matcher2.group(1);
                referencedSections.add(next);
                connections.add("\"" + id + "\"->\"" + next + "\";");
            }
            if (section.contains("<fight")) {
                fightSections.add(id);
            }
            if (section.contains("<gatherItem") || section.contains("takeItem")) {
                gatherItemSections.add(id);
            }
        }
        referencedSections.removeAll(implementedSections);
        for (final String id : referencedSections) {
            map += "\"" + id + "\"[fillcolor=limegreen,style=filled];";
        }
        for (final String id : gatherItemSections) {
            map += "\"" + id + "\"[fillcolor=cyan,style=filled];";
        }
        for (final String connection : connections) {
            map += connection;
        }
        for (final String id : fightSections) {
            map += "\"" + id + "\"" + "[shape=diamond];";
        }
        map += "}";
        return map;
    }

    private static String getContent(final File xml) throws FileNotFoundException {
        String content = "";
        final Scanner scanner = new Scanner(xml, "utf-8");
        while (scanner.hasNextLine()) {
            content += scanner.nextLine();
        }
        scanner.close();
        return content;
    }

}
