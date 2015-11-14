package hu.zagor.gamebooks.initiator.book;

import java.util.ArrayList;
import java.util.List;

public class CreateBookProject {

    public void create() {
        final List<BookLangData> books = new ArrayList<>();

        books.add(getSotkEn(true));
        books.add(getSotkHu(true));
        books.add(getSotkPtBr(false));

        new GenerateBookProjectFiles().generateBookProjectFiles(getBaseData(), books);
    }

    final BookBaseData baseData = new BookBaseData();

    private BookBaseData getBaseData() {

        baseData.setSeriesCode("ff");
        baseData.setTitleCode("mom");
        baseData.setPosition(23);
        baseData.setCollectorCode("ff");
        baseData.setCollectorName("fightingfantasy");
        baseData.setRuleset("ff");
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
        return data;
    }

    private BookLangData getSotkHu(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setSeriesCode("kjk");
        data.setPosition(15);
        data.setBookId("KalandJatekKockazat.A_PUSZTITAS_MASZKJAI");
        data.setLang("hu");
        data.setTitle("A Pusztítás Maszkjai");
        data.setGeneratable(shouldBeGenerated);
        return data;
    }

    private BookLangData getSotkEn(final boolean shouldBeGenerated) {
        final BookLangData data = new BookLangData();
        data.setHidden(true);
        data.setBookId("FightingFantasy.MASKS_OF_MAYHEM");
        data.setLang("en");
        data.setTitle("Masks of Mayhem");
        data.setGeneratable(shouldBeGenerated);
        return data;
    }

}
