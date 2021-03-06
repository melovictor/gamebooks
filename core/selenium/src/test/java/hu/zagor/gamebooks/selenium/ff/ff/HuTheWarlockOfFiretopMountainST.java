package hu.zagor.gamebooks.selenium.ff.ff;

import hu.zagor.gamebooks.selenium.ff.BasicFfSeleniumTest;

import java.util.Arrays;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Selenium test for book KJK2.
 * @author Tamas_Szekeres
 */
@Test
public class HuTheWarlockOfFiretopMountainST extends BasicFfSeleniumTest {

    @BeforeClass
    public void setUpClass() {
        startBook("990162002", "kjk2");
    }

    public void testHappyPath() {
        setUpRandomRolls(Arrays.asList(1, 6, 6, 5, 6, 1, 2, 2, 5, 2, 2, 1, 1, 2, 5, 3, 1, 2, 2, 1, 1, 2, 5, 5, 3, 1, 1, 1, 2, 2, 2, 2, 1, 6, 2, 4, 6, 6, 4, 2, 3, 4, 3,
            6, 2, 6, 1, 1, 2, 2, 5, 5, 6, 1, 2, 2, 2, 2, 2, 2, 2, 2, 5, 6, 4, 4, 1, 4, 3, 5, 2, 1, 2, 2, 1, 5, 1, 1, 6, 4, 6, 2, 2, 4, 6, 2, 2, 2, 4, 1, 2, 2, 1, 6, 3,
            2, 1, 2, 1, 1, 3, 1, 4, 5, 6, 6, 1, 1, 1, 1, 2, 1, 2, 2, 3, 4, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 4, 3, 3, 3, 2, 2, 3, 6, 6, 6, 2, 5, 6, 6, 5, 1, 4, 1, 3, 3,
            2, 4, 5, 1, 2, 2, 5, 4, 5, 4, 2, 1, 2, 1, 3, 3, 5, 6, 2, 4, 4, 4, 5, 1, 2, 3, 3, 6, 1, 2, 5, 5, 5, 1, 2, 2, 2, 1, 2, 1, 2, 3, 2, 1, 3, 1, 2, 1, 2, 2, 1, 2,
            1, 2, 2, 4, 4, 1, 5, 1, 1, 6, 5, 5, 1, 2, 2, 5, 5, 4, 1, 2, 1, 5, 5, 4, 4, 2, 1, 6, 2, 1, 1, 3, 2, 2, 5, 1, 5, 3, 1, 5, 1, 2, 1, 1, 2, 2, 2, 1, 2, 2, 1, 2,
            2, 2, 2, 2, 1, 5, 4, 2, 3, 2, 2, 1, 2, 2, 4, 1, 2, 1, 1, 1, 2, 2, 5, 4, 1, 2, 4, 3, 6, 1, 2, 5, 6, 4, 4, 2, 4, 4, 4, 1, 2, 1, 1, 1, 3, 1, 1, 2, 2, 1, 2, 1,
            1, 1, 2, 2, 2, 2, 1, 2, 1, 6, 2, 5, 1, 1, 4, 3, 4, 2, 6, 1, 2, 6, 2, 3, 3, 3, 2, 2, 6, 1, 3, 5, 1, 1, 1, 1, 2, 1, 1, 2, 2, 2, 1, 1, 1, 1, 2, 2, 1, 2, 2, 2,
            2, 2, 2, 2, 1, 1, 2, 2, 1, 1));
        goToPosition("0", "background");
        verifyPosition(0, 1);
        goToPosition(0, 1);
        verifyPosition(0, 71);
        verifyPosition(1, 278);
        goToPosition(0, 71);
        doAttributeCheck();
        verifyPosition(0, 301);
        goToPosition(0, 301);
        verifyPosition(0, 82);
        verifyPosition(1, 208);
        goToPosition(0, 82);
        verifyPosition(0, 208);
        verifyPosition(1, 82);
        goToPosition(1, 82);
        doAttributeCheck();
        verifyPosition(0, 147);
        goToPosition(0, 147);
        verifyPosition(0, 208);
        takeItem("gold");
        goToPosition(0, 208);
        verifyPosition(0, 397);
        verifyPosition(1, 363);
        goToPosition(0, 397);
        verifyPosition(0, 240);
        verifyPosition(1, 363);
        goToPosition(0, 240);
        prepareLuckSettings(false, false, false);
        attackEnemy("3");
        verifyPosition(0, 145);
        goToPosition(0, 145);
        verifyPosition(0, 363);
        takeItem("3002");
        goToPosition(0, 363);
        verifyPosition(0, 370);
        verifyPosition(1, 42);
        goToPosition(0, 370);
        verifyPosition(0, 116);
        verifyPosition(1, 42);
        goToPosition(0, 116);
        prepareLuckSettings(false, false, false);
        attackEnemy("4");
        prepareLuckSettings(false, false, false);
        attackEnemy("4");
        prepareLuckSettings(false, false, false);
        attackEnemy("5");
        prepareLuckSettings(false, false, false);
        attackEnemy("5");
        prepareLuckSettings(false, false, false);
        attackEnemy("5");
        verifyPosition(0, 378);
        goToPosition(0, 378);
        verifyPosition(0, 296);
        verifyPosition(1, 42);
        goToPosition(0, 296);
        verifyPosition(0, 42);
        goToPosition(0, 42);
        verifyPosition(0, 257);
        verifyPosition(1, 113);
        goToPosition(1, 113);
        verifyPosition(0, 285);
        verifyPosition(1, 78);
        goToPosition(1, 78);
        verifyPosition(0, 159);
        verifyPosition(1, 237);
        goToPosition(0, 159);
        verifyPosition(0, 365);
        verifyPosition(1, 159);
        goToPosition(0, 365);
        prepareLuckSettings(false, false, false);
        attackEnemy("10");
        prepareLuckSettings(false, false, false);
        attackEnemy("10");
        prepareLuckSettings(false, false, false);
        attackEnemy("11");
        prepareLuckSettings(false, false, false);
        attackEnemy("11");
        prepareLuckSettings(false, false, false);
        attackEnemy("12");
        prepareLuckSettings(false, false, false);
        attackEnemy("12");
        prepareLuckSettings(false, false, false);
        attackEnemy("13");
        prepareLuckSettings(false, false, false);
        attackEnemy("14");
        prepareLuckSettings(false, false, false);
        attackEnemy("14");
        verifyPosition(1, 183);
        goToPosition(1, 183);
        verifyPosition(0, 266);
        verifyPosition(1, 237);
        goToPosition(0, 266);
        verifyPosition(0, 237);
        takeItem("3007");
        goToPosition(0, 237);
        verifyPosition(0, 285);
        goToPosition(0, 285);
        verifyPosition(0, 213);
        verifyPosition(1, 314);
        goToPosition(1, 314);
        verifyPosition(0, 223);
        verifyPosition(1, 300);
        goToPosition(0, 223);
        verifyPosition(0, 53);
        verifyPosition(1, 300);
        goToPosition(0, 53);
        doAttributeCheck();
        verifyPosition(0, 155);
        goToPosition(0, 155);
        verifyPosition(0, 300);
        replaceItem("3005", "3001");
        applyItem("3005");
        goToPosition(0, 300);
        verifyPosition(0, 102);
        verifyPosition(1, 303);
        goToPosition(1, 303);
        verifyPosition(0, 128);
        verifyPosition(1, 243);
        goToPosition(0, 128);
        verifyPosition(0, 210);
        verifyPosition(1, 58);
        goToPosition(1, 58);
        verifyPosition(0, 15);
        verifyPosition(1, 367);
        goToPosition(1, 367);
        verifyPosition(0, 235);
        verifyPosition(1, 323);
        goToPosition(1, 323);
        verifyPosition(0, 8);
        verifyPosition(1, 255);
        goToPosition(1, 255);
        verifyPosition(0, 193);
        verifyPosition(1, 93);
        goToPosition(0, 193);
        verifyPosition(0, 93);
        verifyPosition(1, 338);
        goToPosition(1, 338);
        prepareLuckSettings(false, false, false);
        attackEnemy("24");
        prepareLuckSettings(false, false, false);
        attackEnemy("24");
        prepareLuckSettings(false, false, false);
        attackEnemy("24");
        prepareLuckSettings(false, false, false);
        attackEnemy("24");
        prepareLuckSettings(false, false, false);
        attackEnemy("24");
        prepareLuckSettings(false, false, false);
        attackEnemy("24");
        verifyPosition(1, 75);
        goToPosition(1, 75);
        verifyPosition(0, 93);
        takeItem("3011");
        takeItem("3010");
        goToPosition(0, 93);
        verifyPosition(0, 8);
        goToPosition(0, 8);
        prepareLuckSettings(false, false, false);
        attackEnemy("25");
        prepareLuckSettings(false, false, false);
        attackEnemy("25");
        prepareLuckSettings(false, false, false);
        attackEnemy("25");
        prepareLuckSettings(false, false, false);
        attackEnemy("25");
        verifyPosition(1, 273);
        goToPosition(1, 273);
        verifyPosition(0, 189);
        takeItem("3012");
        takeItem("3013");
        goToPosition(0, 189);
        verifyPosition(0, 90);
        verifyPosition(1, 25);
        goToPosition(0, 90);
        verifyPosition(0, 253);
        goToPosition(0, 253);
        verifyPosition(0, 328);
        verifyPosition(1, 125);
        verifyPosition(2, 73);
        goToPosition(2, 73);
        verifyPosition(0, 218);
        goToPosition(0, 218);
        verifyPosition(0, 3);
        verifyPosition(1, 386);
        verifyPosition(2, 209);
        verifyPosition(3, 316);
        goToPosition(2, 209);
        doRandomRoll();
        verifyPosition(1, 47);
        goToPosition(1, 47);
        doRandomRoll();
        verifyPosition(1, 298);
        goToPosition(1, 298);
        doRandomRoll();
        verifyPosition(1, 7);
        goToPosition(1, 7);
        verifyPosition(0, 214);
        goToPosition(0, 214);
        verifyPosition(0, 271);
        verifyPosition(1, 104);
        verifyPosition(2, 99);
        goToPosition(0, 271);
        verifyPosition(0, 336);
        verifyPosition(1, 214);
        goToPosition(0, 336);
        verifyPosition(0, 66);
        verifyPosition(1, 172);
        verifyPosition(2, 249);
        goToPosition(2, 249);
        prepareLuckSettings(false, false, false);
        attackEnemy("35");
        prepareLuckSettings(false, false, false);
        attackEnemy("35");
        prepareLuckSettings(false, false, false);
        attackEnemy("35");
        verifyPosition(1, 66);
        verifyPosition(2, 304);
        goToPosition(2, 304);
        verifyPosition(0, 66);
        verifyPosition(1, 304);
        goToPosition(1, 304);
        prepareLuckSettings(false, false, false);
        attackEnemy("36");
        prepareLuckSettings(false, false, false);
        attackEnemy("36");
        prepareLuckSettings(false, false, false);
        attackEnemy("36");
        prepareLuckSettings(false, false, false);
        attackEnemy("36");
        verifyPosition(0, 203);
        goToPosition(0, 203);
        verifyPosition(0, 38);
        verifyPosition(1, 66);
        takeItem("3019");
        goToPosition(0, 38);
        verifyPosition(0, 66);
        takeItem("2000");
        goToPosition(0, 66);
        verifyPosition(0, 104);
        verifyPosition(1, 99);
        goToPosition(1, 99);
        verifyPosition(0, 383);
        goToPosition(0, 383);
        verifyPosition(0, 80);
        goToPosition(0, 80);
        verifyPosition(0, 129);
        verifyPosition(1, 123);
        verifyPosition(2, 195);
        verifyPosition(3, 140);
        goToPosition(1, 123);
        doRandomRoll();
        verifyPosition(0, 184);
        goToPosition(0, 184);
        verifyPosition(0, 322);
        verifyPosition(1, 34);
        goToPosition(0, 322);
        verifyPosition(0, 96);
        replaceItem("3020", "3012");
        goToPosition(0, 96);
        verifyPosition(0, 374);
        goToPosition(0, 374);
        verifyPosition(0, 207);
        goToPosition(0, 207);
        verifyPosition(0, 83);
        verifyPosition(1, 154);
        goToPosition(1, 154);
        verifyPosition(0, 41);
        goToPosition(0, 41);
        prepareLuckSettings(false, false, false);
        attackEnemy("43");
        verifyPosition(0, 310);
        goToPosition(0, 310);
        verifyPosition(0, 211);
        goToPosition(0, 211);
        verifyPosition(0, 211);
        verifyPosition(1, 211);
        goToPosition(1, 211);
        doAttributeCheck();
        verifyPosition(0, 135);
        goToPosition(0, 135);
        verifyPosition(0, 360);
        takeItem("gold");
        goToPosition(0, 360);
        verifyPosition(0, 89);
        goToPosition(0, 89);
        verifyPosition(0, 286);
        goToPosition(0, 286);
        verifyPosition(0, 294);
        verifyPosition(1, 275);
        verifyPosition(2, 148);
        verifyPosition(3, 107);
        goToPosition(0, 294);
        verifyPosition(1, 275);
        verifyPosition(2, 148);
        verifyPosition(3, 107);
        takeItem("gold");
        goToPosition(1, 275);
        doAttributeCheck();
        verifyPosition(0, 230);
        goToPosition(0, 230);
        prepareLuckSettings(false, false, false);
        attackEnemy("49");
        prepareLuckSettings(false, false, false);
        attackEnemy("49");
        prepareLuckSettings(false, false, false);
        attackEnemy("49");
        prepareLuckSettings(false, false, false);
        attackEnemy("49");
        verifyPosition(0, 390);
        goToPosition(0, 390);
        verifyPosition(1, 120);
        verifyPosition(2, 393);
        takeItem("3027");
        goToPosition(2, 393);
        verifyPosition(0, 212);
        verifyPosition(1, 369);
        takeItem("gold");
        goToPosition(0, 212);
        verifyPosition(1, 369);
        verifyPosition(2, 120);
        takeItem("3028");
        goToPosition(1, 369);
        verifyPosition(0, 109);
        goToPosition(0, 109);
        verifyPosition(0, 120);
        goToPosition(0, 120);
        verifyPosition(0, 197);
        goToPosition(0, 197);
        verifyPosition(0, 48);
        verifyPosition(1, 295);
        goToPosition(0, 48);
        verifyPosition(0, 391);
        verifyPosition(1, 60);
        goToPosition(0, 391);
        verifyPosition(0, 52);
        verifyPosition(1, 362);
        verifyPosition(2, 48);
        goToPosition(0, 52);
        verifyPosition(0, 391);
        verifyPosition(1, 362);
        verifyPosition(2, 354);
        verifyPosition(3, 234);
        verifyPosition(4, 291);
        goToPosition(2, 354);
        verifyPosition(0, 308);
        verifyPosition(1, 52);
        verifyPosition(2, 14);
        verifyPosition(3, 234);
        goToPosition(0, 308);
        verifyPosition(0, 187);
        verifyPosition(1, 54);
        verifyPosition(2, 160);
        verifyPosition(3, 354);
        goToPosition(1, 54);
        verifyPosition(0, 179);
        verifyPosition(1, 308);
        goToPosition(0, 179);
        prepareLuckSettings(false, false, false);
        attackEnemy("54");
        prepareLuckSettings(false, false, false);
        attackEnemy("54");
        prepareLuckSettings(false, false, false);
        attackEnemy("54");
        prepareLuckSettings(false, false, false);
        attackEnemy("54");
        prepareLuckSettings(false, false, false);
        attackEnemy("54");
        verifyPosition(1, 258);
        goToPosition(1, 258);
        verifyPosition(0, 54);
        takeItem("gold");
        takeItem("3029");
        goToPosition(0, 54);
        verifyPosition(0, 179);
        verifyPosition(1, 308);
        goToPosition(1, 308);
        verifyPosition(0, 187);
        verifyPosition(1, 54);
        verifyPosition(2, 160);
        verifyPosition(3, 354);
        goToPosition(2, 160);
        verifyPosition(0, 267);
        goToPosition(0, 267);
        verifyPosition(0, 312);
        verifyPosition(1, 246);
        verifyPosition(2, 79);
        verifyPosition(3, 349);
        goToPosition(1, 246);
        verifyPosition(0, 329);
        verifyPosition(1, 180);
        verifyPosition(2, 70);
        goToPosition(0, 329);
        verifyPosition(0, 157);
        verifyPosition(1, 392);
        verifyPosition(2, 299);
        verifyPosition(3, 238);
        goToPosition(2, 299);
        verifyPosition(0, 260);
        verifyPosition(1, 359);
        goToPosition(1, 359);
        verifyPosition(0, 190);
        verifyPosition(1, 385);
        verifyPosition(2, 94);
        verifyPosition(3, 121);
        goToPosition(1, 385);
        verifyPosition(0, 114);
        verifyPosition(1, 297);
        verifyPosition(2, 398);
        goToPosition(1, 297);
        verifyPosition(0, 150);
        verifyPosition(1, 256);
        goToPosition(0, 150);
        verifyPosition(0, 222);
        verifyPosition(1, 297);
        verifyPosition(2, 133);
        goToPosition(0, 222);
        verifyPosition(0, 85);
        goToPosition(0, 85);
        verifyPosition(0, 106);
        verifyPosition(1, 373);
        verifyPosition(2, 318);
        verifyPosition(3, 59);
        goToPosition(0, 106);
        verifyPosition(1, 152);
        verifyPosition(2, 26);
        goToPosition(2, 26);
        verifyPosition(0, 371);
        goToPosition(0, 371);
        verifyPosition(0, 274);
        goToPosition(0, 274);
        verifyPosition(0, 324);
        verifyPosition(1, 356);
        verifyPosition(2, 98);
        goToPosition(1, 356);
        verifyPosition(0, 358);
        goToPosition(0, 358);
        verifyPosition(0, 142);
        verifyPosition(1, 105);
        verifyPosition(2, 389);
        goToPosition(1, 105);
        verifyPosition(2, 382);
        verifyPosition(6, 142);
        goToPosition(2, 382);
        verifyPosition(0, 396);
        goToPosition(0, 396);
        verifyPosition(0, 242);
        goToPosition(0, 242);
        verifyPosition(0, 379);
        verifyPosition(1, 139);
        goToPosition(1, 139);
        verifyPosition(7, 276);
        verifyPosition(9, 288);
        verifyPosition(11, 321);
        goToPosition(9, 288);
        verifyPosition(0, 139);
        goToPosition(0, 139);
        verifyPosition(7, 276);
        verifyPosition(11, 321);
        goToPosition(11, 321);
        verifyPosition(0, 400);
        goToPosition(0, 400);
    }
}
