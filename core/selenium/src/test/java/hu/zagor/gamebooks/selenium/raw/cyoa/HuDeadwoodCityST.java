package hu.zagor.gamebooks.selenium.raw.cyoa;

import hu.zagor.gamebooks.selenium.BasicSeleniumTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Selenium test for book VKM5.
 * @author Tamas_Szekeres
 */
@Test
public class HuDeadwoodCityST extends BasicSeleniumTest {

    @BeforeClass
    public void setUpClass() {
        startBook("990952005", "vkm5");
    }

    public void testPath106() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 18);
        verifyPosition(1, 19);
        goToPosition(1, 19);
        verifyPosition(0, 37);
        verifyPosition(1, 38);
        goToPosition(1, 38);
        verifyPosition(0, 64);
        verifyPosition(1, 65);
        goToPosition(1, 65);
        verifyPosition(0, 106);
        verifyPosition(1, 107);
        goToPosition(0, 106);
    }

    public void testPath107() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 18);
        verifyPosition(1, 19);
        goToPosition(1, 19);
        verifyPosition(0, 37);
        verifyPosition(1, 38);
        goToPosition(1, 38);
        verifyPosition(0, 64);
        verifyPosition(1, 65);
        goToPosition(1, 65);
        verifyPosition(0, 106);
        verifyPosition(1, 107);
        goToPosition(1, 107);
    }

    public void testPath64() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(1, 17);
        verifyPosition(0, 12);
        goToPosition(0, 12);
        verifyPosition(0, 22);
        verifyPosition(1, 24);
        goToPosition(1, 24);
        verifyPosition(0, 44);
        verifyPosition(1, 45);
        goToPosition(1, 45);
        verifyPosition(0, 15);
        goToPosition(0, 15);
        verifyPosition(0, 28);
        verifyPosition(1, 30);
        goToPosition(1, 30);
        verifyPosition(0, 54);
        verifyPosition(1, 55);
        goToPosition(1, 55);
        verifyPosition(0, 41);
        verifyPosition(1, 86);
        goToPosition(0, 41);
        verifyPosition(0, 75);
        verifyPosition(1, 76);
        goToPosition(0, 75);
        verifyPosition(0, 108);
        verifyPosition(1, 109);
        goToPosition(1, 109);
        verifyPosition(0, 38);
        verifyPosition(1, 112);
        goToPosition(0, 38);
        verifyPosition(0, 64);
        verifyPosition(1, 65);
        goToPosition(0, 64);
    }

    public void testPath37() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 18);
        verifyPosition(1, 19);
        goToPosition(0, 18);
        verifyPosition(0, 34);
        verifyPosition(1, 36);
        goToPosition(1, 36);
        verifyPosition(0, 92);
        verifyPosition(1, 94);
        goToPosition(1, 94);
        verifyPosition(0, 102);
        verifyPosition(1, 104);
        goToPosition(0, 102);
        verifyPosition(0, 19);
        goToPosition(0, 19);
        verifyPosition(0, 37);
        verifyPosition(1, 38);
        goToPosition(0, 37);
    }

    public void testPath60() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 18);
        verifyPosition(1, 19);
        goToPosition(0, 18);
        verifyPosition(0, 34);
        verifyPosition(1, 36);
        goToPosition(0, 34);
        verifyPosition(0, 60);
        verifyPosition(1, 62);
        verifyPosition(2, 63);
        goToPosition(0, 60);
    }

    public void testPath62() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 18);
        verifyPosition(1, 19);
        goToPosition(0, 18);
        verifyPosition(0, 34);
        verifyPosition(1, 36);
        goToPosition(0, 34);
        verifyPosition(0, 60);
        verifyPosition(1, 62);
        verifyPosition(2, 63);
        goToPosition(1, 62);
    }

    public void testPath63() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 18);
        verifyPosition(1, 19);
        goToPosition(0, 18);
        verifyPosition(0, 34);
        verifyPosition(1, 36);
        goToPosition(0, 34);
        verifyPosition(0, 60);
        verifyPosition(1, 62);
        verifyPosition(2, 63);
        goToPosition(2, 63);
    }

    public void testPath92() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(1, 17);
        verifyPosition(0, 12);
        goToPosition(0, 12);
        verifyPosition(0, 22);
        verifyPosition(1, 24);
        goToPosition(1, 24);
        verifyPosition(0, 44);
        verifyPosition(1, 45);
        goToPosition(0, 44);
        verifyPosition(0, 25);
        verifyPosition(1, 71);
        verifyPosition(2, 72);
        goToPosition(2, 72);
        verifyPosition(0, 36);
        goToPosition(0, 36);
        verifyPosition(0, 92);
        verifyPosition(1, 94);
        goToPosition(0, 92);
    }

    public void testPath106v2() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(1, 10);
        verifyPosition(0, 18);
        verifyPosition(1, 19);
        goToPosition(0, 18);
        verifyPosition(0, 34);
        verifyPosition(1, 36);
        goToPosition(1, 36);
        verifyPosition(0, 92);
        verifyPosition(1, 94);
        goToPosition(1, 94);
        verifyPosition(0, 102);
        verifyPosition(1, 104);
        goToPosition(1, 104);
        verifyPosition(0, 106);
        goToPosition(0, 106);
    }

    public void testPath71() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(1, 17);
        verifyPosition(0, 12);
        goToPosition(0, 12);
        verifyPosition(0, 22);
        verifyPosition(1, 24);
        goToPosition(1, 24);
        verifyPosition(0, 44);
        verifyPosition(1, 45);
        goToPosition(0, 44);
        verifyPosition(0, 25);
        verifyPosition(1, 71);
        verifyPosition(2, 72);
        goToPosition(1, 71);
    }

    public void testPath97() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(1, 6);
        verifyPosition(0, 11);
        verifyPosition(1, 12);
        goToPosition(1, 12);
        verifyPosition(0, 22);
        verifyPosition(1, 24);
        goToPosition(0, 22);
        verifyPosition(0, 42);
        goToPosition(0, 42);
        verifyPosition(0, 73);
        verifyPosition(1, 74);
        goToPosition(0, 73);
        verifyPosition(0, 97);
        verifyPosition(1, 99);
        goToPosition(0, 97);
    }

    public void testPath99() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(1, 6);
        verifyPosition(0, 11);
        verifyPosition(1, 12);
        goToPosition(1, 12);
        verifyPosition(0, 22);
        verifyPosition(1, 24);
        goToPosition(0, 22);
        verifyPosition(0, 42);
        goToPosition(0, 42);
        verifyPosition(0, 73);
        verifyPosition(1, 74);
        goToPosition(0, 73);
        verifyPosition(0, 97);
        verifyPosition(1, 99);
        goToPosition(1, 99);
    }

    public void testPath74() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(1, 6);
        verifyPosition(0, 11);
        verifyPosition(1, 12);
        goToPosition(1, 12);
        verifyPosition(0, 22);
        verifyPosition(1, 24);
        goToPosition(0, 22);
        verifyPosition(0, 42);
        goToPosition(0, 42);
        verifyPosition(0, 73);
        verifyPosition(1, 74);
        goToPosition(1, 74);
    }

    public void testPath69() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(1, 6);
        verifyPosition(0, 11);
        verifyPosition(1, 12);
        goToPosition(0, 11);
        verifyPosition(0, 20);
        verifyPosition(1, 21);
        goToPosition(1, 21);
        verifyPosition(0, 69);
        verifyPosition(1, 70);
        goToPosition(0, 69);
    }

    public void testPath70() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(1, 6);
        verifyPosition(0, 11);
        verifyPosition(1, 12);
        goToPosition(0, 11);
        verifyPosition(0, 20);
        verifyPosition(1, 21);
        goToPosition(1, 21);
        verifyPosition(0, 69);
        verifyPosition(1, 70);
        goToPosition(1, 70);
    }

    public void testPath67() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(0, 11);
        verifyPosition(0, 20);
        verifyPosition(1, 21);
        goToPosition(0, 20);
        verifyPosition(0, 39);
        verifyPosition(1, 66);
        goToPosition(0, 39);
        verifyPosition(0, 67);
        verifyPosition(1, 95);
        goToPosition(0, 67);
    }

    public void testPath95() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(0, 11);
        verifyPosition(0, 20);
        verifyPosition(1, 21);
        goToPosition(0, 20);
        verifyPosition(0, 39);
        verifyPosition(1, 66);
        goToPosition(0, 39);
        verifyPosition(0, 67);
        verifyPosition(1, 95);
        goToPosition(1, 95);
    }

    public void testPath96() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(0, 11);
        verifyPosition(0, 20);
        verifyPosition(1, 21);
        goToPosition(0, 20);
        verifyPosition(0, 39);
        verifyPosition(1, 66);
        goToPosition(1, 66);
        verifyPosition(0, 96);
        verifyPosition(1, 110);
        goToPosition(0, 96);
    }

    public void testPath110() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(0, 11);
        verifyPosition(0, 20);
        verifyPosition(1, 21);
        goToPosition(0, 20);
        verifyPosition(0, 39);
        verifyPosition(1, 66);
        goToPosition(1, 66);
        verifyPosition(0, 96);
        verifyPosition(1, 110);
        goToPosition(1, 110);
    }

    public void testPath50() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(2, 7);
        verifyPosition(0, 14);
        verifyPosition(1, 15);
        goToPosition(0, 14);
        verifyPosition(0, 15);
        verifyPosition(1, 40);
        goToPosition(1, 40);
        verifyPosition(0, 26);
        verifyPosition(1, 27);
        goToPosition(0, 26);
        verifyPosition(0, 50);
        verifyPosition(1, 51);
        goToPosition(0, 50);
    }

    public void testPath51() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(2, 7);
        verifyPosition(0, 14);
        verifyPosition(1, 15);
        goToPosition(0, 14);
        verifyPosition(0, 15);
        verifyPosition(1, 40);
        goToPosition(1, 40);
        verifyPosition(0, 26);
        verifyPosition(1, 27);
        goToPosition(0, 26);
        verifyPosition(0, 50);
        verifyPosition(1, 51);
        goToPosition(1, 51);
    }

    public void testPath47() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(2, 33);
        verifyPosition(0, 58);
        verifyPosition(1, 59);
        goToPosition(1, 59);
        verifyPosition(0, 14);
        verifyPosition(1, 91);
        goToPosition(0, 14);
        verifyPosition(0, 15);
        verifyPosition(1, 40);
        goToPosition(1, 40);
        verifyPosition(0, 26);
        verifyPosition(1, 27);
        goToPosition(1, 27);
        verifyPosition(0, 47);
        verifyPosition(1, 49);
        goToPosition(0, 47);
    }

    public void testPath49() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(2, 33);
        verifyPosition(0, 58);
        verifyPosition(1, 59);
        goToPosition(1, 59);
        verifyPosition(0, 14);
        verifyPosition(1, 91);
        goToPosition(0, 14);
        verifyPosition(0, 15);
        verifyPosition(1, 40);
        goToPosition(1, 40);
        verifyPosition(0, 26);
        verifyPosition(1, 27);
        goToPosition(1, 27);
        verifyPosition(0, 47);
        verifyPosition(1, 49);
        goToPosition(1, 49);
    }

    public void testPath91() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(2, 33);
        verifyPosition(0, 58);
        verifyPosition(1, 59);
        goToPosition(1, 59);
        verifyPosition(0, 14);
        verifyPosition(1, 91);
        goToPosition(1, 91);
    }

    public void testPath112() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(1, 17);
        verifyPosition(0, 12);
        goToPosition(0, 12);
        verifyPosition(0, 22);
        verifyPosition(1, 24);
        goToPosition(1, 24);
        verifyPosition(0, 44);
        verifyPosition(1, 45);
        goToPosition(1, 45);
        verifyPosition(0, 15);
        goToPosition(0, 15);
        verifyPosition(0, 28);
        verifyPosition(1, 30);
        goToPosition(1, 30);
        verifyPosition(0, 54);
        verifyPosition(1, 55);
        goToPosition(1, 55);
        verifyPosition(0, 41);
        verifyPosition(1, 86);
        goToPosition(0, 41);
        verifyPosition(0, 75);
        verifyPosition(1, 76);
        goToPosition(0, 75);
        verifyPosition(0, 108);
        verifyPosition(1, 109);
        goToPosition(1, 109);
        verifyPosition(0, 38);
        verifyPosition(1, 112);
        goToPosition(1, 112);
    }

    public void testPath108() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(1, 17);
        verifyPosition(0, 12);
        goToPosition(0, 12);
        verifyPosition(0, 22);
        verifyPosition(1, 24);
        goToPosition(1, 24);
        verifyPosition(0, 44);
        verifyPosition(1, 45);
        goToPosition(0, 44);
        verifyPosition(0, 25);
        verifyPosition(1, 71);
        verifyPosition(2, 72);
        goToPosition(0, 25);
        verifyPosition(0, 41);
        verifyPosition(1, 46);
        goToPosition(0, 41);
        verifyPosition(0, 75);
        verifyPosition(1, 76);
        goToPosition(0, 75);
        verifyPosition(0, 108);
        verifyPosition(1, 109);
        goToPosition(0, 108);
    }

    public void testPath86() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(1, 17);
        verifyPosition(0, 12);
        goToPosition(0, 12);
        verifyPosition(0, 22);
        verifyPosition(1, 24);
        goToPosition(1, 24);
        verifyPosition(0, 44);
        verifyPosition(1, 45);
        goToPosition(1, 45);
        verifyPosition(0, 15);
        goToPosition(0, 15);
        verifyPosition(0, 28);
        verifyPosition(1, 30);
        goToPosition(1, 30);
        verifyPosition(0, 54);
        verifyPosition(1, 55);
        goToPosition(1, 55);
        verifyPosition(0, 41);
        verifyPosition(1, 86);
        goToPosition(1, 86);
    }

    public void testPath105() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(2, 7);
        verifyPosition(0, 14);
        verifyPosition(1, 15);
        goToPosition(1, 15);
        verifyPosition(0, 28);
        verifyPosition(1, 30);
        goToPosition(0, 28);
        verifyPosition(0, 52);
        verifyPosition(1, 53);
        goToPosition(0, 52);
        verifyPosition(0, 80);
        verifyPosition(1, 100);
        goToPosition(0, 80);
        verifyPosition(0, 105);
        goToPosition(0, 105);
    }

    public void testPath100() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(2, 7);
        verifyPosition(0, 14);
        verifyPosition(1, 15);
        goToPosition(1, 15);
        verifyPosition(0, 28);
        verifyPosition(1, 30);
        goToPosition(0, 28);
        verifyPosition(0, 52);
        verifyPosition(1, 53);
        goToPosition(0, 52);
        verifyPosition(0, 80);
        verifyPosition(1, 100);
        goToPosition(1, 100);
    }

    public void testPath82() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(2, 7);
        verifyPosition(0, 14);
        verifyPosition(1, 15);
        goToPosition(0, 14);
        verifyPosition(0, 15);
        verifyPosition(1, 40);
        goToPosition(0, 15);
        verifyPosition(0, 28);
        verifyPosition(1, 30);
        goToPosition(0, 28);
        verifyPosition(0, 52);
        verifyPosition(1, 53);
        goToPosition(1, 53);
        verifyPosition(0, 82);
        verifyPosition(1, 83);
        goToPosition(0, 82);
    }

    public void testPath83() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(2, 7);
        verifyPosition(0, 14);
        verifyPosition(1, 15);
        goToPosition(0, 14);
        verifyPosition(0, 15);
        verifyPosition(1, 40);
        goToPosition(0, 15);
        verifyPosition(0, 28);
        verifyPosition(1, 30);
        goToPosition(0, 28);
        verifyPosition(0, 52);
        verifyPosition(1, 53);
        goToPosition(1, 53);
        verifyPosition(0, 82);
        verifyPosition(1, 83);
        goToPosition(1, 83);
    }

    public void testPath84() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(1, 32);
        verifyPosition(0, 56);
        verifyPosition(1, 57);
        goToPosition(1, 57);
        verifyPosition(0, 15);
        goToPosition(0, 15);
        verifyPosition(0, 28);
        verifyPosition(1, 30);
        goToPosition(1, 30);
        verifyPosition(0, 54);
        verifyPosition(1, 55);
        goToPosition(0, 54);
        verifyPosition(0, 84);
        verifyPosition(1, 85);
        goToPosition(0, 84);
    }

    public void testPath85() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(1, 32);
        verifyPosition(0, 56);
        verifyPosition(1, 57);
        goToPosition(1, 57);
        verifyPosition(0, 15);
        goToPosition(0, 15);
        verifyPosition(0, 28);
        verifyPosition(1, 30);
        goToPosition(1, 30);
        verifyPosition(0, 54);
        verifyPosition(1, 55);
        goToPosition(0, 54);
        verifyPosition(0, 84);
        verifyPosition(1, 85);
        goToPosition(1, 85);
    }

    public void testPath88() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(1, 32);
        verifyPosition(0, 56);
        verifyPosition(1, 57);
        goToPosition(0, 56);
        verifyPosition(0, 88);
        verifyPosition(1, 90);
        goToPosition(0, 88);
    }

    public void testPath90() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(1, 32);
        verifyPosition(0, 56);
        verifyPosition(1, 57);
        goToPosition(0, 56);
        verifyPosition(0, 88);
        verifyPosition(1, 90);
        goToPosition(1, 90);
    }

    public void testPath76() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(0, 16);
        verifyPosition(0, 11);
        verifyPosition(1, 32);
        verifyPosition(2, 33);
        goToPosition(2, 33);
        verifyPosition(0, 58);
        verifyPosition(1, 59);
        goToPosition(0, 58);
        verifyPosition(0, 41);
        goToPosition(0, 41);
        verifyPosition(0, 75);
        verifyPosition(1, 76);
        goToPosition(1, 76);
    }

    public void testPath78() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(1, 17);
        verifyPosition(0, 12);
        goToPosition(0, 12);
        verifyPosition(0, 22);
        verifyPosition(1, 24);
        goToPosition(1, 24);
        verifyPosition(0, 44);
        verifyPosition(1, 45);
        goToPosition(0, 44);
        verifyPosition(0, 25);
        verifyPosition(1, 71);
        verifyPosition(2, 72);
        goToPosition(0, 25);
        verifyPosition(0, 41);
        verifyPosition(1, 46);
        goToPosition(1, 46);
        verifyPosition(0, 78);
        verifyPosition(1, 79);
        goToPosition(0, 78);
    }

    public void testPath79() {
        goToPosition(0, 2);
        verifyPosition(0, 5);
        verifyPosition(1, 6);
        verifyPosition(2, 7);
        goToPosition(0, 5);
        verifyPosition(0, 8);
        verifyPosition(1, 10);
        goToPosition(0, 8);
        verifyPosition(0, 16);
        verifyPosition(1, 17);
        goToPosition(1, 17);
        verifyPosition(0, 12);
        goToPosition(0, 12);
        verifyPosition(0, 22);
        verifyPosition(1, 24);
        goToPosition(1, 24);
        verifyPosition(0, 44);
        verifyPosition(1, 45);
        goToPosition(0, 44);
        verifyPosition(0, 25);
        verifyPosition(1, 71);
        verifyPosition(2, 72);
        goToPosition(0, 25);
        verifyPosition(0, 41);
        verifyPosition(1, 46);
        goToPosition(1, 46);
        verifyPosition(0, 78);
        verifyPosition(1, 79);
        goToPosition(1, 79);
    }
}
