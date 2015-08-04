package hu.zagor.gamebooks.selenium.ff.ff;

import hu.zagor.gamebooks.selenium.ff.BasicFfSeleniumTest;

import java.util.Arrays;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Selenium test for book FH6.
 * @author Tamas_Szekeres
 */
@Test
public class HuRebelPlanetST extends BasicFfSeleniumTest {

    @BeforeClass
    public void setUpClass() {
        startBook("990133006", "fh6");
    }

    public void testFailureByClubBeingDismantled() {
        setUpRandomRolls(Arrays.asList(4, 4, 2, 2, 5, 3, 1, 4, 4, 4, 5, 6, 6, 1, 1, 4, 6, 6, 6, 2, 6, 6, 3, 3, 3, 1, 3, 5, 2, 1, 6, 6, 3, 6, 3, 2, 5, 6, 5, 1, 1, 6, 5,
            1, 1, 1, 4, 3, 3, 2, 3, 2, 3, 4, 4, 1, 4, 1, 3, 4, 3));
        goToPosition("0", "background");
        verifyPosition(0, 1);
        goToPosition(0, 1);
        verifyPosition(0, 48);
        verifyPosition(1, 398);
        goToPosition(0, 48);
        verifyPosition(0, 164);
        goToPosition(0, 164);
        verifyPosition(0, 246);
        verifyPosition(1, 17);
        goToPosition(1, 17);
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        verifyPosition(1, 291);
        goToPosition(1, 291);
        verifyPosition(0, 87);
        verifyPosition(1, 276);
        goToPosition(1, 276);
        verifyPosition(0, 180);
        verifyPosition(1, 386);
        goToPosition(1, 386);
        verifyPosition(0, 299);
        goToPosition(0, 299);
        verifyPosition(0, 176);
        verifyPosition(1, 196);
        goToPosition(1, 196);
        verifyPosition(0, 29);
        verifyPosition(1, 188);
        goToPosition(1, 188);
        verifyPosition(0, 29);
        verifyPosition(1, 172);
        goToPosition(0, 29);
        verifyPosition(0, 354);
        verifyPosition(1, 60);
        goToPosition(1, 60);
        verifyPosition(0, 99);
        goToPosition(0, 99);
        verifyPosition(0, 152);
        verifyPosition(1, 382);
        goToPosition(1, 382);
        marketing("Sale", "3002");
        marketing("Sale", "3005");
        closeMarket();
        giveResponse("5", 9058);
        goToPosition(0, 5);
        verifyPosition(0, 204);
        goToPosition(0, 204);
        verifyPosition(0, 287);
        verifyPosition(1, 240);
        verifyPosition(2, 45);
        verifyPosition(3, 326);
        goToPosition(2, 45);
        verifyPosition(1, 366);
        goToPosition(0, 390);
    }

    public void testFailureByCopBeatingUs() {
        setUpRandomRolls(Arrays.asList(3, 2, 3, 3, 5, 3, 4, 3, 1, 2, 3, 2, 4, 6, 4, 2, 1, 4, 6, 3, 4));
        goToPosition("0", "background");
        verifyPosition(0, 1);
        goToPosition(0, 1);
        verifyPosition(0, 48);
        verifyPosition(1, 398);
        goToPosition(0, 48);
        verifyPosition(0, 164);
        goToPosition(0, 164);
        verifyPosition(0, 246);
        verifyPosition(1, 17);
        goToPosition(1, 17);
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        verifyPosition(0, 328);
        goToPosition(0, 328);
    }

    public void testPushedWrongButtonForSecretPolice() {
        setUpRandomRolls(Arrays.asList(3, 6, 4, 4, 2, 2, 4, 4, 3, 2, 6, 3, 4, 4, 2, 4, 6, 1, 4, 4, 3, 1, 2, 5, 1, 5, 3, 6, 6, 1, 1, 6, 4, 4, 2, 4, 3, 1, 3));
        goToPosition("0", "background");
        verifyPosition(0, 1);
        goToPosition(0, 1);
        verifyPosition(0, 48);
        verifyPosition(1, 398);
        goToPosition(0, 48);
        verifyPosition(0, 164);
        goToPosition(0, 164);
        verifyPosition(0, 246);
        verifyPosition(1, 17);
        goToPosition(1, 17);
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        prepareLuckSettings(false, false, false);
        attackEnemy("1");
        verifyPosition(1, 291);
        goToPosition(1, 291);
        verifyPosition(0, 87);
        verifyPosition(1, 276);
        goToPosition(0, 87);
        verifyPosition(0, 276);
        goToPosition(0, 276);
        verifyPosition(0, 180);
        verifyPosition(1, 386);
        goToPosition(0, 180);
    }

    public void testWentAgainstRebelsCode() {
        setUpRandomRolls(Arrays.asList(3, 2, 4, 4, 5, 3, 2, 1, 1, 3, 1, 1, 1, 1, 4, 3, 2, 3, 4, 4, 2, 1, 3, 5, 2, 3, 3, 5, 6, 3, 1, 3, 3, 5, 6, 5, 1, 3, 5, 3, 2, 4, 4,
            5, 4, 6, 1, 4, 3, 1, 4, 5, 3, 4, 1, 5, 5, 6, 1, 5, 6, 4, 6, 1, 1, 2, 1, 4, 1, 3, 3, 1, 3, 1, 2, 2, 2, 1, 2, 3));
        goToPosition("0", "background");
        verifyPosition(0, 1);
        goToPosition(0, 1);
        verifyPosition(0, 48);
        verifyPosition(1, 398);
        goToPosition(0, 48);
        verifyPosition(0, 164);
        goToPosition(0, 164);
        verifyPosition(0, 246);
        verifyPosition(1, 17);
        goToPosition(0, 246);
        doAttributeCheck();
        verifyPosition(0, 144);
        goToPosition(0, 144);
        verifyPosition(0, 370);
        goToPosition(0, 370);
        verifyPosition(0, 299);
        goToPosition(0, 299);
        verifyPosition(0, 176);
        verifyPosition(1, 196);
        goToPosition(0, 176);
        verifyPosition(0, 196);
        goToPosition(0, 196);
        verifyPosition(0, 29);
        verifyPosition(1, 188);
        goToPosition(0, 29);
        verifyPosition(0, 354);
        verifyPosition(1, 60);
        goToPosition(0, 354);
        verifyPosition(0, 136);
        verifyPosition(1, 60);
        goToPosition(0, 136);
        prepareLuckSettings(false, false, false);
        attackEnemy("2");
        prepareLuckSettings(false, false, false);
        attackEnemy("2");
        prepareLuckSettings(false, false, false);
        attackEnemy("2");
        prepareLuckSettings(false, false, false);
        attackEnemy("2");
        prepareLuckSettings(false, false, false);
        attackEnemy("2");
        prepareLuckSettings(false, false, false);
        attackEnemy("2");
        prepareLuckSettings(false, false, false);
        attackEnemy("2");
        prepareLuckSettings(false, false, false);
        attackEnemy("2");
        verifyPosition(0, 268);
        goToPosition(0, 268);
        verifyPosition(0, 314);
        verifyPosition(1, 394);
        goToPosition(0, 314);
        verifyPosition(0, 99);
        goToPosition(0, 99);
        verifyPosition(0, 152);
        verifyPosition(1, 382);
        goToPosition(0, 152);
        verifyPosition(0, 335);
        verifyPosition(1, 260);
        goToPosition(0, 335);
        verifyPosition(0, 382);
        goToPosition(0, 382);
        marketing("Sale", "3005");
        closeMarket();
        giveResponse("", 2001);
        goToPosition(1, 44);
        verifyPosition(0, 204);
        goToPosition(0, 204);
        verifyPosition(0, 287);
        verifyPosition(1, 240);
        verifyPosition(2, 45);
        verifyPosition(3, 326);
        goToPosition(0, 287);
        verifyPosition(0, 95);
        verifyPosition(1, 366);
        goToPosition(0, 95);
        verifyPosition(0, 156);
        verifyPosition(1, 272);
        goToPosition(1, 272);
        verifyPosition(0, 207);
        goToPosition(0, 207);
        verifyPosition(0, 168);
        verifyPosition(1, 105);
        goToPosition(0, 168);
        verifyPosition(0, 68);
        verifyPosition(1, 274);
        goToPosition(0, 68);
    }
}
