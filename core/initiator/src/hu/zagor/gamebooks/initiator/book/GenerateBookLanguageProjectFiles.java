package hu.zagor.gamebooks.initiator.book;

import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getContentFile;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getEnemiesFile;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getExceptionController;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getExceptionControllerTest;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getImageController;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getImageControllerTest;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getInventoryController;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getInventoryControllerTest;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getItemsFile;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getLoadController;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getLoadControllerTest;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getNewGameController;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getNewGameControllerTest;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getSaveController;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getSaveControllerTest;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getSectionController;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getSectionControllerTest;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getSpringFile;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getTakeItemController;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getTakeItemControllerTest;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getWelcomeController;
import static hu.zagor.gamebooks.initiator.book.lang.LanguageProjectDataContents.getWelcomeControllerTest;
import hu.zagor.gamebooks.initiator.AbstractGenerator;
import hu.zagor.gamebooks.initiator.Console;

import java.io.File;
import java.io.IOException;

public class GenerateBookLanguageProjectFiles extends AbstractGenerator {

    public void generate(final BookBaseData baseData, final BookLangData data) {
        final File langRootPath = new File("d:/System/eclipsegit/books/" + baseData.getSeriesCode() + "/language-" + baseData.getSeriesCodeCapital());
        generateLanguageProject(langRootPath, baseData, data);

        final File presRootPath = new File("d:/System/eclipsegit/books/" + baseData.getSeriesCode() + "/presentation-" + baseData.getSeriesCodeCapital());
        generatePresentationProject(presRootPath, baseData, data);
    }

    private void generatePresentationProject(final File rootPath, final BookBaseData baseData, final BookLangData data) {
        final Console console = Console.getConsole();
        try {
            final File resourceDir = new File(rootPath, "src/main/resources");
            createFile(resourceDir, "spring", data.getSeriesCode() + data.getPosition() + "-spring.xml", getSpringFile(baseData, data));

            final File codeDir = new File(rootPath, "src/main/java/hu/zagor/gamebooks/" + baseData.getRuleset() + "/" + baseData.getSeriesCode() + "/"
                + baseData.getTitleCode() + "/mvc/books/");
            createFile(codeDir, "exception/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookExceptionController.java",
                getExceptionController(baseData, data));
            createFile(codeDir, "image/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookImageController.java", getImageController(baseData, data));
            createFile(codeDir, "inventory/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookInventoryController.java",
                getInventoryController(baseData, data));
            createFile(codeDir, "inventory/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookTakeItemController.java",
                getTakeItemController(baseData, data));
            createFile(codeDir, "load/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookLoadController.java", getLoadController(baseData, data));
            createFile(codeDir, "newgame/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookNewGameController.java",
                getNewGameController(baseData, data));
            createFile(codeDir, "save/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookSaveController.java", getSaveController(baseData, data));
            createFile(codeDir, "section/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookSectionController.java",
                getSectionController(baseData, data));
            createFile(codeDir, "section/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookWelcomeController.java",
                getWelcomeController(baseData, data));

            final File testCodeDir = new File(rootPath, "src/test/java/hu/zagor/gamebooks/" + baseData.getRuleset() + "/" + baseData.getSeriesCode() + "/"
                + baseData.getTitleCode() + "/mvc/books/");
            createFile(testCodeDir, "exception/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookExceptionControllerTest.java",
                getExceptionControllerTest(baseData, data));
            createFile(testCodeDir, "image/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookImageControllerTest.java",
                getImageControllerTest(baseData, data));
            createFile(testCodeDir, "inventory/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookInventoryControllerTest.java",
                getInventoryControllerTest(baseData, data));
            createFile(testCodeDir, "inventory/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookTakeItemControllerTest.java",
                getTakeItemControllerTest(baseData, data));
            createFile(testCodeDir, "load/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookLoadControllerTest.java",
                getLoadControllerTest(baseData, data));
            createFile(testCodeDir, "newgame/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookNewGameControllerTest.java",
                getNewGameControllerTest(baseData, data));
            createFile(testCodeDir, "save/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookSaveControllerTest.java",
                getSaveControllerTest(baseData, data));
            createFile(testCodeDir, "section/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookSectionControllerTest.java",
                getSectionControllerTest(baseData, data));
            createFile(testCodeDir, "section/controller", data.getSeriesCodeCapital() + data.getPosition() + "BookWelcomeControllerTest.java",
                getWelcomeControllerTest(baseData, data));

        } catch (final IOException exception) {
            console.print("Failed to create all necessary files.");
            exception.printStackTrace(System.out);
        }
    }

    private void generateLanguageProject(final File langRootPath, final BookBaseData baseData, final BookLangData data) {
        final Console console = Console.getConsole();
        try {

            final File resourceDir = new File(langRootPath, "src/main/resources");
            createFile(resourceDir, data.getSeriesCode() + data.getPosition() + "content.xml", getContentFile(baseData));
            if (baseData.hasItems()) {
                createFile(resourceDir, data.getSeriesCode() + data.getPosition() + "items.xml", getItemsFile(baseData));
            }
            if (baseData.hasEnemies()) {
                createFile(resourceDir, data.getSeriesCode() + data.getPosition() + "enemies.xml", getEnemiesFile(baseData));
            }

            createDir(resourceDir, "messages");
            createFile(resourceDir, "messages", "messages-" + baseData.getSeriesCode() + baseData.getPosition() + "_" + data.getLang() + ".properties", "");

        } catch (final IOException exception) {
            console.print("Failed to create all necessary files.");
            exception.printStackTrace(System.out);
        }
    }

}
