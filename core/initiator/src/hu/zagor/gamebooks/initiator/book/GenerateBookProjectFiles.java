package hu.zagor.gamebooks.initiator.book;

import hu.zagor.gamebooks.initiator.AbstractGenerator;

import java.io.File;
import java.util.List;

public class GenerateBookProjectFiles extends AbstractGenerator {

    private final GenerateBookLanguageProjectFiles langGenerator = new GenerateBookLanguageProjectFiles();
    private final GenerateBookMediaProjectFiles medGenerator = new GenerateBookMediaProjectFiles();

    public void generateBookProjectFiles(final BookBaseData baseData, final List<BookLangData> books) {
        boolean verificationFailed = false;
        System.out.println("Verifying abbreviation availability for book(s).");

        final File rootPath = new File("d:/System/eclipsegit/books/" + baseData.getCollectorCode() + "/presentation-" + baseData.getCollectorName());
        final File codeDir = new File(rootPath, "src/main/java/hu/zagor/gamebooks/" + baseData.getRuleset() + "/" + baseData.getSeriesCode() + "/"
            + baseData.getTitleCode() + "/mvc/books/");
        if (codeDir.exists()) {
            verificationFailed = true;
            System.out.println("Verification failed. No code will be generated.");
        }

        if (!verificationFailed) {
            for (final BookLangData data : books) {
                System.out.println("Start generating book " + data.getTitle());
                if (data.isGeneratable()) {
                    data.init(baseData);
                    generateFiles(baseData, data);
                }
            }
        }
    }

    private void generateFiles(final BookBaseData baseData, final BookLangData data) {
        langGenerator.generate(baseData, data);
        if (baseData.hasMediaProject()) {
            medGenerator.generate(baseData);
        }
    }

}
