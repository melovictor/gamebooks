package hu.zagor.gamebooks.initiator.book;

import hu.zagor.gamebooks.initiator.AbstractGenerator;
import java.io.File;
import java.util.List;

public class GenerateBookProjectFiles extends AbstractGenerator {

    private final GenerateBookLanguageProjectFiles langGenerator = new GenerateBookLanguageProjectFiles();
    private final GenerateBookMediaProjectFiles medGenerator = new GenerateBookMediaProjectFiles();

    public void generateBookProjectFiles(final BookBaseData baseData, final List<BookLangData> books) {
        System.out.println("Verifying abbreviation availability for book(s).");

        final File rootPath = new File("c:/springsource/eclipsegit/books/" + baseData.getCollectorCode() + "/presentation-" + baseData.getCollectorName());
        final File codeDir = new File(rootPath,
            "src/main/java/hu/zagor/gamebooks/" + baseData.getRuleset() + "/" + baseData.getSeriesCode() + "/" + baseData.getTitleCode() + "/mvc/books/");
        if (codeDir.exists() && !baseData.isForceOverride()) {
            System.out.println("Verification failed. No code will be generated.");
        } else {
            for (final BookLangData data : books) {
                if (data.isGeneratable()) {
                    System.out.println("Start generating book " + data.getTitle());
                    data.init(baseData);
                    generateFiles(baseData, data);
                }
            }
            if (baseData.isCharPageRequired()) {
                langGenerator.generate(baseData);
            }
        }
    }

    private void generateFiles(final BookBaseData baseData, final BookLangData data) {
        langGenerator.generate(baseData, data);
        if (baseData.hasMediaProject()) {
            medGenerator.generate(baseData, data);
        }
    }

}
