package hu.zagor.gamebooks.initiator.book;

import java.util.ArrayList;
import java.util.List;

public class CreateBookProject {

    public void create() {
        final List<BookLangData> books = new ArrayList<>();

        books.add(getSotkEn(true));
        books.add(getSotkHu(true));
        books.add(getSotkPtBr(true));

        new GenerateBookProjectFiles().generateBookProjectFiles(getBaseData(), books);
    }

    final BookBaseData baseData = new BookBaseData();

    private BookBaseData getBaseData() {

        baseData.setSeriesCode("ff");
        baseData.setTitleCode("cot");
        baseData.setPosition(5);
        baseData.setCollectorCode("ff");
        baseData.setCollectorName("fightingfantasy");
        baseData.setRuleset("ff");
        baseData.setHasEnemies(true);
        baseData.setHasInventory(true);
        baseData.setHasItems(true);
        baseData.setHasMap(false);
        baseData.setMediaProject(true);
        baseData.setDefaultSkillTestType("Le"); // Le: lower or equal; L: lower; if neither, should be empty!

        return baseData;
    }

    private BookLangData getSotkPtBr(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setSeriesCode("afb");
        data.setPosition(1);
        data.setBookId("AventurasFantasticas.A_CIDADE_DOS_LADROES");
        data.setLang("pt_BR");
        data.setTitle("A cidadela do caos");
        data.setGeneratable(shouldBeGenerated);
        return data;
    }

    private BookLangData getSotkHu(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setSeriesCode("kjk");
        data.setPosition(6);
        data.setBookId("KalandJatekKockazat.A_KAOSZ_FELLEGVARA");
        data.setLang("hu");
        data.setTitle("A Káosz Fellegvára");
        data.setGeneratable(shouldBeGenerated);
        return data;
    }

    private BookLangData getSotkEn(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setBookId("FightingFantasy.THE_CITADEL_OF_CHAOS");
        data.setLang("en");
        data.setTitle("The Citadel of Chaos");
        data.setGeneratable(shouldBeGenerated);
        return data;
    }

}
