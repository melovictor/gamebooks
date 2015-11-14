package hu.zagor.gamebooks.initiator.book;

import hu.zagor.gamebooks.initiator.AbstractGenerator;
import hu.zagor.gamebooks.initiator.Console;

import java.io.File;
import java.io.IOException;

public class GenerateBookMediaProjectFiles extends AbstractGenerator {

    public void generate(final BookBaseData baseData) {
        final File medRootPath = new File("c:/springsource/eclipsegit/books/" + baseData.getCollectorCode() + "/presentation-" + baseData.getCollectorName());
        if (!medRootPath.exists()) {
            generateMediaProject(medRootPath, baseData);
        }
    }

    private void generateMediaProject(final File medRootPath, final BookBaseData baseData) {
        final Console console = Console.getConsole();

        try {

            createDir(medRootPath, "src/main/resources/" + baseData.getSeriesCode() + baseData.getPosition());

            if (baseData.hasInventory()) {
                createDir(medRootPath, "src/main/resources/WEB-INF/tiles/ruleset/" + baseData.getSeriesCode() + "/" + baseData.getSeriesCode() + baseData.getPosition());
                createFile(medRootPath, "src/main/resources/WEB-INF/tiles/ruleset/" + baseData.getSeriesCode() + "/" + baseData.getSeriesCode() + baseData.getPosition(),
                    "charpage.jsp", getCharpageContent());
            }
        } catch (final IOException exception) {
            console.print("Failed to create all necessary files.");
            exception.printStackTrace(System.out);
        }
    }

    private String getCharpageContent() {
        return "<%@page pageEncoding=\"utf-8\" contentType=\"text/html; charset=utf-8\"%>\r\n"
            + "<%@taglib uri=\"http://tiles.apache.org/tags-tiles\" prefix=\"tiles\"%>\r\n" + "\r\n"
            + "<tiles:insertTemplate template=\"../charpage/charpage-basic.jsp\" />\r\n";
    }

}
