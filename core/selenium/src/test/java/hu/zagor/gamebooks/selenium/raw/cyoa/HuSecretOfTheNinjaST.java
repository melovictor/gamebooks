package hu.zagor.gamebooks.selenium.raw.cyoa;

import hu.zagor.gamebooks.selenium.BasicSeleniumTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Selenium test for book VKM4.
 * @author Tamas_Szekeres
 */
@Test
public class HuSecretOfTheNinjaST extends BasicSeleniumTest {

    @BeforeClass
    public void setUpClass() {
        startBook("990952004", "vkm4");
    }

    public void testPath80() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(0, 75);
        verifyPosition(0, 88);
        verifyPosition(1, 100);
        goToPosition(0, 88);
        verifyPosition(0, 90);
        verifyPosition(1, 71);
        goToPosition(0, 90);
        verifyPosition(0, 77);
        verifyPosition(1, 26);
        goToPosition(1, 26);
        verifyPosition(0, 98);
        verifyPosition(1, 83);
        goToPosition(1, 83);
        verifyPosition(0, 80);
        verifyPosition(1, 116);
        goToPosition(0, 80);
    }

    public void testPath116() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(0, 75);
        verifyPosition(0, 88);
        verifyPosition(1, 100);
        goToPosition(0, 88);
        verifyPosition(0, 90);
        verifyPosition(1, 71);
        goToPosition(0, 90);
        verifyPosition(0, 77);
        verifyPosition(1, 26);
        goToPosition(1, 26);
        verifyPosition(0, 98);
        verifyPosition(1, 83);
        goToPosition(1, 83);
        verifyPosition(0, 80);
        verifyPosition(1, 116);
        goToPosition(1, 116);
    }

    public void testPath98() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(0, 75);
        verifyPosition(0, 88);
        verifyPosition(1, 100);
        goToPosition(0, 88);
        verifyPosition(0, 90);
        verifyPosition(1, 71);
        goToPosition(0, 90);
        verifyPosition(0, 77);
        verifyPosition(1, 26);
        goToPosition(1, 26);
        verifyPosition(0, 98);
        verifyPosition(1, 83);
        goToPosition(0, 98);
    }

    public void testPath77() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(0, 75);
        verifyPosition(0, 88);
        verifyPosition(1, 100);
        goToPosition(0, 88);
        verifyPosition(0, 90);
        verifyPosition(1, 71);
        goToPosition(0, 90);
        verifyPosition(0, 77);
        verifyPosition(1, 26);
        goToPosition(0, 77);
    }

    public void testPath71() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(0, 75);
        verifyPosition(0, 88);
        verifyPosition(1, 100);
        goToPosition(0, 88);
        verifyPosition(0, 90);
        verifyPosition(1, 71);
        goToPosition(1, 71);
    }

    public void testPath104() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(0, 75);
        verifyPosition(0, 88);
        verifyPosition(1, 100);
        goToPosition(1, 100);
        verifyPosition(0, 84);
        verifyPosition(1, 86);
        goToPosition(1, 86);
        verifyPosition(0, 104);
        verifyPosition(1, 68);
        goToPosition(0, 104);
    }

    public void testPath68() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(0, 75);
        verifyPosition(0, 88);
        verifyPosition(1, 100);
        goToPosition(1, 100);
        verifyPosition(0, 84);
        verifyPosition(1, 86);
        goToPosition(1, 86);
        verifyPosition(0, 104);
        verifyPosition(1, 68);
        goToPosition(1, 68);
    }

    public void testPath84() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(0, 75);
        verifyPosition(0, 88);
        verifyPosition(1, 100);
        goToPosition(1, 100);
        verifyPosition(0, 84);
        verifyPosition(1, 86);
        goToPosition(0, 84);
    }

    public void testPath72() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(1, 33);
        verifyPosition(0, 52);
        verifyPosition(1, 54);
        goToPosition(1, 54);
        verifyPosition(0, 114);
        verifyPosition(1, 57);
        goToPosition(1, 57);
        verifyPosition(0, 72);
        verifyPosition(1, 48);
        goToPosition(0, 72);
    }

    public void testPath48() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(1, 33);
        verifyPosition(0, 52);
        verifyPosition(1, 54);
        goToPosition(1, 54);
        verifyPosition(0, 114);
        verifyPosition(1, 57);
        goToPosition(1, 57);
        verifyPosition(0, 72);
        verifyPosition(1, 48);
        goToPosition(1, 48);
    }

    public void testPath114() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(1, 33);
        verifyPosition(0, 52);
        verifyPosition(1, 54);
        goToPosition(1, 54);
        verifyPosition(0, 114);
        verifyPosition(1, 57);
        goToPosition(0, 114);
    }

    public void testPath52() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(1, 33);
        verifyPosition(0, 52);
        verifyPosition(1, 54);
        goToPosition(0, 52);
    }

    public void testPath108() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(1, 108);
    }

    public void testPath82() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(1, 82);
    }

    public void testPath74() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(0, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 50);
        goToPosition(0, 24);
        verifyPosition(0, 25);
        verifyPosition(1, 102);
        goToPosition(0, 25);
        verifyPosition(0, 28);
        verifyPosition(1, 111);
        goToPosition(0, 28);
        verifyPosition(0, 94);
        verifyPosition(1, 115);
        goToPosition(0, 94);
        verifyPosition(0, 74);
        verifyPosition(1, 13);
        goToPosition(0, 74);
    }

    public void testPath13() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(0, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 50);
        goToPosition(0, 24);
        verifyPosition(0, 25);
        verifyPosition(1, 102);
        goToPosition(0, 25);
        verifyPosition(0, 28);
        verifyPosition(1, 111);
        goToPosition(0, 28);
        verifyPosition(0, 94);
        verifyPosition(1, 115);
        goToPosition(0, 94);
        verifyPosition(0, 74);
        verifyPosition(1, 13);
        goToPosition(1, 13);
    }

    public void testPath115() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(0, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 50);
        goToPosition(0, 24);
        verifyPosition(0, 25);
        verifyPosition(1, 102);
        goToPosition(0, 25);
        verifyPosition(0, 28);
        verifyPosition(1, 111);
        goToPosition(0, 28);
        verifyPosition(0, 94);
        verifyPosition(1, 115);
        goToPosition(1, 115);
    }

    public void testPath111() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(0, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 50);
        goToPosition(0, 24);
        verifyPosition(0, 25);
        verifyPosition(1, 102);
        goToPosition(0, 25);
        verifyPosition(0, 28);
        verifyPosition(1, 111);
        goToPosition(1, 111);
    }

    public void testPath102() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(0, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 50);
        goToPosition(0, 24);
        verifyPosition(0, 25);
        verifyPosition(1, 102);
        goToPosition(1, 102);
    }

    public void testPath44() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(0, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 50);
        goToPosition(1, 50);
        verifyPosition(0, 34);
        verifyPosition(1, 109);
        goToPosition(0, 34);
        verifyPosition(0, 44);
        verifyPosition(1, 53);
        goToPosition(0, 44);
    }

    public void testPath53() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(0, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 50);
        goToPosition(1, 50);
        verifyPosition(0, 34);
        verifyPosition(1, 109);
        goToPosition(0, 34);
        verifyPosition(0, 44);
        verifyPosition(1, 53);
        goToPosition(1, 53);
    }

    public void testPath109() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(1, 41);
        verifyPosition(0, 14);
        verifyPosition(1, 82);
        goToPosition(0, 14);
        verifyPosition(0, 23);
        verifyPosition(1, 108);
        goToPosition(0, 23);
        verifyPosition(0, 16);
        verifyPosition(1, 33);
        goToPosition(0, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 50);
        goToPosition(1, 50);
        verifyPosition(0, 34);
        verifyPosition(1, 109);
        goToPosition(1, 109);
    }

    public void testPath65() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(0, 63);
        verifyPosition(0, 36);
        verifyPosition(1, 61);
        goToPosition(1, 61);
        verifyPosition(0, 78);
        verifyPosition(1, 69);
        goToPosition(0, 78);
        verifyPosition(0, 65);
        verifyPosition(1, 40);
        goToPosition(0, 65);
    }

    public void testPath40() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(0, 63);
        verifyPosition(0, 36);
        verifyPosition(1, 61);
        goToPosition(1, 61);
        verifyPosition(0, 78);
        verifyPosition(1, 69);
        goToPosition(0, 78);
        verifyPosition(0, 65);
        verifyPosition(1, 40);
        goToPosition(1, 40);
    }

    public void testPath69() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(0, 63);
        verifyPosition(0, 36);
        verifyPosition(1, 61);
        goToPosition(1, 61);
        verifyPosition(0, 78);
        verifyPosition(1, 69);
        goToPosition(1, 69);
    }

    public void testPath112() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(0, 63);
        verifyPosition(0, 36);
        verifyPosition(1, 61);
        goToPosition(0, 36);
        verifyPosition(0, 30);
        verifyPosition(1, 66);
        goToPosition(1, 66);
        verifyPosition(0, 60);
        verifyPosition(1, 91);
        goToPosition(1, 91);
        verifyPosition(0, 112);
        verifyPosition(1, 107);
        goToPosition(0, 112);
    }

    public void testPath107() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(0, 63);
        verifyPosition(0, 36);
        verifyPosition(1, 61);
        goToPosition(0, 36);
        verifyPosition(0, 30);
        verifyPosition(1, 66);
        goToPosition(1, 66);
        verifyPosition(0, 60);
        verifyPosition(1, 91);
        goToPosition(1, 91);
        verifyPosition(0, 112);
        verifyPosition(1, 107);
        goToPosition(1, 107);
    }

    public void testPath60() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(0, 63);
        verifyPosition(0, 36);
        verifyPosition(1, 61);
        goToPosition(0, 36);
        verifyPosition(0, 30);
        verifyPosition(1, 66);
        goToPosition(1, 66);
        verifyPosition(0, 60);
        verifyPosition(1, 91);
        goToPosition(0, 60);
    }

    public void testPath30() {
        goToPosition(1, 2);
        verifyPosition(0, 3);
        goToPosition(0, 3);
        verifyPosition(0, 75);
        verifyPosition(1, 20);
        goToPosition(1, 20);
        verifyPosition(0, 63);
        verifyPosition(1, 41);
        goToPosition(0, 63);
        verifyPosition(0, 36);
        verifyPosition(1, 61);
        goToPosition(0, 36);
        verifyPosition(0, 30);
        verifyPosition(1, 66);
        goToPosition(0, 30);
    }
}
