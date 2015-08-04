package hu.zagor.gamebooks.selenium.domain;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestPack {

    private final WebDriver driver;
    private final EventFiringWebDriver eventFiringWebDriver;
    private final WebDriverWait wait;
    private final Process chromeDriver;

    public TestPack(final WebDriver driver, final EventFiringWebDriver eventFiringWebDriver, final WebDriverWait wait, final Process chromeDriver) {
        this.driver = driver;
        this.eventFiringWebDriver = eventFiringWebDriver;
        this.wait = wait;
        this.chromeDriver = chromeDriver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public EventFiringWebDriver getEventFiringWebDriver() {
        return eventFiringWebDriver;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public Process getChromeDriver() {
        return chromeDriver;
    }

}
