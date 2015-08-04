package hu.zagor.gamebooks.selenium.raw.sc;

import hu.zagor.gamebooks.selenium.BasicSeleniumTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Selenium test for book CsP3.
 * @author Tamas_Szekeres
 */
@Test
public class HuTheWeirdZoneST extends BasicSeleniumTest {

    @BeforeClass
    public void setUpClass() {
        startBook("990327003", "csp3");
    }

    public void testPath96() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(1, 20);
        goToPosition(0, 54);
        goToPosition(0, 70);
        verifyPosition(1, 67);
        goToPosition(0, 109);
        verifyPosition(1, 66);
        goToPosition(0, 64);
        goToPosition(0, 8);
        verifyPosition(1, 85);
        goToPosition(0, 96);
        death();
    }

    public void testPath50() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(1, 20);
        goToPosition(0, 54);
        goToPosition(0, 70);
        verifyPosition(1, 67);
        goToPosition(0, 109);
        verifyPosition(1, 66);
        goToPosition(0, 64);
        goToPosition(0, 8);
        verifyPosition(0, 96);
        goToPosition(1, 85);
        goToPosition(0, 50);
        goToPosition(0, 115);
        score("50 233");
    }

    public void testPath106() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(1, 20);
        goToPosition(0, 54);
        goToPosition(0, 70);
        verifyPosition(1, 67);
        goToPosition(0, 109);
        verifyPosition(0, 64);
        goToPosition(1, 66);
        goToPosition(0, 5);
        goToPosition(0, 106);
        goToPosition(0, 115);
        score("384");
    }

    public void testPath63() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(1, 20);
        goToPosition(0, 54);
        goToPosition(0, 70);
        verifyPosition(0, 109);
        goToPosition(1, 67);
        goToPosition(0, 78);
        verifyPosition(0, 49);
        goToPosition(1, 90);
        verifyPosition(0, 21);
        goToPosition(1, 36);
        goToPosition(0, 37);
        verifyPosition(1, 103);
        goToPosition(0, 111);
        verifyPosition(1, 39);
        goToPosition(0, 63);
        goToPosition(0, 115);
        score("68 993");
    }

    public void testPath39() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(1, 20);
        goToPosition(0, 54);
        goToPosition(0, 70);
        verifyPosition(0, 109);
        goToPosition(1, 67);
        goToPosition(0, 78);
        verifyPosition(0, 49);
        goToPosition(1, 90);
        verifyPosition(0, 21);
        goToPosition(1, 36);
        goToPosition(0, 37);
        verifyPosition(1, 103);
        goToPosition(0, 111);
        verifyPosition(0, 63);
        goToPosition(1, 39);
        death();
    }

    public void testPath103() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(1, 20);
        goToPosition(0, 54);
        goToPosition(0, 70);
        verifyPosition(0, 109);
        goToPosition(1, 67);
        goToPosition(0, 78);
        verifyPosition(0, 49);
        goToPosition(1, 90);
        verifyPosition(0, 21);
        goToPosition(1, 36);
        goToPosition(0, 37);
        verifyPosition(0, 111);
        goToPosition(1, 103);
        goToPosition(0, 115);
        score("437 750");
    }

    public void testPath59() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(1, 20);
        goToPosition(0, 54);
        goToPosition(0, 70);
        verifyPosition(0, 109);
        goToPosition(1, 67);
        goToPosition(0, 78);
        verifyPosition(0, 49);
        goToPosition(1, 90);
        verifyPosition(1, 36);
        goToPosition(0, 21);
        goToPosition(0, 59);
        goToPosition(0, 115);
        score("37 864");
    }

    public void testPath49() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(1, 20);
        goToPosition(0, 54);
        goToPosition(0, 70);
        verifyPosition(0, 109);
        goToPosition(1, 67);
        goToPosition(0, 78);
        verifyPosition(1, 90);
        goToPosition(0, 49);
        death();
    }

    public void testPath12() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(0, 54);
        goToPosition(1, 20);
        verifyPosition(1, 91);
        goToPosition(0, 71);
        goToPosition(0, 89);
        verifyPosition(0, 58);
        goToPosition(1, 12);
        death();
    }

    public void testPath45() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(0, 54);
        goToPosition(1, 20);
        verifyPosition(1, 91);
        goToPosition(0, 71);
        goToPosition(0, 89);
        verifyPosition(1, 12);
        goToPosition(0, 58);
        goToPosition(0, 114);
        verifyPosition(1, 46);
        goToPosition(0, 45);
        goToPosition(0, 115);
        score("4 983 210");
    }

    public void testPath87() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(0, 54);
        goToPosition(1, 20);
        verifyPosition(1, 91);
        goToPosition(0, 71);
        goToPosition(0, 89);
        verifyPosition(1, 12);
        goToPosition(0, 58);
        goToPosition(0, 114);
        verifyPosition(0, 45);
        goToPosition(1, 46);
        verifyPosition(1, 17);
        goToPosition(0, 87);
        goToPosition(0, 115);
        score("3 286 347");
    }

    public void testPath17() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(0, 54);
        goToPosition(1, 20);
        verifyPosition(1, 91);
        goToPosition(0, 71);
        goToPosition(0, 89);
        verifyPosition(1, 12);
        goToPosition(0, 58);
        goToPosition(0, 114);
        verifyPosition(0, 45);
        goToPosition(1, 46);
        verifyPosition(0, 87);
        goToPosition(1, 17);
        goToPosition(0, 115);
        score("1 625 009");
    }

    public void testPath31() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(0, 54);
        goToPosition(1, 20);
        verifyPosition(0, 71);
        goToPosition(1, 91);
        goToPosition(0, 62);
        verifyPosition(0, 92);
        goToPosition(1, 79);
        verifyPosition(1, 51);
        goToPosition(0, 23);
        goToPosition(0, 31);
        goToPosition(0, 115);
        score("21 666");
    }

    public void testPath30() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(0, 54);
        goToPosition(1, 20);
        verifyPosition(0, 71);
        goToPosition(1, 91);
        goToPosition(0, 62);
        verifyPosition(0, 92);
        goToPosition(1, 79);
        verifyPosition(0, 23);
        goToPosition(1, 51);
        goToPosition(0, 86);
        verifyPosition(1, 41);
        goToPosition(0, 7);
        verifyPosition(0, 44);
        goToPosition(1, 94);
        goToPosition(0, 30);
        death();
    }

    public void testPath44() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(0, 54);
        goToPosition(1, 20);
        verifyPosition(0, 71);
        goToPosition(1, 91);
        goToPosition(0, 62);
        verifyPosition(0, 92);
        goToPosition(1, 79);
        verifyPosition(0, 23);
        goToPosition(1, 51);
        goToPosition(0, 86);
        verifyPosition(1, 41);
        goToPosition(0, 7);
        verifyPosition(1, 94);
        goToPosition(0, 44);
        goToPosition(0, 115);
        score("711 049");
    }

    public void testPath41() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(0, 54);
        goToPosition(1, 20);
        verifyPosition(0, 71);
        goToPosition(1, 91);
        goToPosition(0, 62);
        verifyPosition(0, 92);
        goToPosition(1, 79);
        verifyPosition(0, 23);
        goToPosition(1, 51);
        goToPosition(0, 86);
        verifyPosition(0, 7);
        goToPosition(1, 41);
        death();
    }

    public void testPath108() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(0, 54);
        goToPosition(1, 20);
        verifyPosition(0, 71);
        goToPosition(1, 91);
        goToPosition(0, 62);
        verifyPosition(1, 79);
        goToPosition(0, 92);
        goToPosition(0, 93);
        verifyPosition(1, 55);
        goToPosition(0, 108);
        death();
    }

    public void testPath107() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(0, 47);
        goToPosition(1, 25);
        verifyPosition(0, 54);
        goToPosition(1, 20);
        verifyPosition(0, 71);
        goToPosition(1, 91);
        goToPosition(0, 62);
        verifyPosition(1, 79);
        goToPosition(0, 92);
        goToPosition(0, 93);
        verifyPosition(0, 108);
        goToPosition(1, 55);
        goToPosition(0, 107);
        goToPosition(0, 115);
        score("803 101");
    }

    public void testPath95() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(0, 32);
        goToPosition(1, 40);
        verifyPosition(1, 69);
        verifyPosition(2, 76);
        goToPosition(0, 98);
        goToPosition(0, 35);
        verifyPosition(1, 80);
        goToPosition(0, 14);
        goToPosition(0, 95);
        goToPosition(0, 115);
        score("3 281");
    }

    public void testPath72() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(0, 32);
        goToPosition(1, 40);
        verifyPosition(1, 69);
        verifyPosition(2, 76);
        goToPosition(0, 98);
        goToPosition(0, 35);
        verifyPosition(0, 14);
        goToPosition(1, 80);
        goToPosition(0, 101);
        verifyPosition(0, 99);
        goToPosition(1, 13);
        goToPosition(0, 72);
        goToPosition(0, 115);
        score("509 883");
    }

    public void testPath99() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(0, 32);
        goToPosition(1, 40);
        verifyPosition(1, 69);
        verifyPosition(2, 76);
        goToPosition(0, 98);
        goToPosition(0, 35);
        verifyPosition(0, 14);
        goToPosition(1, 80);
        goToPosition(0, 101);
        verifyPosition(1, 13);
        goToPosition(0, 99);
        death();
    }

    public void testPath110() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(0, 32);
        goToPosition(1, 40);
        verifyPosition(1, 69);
        verifyPosition(0, 98);
        goToPosition(2, 76);
        goToPosition(0, 82);
        verifyPosition(0, 104);
        goToPosition(1, 112);
        goToPosition(0, 15);
        verifyPosition(1, 29);
        goToPosition(0, 110);
        goToPosition(0, 115);
        score("47 028");
    }

    public void testPath29() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(0, 32);
        goToPosition(1, 40);
        verifyPosition(1, 69);
        verifyPosition(0, 98);
        goToPosition(2, 76);
        goToPosition(0, 82);
        verifyPosition(0, 104);
        goToPosition(1, 112);
        goToPosition(0, 15);
        verifyPosition(0, 110);
        goToPosition(1, 29);
        goToPosition(0, 115);
        score("625");
    }

    public void testPath74() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(0, 32);
        goToPosition(1, 40);
        verifyPosition(1, 69);
        verifyPosition(0, 98);
        goToPosition(2, 76);
        goToPosition(0, 82);
        verifyPosition(1, 112);
        goToPosition(0, 104);
        goToPosition(0, 74);
        death();
    }

    public void testPath81() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(0, 32);
        goToPosition(1, 40);
        verifyPosition(0, 98);
        verifyPosition(2, 76);
        goToPosition(1, 69);
        goToPosition(0, 81);
        death();
    }

    public void testPath52() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(1, 40);
        goToPosition(0, 32);
        verifyPosition(1, 10);
        goToPosition(0, 19);
        goToPosition(0, 68);
        verifyPosition(1, 28);
        goToPosition(0, 84);
        verifyPosition(0, 6);
        goToPosition(1, 52);
        death();
    }

    public void testPath24() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(1, 40);
        goToPosition(0, 32);
        verifyPosition(1, 10);
        goToPosition(0, 19);
        goToPosition(0, 68);
        verifyPosition(1, 28);
        goToPosition(0, 84);
        verifyPosition(1, 52);
        goToPosition(0, 6);
        goToPosition(0, 18);
        verifyPosition(1, 73);
        goToPosition(0, 24);
        death();
    }

    public void testPath73() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(1, 40);
        goToPosition(0, 32);
        verifyPosition(1, 10);
        goToPosition(0, 19);
        goToPosition(0, 68);
        verifyPosition(1, 28);
        goToPosition(0, 84);
        verifyPosition(1, 52);
        goToPosition(0, 6);
        goToPosition(0, 18);
        verifyPosition(0, 24);
        goToPosition(1, 73);
        goToPosition(0, 115);
        score("621 909");
    }

    public void testPath28() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(1, 40);
        goToPosition(0, 32);
        verifyPosition(1, 10);
        goToPosition(0, 19);
        goToPosition(0, 68);
        verifyPosition(0, 84);
        goToPosition(1, 28);
        goToPosition(0, 115);
        score("978 542");
    }

    public void testPath10() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(0, 16);
        goToPosition(1, 83);
        goToPosition(0, 56);
        verifyPosition(1, 40);
        goToPosition(0, 32);
        verifyPosition(0, 19);
        goToPosition(1, 10);
        death();
    }

    public void testPath57() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(1, 83);
        goToPosition(0, 16);
        goToPosition(0, 77);
        verifyPosition(1, 65);
        goToPosition(0, 26);
        goToPosition(0, 113);
        verifyPosition(0, 43);
        goToPosition(1, 53);
        verifyPosition(1, 27);
        goToPosition(0, 57);
        death();
    }

    public void testPath4() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(1, 83);
        goToPosition(0, 16);
        goToPosition(0, 77);
        verifyPosition(1, 65);
        goToPosition(0, 26);
        goToPosition(0, 113);
        verifyPosition(0, 43);
        goToPosition(1, 53);
        verifyPosition(0, 57);
        goToPosition(1, 27);
        goToPosition(0, 4);
        goToPosition(0, 115);
        score("7 790");
    }

    public void testPath43() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(1, 83);
        goToPosition(0, 16);
        goToPosition(0, 77);
        verifyPosition(1, 65);
        goToPosition(0, 26);
        goToPosition(0, 113);
        verifyPosition(1, 53);
        goToPosition(0, 43);
        goToPosition(0, 115);
        score("79 128");
    }

    public void testPath38() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(1, 83);
        goToPosition(0, 16);
        goToPosition(0, 77);
        verifyPosition(0, 26);
        goToPosition(1, 65);
        goToPosition(0, 97);
        verifyPosition(0, 33);
        goToPosition(1, 61);
        goToPosition(0, 11);
        goToPosition(0, 100);
        verifyPosition(0, 42);
        goToPosition(1, 38);
        death();
    }

    public void testPath42() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(1, 83);
        goToPosition(0, 16);
        goToPosition(0, 77);
        verifyPosition(0, 26);
        goToPosition(1, 65);
        goToPosition(0, 97);
        verifyPosition(0, 33);
        goToPosition(1, 61);
        goToPosition(0, 11);
        goToPosition(0, 100);
        verifyPosition(1, 38);
        goToPosition(0, 42);
        goToPosition(0, 115);
        score("4 113");
    }

    public void testPath105() {
        goToPosition(0, 1);
        goToPosition(0, 2);
        goToPosition(0, 3);
        verifyPosition(1, 25);
        goToPosition(0, 47);
        verifyPosition(1, 83);
        goToPosition(0, 16);
        goToPosition(0, 77);
        verifyPosition(0, 26);
        goToPosition(1, 65);
        goToPosition(0, 97);
        verifyPosition(1, 61);
        goToPosition(0, 33);
        goToPosition(0, 105);
        goToPosition(0, 115);
        score("81 300");
    }

    private void score(final String score) {
        verifyText(score);
    }

    private void death() {
        verifyText("VÉGED!");
        ending();
    }

}
