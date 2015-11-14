package hu.zagor.gamebooks.initiator.book.lang;

import hu.zagor.gamebooks.initiator.book.BookBaseData;
import hu.zagor.gamebooks.initiator.book.BookLangData;

public class LanguageProjectDataContents {

    public static String getImageController(final BookBaseData baseData, final BookLangData data) {
        String extendedClass;
        if ("ff".equals(baseData.getRuleset())) {
            extendedClass = null;
        } else {
            extendedClass = "Generic";
        }

        String tic;
        tic = "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode() + ".mvc.books.image.controller;\r\n";
        tic += "\r\n";
        tic += "import hu.zagor.gamebooks.PageAddresses;\r\n";
        tic += getParentImport(baseData, data, "image", "Image", extendedClass);
        tic += "import hu.zagor.gamebooks.support.bookids." + data.getFullLang() + "." + data.getBookIdDomain() + ";\r\n";
        tic += "\r\n";
        tic += "import org.springframework.stereotype.Controller;\r\n";
        tic += "import org.springframework.web.bind.annotation.RequestMapping;\r\n";
        tic += "\r\n";
        tic += "/**\r\n";
        tic += " * Controller for handling the image request to the given book.\r\n";
        tic += " * @author Tamas_Szekeres\r\n";
        tic += " *\r\n";
        tic += " */\r\n";
        tic += "@Controller\r\n";
        tic += "@RequestMapping(value = PageAddresses.BOOK_PAGE + \"/\" + " + data.getBookId() + ")\r\n";
        tic += "public class " + data.getSeriesCodeCapital() + data.getPosition() + "BookImageController extends " + getParentClassPrefix(baseData, data, extendedClass)
            + "BookImageController {\r\n";
        tic += "}\r\n";

        return tic;
    }

    private static String getParentClassPrefix(final BookBaseData baseData, final BookLangData data, final String prefix) {
        String parentClassPrefix;
        if (baseData.getSeriesCode().equals(data.getSeriesCode())) {
            parentClassPrefix = prefix == null ? baseData.getRulesetCapital() : prefix;
        } else {
            parentClassPrefix = baseData.getSeriesCodeCapital() + baseData.getPosition();
        }
        return parentClassPrefix;
    }

    private static String getParentImport(final BookBaseData baseData, final BookLangData data, final String type, final String classType, final String prefix) {
        String parentImport;
        if (baseData.getSeriesCode().equals(data.getSeriesCode())) {
            if (prefix == null) {
                parentImport = "import hu.zagor.gamebooks." + baseData.getRuleset() + ".mvc.book." + type + ".controller." + baseData.getRulesetCapital() + "Book"
                    + classType + "Controller;\r\n";
            } else {
                parentImport = "import hu.zagor.gamebooks.mvc.book." + type + ".controller.GenericBook" + classType + "Controller;\r\n";
            }
        } else {
            parentImport = "";
        }
        return parentImport;
    }

    public static String getSpringFile(final BookBaseData baseData, final BookLangData data) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
            + "<beans xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.springframework.org/schema/beans\" xmlns:p=\"http://www.springframework.org/schema/p\"\r\n"
            + "  xmlns:util=\"http://www.springframework.org/schema/util\" xmlns:c=\"http://www.springframework.org/schema/c\"\r\n"
            + "  xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd\r\n"
            + "        http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.0.xsd\">\r\n" + "\r\n"
            + "  <util:constant static-field=\"hu.zagor.gamebooks.support.bookids."
            + data.getFullLang()
            + "."
            + data.getBookId()
            + "\" id=\""
            + data.getSeriesCode()
            + data.getPosition()
            + "Id\" />\r\n"
            + "\r\n"
            + "  <bean class=\"hu.zagor.gamebooks.domain."
            + baseData.getBookInfoPrefix()
            + "BookInformations\" id=\""
            + data.getSeriesCode()
            + data.getPosition()
            + "Info\" c:id-ref=\""
            + data.getSeriesCode()
            + data.getPosition()
            + "Id\" p:coverPath=\"resources/"
            + baseData.getSeriesCode()
            + baseData.getPosition()
            + "/cover.jpg\" p:resourceDir=\""
            + baseData.getSeriesCode()
            + baseData.getPosition()
            + "\"\r\n"
            + "    p:contentTransformers-ref=\"default"
            + baseData.getRulesetCapital()
            + "ContentTransformers\" p:paragraphResolver-ref=\""
            + baseData.getRuleset()
            + "RuleBookParagraphResolver"
            + baseData.getDefaultSkillTestType()
            + "\" p:locale-ref=\""
            + data.getCompactLang()
            + "Locale\" p:position=\""
            + data.getActualPosition()
            + "\" p:series-ref=\""
            + data.getSeriesCode()
            + "Title\" p:title=\""
            + data.getTitle()
            + "\" p:characterHandler-ref=\""
            + baseData.getRuleset()
            + "CharacterHandler\" p:commandResolvers-ref=\""
            + baseData.getRuleset()
            + "CommandResolvers\" p:unfinished=\"true\""
            + (data.isHidden() ? " p:hidden=\"true\"" : "")
            + ">\r\n"
            + "    <property name=\"contents\">\r\n"
            + "      <bean class=\"hu.zagor.gamebooks.domain.BookContentFiles\" c:enemies=\""
            + data.getEnemiesFileName()
            + "\" c:items=\""
            + data.getItemsFileName()
            + "\" c:paragraphs=\""
            + data.getContentFileName()
            + "\" />\r\n"
            + "    </property>\r\n"
            + "    <property name=\"contentSpecification\">\r\n"
            + "      <bean class=\"hu.zagor.gamebooks.domain.BookContentSpecification\" p:inventoryAvailable=\""
            + baseData.hasInventory()
            + "\" p:mapAvailable=\""
            + baseData.hasMap() + "\" />\r\n" + "    </property>\r\n" + "  </bean>\r\n" + "\r\n" + "</beans>\r\n";
    }

    public static String getContentFile(final BookBaseData data) {
        String result = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n";
        result += "<content xmlns=\"http://gamebooks.zagor.hu\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://gamebooks.zagor.hu http://zagor.hu/xsd/"
            + data.getRuleset() + ".xsd\">\r\n";
        result += "  <p id=\"back_cover\">\r\n";
        result += "    <text>[p][/p]</text>\r\n";
        result += "    <next id=\"background\"/>\r\n";
        result += "  </p>\r\n";
        result += "  <p id=\"background\" display=\"\">\r\n";
        result += "    <text>[p][/p]</text>\r\n";
        result += "    <next id=\"1\"></next>\r\n";
        result += "  </p>\r\n";
        result += "</content>";
        return result;
    }

    public static String getItemsFile(final BookBaseData data) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n"
            + "<items xmlns=\"http://gamebooks.zagor.hu\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://gamebooks.zagor.hu http://zagor.hu/xsd/"
            + data.getRuleset() + "-items.xsd\">\r\n" + "  <item id=\"defWpn\" name=\"Ököl\" type=\"weapon1\" weaponSubType=\"weakBlunt\" staminaDamage=\"2\" />\r\n"
            + "\r\n" + "</items>\r\n";
    }

    public static String getEnemiesFile(final BookBaseData data) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n"
            + "<enemies xmlns=\"http://gamebooks.zagor.hu\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://gamebooks.zagor.hu http://zagor.hu/xsd/"
            + data.getRuleset() + "-enemies.xsd\">\r\n" + "  <enemy name=\"Thief\" id=\"1\" stamina=\"6\" skill=\"7\" />\r\n" + "\r\n" + "</enemies>\r\n";
    }

    public static String getSectionController(final BookBaseData baseData, final BookLangData data) {
        return "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
            + ".mvc.books.section.controller;\r\n" + "\r\n" + "import hu.zagor.gamebooks.PageAddresses;\r\n"
            + "import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;\r\n" + getParentImport(baseData, data, "section", "Section", null)
            + "import hu.zagor.gamebooks.support.bookids." + data.getFullLang() + "." + data.getBookIdDomain() + ";\r\n" + "\r\n"
            + "import org.springframework.beans.factory.annotation.Autowired;\r\n" + "import org.springframework.beans.factory.annotation.Qualifier;\r\n"
            + "import org.springframework.stereotype.Controller;\r\n" + "import org.springframework.web.bind.annotation.RequestMapping;\r\n" + "\r\n" + "/**\r\n"
            + " * Controller for handling the section changes in the given book.\r\n" + " * @author Tamas_Szekeres\r\n" + " *\r\n" + " */\r\n" + "@Controller\r\n"
            + "@RequestMapping(value = PageAddresses.BOOK_PAGE + \"/\" + " + data.getBookId() + ")\r\n" + "public class " + data.getSeriesCodeCapital()
            + data.getPosition() + "BookSectionController extends " + getParentClassPrefix(baseData, data, null) + "BookSectionController {\r\n" + "    /**\r\n"
            + "     * Constructor expecting the {@link SectionHandlingService} bean.\r\n"
            + "     * @param sectionHandlingService the {@link SectionHandlingService} bean\r\n" + "     */\r\n" + "    @Autowired\r\n" + "    public "
            + data.getSeriesCodeCapital() + data.getPosition() + "BookSectionController(@Qualifier(\"" + baseData.getRuleset()
            + "SectionHandlingService\") final SectionHandlingService sectionHandlingService) {\r\n" + "        super(sectionHandlingService);\r\n" + "    }\r\n"
            + "}\r\n";
    }

    public static String getWelcomeController(final BookBaseData baseData, final BookLangData data) {
        return "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
            + ".mvc.books.section.controller;\r\n" + "\r\n" + "import hu.zagor.gamebooks.PageAddresses;\r\n"
            + getParentImport(baseData, data, "section", "Welcome", "Generic") + "import hu.zagor.gamebooks.support.bookids." + data.getFullLang() + "."
            + data.getBookIdDomain() + ";\r\n" + "\r\n" + "import org.springframework.stereotype.Controller;\r\n"
            + "import org.springframework.web.bind.annotation.RequestMapping;\r\n" + "\r\n" + "/**\r\n"
            + " * Controller for handling the welcome requests to the given book.\r\n" + " * @author Tamas_Szekeres\r\n" + " *\r\n" + " */\r\n" + "@Controller\r\n"
            + "@RequestMapping(value = PageAddresses.BOOK_PAGE + \"/\" + " + data.getBookId() + ")\r\n" + "public class " + data.getSeriesCodeCapital()
            + data.getPosition() + "BookWelcomeController extends " + getParentClassPrefix(baseData, data, "Generic") + "BookWelcomeController {\r\n" + "}\r\n";
    }

    public static String getNewGameController(final BookBaseData baseData, final BookLangData data) {
        return "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
            + ".mvc.books.newgame.controller;\r\n" + "\r\n" + "import hu.zagor.gamebooks.PageAddresses;\r\n"
            + getParentImport(baseData, data, "newgame", "NewGame", null) + "import hu.zagor.gamebooks.support.bookids." + data.getFullLang() + "."
            + data.getBookIdDomain() + ";\r\n" + "\r\n" + "import org.springframework.stereotype.Controller;\r\n"
            + "import org.springframework.web.bind.annotation.RequestMapping;\r\n" + "\r\n" + "/**\r\n"
            + " * Controller for handling the new game requests to the given book.\r\n" + " * @author Tamas_Szekeres\r\n" + " *\r\n" + " */\r\n" + "@Controller\r\n"
            + "@RequestMapping(value = PageAddresses.BOOK_PAGE + \"/\" + " + data.getBookId() + ")\r\n" + "public class " + data.getSeriesCodeCapital()
            + data.getPosition() + "BookNewGameController extends " + getParentClassPrefix(baseData, data, null) + "BookNewGameController {\r\n" + "}\r\n";
    }

    public static String getLoadController(final BookBaseData baseData, final BookLangData data) {
        return "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode() + ".mvc.books.load.controller;\r\n"
            + "\r\n" + "import hu.zagor.gamebooks.PageAddresses;\r\n" + "import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;\r\n"
            + getParentImport(baseData, data, "load", "Load", null) + "import hu.zagor.gamebooks.support.bookids." + data.getFullLang() + "." + data.getBookIdDomain()
            + ";\r\n" + "\r\n" + "import org.springframework.beans.factory.annotation.Autowired;\r\n"
            + "import org.springframework.beans.factory.annotation.Qualifier;\r\n" + "import org.springframework.stereotype.Controller;\r\n"
            + "import org.springframework.web.bind.annotation.RequestMapping;\r\n" + "\r\n" + "/**\r\n"
            + " * Controller for handling the load request to the given book.\r\n" + " * @author Tamas_Szekeres\r\n" + " *\r\n" + " */\r\n" + "@Controller\r\n"
            + "@RequestMapping(value = PageAddresses.BOOK_PAGE + \"/\" + " + data.getBookId() + ")\r\n" + "public class " + data.getSeriesCodeCapital()
            + data.getPosition() + "BookLoadController extends " + getParentClassPrefix(baseData, data, null) + "BookLoadController {\r\n" + "    /**\r\n"
            + "     * Constructor expecting the {@link SectionHandlingService} bean.\r\n"
            + "     * @param sectionHandlingService the {@link SectionHandlingService} bean\r\n" + "     */\r\n" + "    @Autowired\r\n" + "    public "
            + data.getSeriesCodeCapital() + data.getPosition() + "BookLoadController(@Qualifier(\"" + baseData.getRuleset()
            + "SectionHandlingService\") final SectionHandlingService sectionHandlingService) {\r\n" + "        super(sectionHandlingService);\r\n" + "    }\r\n"
            + "}\r\n";
    }

    public static String getInventoryController(final BookBaseData baseData, final BookLangData data) {
        return "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
            + ".mvc.books.inventory.controller;\r\n" + "\r\n" + "import hu.zagor.gamebooks.PageAddresses;\r\n"
            + getParentImport(baseData, data, "inventory", "Inventory", null) + "import hu.zagor.gamebooks.support.bookids." + data.getFullLang() + "."
            + data.getBookIdDomain() + ";\r\n" + "\r\n" + "import org.springframework.stereotype.Controller;\r\n"
            + "import org.springframework.web.bind.annotation.RequestMapping;\r\n" + "\r\n" + "/**\r\n"
            + " * Controller for handling the inventory list request to the given book.\r\n" + " * @author Tamas_Szekeres\r\n" + " *\r\n" + " */\r\n" + "@Controller\r\n"
            + "@RequestMapping(value = PageAddresses.BOOK_PAGE + \"/\" + " + data.getBookId() + ")\r\n" + "public class " + data.getSeriesCodeCapital()
            + data.getPosition() + "BookInventoryController extends " + getParentClassPrefix(baseData, data, null) + "BookInventoryController {\r\n" + "}\r\n";
    }

    public static String getTakeItemController(final BookBaseData baseData, final BookLangData data) {
        String tic;
        tic = "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
            + ".mvc.books.inventory.controller;\r\n";
        tic += "\r\n";
        tic += "import hu.zagor.gamebooks.PageAddresses;\r\n";
        tic += getParentImport(baseData, data, "inventory", "TakeItem", null);
        tic += "import hu.zagor.gamebooks.support.bookids." + data.getFullLang() + "." + data.getBookIdDomain() + ";\r\n";
        tic += "\r\n";
        tic += "import org.springframework.stereotype.Controller;\r\n";
        tic += "import org.springframework.web.bind.annotation.RequestMapping;\r\n";
        tic += "\r\n";
        tic += "/**\r\n";
        tic += " * Controller for handling the item taking request to the given book.\r\n";
        tic += " * @author Tamas_Szekeres\r\n";
        tic += " *\r\n";
        tic += " */\r\n";
        tic += "@Controller\r\n";
        tic += "@RequestMapping(value = PageAddresses.BOOK_PAGE + \"/\" + " + data.getBookId() + ")\r\n";
        tic += "public class " + data.getSeriesCodeCapital() + data.getPosition() + "BookTakeItemController extends " + getParentClassPrefix(baseData, data, null)
            + "BookTakeItemController {\r\n";
        tic += "}\r\n";
        return tic;
    }

    // ************************

    public static String getImageControllerTest(final BookBaseData baseData, final BookLangData data) {
        String tic;
        tic = "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode() + ".mvc.books.image.controller;\r\n";
        tic += "\r\n";
        tic += "import org.testng.annotations.Test;\r\n";
        tic += "\r\n";
        tic += "/**\r\n";
        tic += " * Unit test for class {@link " + data.getSeriesCodeCapital() + data.getPosition() + "BookImageController}.\r\n";
        tic += " * @author Tamas_Szekeres\r\n";
        tic += " */\r\n";
        tic += "@Test\r\n";
        tic += "public class " + data.getSeriesCodeCapital() + data.getPosition() + "BookImageControllerTest {\r\n";
        tic += "\r\n";
        tic += "    public void testConstructor() {\r\n";
        tic += "        // GIVEN\r\n";
        tic += "        // WHEN\r\n";
        tic += "        new " + data.getSeriesCodeCapital() + data.getPosition() + "BookImageController().getClass();\r\n";
        tic += "        // THEN\r\n";
        tic += "    }\r\n";
        tic += "}\r\n";
        return tic;
    }

    public static String getInventoryControllerTest(final BookBaseData baseData, final BookLangData data) {
        return "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
            + ".mvc.books.inventory.controller;\r\n" + "\r\n" + "import org.testng.annotations.Test;\r\n" + "\r\n" + "/**\r\n" + " * Unit test for class {@link "
            + data.getSeriesCodeCapital() + data.getPosition() + "BookInventoryController}.\r\n" + " * @author Tamas_Szekeres\r\n" + " */\r\n" + "@Test\r\n"
            + "public class " + data.getSeriesCodeCapital() + data.getPosition() + "BookInventoryControllerTest {\r\n" + "\r\n"
            + "    public void testConstructor() {\r\n" + "        // GIVEN\r\n" + "        // WHEN\r\n" + "        new " + data.getSeriesCodeCapital()
            + data.getPosition() + "BookInventoryController().getClass();\r\n" + "        // THEN\r\n" + "    }\r\n" + "}\r\n";

    }

    public static String getTakeItemControllerTest(final BookBaseData baseData, final BookLangData data) {
        return "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
            + ".mvc.books.inventory.controller;\r\n" + "\r\n" + "import org.testng.annotations.Test;\r\n" + "\r\n" + "/**\r\n" + " * Unit test for class {@link "
            + data.getSeriesCodeCapital() + data.getPosition() + "BookTakeItemController}.\r\n" + " * @author Tamas_Szekeres\r\n" + " */\r\n" + "@Test\r\n"
            + "public class " + data.getSeriesCodeCapital() + data.getPosition() + "BookTakeItemControllerTest {\r\n" + "\r\n"
            + "    public void testConstructor() {\r\n" + "        // GIVEN\r\n" + "        // WHEN\r\n" + "        new " + data.getSeriesCodeCapital()
            + data.getPosition() + "BookTakeItemController().getClass();\r\n" + "        // THEN\r\n" + "    }\r\n" + "}\r\n";
    }

    public static String getLoadControllerTest(final BookBaseData baseData, final BookLangData data) {
        return "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode() + ".mvc.books.load.controller;\r\n"
            + "\r\n" + "import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;\r\n" + "\r\n" + "import org.easymock.EasyMock;\r\n"
            + "import org.easymock.IMocksControl;\r\n" + "import org.testng.annotations.AfterMethod;\r\n" + "import org.testng.annotations.BeforeClass;\r\n"
            + "import org.testng.annotations.BeforeMethod;\r\n" + "import org.testng.annotations.Test;\r\n" + "\r\n" + "/**\r\n" + " * Unit test for class {@link "
            + data.getSeriesCodeCapital() + data.getPosition() + "BookLoadController}.\r\n" + " * @author Tamas_Szekeres\r\n" + " */\r\n" + "@Test\r\n" + "public class "
            + data.getSeriesCodeCapital() + data.getPosition() + "BookLoadControllerTest {\r\n" + "\r\n" + "    private IMocksControl mockControl;\r\n"
            + "    private SectionHandlingService sectionHandler;\r\n" + "\r\n" + "    @BeforeClass\r\n" + "    public void setUpClass() {\r\n"
            + "        mockControl = EasyMock.createStrictControl();\r\n" + "        sectionHandler = mockControl.createMock(SectionHandlingService.class);\r\n"
            + "    }\r\n" + "\r\n" + "    @BeforeMethod\r\n" + "    public void setUpMethod() {\r\n" + "        mockControl.reset();\r\n" + "    }\r\n" + "\r\n"
            + "    @Test(expectedExceptions = IllegalArgumentException.class)\r\n"
            + "    public void testConstructorWhenSectionHandlerIsNullShouldThrowException() {\r\n" + "        // GIVEN\r\n" + "        mockControl.replay();\r\n"
            + "        // WHEN\r\n" + "        new " + data.getSeriesCodeCapital() + data.getPosition() + "BookLoadController(null).getClass();\r\n"
            + "        // THEN throws exception\r\n" + "    }\r\n" + "\r\n" + "    public void testConstructorWhenSectionHandlerProvidedShouldCreateObject() {\r\n"
            + "        // GIVEN\r\n" + "        mockControl.replay();\r\n" + "        // WHEN\r\n" + "        new " + data.getSeriesCodeCapital() + data.getPosition()
            + "BookLoadController(sectionHandler).getClass();\r\n" + "        // THEN\r\n" + "    }\r\n" + "\r\n" + "    @AfterMethod\r\n"
            + "    public void tearDownMethod() {\r\n" + "        mockControl.verify();\r\n" + "    }\r\n" + "}\r\n";
    }

    public static String getNewGameControllerTest(final BookBaseData baseData, final BookLangData data) {
        String controllerContent;
        if ("tm".equals(data.getSeriesCode())) {
            controllerContent = "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
                + ".mvc.books.newgame.controller;\r\n" + "\r\n" + "import static org.easymock.EasyMock.expect;\r\n"
                + "import hu.zagor.gamebooks.character.Character;\r\n" + "import hu.zagor.gamebooks.character.handler.CharacterHandler;\r\n"
                + "import hu.zagor.gamebooks.character.handler.item.CharacterItemHandler;\r\n" + "import hu.zagor.gamebooks.domain.BookInformations;\r\n" + "\r\n"
                + "import java.util.Locale;\r\n" + "\r\n" + "import org.easymock.EasyMock;\r\n" + "import org.easymock.IMocksControl;\r\n"
                + "import org.powermock.reflect.Whitebox;\r\n" + "import org.springframework.beans.factory.BeanFactory;\r\n"
                + "import org.springframework.context.ApplicationContext;\r\n" + "import org.testng.Assert;\r\n" + "import org.testng.annotations.AfterMethod;\r\n"
                + "import org.testng.annotations.BeforeClass;\r\n" + "import org.testng.annotations.BeforeMethod;\r\n" + "import org.testng.annotations.Test;\r\n"
                + "\r\n" + "/**\r\n" + " * Unit test for class {@link " + data.getSeriesCodeCapital() + data.getPosition() + "BookNewGameController}.\r\n"
                + " * @author Tamas_Szekeres\r\n" + " */\r\n" + "@Test\r\n" + "public class " + data.getSeriesCodeCapital() + data.getPosition()
                + "BookNewGameControllerTest {\r\n" + "\r\n" + "    private static final String DATA_BANK_STRING = \"dataBankString\";\r\n"
                + "    private IMocksControl mockControl;\r\n" + "    private " + data.getSeriesCodeCapital() + data.getPosition()
                + "BookNewGameController underTest;\r\n" + "    private Locale locale;\r\n" + "    private Character character;\r\n"
                + "    private BeanFactory beanFactory;\r\n" + "    private ApplicationContext applicationContext;\r\n" + "    private BookInformations info;\r\n"
                + "    private CharacterHandler characterHandler;\r\n" + "    private CharacterItemHandler itemHandler;\r\n" + "\r\n" + "    @BeforeClass\r\n"
                + "    public void setUpClass() {\r\n" + "        mockControl = EasyMock.createStrictControl();\r\n" + "        locale = Locale.ENGLISH;\r\n"
                + "        beanFactory = mockControl.createMock(BeanFactory.class);\r\n"
                + "        applicationContext = mockControl.createMock(ApplicationContext.class);\r\n"
                + "        itemHandler = mockControl.createMock(CharacterItemHandler.class);\r\n" + "\r\n" + "        characterHandler = new CharacterHandler();\r\n"
                + "        characterHandler.setItemHandler(itemHandler);\r\n" + "\r\n" + "        info = new BookInformations(1L);\r\n"
                + "        info.setResourceDir(\"" + data.getSeriesCode() + data.getPosition() + "\");\r\n" + "        info.setCharacterHandler(characterHandler);\r\n"
                + "\r\n" + "        underTest = new Ir1BookNewGameController();\r\n" + "        underTest.setBeanFactory(beanFactory);\r\n"
                + "        underTest.setApplicationContext(applicationContext);\r\n" + "        Whitebox.setInternalState(underTest, \"info\", info);\r\n" + "    }\r\n"
                + "\r\n" + "    @BeforeMethod\r\n" + "    public void setUpMethod() {\r\n" + "        character = new Character();\r\n"
                + "        mockControl.reset();\r\n" + "    }\r\n" + "\r\n" + "    @Test(expectedExceptions = IllegalArgumentException.class)\r\n"
                + "    public void testGetCharacterWhenLocaleIsNullShouldThrowException() {\r\n" + "        // GIVEN\r\n" + "        mockControl.replay();\r\n"
                + "        // WHEN\r\n" + "        underTest.getCharacter(null);\r\n" + "        // THEN throws exception\r\n" + "    }\r\n" + "\r\n"
                + "    public void testGetCharacterWhenLocaleIsSetShouldReturnCharacter() {\r\n" + "        // GIVEN\r\n"
                + "        expect(beanFactory.getBean(\"character\")).andReturn(character);\r\n" + "        expect(applicationContext.getMessage(\""
                + data.getSeriesCode() + data.getPosition() + ".databank\", null, locale)).andReturn(DATA_BANK_STRING);\r\n"
                + "        expect(itemHandler.addItem(character, \"1001\", 1)).andReturn(1);\r\n" + "        mockControl.replay();\r\n" + "        // WHEN\r\n"
                + "        final Character returned = underTest.getCharacter(locale);\r\n" + "        // THEN\r\n"
                + "        Assert.assertSame(returned, character);\r\n" + "        Assert.assertEquals(returned.getNotes(), DATA_BANK_STRING);\r\n" + "    }\r\n"
                + "\r\n" + "    @AfterMethod\r\n" + "    public void tearDownMethod() {\r\n" + "        mockControl.verify();\r\n" + "    }\r\n" + "\r\n" + "}\r\n";
        } else {
            controllerContent = "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
                + ".mvc.books.newgame.controller;\r\n" + "\r\n" + "import org.testng.annotations.Test;\r\n" + "\r\n" + "/**\r\n" + " * Unit test for class {@link "
                + data.getSeriesCodeCapital() + data.getPosition() + "BookNewGameController}.\r\n" + " * @author Tamas_Szekeres\r\n" + " */\r\n" + "@Test\r\n"
                + "public class " + data.getSeriesCodeCapital() + data.getPosition() + "BookNewGameControllerTest {\r\n" + "\r\n"
                + "    public void testConstructor() {\r\n" + "        // GIVEN\r\n" + "        // WHEN\r\n" + "        new " + data.getSeriesCodeCapital()
                + data.getPosition() + "BookNewGameController().getClass();\r\n" + "        // THEN\r\n" + "    }\r\n" + "}\r\n";
        }
        return controllerContent;
    }

    public static String getSectionControllerTest(final BookBaseData baseData, final BookLangData data) {
        return "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
            + ".mvc.books.section.controller;\r\n" + "\r\n" + "import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;\r\n" + "\r\n"
            + "import org.easymock.EasyMock;\r\n" + "import org.easymock.IMocksControl;\r\n" + "import org.testng.annotations.AfterMethod;\r\n"
            + "import org.testng.annotations.BeforeClass;\r\n" + "import org.testng.annotations.BeforeMethod;\r\n" + "import org.testng.annotations.Test;\r\n" + "\r\n"
            + "/**\r\n" + " * Unit test for class {@link " + data.getSeriesCodeCapital() + data.getPosition() + "BookSectionController}.\r\n"
            + " * @author Tamas_Szekeres\r\n" + " */\r\n" + "@Test\r\n" + "public class " + data.getSeriesCodeCapital() + data.getPosition()
            + "BookSectionControllerTest {\r\n" + "\r\n" + "    private IMocksControl mockControl;\r\n" + "    private SectionHandlingService sectionHandler;\r\n"
            + "\r\n" + "    @BeforeClass\r\n" + "    public void setUpClass() {\r\n" + "        mockControl = EasyMock.createStrictControl();\r\n"
            + "        sectionHandler = mockControl.createMock(SectionHandlingService.class);\r\n" + "    }\r\n" + "\r\n" + "    @BeforeMethod\r\n"
            + "    public void setUpMethod() {\r\n" + "        mockControl.reset();\r\n" + "    }\r\n" + "\r\n"
            + "    @Test(expectedExceptions = IllegalArgumentException.class)\r\n"
            + "    public void testConstructorWhenSectionHandlerIsNullShouldThrowException() {\r\n" + "        // GIVEN\r\n" + "        mockControl.replay();\r\n"
            + "        // WHEN\r\n" + "        new " + data.getSeriesCodeCapital() + data.getPosition() + "BookSectionController(null).getClass();\r\n"
            + "        // THEN throws exception\r\n" + "    }\r\n" + "\r\n" + "    public void testConstructorWhenSectionHandlerProvidedShouldCreateObject() {\r\n"
            + "        // GIVEN\r\n" + "        mockControl.replay();\r\n" + "        // WHEN\r\n" + "        new " + data.getSeriesCodeCapital() + data.getPosition()
            + "BookSectionController(sectionHandler).getClass();\r\n" + "        // THEN\r\n" + "    }\r\n" + "\r\n" + "    @AfterMethod\r\n"
            + "    public void tearDownMethod() {\r\n" + "        mockControl.verify();\r\n" + "    }\r\n" + "}\r\n";
    }

    public static String getWelcomeControllerTest(final BookBaseData baseData, final BookLangData data) {
        return "package hu.zagor.gamebooks." + baseData.getRuleset() + "." + baseData.getSeriesCode() + "." + baseData.getTitleCode()
            + ".mvc.books.section.controller;\r\n" + "\r\n" + "import org.testng.annotations.Test;\r\n" + "\r\n" + "/**\r\n" + " * Unit test for class {@link "
            + data.getSeriesCodeCapital() + data.getPosition() + "BookWelcomeController}.\r\n" + " * @author Tamas_Szekeres\r\n" + " */\r\n" + "@Test\r\n"
            + "public class " + data.getSeriesCodeCapital() + data.getPosition() + "BookWelcomeControllerTest {\r\n" + "\r\n" + "    public void testConstructor() {\r\n"
            + "        // GIVEN\r\n" + "        // WHEN\r\n" + "        new " + data.getSeriesCodeCapital() + data.getPosition()
            + "BookWelcomeController().getClass();\r\n" + "        // THEN\r\n" + "    }\r\n" + "}\r\n";
    }

    public static String getCharPage() {
        return "<%@page pageEncoding=\"utf-8\" contentType=\"text/html; charset=utf-8\"%>\r\n"
            + "<%@taglib uri=\"http://tiles.apache.org/tags-tiles\" prefix=\"tiles\"%>\r\n" + "\r\n"
            + "<tiles:insertTemplate template=\"../charpage/charpage-basic.jsp\" />\r\n";
    }

}
