package hu.zagor.gamebooks.initiator.book;

import java.util.ArrayList;
import java.util.List;

public class CreateBookProject {

    public void create() {
        final List<BookLangData> books = new ArrayList<>();

        books.add(getSotkHu(false));
        books.add(getSotkEn(true));
        books.add(getSotkPtBr(false));

        new GenerateBookProjectFiles().generateBookProjectFiles(getBaseData(), books);
    }

    final BookBaseData baseData = new BookBaseData();

    private BookBaseData getBaseData() {

        baseData.setSeriesCode("z"); // ff, pt, wm, fff, sor
        baseData.setTitleCode("caq");
        baseData.setPosition(4);
        baseData.setCollectorCode("z");
        baseData.setCollectorName("zork");
        baseData.setRuleset("raw");
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
        return data;
    }

    private BookLangData getSotkHu(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setSeriesCode("kjv");
        data.setPosition(3);
        data.setBookId("KalandJatekVarazslat.HET_SARKANYKIGYO");
        data.setLang("hu");
        data.setTitle("Hét Sárkánykígyó");
        data.setGeneratable(shouldBeGenerated);
        return data;
    }

    private BookLangData getSotkEn(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setHidden(false);
        data.setBookId("Zork.CONQUEST_AT_QUENDOR");
        data.setLang("en");
        data.setTitle("Conquest at Quendor");
        data.setGeneratable(shouldBeGenerated);
        return data;
    }

}
