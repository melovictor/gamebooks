package hu.zagor.gamebooks.selenium.raw.sc;

import hu.zagor.gamebooks.selenium.BasicSeleniumTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Selenium test for book CsP1.
 * @author Tamas_Szekeres
 */
@Test
public class HuTheCosmicFunhouseST extends BasicSeleniumTest {

    @BeforeClass
    public void setUpClass() {
        startBook("990327001", "csp1");
    }

    public void testPath34() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(1, 15);
        goToPosition(0, 23);
        verifyPosition(1, 96);
        goToPosition(0, 92);
        verifyPosition(1, 37);
        goToPosition(0, 34);
        death();
    }

    public void testPath31() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(1, 15);
        goToPosition(0, 23);
        verifyPosition(1, 96);
        goToPosition(0, 92);
        verifyPosition(0, 34);
        goToPosition(1, 37);
        goToPosition(0, 77);
        verifyPosition(1, 42);
        goToPosition(0, 31);
        death();
    }

    public void testPath42() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(1, 15);
        goToPosition(0, 23);
        verifyPosition(1, 96);
        goToPosition(0, 92);
        verifyPosition(0, 34);
        goToPosition(1, 37);
        goToPosition(0, 77);
        verifyPosition(0, 31);
        goToPosition(1, 42);
        goToPosition(0, 115);
        score(2, "749 980");
    }

    public void testPath80() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(1, 15);
        goToPosition(0, 23);
        verifyPosition(0, 92);
        goToPosition(1, 96);
        verifyPosition(0, 54);
        goToPosition(1, 36);
        goToPosition(0, 80);
        goToPosition(0, 115);
        score(3, "57 993");
    }

    public void testPath55() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(1, 15);
        goToPosition(0, 23);
        verifyPosition(0, 92);
        goToPosition(1, 96);
        verifyPosition(1, 36);
        goToPosition(0, 54);
        goToPosition(0, 55);
        goToPosition(0, 115);
        score(3, "89 168");
    }

    public void testPath51() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(0, 23);
        goToPosition(1, 15);
        verifyPosition(1, 48);
        goToPosition(0, 105);
        goToPosition(0, 107);
        verifyPosition(0, 93);
        goToPosition(1, 57);
        verifyPosition(0, 69);
        goToPosition(1, 75);
        goToPosition(0, 51);
        goToPosition(0, 115);
        score(3, "42 544");
    }

    public void testPath69() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(0, 23);
        goToPosition(1, 15);
        verifyPosition(1, 48);
        goToPosition(0, 105);
        goToPosition(0, 107);
        verifyPosition(0, 93);
        goToPosition(1, 57);
        verifyPosition(1, 75);
        goToPosition(0, 69);
        death();
    }

    public void testPath29() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(0, 23);
        goToPosition(1, 15);
        verifyPosition(1, 48);
        goToPosition(0, 105);
        goToPosition(0, 107);
        verifyPosition(1, 57);
        goToPosition(0, 93);
        verifyPosition(1, 95);
        goToPosition(0, 53);
        verifyPosition(1, 95);
        goToPosition(0, 29);
        death();
    }

    public void testPath95A() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(0, 23);
        goToPosition(1, 15);
        verifyPosition(1, 48);
        goToPosition(0, 105);
        goToPosition(0, 107);
        verifyPosition(1, 57);
        goToPosition(0, 93);
        verifyPosition(1, 95);
        goToPosition(0, 53);
        verifyPosition(0, 29);
        goToPosition(1, 95);
        goToPosition(0, 115);
        score(2, "293 400");
    }

    public void testPath95B() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(0, 23);
        goToPosition(1, 15);
        verifyPosition(1, 48);
        goToPosition(0, 105);
        goToPosition(0, 107);
        verifyPosition(1, 57);
        goToPosition(0, 93);
        verifyPosition(0, 53);
        goToPosition(1, 95);
        goToPosition(0, 115);
        score(2, "293 400");
    }

    public void testPath46() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(0, 23);
        goToPosition(1, 15);
        verifyPosition(0, 105);
        goToPosition(1, 48);
        goToPosition(0, 78);
        verifyPosition(0, 83);
        goToPosition(1, 84);
        goToPosition(0, 46);
        goToPosition(0, 115);
        score(5, "117");
    }

    public void testPath83() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(1, 11);
        goToPosition(0, 8);
        verifyPosition(0, 23);
        goToPosition(1, 15);
        verifyPosition(0, 105);
        goToPosition(1, 48);
        goToPosition(0, 78);
        verifyPosition(1, 84);
        goToPosition(0, 83);
        goToPosition(0, 115);
        score(3, "40 318");
    }

    public void testPath103() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(0, 26);
        goToPosition(1, 35);
        verifyPosition(0, 28);
        goToPosition(1, 39);
        verifyPosition(1, 45);
        goToPosition(0, 88);
        verifyPosition(1, 43);
        goToPosition(0, 103);
        goToPosition(0, 115);
        score(5, "192");
    }

    public void testPath41A() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(0, 26);
        goToPosition(1, 35);
        verifyPosition(0, 28);
        goToPosition(1, 39);
        verifyPosition(1, 45);
        goToPosition(0, 88);
        verifyPosition(0, 103);
        goToPosition(1, 43);
        goToPosition(0, 41);
        goToPosition(0, 115);
        score(4, "6 842");
    }

    public void testPath59() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(0, 26);
        goToPosition(1, 35);
        verifyPosition(0, 28);
        goToPosition(1, 39);
        verifyPosition(0, 88);
        goToPosition(1, 45);
        verifyPosition(1, 38);
        goToPosition(0, 58);
        goToPosition(0, 59);
        goToPosition(0, 115);
        score(4, "4 407");
    }

    public void testPath38() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(0, 26);
        goToPosition(1, 35);
        verifyPosition(0, 28);
        goToPosition(1, 39);
        verifyPosition(0, 88);
        goToPosition(1, 45);
        verifyPosition(0, 58);
        goToPosition(1, 38);
        death();
    }

    public void testPath41B() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(0, 26);
        goToPosition(1, 35);
        verifyPosition(1, 39);
        goToPosition(0, 28);
        verifyPosition(1, 81);
        goToPosition(0, 19);
        verifyPosition(1, 24);
        goToPosition(0, 85);
        goToPosition(0, 41);
        goToPosition(0, 115);
        score(4, "6 842");
    }

    public void testPath24() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(0, 26);
        goToPosition(1, 35);
        verifyPosition(1, 39);
        goToPosition(0, 28);
        verifyPosition(1, 81);
        goToPosition(0, 19);
        verifyPosition(0, 85);
        goToPosition(1, 24);
        goToPosition(0, 115);
        score(3, "90 996");
    }

    public void testPath104() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(0, 26);
        goToPosition(1, 35);
        verifyPosition(1, 39);
        goToPosition(0, 28);
        verifyPosition(0, 19);
        goToPosition(1, 81);
        goToPosition(0, 82);
        verifyPosition(0, 25);
        goToPosition(1, 104);
        death();
    }

    public void testPath25() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(0, 26);
        goToPosition(1, 35);
        verifyPosition(1, 39);
        goToPosition(0, 28);
        verifyPosition(0, 19);
        goToPosition(1, 81);
        goToPosition(0, 82);
        verifyPosition(1, 104);
        goToPosition(0, 25);
        goToPosition(0, 115);
        score(2, "434 960");
    }

    public void testPath112() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(1, 35);
        goToPosition(0, 26);
        goToPosition(0, 63);
        verifyPosition(1, 76);
        goToPosition(0, 106);
        goToPosition(0, 113);
        verifyPosition(0, 27);
        goToPosition(1, 5);
        goToPosition(0, 112);
        goToPosition(0, 115);

        score(1, "9 712 319");
    }

    public void testPath27() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(1, 35);
        goToPosition(0, 26);
        goToPosition(0, 63);
        verifyPosition(1, 76);
        goToPosition(0, 106);
        goToPosition(0, 113);
        verifyPosition(1, 5);
        goToPosition(0, 27);
        goToPosition(0, 115);
        score(4, "8 943");
    }

    public void testPath40() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(1, 35);
        goToPosition(0, 26);
        goToPosition(0, 63);
        verifyPosition(0, 106);
        goToPosition(1, 76);
        goToPosition(0, 99);
        verifyPosition(0, 109);
        goToPosition(1, 40);
        death();
    }

    public void testPath109() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 4);
        goToPosition(1, 6);
        verifyPosition(0, 8);
        goToPosition(1, 11);
        verifyPosition(1, 35);
        goToPosition(0, 26);
        goToPosition(0, 63);
        verifyPosition(0, 106);
        goToPosition(1, 76);
        goToPosition(0, 99);
        verifyPosition(1, 40);
        goToPosition(0, 109);
        goToPosition(0, 115);
        score(3, "87 614");
    }

    public void testPath87() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(1, 10);
        goToPosition(0, 9);
        verifyPosition(1, 12);
        goToPosition(0, 20);
        goToPosition(0, 102);
        verifyPosition(1, 98);
        goToPosition(0, 97);
        verifyPosition(1, 91);
        goToPosition(0, 87);
        death();
    }

    public void testPath91() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(1, 10);
        goToPosition(0, 9);
        verifyPosition(1, 12);
        goToPosition(0, 20);
        goToPosition(0, 102);
        verifyPosition(1, 98);
        goToPosition(0, 97);
        verifyPosition(0, 87);
        goToPosition(1, 91);
        goToPosition(0, 115);
        score(2, "865 890");
    }

    public void testPath71() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(1, 10);
        goToPosition(0, 9);
        verifyPosition(1, 12);
        goToPosition(0, 20);
        goToPosition(0, 102);
        verifyPosition(0, 97);
        goToPosition(1, 98);
        goToPosition(0, 64);
        verifyPosition(1, 110);
        goToPosition(0, 71);
        goToPosition(0, 115);
        score(5, "234");
    }

    public void testPath66() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(1, 10);
        goToPosition(0, 9);
        verifyPosition(1, 12);
        goToPosition(0, 20);
        goToPosition(0, 102);
        verifyPosition(0, 97);
        goToPosition(1, 98);
        goToPosition(0, 64);
        verifyPosition(0, 71);
        goToPosition(1, 110);
        goToPosition(0, 111);
        goToPosition(0, 44);
        verifyPosition(0, 61);
        goToPosition(1, 66);
        goToPosition(0, 115);
        score(4, "5 845");
    }

    public void testPath61() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(1, 10);
        goToPosition(0, 9);
        verifyPosition(1, 12);
        goToPosition(0, 20);
        goToPosition(0, 102);
        verifyPosition(0, 97);
        goToPosition(1, 98);
        goToPosition(0, 64);
        verifyPosition(0, 71);
        goToPosition(1, 110);
        goToPosition(0, 111);
        goToPosition(0, 44);
        verifyPosition(1, 66);
        goToPosition(0, 61);
        death();
    }

    public void testPath21() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(1, 10);
        goToPosition(0, 9);
        verifyPosition(0, 20);
        goToPosition(1, 12);
        goToPosition(0, 13);
        verifyPosition(0, 30);
        goToPosition(1, 18);
        verifyPosition(0, 22);
        goToPosition(1, 21);
        goToPosition(0, 115);
        score(2, "325 472");
    }

    public void testPath65() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(1, 10);
        goToPosition(0, 9);
        verifyPosition(0, 20);
        goToPosition(1, 12);
        goToPosition(0, 13);
        verifyPosition(0, 30);
        goToPosition(1, 18);
        verifyPosition(1, 21);
        goToPosition(0, 22);
        verifyPosition(0, 49);
        goToPosition(1, 65);
        death();
    }

    public void testPath49() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(1, 10);
        goToPosition(0, 9);
        verifyPosition(0, 20);
        goToPosition(1, 12);
        goToPosition(0, 13);
        verifyPosition(0, 30);
        goToPosition(1, 18);
        verifyPosition(1, 21);
        goToPosition(0, 22);
        verifyPosition(1, 65);
        goToPosition(0, 49);
        goToPosition(0, 115);
        score(3, "76 982");
    }

    public void testPath67() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(1, 10);
        goToPosition(0, 9);
        verifyPosition(0, 20);
        goToPosition(1, 12);
        goToPosition(0, 13);
        verifyPosition(1, 18);
        goToPosition(0, 30);
        goToPosition(0, 67);
        goToPosition(0, 115);
        score(3, "27 511");
    }

    public void testPath86() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(0, 9);
        goToPosition(1, 10);
        verifyPosition(1, 17);
        goToPosition(0, 14);
        goToPosition(0, 73);
        verifyPosition(1, 50);
        goToPosition(0, 68);
        verifyPosition(1, 94);
        goToPosition(0, 86);
        death();
    }

    public void testPath90() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(0, 9);
        goToPosition(1, 10);
        verifyPosition(1, 17);
        goToPosition(0, 14);
        goToPosition(0, 73);
        verifyPosition(1, 50);
        goToPosition(0, 68);
        verifyPosition(0, 86);
        goToPosition(1, 94);
        verifyPosition(0, 56);
        goToPosition(1, 114);
        goToPosition(0, 90);
        goToPosition(0, 115);
        score(2, "375 666");
    }

    public void testPath72() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(0, 9);
        goToPosition(1, 10);
        verifyPosition(1, 17);
        goToPosition(0, 14);
        goToPosition(0, 73);
        verifyPosition(1, 50);
        goToPosition(0, 68);
        verifyPosition(0, 86);
        goToPosition(1, 94);
        verifyPosition(1, 114);
        goToPosition(0, 56);
        goToPosition(0, 72);
        goToPosition(0, 115);
        score(5, "492");
    }

    public void testPath50A() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(0, 9);
        goToPosition(1, 10);
        verifyPosition(1, 17);
        goToPosition(0, 14);
        goToPosition(0, 73);
        verifyPosition(0, 68);
        goToPosition(1, 50);
        death();
    }

    public void testPath50B() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(0, 9);
        goToPosition(1, 10);
        verifyPosition(0, 14);
        goToPosition(1, 17);
        verifyPosition(0, 108);
        goToPosition(1, 50);
        death();
    }

    public void testPath60() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(0, 9);
        goToPosition(1, 10);
        verifyPosition(0, 14);
        goToPosition(1, 17);
        verifyPosition(1, 50);
        goToPosition(0, 108);
        goToPosition(0, 101);
        verifyPosition(1, 70);
        goToPosition(0, 52);
        goToPosition(0, 60);
        goToPosition(0, 115);
        score(1, "7 821 806");
    }

    public void testPath74() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(0, 9);
        goToPosition(1, 10);
        verifyPosition(0, 14);
        goToPosition(1, 17);
        verifyPosition(1, 50);
        goToPosition(0, 108);
        goToPosition(0, 101);
        verifyPosition(0, 52);
        goToPosition(1, 70);
        verifyPosition(0, 32);
        goToPosition(1, 74);
        goToPosition(0, 115);
        score(4, "2 877");
    }

    public void testPath32() {
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 6);
        goToPosition(0, 4);
        verifyPosition(0, 9);
        goToPosition(1, 10);
        verifyPosition(0, 14);
        goToPosition(1, 17);
        verifyPosition(1, 50);
        goToPosition(0, 108);
        goToPosition(0, 101);
        verifyPosition(0, 52);
        goToPosition(1, 70);
        verifyPosition(1, 74);
        goToPosition(0, 32);
        death();
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
