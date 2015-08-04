package hu.zagor.gamebooks.selenium.raw.sc;

import hu.zagor.gamebooks.selenium.BasicSeleniumTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Selenium test for book CsP3.
 * @author Tamas_Szekeres
 */
@Test
public class HuDimensionOfDoomST extends BasicSeleniumTest {

    @BeforeClass
    public void setUpClass() {
        startBook("990327002", "csp2");
    }

    public void testPath34() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(1, 48);
        goToPosition(0, 31);
        goToPosition(0, 28);
        verifyPosition(1, 67);
        goToPosition(0, 34);
        death();
    }

    public void testPath81() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(1, 48);
        goToPosition(0, 31);
        goToPosition(0, 28);
        verifyPosition(0, 34);
        goToPosition(1, 67);
        goToPosition(0, 81);
        death();
    }

    public void testPath77() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(0, 78);
        goToPosition(1, 25);
        goToPosition(0, 70);
        goToPosition(0, 71);
        verifyPosition(1, 68);
        goToPosition(0, 12);
        goToPosition(0, 89);
        verifyPosition(0, 37);
        goToPosition(1, 76);
        goToPosition(0, 77);
        goToPosition(0, 115);
        score(3, "10 231");
    }

    public void testPath37() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(0, 78);
        goToPosition(1, 25);
        goToPosition(0, 70);
        goToPosition(0, 71);
        verifyPosition(1, 68);
        goToPosition(0, 12);
        goToPosition(0, 89);
        verifyPosition(1, 76);
        goToPosition(0, 37);
        death();
    }

    public void testPath74() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(0, 78);
        goToPosition(1, 25);
        goToPosition(0, 70);
        goToPosition(0, 71);
        verifyPosition(0, 12);
        goToPosition(1, 68);
        verifyPosition(0, 42);
        goToPosition(1, 74);
        death();
    }

    public void testPath43() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(0, 78);
        goToPosition(1, 25);
        goToPosition(0, 70);
        goToPosition(0, 71);
        verifyPosition(0, 12);
        goToPosition(1, 68);
        verifyPosition(1, 74);
        goToPosition(0, 42);
        goToPosition(0, 43);
        goToPosition(0, 115);
        score(5, "695");
    }

    public void testPath45() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(1, 69);
        goToPosition(0, 30);
        verifyPosition(0, 107);
        goToPosition(1, 45);
        goToPosition(0, 115);
        score(2, "287 611");
    }

    public void testPath5() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(0, 62);
        goToPosition(1, 40);
        goToPosition(0, 5);
        death();
    }

    public void testPath95() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(0, 47);
        goToPosition(1, 4);
        verifyPosition(1, 63);
        goToPosition(0, 95);
        death();
    }

    public void testPath63() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(0, 47);
        goToPosition(1, 4);
        verifyPosition(0, 95);
        goToPosition(1, 63);
        goToPosition(0, 115);
        score(1, "8 420 364");
    }

    public void testPath90() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(1, 22);
        goToPosition(0, 112);
        goToPosition(0, 113);
        verifyPosition(0, 18);
        goToPosition(1, 90);
        death();
    }

    public void testPath93() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(1, 22);
        goToPosition(0, 112);
        goToPosition(0, 113);
        verifyPosition(1, 90);
        goToPosition(0, 18);
        verifyPosition(1, 110);
        goToPosition(0, 93);
        death();
    }

    public void testPath11() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(1, 22);
        goToPosition(0, 112);
        goToPosition(0, 113);
        verifyPosition(1, 90);
        goToPosition(0, 18);
        verifyPosition(0, 93);
        goToPosition(1, 110);
        verifyPosition(1, 14);
        goToPosition(0, 11);
        death();
    }

    public void testPath15() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(1, 22);
        goToPosition(0, 112);
        goToPosition(0, 113);
        verifyPosition(1, 90);
        goToPosition(0, 18);
        verifyPosition(0, 93);
        goToPosition(1, 110);
        verifyPosition(0, 11);
        goToPosition(1, 14);
        goToPosition(0, 15);
        goToPosition(0, 115);
        score(2, "399 874");
    }

    public void testPath101() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 16);
        goToPosition(0, 88);
        verifyPosition(0, 53);
        goToPosition(1, 105);
        verifyPosition(0, 39);
        goToPosition(1, 97);
        verifyPosition(1, 114);
        goToPosition(0, 20);
        verifyPosition(1, 91);
        goToPosition(0, 41);
        goToPosition(0, 101);
        goToPosition(0, 115);
        score(3, "32 729");
    }

    public void testPath36() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 16);
        goToPosition(0, 88);
        verifyPosition(0, 53);
        goToPosition(1, 105);
        verifyPosition(0, 39);
        goToPosition(1, 97);
        verifyPosition(1, 114);
        goToPosition(0, 20);
        verifyPosition(0, 41);
        goToPosition(1, 91);
        goToPosition(0, 36);
        death();
    }

    public void testPath51() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 16);
        goToPosition(0, 88);
        verifyPosition(0, 53);
        goToPosition(1, 105);
        verifyPosition(0, 39);
        goToPosition(1, 97);
        verifyPosition(0, 20);
        goToPosition(1, 114);
        verifyPosition(1, 99);
        goToPosition(0, 73);
        verifyPosition(1, 61);
        goToPosition(0, 51);
        goToPosition(0, 115);
        score(4, "2 864");
    }

    public void testPath61() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 16);
        goToPosition(0, 88);
        verifyPosition(0, 53);
        goToPosition(1, 105);
        verifyPosition(0, 39);
        goToPosition(1, 97);
        verifyPosition(0, 20);
        goToPosition(1, 114);
        verifyPosition(1, 99);
        goToPosition(0, 73);
        verifyPosition(0, 51);
        goToPosition(1, 61);
        goToPosition(0, 115);
        score(2, "975 225");
    }

    public void testPath99() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 16);
        goToPosition(0, 88);
        verifyPosition(0, 53);
        goToPosition(1, 105);
        verifyPosition(0, 39);
        goToPosition(1, 97);
        verifyPosition(0, 20);
        goToPosition(1, 114);
        verifyPosition(0, 73);
        goToPosition(1, 99);
        death();
    }

    public void testPath55() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 16);
        goToPosition(0, 88);
        verifyPosition(0, 53);
        goToPosition(1, 105);
        verifyPosition(1, 97);
        goToPosition(0, 39);
        verifyPosition(1, 58);
        goToPosition(0, 55);
        death();
    }

    public void testPath87() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 16);
        goToPosition(0, 88);
        verifyPosition(0, 53);
        goToPosition(1, 105);
        verifyPosition(1, 97);
        goToPosition(0, 39);
        verifyPosition(0, 55);
        goToPosition(1, 58);
        verifyPosition(0, 13);
        goToPosition(1, 87);
        death();
    }

    public void testPath6() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 16);
        goToPosition(0, 88);
        verifyPosition(0, 53);
        goToPosition(1, 105);
        verifyPosition(1, 97);
        goToPosition(0, 39);
        verifyPosition(0, 55);
        goToPosition(1, 58);
        verifyPosition(1, 87);
        goToPosition(0, 13);
        verifyPosition(1, 65);
        goToPosition(0, 6);
        death();
    }

    public void testPath65() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 16);
        goToPosition(0, 88);
        verifyPosition(0, 53);
        goToPosition(1, 105);
        verifyPosition(1, 97);
        goToPosition(0, 39);
        verifyPosition(0, 55);
        goToPosition(1, 58);
        verifyPosition(1, 87);
        goToPosition(0, 13);
        verifyPosition(0, 6);
        goToPosition(1, 65);
        goToPosition(0, 115);
        score(2, "810 633");
    }

    public void testPath35() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(1, 69);
        goToPosition(0, 30);
        verifyPosition(1, 45);
        goToPosition(0, 107);
        goToPosition(0, 88);
        verifyPosition(1, 105);
        goToPosition(0, 53);
        verifyPosition(1, 84);
        goToPosition(0, 79);
        verifyPosition(1, 102);
        goToPosition(0, 35);
        goToPosition(0, 115);
        score(3, "58 267");
    }

    public void testPath102() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(1, 69);
        goToPosition(0, 30);
        verifyPosition(1, 45);
        goToPosition(0, 107);
        goToPosition(0, 88);
        verifyPosition(1, 105);
        goToPosition(0, 53);
        verifyPosition(1, 84);
        goToPosition(0, 79);
        verifyPosition(0, 35);
        goToPosition(1, 102);
        death();
    }

    public void testPath98() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(1, 69);
        goToPosition(0, 30);
        verifyPosition(1, 45);
        goToPosition(0, 107);
        goToPosition(0, 88);
        verifyPosition(1, 105);
        goToPosition(0, 53);
        verifyPosition(0, 79);
        goToPosition(1, 84);
        goToPosition(0, 85);
        verifyPosition(1, 10);
        goToPosition(0, 54);
        verifyPosition(0, 80);
        goToPosition(1, 98);
        death();
    }

    public void testPath17() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(0, 112);
        goToPosition(1, 22);
        goToPosition(0, 85);
        verifyPosition(1, 10);
        goToPosition(0, 54);
        verifyPosition(1, 98);
        goToPosition(0, 80);
        verifyPosition(0, 104);
        goToPosition(1, 24);
        goToPosition(0, 29);
        verifyPosition(0, 92);
        goToPosition(1, 17);
        goToPosition(0, 115);
        score(4, "4 428");
    }

    public void testPath27() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(0, 112);
        goToPosition(1, 22);
        goToPosition(0, 85);
        verifyPosition(1, 10);
        goToPosition(0, 54);
        verifyPosition(1, 98);
        goToPosition(0, 80);
        verifyPosition(0, 104);
        goToPosition(1, 24);
        goToPosition(0, 29);
        verifyPosition(1, 17);
        goToPosition(0, 92);
        verifyPosition(0, 56);
        goToPosition(1, 23);
        goToPosition(0, 103);
        verifyPosition(1, 72);
        goToPosition(0, 27);
        death();
    }

    public void testPath72() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(0, 112);
        goToPosition(1, 22);
        goToPosition(0, 85);
        verifyPosition(1, 10);
        goToPosition(0, 54);
        verifyPosition(1, 98);
        goToPosition(0, 80);
        verifyPosition(0, 104);
        goToPosition(1, 24);
        goToPosition(0, 29);
        verifyPosition(1, 17);
        goToPosition(0, 92);
        verifyPosition(0, 56);
        goToPosition(1, 23);
        goToPosition(0, 103);
        verifyPosition(0, 27);
        goToPosition(1, 72);
        death();
    }

    public void testPath82() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(0, 112);
        goToPosition(1, 22);
        goToPosition(0, 85);
        verifyPosition(1, 10);
        goToPosition(0, 54);
        verifyPosition(1, 98);
        goToPosition(0, 80);
        verifyPosition(0, 104);
        goToPosition(1, 24);
        goToPosition(0, 29);
        verifyPosition(1, 17);
        goToPosition(0, 92);
        verifyPosition(1, 23);
        goToPosition(0, 56);
        goToPosition(0, 57);
        verifyPosition(0, 8);
        goToPosition(1, 82);
        goToPosition(0, 115);
        score(3, "18 402");
    }

    public void testPath9() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(0, 112);
        goToPosition(1, 22);
        goToPosition(0, 85);
        verifyPosition(1, 10);
        goToPosition(0, 54);
        verifyPosition(1, 98);
        goToPosition(0, 80);
        verifyPosition(0, 104);
        goToPosition(1, 24);
        goToPosition(0, 29);
        verifyPosition(1, 17);
        goToPosition(0, 92);
        verifyPosition(1, 23);
        goToPosition(0, 56);
        goToPosition(0, 57);
        verifyPosition(1, 82);
        goToPosition(0, 8);
        goToPosition(0, 9);
        goToPosition(0, 115);
        score(2, "537 328");
    }

    public void testPath21() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(0, 112);
        goToPosition(1, 22);
        goToPosition(0, 85);
        verifyPosition(1, 10);
        goToPosition(0, 54);
        verifyPosition(1, 98);
        goToPosition(0, 80);
        verifyPosition(1, 24);
        goToPosition(0, 104);
        verifyPosition(0, 100);
        goToPosition(1, 108);
        goToPosition(0, 109);
        verifyPosition(0, 32);
        goToPosition(1, 21);
        goToPosition(0, 115);
        score(3, "79 431");
    }

    public void testPath32() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(0, 112);
        goToPosition(1, 22);
        goToPosition(0, 85);
        verifyPosition(1, 10);
        goToPosition(0, 54);
        verifyPosition(1, 98);
        goToPosition(0, 80);
        verifyPosition(1, 24);
        goToPosition(0, 104);
        verifyPosition(0, 100);
        goToPosition(1, 108);
        goToPosition(0, 109);
        verifyPosition(1, 21);
        goToPosition(0, 32);
        death();
    }

    public void testPath60() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(0, 112);
        goToPosition(1, 22);
        goToPosition(0, 85);
        verifyPosition(1, 10);
        goToPosition(0, 54);
        verifyPosition(1, 98);
        goToPosition(0, 80);
        verifyPosition(1, 24);
        goToPosition(0, 104);
        verifyPosition(1, 108);
        goToPosition(0, 100);
        verifyPosition(1, 83);
        goToPosition(0, 64);
        verifyPosition(1, 86);
        goToPosition(0, 46);
        goToPosition(0, 60);
        goToPosition(0, 115);
        score(2, "640 173");
    }

    public void testPath86() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(0, 112);
        goToPosition(1, 22);
        goToPosition(0, 85);
        verifyPosition(1, 10);
        goToPosition(0, 54);
        verifyPosition(1, 98);
        goToPosition(0, 80);
        verifyPosition(1, 24);
        goToPosition(0, 104);
        verifyPosition(1, 108);
        goToPosition(0, 100);
        verifyPosition(1, 83);
        goToPosition(0, 64);
        verifyPosition(0, 46);
        goToPosition(1, 86);
        death();
    }

    public void testPath83() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(0, 30);
        goToPosition(1, 69);
        verifyPosition(1, 40);
        goToPosition(0, 62);
        verifyPosition(1, 4);
        goToPosition(0, 47);
        verifyPosition(0, 112);
        goToPosition(1, 22);
        goToPosition(0, 85);
        verifyPosition(1, 10);
        goToPosition(0, 54);
        verifyPosition(1, 98);
        goToPosition(0, 80);
        verifyPosition(1, 24);
        goToPosition(0, 104);
        verifyPosition(1, 108);
        goToPosition(0, 100);
        verifyPosition(0, 64);
        goToPosition(1, 83);
        death();
    }

    public void testPath59() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(1, 69);
        goToPosition(0, 30);
        verifyPosition(1, 45);
        goToPosition(0, 107);
        goToPosition(0, 88);
        verifyPosition(1, 105);
        goToPosition(0, 53);
        verifyPosition(0, 79);
        goToPosition(1, 84);
        goToPosition(0, 85);
        verifyPosition(0, 54);
        goToPosition(1, 10);
        verifyPosition(0, 52);
        goToPosition(1, 96);
        verifyPosition(0, 44);
        goToPosition(1, 59);
        death();
    }

    public void testPath106() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(1, 69);
        goToPosition(0, 30);
        verifyPosition(1, 45);
        goToPosition(0, 107);
        goToPosition(0, 88);
        verifyPosition(1, 105);
        goToPosition(0, 53);
        verifyPosition(0, 79);
        goToPosition(1, 84);
        goToPosition(0, 85);
        verifyPosition(0, 54);
        goToPosition(1, 10);
        verifyPosition(0, 52);
        goToPosition(1, 96);
        verifyPosition(1, 59);
        goToPosition(0, 44);
        goToPosition(0, 106);
        goToPosition(0, 115);
        score(3, "69 557");
    }

    public void testPath26() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(1, 69);
        goToPosition(0, 30);
        verifyPosition(1, 45);
        goToPosition(0, 107);
        goToPosition(0, 88);
        verifyPosition(1, 105);
        goToPosition(0, 53);
        verifyPosition(0, 79);
        goToPosition(1, 84);
        goToPosition(0, 85);
        verifyPosition(0, 54);
        goToPosition(1, 10);
        verifyPosition(1, 96);
        goToPosition(0, 52);
        verifyPosition(1, 38);
        goToPosition(0, 26);
        death();
    }

    public void testPath38() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 88);
        goToPosition(1, 16);
        verifyPosition(0, 31);
        goToPosition(1, 48);
        goToPosition(0, 49);
        verifyPosition(1, 25);
        goToPosition(0, 78);
        verifyPosition(1, 69);
        goToPosition(0, 30);
        verifyPosition(1, 45);
        goToPosition(0, 107);
        goToPosition(0, 88);
        verifyPosition(1, 105);
        goToPosition(0, 53);
        verifyPosition(0, 79);
        goToPosition(1, 84);
        goToPosition(0, 85);
        verifyPosition(0, 54);
        goToPosition(1, 10);
        verifyPosition(1, 96);
        goToPosition(0, 52);
        verifyPosition(0, 26);
        goToPosition(1, 38);
        goToPosition(0, 115);
        score(3, "24 950");
    }

    private void score(final int level, final String score) {
        verifyImage("115-" + level);
        verifyText(score);
    }

    private void death() {
        verifyText("KAMPEC!");
        ending();
    }

}
