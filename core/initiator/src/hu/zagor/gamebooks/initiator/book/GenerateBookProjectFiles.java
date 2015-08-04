package hu.zagor.gamebooks.initiator.book;

import hu.zagor.gamebooks.initiator.AbstractGenerator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class GenerateBookProjectFiles extends AbstractGenerator {

    private final GenerateBookLanguageProjectFiles langGenerator = new GenerateBookLanguageProjectFiles();
    private final GenerateBookMediaProjectFiles medGenerator = new GenerateBookMediaProjectFiles();

    public void generateBookProjectFiles(final BookBaseData baseData, final List<BookLangData> books) {
        for (final BookLangData data : books) {
            if (data.isGeneratable()) {
                data.init(baseData);
                generateFiles(baseData, data);
            }
        }
    }

    private void generateFiles(final BookBaseData baseData, final BookLangData data) {
        langGenerator.generate(baseData, data);
        if (baseData.hasMediaProject()) {
            medGenerator.generate(baseData);
        }
        try {
            updateSeriesCollector(baseData, data);
        } catch (final IOException e) {
            System.out.println("Failed to update the series pom.xml");
            e.printStackTrace();
        }
    }

    private void updateSeriesCollector(final BookBaseData baseData, final BookLangData data) throws IOException {
        final File pom = new File("d:\\System\\eclipse\\books\\collector\\" + baseData.getCollectorCode() + "\\pom.xml");
        final Scanner pomScanner = new Scanner(pom);
        pomScanner.useDelimiter("/z");
        String fileContent = pomScanner.next();
        pomScanner.close();
        final String artifactId = data.getSeriesCode() + data.getPosition();
        if (!fileContent.contains(">" + artifactId + "<")) {
            fileContent = fileContent.replace("</dependencies>", "  <dependency>\r\n" + "      <groupId>hu.zagor.gamebooks.books." + baseData.getSeriesCode() + "."
                + baseData.getTitleCode() + "</groupId>\r\n" + "      <artifactId>" + artifactId + "</artifactId>\r\n" + "      <version>1.0.0</version>\r\n"
                + "    </dependency>\r\n" + "  </dependencies>");
            createFile(pom, "", fileContent);
        }
    }
}
