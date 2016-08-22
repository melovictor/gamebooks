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
        baseData.setSeriesCode("ff"); // ff, pt, wm, fff, sor, cyoa, cyoar, tm, lw, eq, gyg, fyf
        baseData.setTitleCode("bnc");
        baseData.setPosition(25);
        baseData.setCollectorCode("ff"); // ff, cyoa, z, fyf, gyg, eq, tm, lw
        baseData.setCollectorName("fightingfantasy"); // fightingfantasy, chooseyourownadventure, endlessquest, timemachine, lonewolf
        baseData.setRuleset("ff"); // raw, tm, ff, lw
        baseData.setHasEnemies(true);
        baseData.setHasInventory(true);
        baseData.setHasItems(true);
        baseData.setHasMap(false);
        baseData.setMediaProject(true);
        baseData.setDefaultSkillTestType("Le"); // Le: lower or equal; L: lower; if neither, should be empty!
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
        data.setSeriesCode("kjkz");
        data.setPosition(9);
        data.setBookId("KalandJatekKockazatZagor.A_LIDERCKASTELY_MELYEN");
        data.setLang("hu");
        data.setTitle("A Lidérckastély mélyén");
        data.setGeneratable(shouldBeGenerated);
        data.setFinished(false);
        return data;
    }

    private BookLangData getSotkEn(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setHidden(true);
        data.setBookId("FightingFantasy.BENEATH_NIGHTMARE_CASTLE");
        data.setLang("en");
        data.setTitle("Beneath Nightmare Castle");
        data.setGeneratable(shouldBeGenerated);
        data.setFinished(false);
        return data;
    }

}
