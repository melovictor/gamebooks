package hu.zagor.gamebooks.selenium.raw.cyoa;

import hu.zagor.gamebooks.selenium.BasicSeleniumTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Selenium test for book VKM1.
 * @author Tamas_Szekeres
 */
@Test
public class HuTheCaveOfTimeST extends BasicSeleniumTest {

    @BeforeClass
    public void setUpClass() {
        startBook("990952001", "vkm1");
    }

    public void testPath102() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(0, 20);
        verifyPosition(0, 93);
        goToPosition(0, 93);
        verifyPosition(0, 31);
        verifyPosition(1, 32);
        goToPosition(1, 32);
        verifyPosition(0, 58);
        verifyPosition(1, 64);
        goToPosition(1, 64);
        verifyPosition(0, 65);
        goToPosition(0, 65);
        verifyPosition(0, 102);
        goToPosition(0, 102);
    }

    public void testPath62() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(0, 20);
        verifyPosition(0, 93);
        goToPosition(0, 93);
        verifyPosition(0, 31);
        verifyPosition(1, 32);
        goToPosition(1, 32);
        verifyPosition(0, 58);
        verifyPosition(1, 64);
        goToPosition(0, 58);
        verifyPosition(0, 62);
        verifyPosition(1, 63);
        goToPosition(0, 62);
    }

    public void testPath63() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(0, 20);
        verifyPosition(0, 93);
        goToPosition(0, 93);
        verifyPosition(0, 31);
        verifyPosition(1, 32);
        goToPosition(1, 32);
        verifyPosition(0, 58);
        verifyPosition(1, 64);
        goToPosition(0, 58);
        verifyPosition(0, 62);
        verifyPosition(1, 63);
        goToPosition(1, 63);
    }

    public void testPath31() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(0, 20);
        verifyPosition(0, 93);
        goToPosition(0, 93);
        verifyPosition(0, 31);
        verifyPosition(1, 32);
        goToPosition(0, 31);
    }

    public void testPath113() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(1, 61);
        verifyPosition(0, 104);
        verifyPosition(1, 106);
        goToPosition(1, 106);
        verifyPosition(0, 111);
        verifyPosition(1, 113);
        goToPosition(1, 113);
    }

    public void testPath112() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(1, 61);
        verifyPosition(0, 104);
        verifyPosition(1, 106);
        goToPosition(1, 106);
        verifyPosition(0, 111);
        verifyPosition(1, 113);
        goToPosition(0, 111);
        verifyPosition(0, 112);
        goToPosition(0, 112);
    }

    public void testPath110() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(1, 61);
        verifyPosition(0, 104);
        verifyPosition(1, 106);
        goToPosition(0, 104);
        verifyPosition(0, 107);
        verifyPosition(1, 109);
        goToPosition(1, 109);
        verifyPosition(0, 110);
        goToPosition(0, 110);
    }

    public void testPath107() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(1, 61);
        verifyPosition(0, 104);
        verifyPosition(1, 106);
        goToPosition(0, 104);
        verifyPosition(0, 107);
        verifyPosition(1, 109);
        goToPosition(0, 107);
    }

    public void testPath76() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(2, 21);
        verifyPosition(0, 33);
        verifyPosition(1, 35);
        goToPosition(1, 35);
        verifyPosition(0, 76);
        verifyPosition(1, 80);
        goToPosition(0, 76);
    }

    public void testPath80() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(2, 21);
        verifyPosition(0, 33);
        verifyPosition(1, 35);
        goToPosition(1, 35);
        verifyPosition(0, 76);
        verifyPosition(1, 80);
        goToPosition(1, 80);
    }

    public void testPath90() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(2, 21);
        verifyPosition(0, 33);
        verifyPosition(1, 35);
        goToPosition(0, 33);
        verifyPosition(0, 68);
        verifyPosition(1, 79);
        goToPosition(1, 79);
        verifyPosition(0, 86);
        verifyPosition(1, 88);
        goToPosition(1, 88);
        verifyPosition(0, 89);
        goToPosition(0, 89);
        verifyPosition(0, 90);
        verifyPosition(1, 91);
        goToPosition(0, 90);
    }

    public void testPath91() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(2, 21);
        verifyPosition(0, 33);
        verifyPosition(1, 35);
        goToPosition(0, 33);
        verifyPosition(0, 68);
        verifyPosition(1, 79);
        goToPosition(1, 79);
        verifyPosition(0, 86);
        verifyPosition(1, 88);
        goToPosition(1, 88);
        verifyPosition(0, 89);
        goToPosition(0, 89);
        verifyPosition(0, 90);
        verifyPosition(1, 91);
        goToPosition(1, 91);
    }

    public void testPath86() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(2, 21);
        verifyPosition(0, 33);
        verifyPosition(1, 35);
        goToPosition(0, 33);
        verifyPosition(0, 68);
        verifyPosition(1, 79);
        goToPosition(1, 79);
        verifyPosition(0, 86);
        verifyPosition(1, 88);
        goToPosition(0, 86);
    }

    public void testPath68() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 20);
        verifyPosition(1, 61);
        verifyPosition(2, 21);
        goToPosition(2, 21);
        verifyPosition(0, 33);
        verifyPosition(1, 35);
        goToPosition(0, 33);
        verifyPosition(0, 68);
        verifyPosition(1, 79);
        goToPosition(0, 68);
    }

    public void testPath97() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(1, 18);
        verifyPosition(0, 29);
        verifyPosition(1, 30);
        goToPosition(1, 30);
        verifyPosition(0, 54);
        verifyPosition(1, 92);
        goToPosition(0, 54);
        verifyPosition(0, 55);
        goToPosition(0, 55);
        verifyPosition(0, 94);
        verifyPosition(1, 98);
        verifyPosition(2, 101);
        goToPosition(0, 94);
        verifyPosition(0, 96);
        verifyPosition(1, 100);
        goToPosition(0, 96);
        verifyPosition(0, 97);
        goToPosition(0, 97);
    }

    public void testPath100() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(1, 18);
        verifyPosition(0, 29);
        verifyPosition(1, 30);
        goToPosition(1, 30);
        verifyPosition(0, 54);
        verifyPosition(1, 92);
        goToPosition(0, 54);
        verifyPosition(0, 55);
        goToPosition(0, 55);
        verifyPosition(0, 94);
        verifyPosition(1, 98);
        verifyPosition(2, 101);
        goToPosition(0, 94);
        verifyPosition(0, 96);
        verifyPosition(1, 100);
        goToPosition(1, 100);
    }

    public void testPath101() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(1, 18);
        verifyPosition(0, 29);
        verifyPosition(1, 30);
        goToPosition(1, 30);
        verifyPosition(0, 54);
        verifyPosition(1, 92);
        goToPosition(0, 54);
        verifyPosition(0, 55);
        goToPosition(0, 55);
        verifyPosition(0, 94);
        verifyPosition(1, 98);
        verifyPosition(2, 101);
        goToPosition(2, 101);
    }

    public void testPath98() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(1, 18);
        verifyPosition(0, 29);
        verifyPosition(1, 30);
        goToPosition(1, 30);
        verifyPosition(0, 54);
        verifyPosition(1, 92);
        goToPosition(0, 54);
        verifyPosition(0, 55);
        goToPosition(0, 55);
        verifyPosition(0, 94);
        verifyPosition(1, 98);
        verifyPosition(2, 101);
        goToPosition(1, 98);
    }

    public void testPath60() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(1, 18);
        verifyPosition(0, 29);
        verifyPosition(1, 30);
        goToPosition(1, 30);
        verifyPosition(0, 54);
        verifyPosition(1, 92);
        goToPosition(1, 92);
        verifyPosition(0, 56);
        goToPosition(0, 56);
        verifyPosition(0, 57);
        verifyPosition(1, 60);
        goToPosition(1, 60);
    }

    public void testPath57() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(1, 18);
        verifyPosition(0, 29);
        verifyPosition(1, 30);
        goToPosition(1, 30);
        verifyPosition(0, 54);
        verifyPosition(1, 92);
        goToPosition(1, 92);
        verifyPosition(0, 56);
        goToPosition(0, 56);
        verifyPosition(0, 57);
        verifyPosition(1, 60);
        goToPosition(0, 57);
    }

    public void testPath52() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(1, 18);
        verifyPosition(0, 29);
        verifyPosition(1, 30);
        goToPosition(0, 29);
        verifyPosition(1, 53);
        goToPosition(0, 52);
    }

    public void testPath53() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(1, 18);
        verifyPosition(0, 29);
        verifyPosition(1, 30);
        goToPosition(0, 29);
        verifyPosition(0, 52);
        goToPosition(1, 53);
    }

    public void testPath43A() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(0, 17);
        verifyPosition(0, 26);
        verifyPosition(1, 28);
        goToPosition(0, 26);
        verifyPosition(0, 46);
        verifyPosition(1, 47);
        goToPosition(1, 47);
        verifyPosition(0, 49);
        verifyPosition(1, 50);
        goToPosition(0, 49);
        verifyPosition(0, 25);
        goToPosition(0, 25);
        verifyPosition(0, 43);
        verifyPosition(1, 41);
        verifyPosition(2, 44);
        verifyPosition(3, 45);
        goToPosition(0, 43);
    }

    public void testPath44() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(0, 17);
        verifyPosition(0, 26);
        verifyPosition(1, 28);
        goToPosition(0, 26);
        verifyPosition(0, 46);
        verifyPosition(1, 47);
        goToPosition(1, 47);
        verifyPosition(0, 49);
        verifyPosition(1, 50);
        goToPosition(0, 49);
        verifyPosition(0, 25);
        goToPosition(0, 25);
        verifyPosition(0, 43);
        verifyPosition(1, 41);
        verifyPosition(2, 44);
        verifyPosition(3, 45);
        goToPosition(2, 44);
    }

    public void testPath41() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(0, 17);
        verifyPosition(0, 26);
        verifyPosition(1, 28);
        goToPosition(0, 26);
        verifyPosition(0, 46);
        verifyPosition(1, 47);
        goToPosition(1, 47);
        verifyPosition(0, 49);
        verifyPosition(1, 50);
        goToPosition(0, 49);
        verifyPosition(0, 25);
        goToPosition(0, 25);
        verifyPosition(0, 43);
        verifyPosition(1, 41);
        verifyPosition(2, 44);
        verifyPosition(3, 45);
        goToPosition(1, 41);
    }

    public void testPath45() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(0, 17);
        verifyPosition(0, 26);
        verifyPosition(1, 28);
        goToPosition(0, 26);
        verifyPosition(0, 46);
        verifyPosition(1, 47);
        goToPosition(1, 47);
        verifyPosition(0, 49);
        verifyPosition(1, 50);
        goToPosition(0, 49);
        verifyPosition(0, 25);
        goToPosition(0, 25);
        verifyPosition(0, 43);
        verifyPosition(1, 41);
        verifyPosition(2, 44);
        verifyPosition(3, 45);
        goToPosition(3, 45);
    }

    public void testPath81() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(0, 17);
        verifyPosition(0, 26);
        verifyPosition(1, 28);
        goToPosition(0, 26);
        verifyPosition(0, 46);
        verifyPosition(1, 47);
        goToPosition(1, 47);
        verifyPosition(0, 49);
        verifyPosition(1, 50);
        goToPosition(1, 50);
        goToPosition(0, 81);
    }

    public void testPath46() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(0, 17);
        verifyPosition(0, 26);
        verifyPosition(1, 28);
        goToPosition(0, 26);
        verifyPosition(0, 46);
        verifyPosition(1, 47);
        goToPosition(0, 46);
    }

    public void testPath51() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(0, 4);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 17);
        verifyPosition(1, 18);
        goToPosition(0, 17);
        verifyPosition(0, 26);
        verifyPosition(1, 28);
        goToPosition(1, 28);
        goToPosition(0, 51);
    }

    public void testPath43B() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(1, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 25);
        goToPosition(1, 25);
        verifyPosition(0, 43);
        verifyPosition(1, 41);
        verifyPosition(2, 44);
        verifyPosition(3, 45);
        goToPosition(0, 43);
    }

    public void testPath82() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(1, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 25);
        goToPosition(0, 24);
        verifyPosition(0, 38);
        verifyPosition(1, 40);
        goToPosition(0, 38);
        verifyPosition(0, 82);
        verifyPosition(1, 87);
        goToPosition(0, 82);
    }

    public void testPath87() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(1, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 25);
        goToPosition(0, 24);
        verifyPosition(0, 38);
        verifyPosition(1, 40);
        goToPosition(0, 38);
        verifyPosition(0, 82);
        verifyPosition(1, 87);
        goToPosition(1, 87);
    }

    public void testPath85() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(1, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 25);
        goToPosition(0, 24);
        verifyPosition(0, 38);
        verifyPosition(1, 40);
        goToPosition(1, 40);
        verifyPosition(0, 115);
        verifyPosition(1, 83);
        goToPosition(1, 83);
        verifyPosition(0, 85);
        verifyPosition(1, 84);
        goToPosition(0, 85);
    }

    public void testPath84() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(1, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 25);
        goToPosition(0, 24);
        verifyPosition(0, 38);
        verifyPosition(1, 40);
        goToPosition(1, 40);
        verifyPosition(0, 115);
        verifyPosition(1, 83);
        goToPosition(1, 83);
        verifyPosition(0, 85);
        verifyPosition(1, 84);
        goToPosition(1, 84);
    }

    public void testPath115() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(1, 16);
        verifyPosition(0, 24);
        verifyPosition(1, 25);
        goToPosition(0, 24);
        verifyPosition(0, 38);
        verifyPosition(1, 40);
        goToPosition(1, 40);
        verifyPosition(0, 115);
        verifyPosition(1, 83);
        goToPosition(0, 115);
    }

    public void testPath72() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(0, 6);
        verifyPosition(0, 22);
        verifyPosition(1, 114);
        goToPosition(0, 22);
        verifyPosition(0, 36);
        verifyPosition(1, 37);
        goToPosition(0, 36);
        verifyPosition(0, 11);
        goToPosition(0, 11);
        verifyPosition(0, 12);
        verifyPosition(1, 13);
        goToPosition(0, 12);
        verifyPosition(0, 66);
        verifyPosition(1, 78);
        goToPosition(0, 66);
        verifyPosition(0, 70);
        verifyPosition(1, 74);
        goToPosition(0, 70);
        verifyPosition(0, 71);
        goToPosition(0, 71);
        verifyPosition(0, 72);
        verifyPosition(1, 103);
        goToPosition(0, 72);
    }

    public void testPath103() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(0, 6);
        verifyPosition(0, 22);
        verifyPosition(1, 114);
        goToPosition(0, 22);
        verifyPosition(0, 36);
        verifyPosition(1, 37);
        goToPosition(0, 36);
        verifyPosition(0, 11);
        goToPosition(0, 11);
        verifyPosition(0, 12);
        verifyPosition(1, 13);
        goToPosition(0, 12);
        verifyPosition(0, 66);
        verifyPosition(1, 78);
        goToPosition(0, 66);
        verifyPosition(0, 70);
        verifyPosition(1, 74);
        goToPosition(0, 70);
        verifyPosition(0, 71);
        goToPosition(0, 71);
        verifyPosition(0, 72);
        verifyPosition(1, 103);
        goToPosition(1, 103);
    }

    public void testPath74() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(0, 6);
        verifyPosition(0, 22);
        verifyPosition(1, 114);
        goToPosition(0, 22);
        verifyPosition(0, 36);
        verifyPosition(1, 37);
        goToPosition(0, 36);
        verifyPosition(0, 11);
        goToPosition(0, 11);
        verifyPosition(0, 12);
        verifyPosition(1, 13);
        goToPosition(0, 12);
        verifyPosition(0, 66);
        verifyPosition(1, 78);
        goToPosition(0, 66);
        verifyPosition(0, 70);
        verifyPosition(1, 74);
        goToPosition(1, 74);
    }

    public void testPath78() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(0, 6);
        verifyPosition(0, 22);
        verifyPosition(1, 114);
        goToPosition(0, 22);
        verifyPosition(0, 36);
        verifyPosition(1, 37);
        goToPosition(0, 36);
        verifyPosition(0, 11);
        goToPosition(0, 11);
        verifyPosition(0, 12);
        verifyPosition(1, 13);
        goToPosition(0, 12);
        verifyPosition(0, 66);
        verifyPosition(1, 78);
        goToPosition(1, 78);
    }

    public void testPath114() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(0, 6);
        verifyPosition(0, 22);
        verifyPosition(1, 114);
        goToPosition(1, 114);
    }

    public void testPath14() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(0, 6);
        verifyPosition(0, 22);
        verifyPosition(1, 114);
        goToPosition(0, 22);
        verifyPosition(0, 36);
        verifyPosition(1, 37);
        goToPosition(1, 37);
        verifyPosition(0, 11);
        goToPosition(0, 11);
        verifyPosition(0, 12);
        verifyPosition(1, 13);
        goToPosition(1, 13);
        verifyPosition(0, 14);
        verifyPosition(1, 15);
        goToPosition(0, 14);
    }

    public void testPath15() {
        goToPosition(0, 2);
        verifyPosition(0, 4);
        verifyPosition(1, 5);
        goToPosition(1, 5);
        verifyPosition(0, 6);
        verifyPosition(1, 16);
        goToPosition(0, 6);
        verifyPosition(0, 22);
        verifyPosition(1, 114);
        goToPosition(0, 22);
        verifyPosition(0, 36);
        verifyPosition(1, 37);
        goToPosition(1, 37);
        verifyPosition(0, 11);
        goToPosition(0, 11);
        verifyPosition(0, 12);
        verifyPosition(1, 13);
        goToPosition(1, 13);
        verifyPosition(0, 14);
        verifyPosition(1, 15);
        goToPosition(1, 15);
    }

}
