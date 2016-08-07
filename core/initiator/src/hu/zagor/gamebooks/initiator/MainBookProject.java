package hu.zagor.gamebooks.initiator;

import hu.zagor.gamebooks.initiator.book.BookBaseData;
import hu.zagor.gamebooks.initiator.book.BookLangData;
import hu.zagor.gamebooks.initiator.book.GenerateBookProjectFiles;
import java.util.ArrayList;
import java.util.List;

public class MainBookProject {

    public static void main(final String[] args) {
        new MainBookProject().create();
    }

    public void create() {
        final List<BookLangData> books = new ArrayList<>();

        books.add(getSotkEn(true));
        books.add(getSotkHu(true));
        books.add(getSotkPtBr(false));

        new GenerateBookProjectFiles().generateBookProjectFiles(getBaseData(), books);
    }

    private BookBaseData getBaseData() {
        final BookBaseData baseData = new BookBaseData();

        baseData.setMainLanguage("en");
        baseData.setSeriesCode("lw"); // ff, pt, wm, fff, sor, cyoa, cyoar, tm, lw
        baseData.setTitleCode("fotw");
        baseData.setPosition(2);
        baseData.setCollectorCode("lw"); // ff, cyoa, z, fyf, gyg, eq, tm, lw
        baseData.setCollectorName("lonewolf"); // fightingfantasy, chooseyourownadventure, endlessquest, timemachine, lonewolf
        baseData.setRuleset("lw"); // raw, tm, ff, lw
        baseData.setHasEnemies(true);
        baseData.setHasInventory(true);
        baseData.setHasItems(true);
        baseData.setHasMap(true);
        baseData.setMediaProject(true);
        baseData.setDefaultSkillTestType(""); // Le: lower or equal; L: lower; if neither, should be empty!
        baseData.setCharPageRequired(true);

        return baseData;
    }

    private BookLangData getSotkPtBr(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setSeriesCode("afb");
        data.setPosition(2);
        data.setBookId("AventurasFantasticas.O_FEITICEIRO_DA_MONTANHA_DE_FOGO");
        data.setLang("pt_BR");
        data.setTitle("O feiticeiro da montanha de fogo");
        data.setGeneratable(shouldBeGenerated);
        data.setFinished(false);
        return data;
    }

    private BookLangData getSotkHu(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setSeriesCode("mf");
        data.setPosition(2);
        data.setBookId("MaganyosFarkas.LANGOLO_TENGER");
        data.setLang("hu");
        data.setTitle("Lángoló tenger");
        data.setGeneratable(shouldBeGenerated);
        data.setFinished(false);
        return data;
    }

    private BookLangData getSotkEn(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setHidden(true);
        data.setBookId("LoneWolf.FIRE_ON_THE_WATER");
        data.setLang("en");
        data.setTitle("Fire on the Water");
        data.setGeneratable(shouldBeGenerated);
        data.setFinished(false);
        return data;
    }

}
