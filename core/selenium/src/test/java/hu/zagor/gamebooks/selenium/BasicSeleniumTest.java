package hu.zagor.gamebooks.selenium;

import hu.zagor.gamebooks.selenium.domain.TestPack;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.reporters.Files;

/**
 * Parent test class for all selenium tests.
 * @author Tamas_Szekeres
 */
public class BasicSeleniumTest {

    private static AtomicInteger counter = new AtomicInteger(0);
    private static List<TestPack> testPacks;
    private String bookCode;
    private final Map<String, Set<String>> verified = new HashMap<>();
    private final Map<String, Set<String>> jumped = new HashMap<>();
    private String lastSection;
    private final Set<String> verifiedSections = new HashSet<>();

    private TestPack testPack;
    private WebDriver driver;
    private EventFiringWebDriver eventFiringWebDriver;
    private WebDriverWait wait;

    @BeforeTest
    public void setUpTest() throws IOException {
        final List<TestPack> packs = new ArrayList<>();

        final int i = counter.getAndIncrement();

        final int port = 9615 + i;
        final ProcessBuilder processBuilder = new ProcessBuilder("d:\\System\\eclipse\\output\\chromedriver.exe", "--port=" + port);
        processBuilder.redirectOutput(Redirect.INHERIT);
        processBuilder.redirectError(Redirect.INHERIT);
        final Process chromeDriver = processBuilder.start();

        driver = new RemoteWebDriver(new URL("http://localhost:" + port), DesiredCapabilities.chrome());
        driver.get("http://localhost:8080/gamebooks/testLogin");
        final EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);
        final WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.manage().window().setPosition(new Point(-1920 + i * 960, 0));
        driver.manage().window().maximize();
        // driver.manage().window().setSize(new Dimension(1920, 1080));
        fill("#adventurerName", "gnome");
        fill("#adventurerPassphrase", "firefox");
        click("#loginSubmitButton");
        packs.add(new TestPack(driver, eventFiringWebDriver, wait, chromeDriver));

        testPacks = Collections.synchronizedList(packs);
    }

    @BeforeMethod
    public void setUpMethod() {
        lastSection = "background";
        verifiedSections.clear();
    }

    protected void startBook(final String bookId, final String bookCode) {
        getTestPack();
        this.bookCode = bookCode;
        final By bookSelector = By.cssSelector("[data-bookId='" + bookId + "']");
        String bookUrl = null;
        while (bookUrl == null) {
            try {
                final WebElement bookElement = driver.findElement(bookSelector);
                bookUrl = bookElement.getAttribute("href");
            } catch (final NoSuchElementException ex) {
            }
        }
        driver.get(bookUrl);
        goToPosition("new");
    }

    protected synchronized void getTestPack() {
        testPack = testPacks.remove(0);
        driver = testPack.getDriver();
        eventFiringWebDriver = testPack.getEventFiringWebDriver();
        wait = testPack.getWait();
    }

    protected WebElement fill(final String selector, final String value) {
        final WebElement element = driver.findElement(By.cssSelector(selector));
        element.clear();
        element.sendKeys(value);
        return element;
    }

    protected void click(final String selector) {
        waitAndFindElement(selector).click();
    }

    private void goToPosition(final String choicePosition) {
        driver.findElement(By.cssSelector("#choice a[href='" + choicePosition + "']")).click();
        verifyBrokenImages(choicePosition, null);
    }

    private void verifyBrokenImages(final String choicePosition, final String choiceSection) {
        try {
            final List<WebElement> allImages = eventFiringWebDriver.findElement(By.id(getMainContentId())).findElements(By.tagName("img"));
            final String script = "return (typeof arguments[0].naturalWidth!=\"undefined\" && arguments[0].naturalWidth>0)";
            for (final WebElement image : allImages) {
                final Object imgStatus = eventFiringWebDriver.executeScript(script, image);
                if (imgStatus.equals(false)) {
                    final String currentImageUrl = image.getAttribute("src");
                    throw new IllegalStateException("The image '" + currentImageUrl + "' is broken in choice position '" + choicePosition + "', choice section '"
                        + choiceSection + "'.");
                }
            }
        } catch (final StaleElementReferenceException exception) {
            verifyBrokenImages(choicePosition, choiceSection);
        }
    }

    protected String getMainContentId() {
        return "gamebookContent";
    }

    protected void goToPosition(final int choicePosition, final int choiceSection) {
        goToPosition(String.valueOf(choicePosition), String.valueOf(choiceSection));
    }

    protected void goToPosition(final String choicePosition, final String choiceSection) {
        final WebElement choice = verifyPosition(choicePosition, choiceSection);
        ensureAllPositionsWereVerified();
        choice.click();
        addEntry(jumped, choiceSection);
        lastSection = choiceSection;
    }

    private void ensureAllPositionsWereVerified() {
        final List<WebElement> choiceElements = driver.findElements(By.cssSelector("#choice a"));
        for (final WebElement choiceElement : choiceElements) {
            String position = choiceElement.getAttribute("href");
            position = position.substring(position.lastIndexOf("/") + 1);
            final String choiceText = choiceElement.getText();
            final int sectionParenhtesisOpen = choiceText.lastIndexOf("(");
            final String section = choiceText.substring(sectionParenhtesisOpen + 1, choiceText.indexOf(")", sectionParenhtesisOpen));
            if (!verifiedSections.contains(position + "|" + section)) {
                throw new IllegalStateException("Choice at position '" + position + "' with target section '" + section
                    + "' hasn't been verified prior to navigating away!");
            }
            verifiedSections.remove(position + "|" + section);
        }
        if (!verifiedSections.isEmpty()) {
            throw new IllegalStateException("Illegal verified section(s) found during verification! The first one is '" + verifiedSections.iterator().next() + "'.");
        }
    }

    protected WebElement verifyPosition(final int choicePosition, final int choiceSection) {
        return verifyPosition(String.valueOf(choicePosition), String.valueOf(choiceSection));
    }

    protected void verifyImage(final String imageName) {
        try {
            Assert.assertTrue(waitAndFindElement(".inlineImage").findElement(By.tagName("img")).getAttribute("src").contains(imageName));
        } catch (final NoSuchElementException exception) {
            throw new NoSuchElementException("Couldn't verify the existence of image '" + imageName + "'.", exception);
        }

    }

    protected WebElement verifyPosition(final String choicePosition, final String choiceSection) {
        try {
            verifiedSections.add(choicePosition + "|" + choiceSection);
            String choiceId = "";
            WebElement element = null;
            while (StringUtils.isEmpty(choiceId)) {
                try {
                    element = waitAndFindElement("#choice a[href='" + choicePosition + "']");
                    choiceId = element.getText().trim();
                } catch (final StaleElementReferenceException ex) {
                }
            }
            Assert.assertTrue(choiceId.endsWith("(" + choiceSection + ")"), "Section " + choiceSection + " is not filed at position " + choicePosition
                + ", we have found '" + choiceId + "' instead!");
            addEntry(verified, choiceSection);
            return element;
        } catch (final NoSuchElementException exception) {
            throw new NoSuchElementException("Couldn't verify the existence of choice position '" + choicePosition + "' for section '" + choiceSection + "'.", exception);
        }
    }

    protected void verifyMissingPosition(final int choiceSection) {
        verifyMissingPosition(String.valueOf(choiceSection));
    }

    protected void verifyMissingPosition(final String choiceSection) {
        try {
            driver.findElement(By.partialLinkText("(" + choiceSection + ")"));
            throw new IllegalStateException("Choice section '" + choiceSection + "' exists, while we expected it to be absent!");
        } catch (final NoSuchElementException exception) {
        }
    }

    private void addEntry(final Map<String, Set<String>> map, final String choiceSection) {
        if (!map.containsKey(lastSection)) {
            map.put(lastSection, new HashSet<String>());
        }
        if (!map.containsKey(choiceSection)) {
            map.put(choiceSection, new HashSet<String>());
        }
        map.get(lastSection).add(choiceSection);
    }

    protected void verifyTakeableItem(final String itemId) {
        try {
            waitAndFindElement(".takeItem[data-id=\"" + itemId + "\"]");
        } catch (final NoSuchElementException exception) {
            throw new NoSuchElementException("Failed to verify the presence of takeable item '" + itemId + "'.", exception);
        }
    }

    protected void verifyNonTakeableItem(final String itemId) {
        try {
            driver.findElement(By.cssSelector(".takeItem[data-id=\"" + itemId + "\"]"));
            throw new IllegalStateException("Failed to verify that item '" + itemId + "' is no longer takeable.");
        } catch (final NoSuchElementException exception) {
        }
    }

    protected void takeItem(final String itemId) {
        waitAndFindElement(".takeItem[data-id=\"" + itemId + "\"]").click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(":not(.takeItem)[data-id=\"" + itemId + "\"]")));
    }

    protected void verifyText(final String text) {
        Assert.assertTrue(waitAndFindElement("#" + getMainContentId()).getText().contains(text));
    }

    private void flushMapInfo() {
        final File origDot = new File("d:\\System\\eclipse\\output\\map\\" + bookCode + "content.dot");
        final File newDot = new File("d:\\System\\eclipse\\output\\map\\" + bookCode + "contentCoverage.dot");
        if (origDot.exists()) {
            try {
                String content = Files.readFile(origDot);
                content = content.replace("}", generateCoverageColors() + "}");
                content = generateArrowColorings(content);
                Files.writeFile(content, newDot);
            } catch (final IOException exception) {

            }
        }
        try {
            final String command = "\"d:/System/Visual Studio/Gamebooks/Output/graphviz/dot.exe\" -Tpng -o" + bookCode + "Coverage.png " + bookCode
                + "contentCoverage.dot";
            final File workDirectory = new File("d:/System/eclipse/output/map");
            Runtime.getRuntime().exec(command, null, workDirectory);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        verified.clear();
        jumped.clear();
    }

    private String generateArrowColorings(final String content) {
        String newContent = content;

        for (final Entry<String, Set<String>> sources : jumped.entrySet()) {
            for (final String target : sources.getValue()) {
                newContent = newContent.replace("\"" + sources.getKey() + "\"->\"" + target + "\";", "\"" + sources.getKey() + "\"->\"" + target + "\"[color=green];");
            }
        }

        return newContent;
    }

    private String generateCoverageColors() {
        final StringBuilder builder = new StringBuilder();
        for (final Entry<String, Set<String>> entry : jumped.entrySet()) {
            builder.append("\"" + entry.getKey() + "\" [style=filled,color=" + (entry.getValue().size() == verified.get(entry.getKey()).size() ? "green" : "orange")
                + "];");
        }
        return builder.toString();
    }

    protected void ending() {
        addEntry(verified, "");
        addEntry(jumped, "");
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected void setBookCode(final String bookCode) {
        this.bookCode = bookCode;
    }

    protected EventFiringWebDriver getEventFiringWebDriver() {
        return eventFiringWebDriver;
    }

    protected void sleep(final int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (final InterruptedException e) {
        }
    }

    protected void noJumpChoicesAvailable() {
        try {
            for (int i = 0; i < 3; i++) {
                driver.findElement(By.cssSelector("#choice li"));
                Thread.sleep(300);
            }
            throw new IllegalStateException("Failed to verify that there are no more selection options available for the player.");
        } catch (final NoSuchElementException exception) {
        } catch (final InterruptedException e) {
            throw new IllegalStateException("Failed to verify that there are no more selection options available for the player.");
        }
    }

    @AfterMethod
    public void tearDownMethod() {
        noJumpChoicesAvailable();
        verifyBrokenImages("closing", "closing");
        waitAndFindElement("#restartBook").click();
    }

    @AfterClass
    public void tearDownClass() {
        testPacks.add(testPack);
        flushMapInfo();
        testPack.getDriver().findElement(By.id("closeBook")).click();
    }

    @AfterTest
    public void tearDownTest() {
        for (final TestPack testPack : testPacks) {
            testPack.getWait().until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#logout")));
            testPack.getDriver().findElement(By.id("logout")).click();
            testPack.getDriver().close();
            testPack.getChromeDriver().destroy();
        }
    }

    protected WebElement waitAndFindElement(final String cssSelector) {
        if (wait != null) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
        }
        return driver.findElement(By.cssSelector(cssSelector));
    }

    public WebDriverWait getWait() {
        return wait;
    }

}
