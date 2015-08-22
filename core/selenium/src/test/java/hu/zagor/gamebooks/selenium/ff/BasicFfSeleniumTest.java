package hu.zagor.gamebooks.selenium.ff;

import hu.zagor.gamebooks.selenium.BasicSeleniumTest;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.util.StringUtils;

public class BasicFfSeleniumTest extends BasicSeleniumTest {

    @Override
    protected void startBook(final String bookId, final String bookCode) {
        getTestPack();
        setBookCode(bookCode);
        final By bookSelector = By.cssSelector("[data-bookId='" + bookId + "']");
        final WebElement bookElement = getDriver().findElement(bookSelector);
        final String bookUrl = bookElement.getAttribute("href");
        getDriver().get(bookUrl);
    }

    @Override
    protected String getMainContentId() {
        return "ffGamebookContent";
    }

    protected void setUpRandomRolls(final List<Integer> randomNumbers) {
        final String join = StringUtils.arrayToCommaDelimitedString(randomNumbers.toArray());
        getDriver().navigate().to(getRootUrl(getDriver().getCurrentUrl()) + "new-" + join);
        click("[data-generator-button='ff']");
    }

    private String getRootUrl(final String currentUrl) {
        return currentUrl.replaceAll("(http:\\/\\/.*book\\/[0-9]+\\/).*", "$1");
    }

    protected void setUpRandomRolls(final String potionId, final List<Integer> randomNumbers) {
        final String join = StringUtils.arrayToCommaDelimitedString(randomNumbers.toArray());
        getDriver().navigate().to(getRootUrl(getDriver().getCurrentUrl()) + "new-" + join);
        selectPotion(potionId);
        click("[data-generator-button='ff']");
    }

    protected void attackEnemy(final String enemyId) {
        noJumpChoicesAvailable();
        click("[data-enemy-id='" + enemyId + "'][data-enemy-name]");
        click("[data-attack='ff']");
    }

    protected void prepareLuckSettings(final boolean luckOnAttack, final boolean luckOnDefense, final boolean luckOnOther) {
        ensureCheckboxStatus("#luckOnEnemyHit", luckOnAttack);
        ensureCheckboxStatus("#luckOnSelfHit", luckOnDefense);
        ensureCheckboxStatus("#luckOnOther", luckOnOther);
    }

    private void ensureCheckboxStatus(final String selector, final boolean isChecked) {
        String extraAttrib;
        if (isChecked) {
            extraAttrib = ":not(:checked)";
        } else {
            extraAttrib = ":checked";
        }
        try {
            getDriver().findElement(By.cssSelector(selector + extraAttrib)).click();
        } catch (final NoSuchElementException exception) {
        } catch (final StaleElementReferenceException ex) {
            ensureCheckboxStatus(selector, isChecked);
        }
    }

    protected void doRandomRoll() {
        noJumpChoicesAvailable();
        click("[data-random='ff']");
    }

    protected void doAttributeCheck() {
        noJumpChoicesAvailable();
        click("[data-attribute-test='ff']");
    }

    protected void giveResponse(final String responseValue, final int elaspedTime) {
        noJumpChoicesAvailable();
        getEventFiringWebDriver().executeScript("$(\"#userInputForm\").append($(\"<input type='hidden' name='forcedTime' value='" + elaspedTime + "' />\"))");
        fill("#responseText", responseValue).sendKeys(Keys.RETURN);
    }

    protected void applyItem(final String itemId) {
        click("#inventory [data-item-id='" + itemId + "']");
    }

    protected void closeMarket() {
        click("#marketCommandFinish");
    }

    protected void selectPotion(final String itemId) {
        final Select potionSelection = new Select(getDriver().findElement(By.id("ffDefaultPotion")));
        potionSelection.selectByValue(itemId);
    }

    protected void marketing(final String purchaseSell, final String itemId) {
        noJumpChoicesAvailable();
        getDriver().findElement(By.id("marketFor" + purchaseSell)).findElement(By.cssSelector("[data-id='" + itemId + "']")).click();
        sleep(500);
    }

    protected void consumeItem(final String itemId) {
        waitAndFindElement("#inventory [data-item-id='" + itemId + "']").click();
    }

    protected void verifyAttributes(final String attributeName, final int attributeValue) {
        verifyAttributes(attributeName, String.valueOf(attributeValue));
    }

    protected void verifyAttributes(final String attributeName, final String expectedValue) {
        String actualValue = "";
        while (StringUtils.isEmpty(actualValue)) {
            actualValue = waitAndFindElement("[data-attribute-" + attributeName + "]").getText();
        }
        if (!expectedValue.equals(actualValue.trim())) {
            throw new IllegalStateException("Expected attribute '" + attributeName + "' to be '" + expectedValue + "', found '" + actualValue + "'.");
        }
    }
}
