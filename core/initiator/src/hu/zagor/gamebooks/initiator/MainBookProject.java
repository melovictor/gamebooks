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
        baseData.setSeriesCode("gyg"); // ff, pt, wm, fff, sor, cyoa, cyoar, tm
        baseData.setTitleCode("eftcoh");
        baseData.setPosition(1);
        baseData.setCollectorCode("gyg"); // ff, cyoa, z, fyf, gyg, eq, tm
        baseData.setCollectorName("giveyourselfgoosebumps"); // fightingfantasy, chooseyourownadventure, endlessquest, timemachine
        baseData.setRuleset("raw"); // raw, tm, ff
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
        data.setSeriesCode("kjk");
        data.setPosition(11);
        data.setBookId("KalandJatekKockazat.A_REMULET_UTVESZTOJE");
        data.setLang("hu");
        data.setTitle("A rémület útvesztője");
        data.setGeneratable(shouldBeGenerated);
        data.setFinished(false);
        return data;
    }

    private BookLangData getSotkEn(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setHidden(false);
        data.setBookId("GiveYourselfGoosebumps.ESCAPE_FROM_THE_CARNIVAL_OF_HORRORS");
        data.setLang("en");
        data.setTitle("Escape from the Carnival of Horrors");
        data.setGeneratable(shouldBeGenerated);
        data.setFinished(false);
        return data;
    }

}
