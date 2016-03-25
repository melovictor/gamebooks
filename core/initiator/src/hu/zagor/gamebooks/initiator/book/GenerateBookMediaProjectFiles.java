package hu.zagor.gamebooks.initiator.book;

import hu.zagor.gamebooks.initiator.AbstractGenerator;
import java.io.File;

public class GenerateBookMediaProjectFiles extends AbstractGenerator {

    public void generate(final BookBaseData baseData, final BookLangData data) {
        final File medRootPath = new File("c:/springsource/eclipsegit/books/" + baseData.getCollectorCode() + "/media-" + baseData.getCollectorName());
        generateMediaProject(medRootPath, baseData, data);
    }

    private void generateMediaProject(final File medRootPath, final BookBaseData baseData, final BookLangData data) {
        createDir(medRootPath, "src/main/resources/" + baseData.getSeriesCode() + baseData.getPosition());
        createDir(medRootPath, "src/main/resources/" + baseData.getSeriesCode() + baseData.getPosition() + data.getLang());
    }

}
