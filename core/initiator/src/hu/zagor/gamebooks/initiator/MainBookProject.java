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

    final BookBaseData baseData = new BookBaseData();

    private BookBaseData getBaseData() {

        baseData.setSeriesCode("ff"); // ff, pt, wm, fff, sor, cyoa, cyoar, tm
        baseData.setTitleCode("b");
        baseData.setPosition(60);
        baseData.setCollectorCode("ff"); // ff, cyoa, z, fyf, gyg, eq, tm
        baseData.setCollectorName("fightingfantasy"); // fightingfantasy, chooseyourownadventure, endlessquest, timemachine
        baseData.setRuleset("ff"); // raw, tm, ff
        baseData.setHasEnemies(true);
        baseData.setHasInventory(true);
        baseData.setHasItems(true);
        baseData.setHasMap(true);
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
        data.setPosition(4);
        data.setBookId("KalandJatekKockazatZagor.VERESCSONT");
        data.setLang("hu");
        data.setTitle("Vérescsont");
        data.setGeneratable(shouldBeGenerated);
        data.setFinished(false);
        return data;
    }

    private BookLangData getSotkEn(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setHidden(true);
        data.setBookId("FightingFantasy.BLOODBONES");
        data.setLang("en");
        data.setTitle("Bloodbones");
        data.setGeneratable(shouldBeGenerated);
        data.setFinished(false);
        return data;
    }

}
