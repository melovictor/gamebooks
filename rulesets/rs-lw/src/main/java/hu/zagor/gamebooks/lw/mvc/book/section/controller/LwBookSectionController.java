package hu.zagor.gamebooks.lw.mvc.book.section.controller;

import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.lw.character.LwCharacterPageData;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.section.controller.RawBookSectionController;
import org.springframework.ui.Model;

public class LwBookSectionController extends RawBookSectionController {

    public LwBookSectionController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    public LwCharacterPageData getCharacterPageData(final Character character) {
        return (LwCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character, getInfo().getCharacterHandler());
    }

    @Override
    protected void addResources(final Model model) {
        super.addResources(model);
        addJsResource(model, "lw");
        addCssResource(model, "lw");
    }

}
