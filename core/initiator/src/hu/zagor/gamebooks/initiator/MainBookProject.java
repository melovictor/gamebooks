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
        books.add(getSotkHu(false));
        books.add(getSotkPtBr(false));

        new GenerateBookProjectFiles().generateBookProjectFiles(getBaseData(), books);
    }

    private BookBaseData getBaseData() {
        final BookBaseData baseData = new BookBaseData();

        baseData.setMainLanguage("en");
        baseData.setSeriesCode("eq"); // ff, pt, wm, fff, sor, cyoa, cyoar, tm, lw, eq, gyg, fyf
        baseData.setTitleCode("rtb");
        baseData.setPosition(4);
        baseData.setCollectorCode("eq"); // ff, cyoa, z, fyf, gyg, eq, tm, lw
        baseData.setCollectorName("endlessquest"); // fightingfantasy, chooseyourownadventure, endlessquest, timemachine, lonewolf
        baseData.setRuleset("raw"); // raw, tm, ff, lw
        baseData.setHasEnemies(false);
        baseData.setHasInventory(false);
        baseData.setHasItems(false);
        baseData.setHasMap(false);
        baseData.setMediaProject(true);
        baseData.setDefaultSkillTestType(""); // Le: lower or equal; L: lower; if neither, should be empty!
        baseData.setCharPageRequired(false);

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
        data.setHidden(false);
        data.setBookId("EndlessQuest.RETURN_TO_BROOKMERE");
        data.setLang("en");
        data.setTitle("Return to Brookmere");
        data.setGeneratable(shouldBeGenerated);
        data.setFinished(false);
        return data;
    }

}
