package hu.zagor.gamebooks.initiator.book;

import hu.zagor.gamebooks.initiator.AbstractGenerator;

import java.util.List;

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
    }

}
