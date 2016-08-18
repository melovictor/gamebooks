package hu.zagor.gamebooks.lw.mvc.book.load.controller;

import hu.zagor.gamebooks.books.saving.domain.SavedGameContainer;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.lw.character.LwCharacterPageData;
import hu.zagor.gamebooks.lw.content.LwParagraphData;
import hu.zagor.gamebooks.mvc.book.load.controller.GenericBookLoadController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.raw.mvc.book.load.controller.RawBookLoadController;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

/**
 * Generic load controller for Lone Wolf books.
 * @author Tamas_Szekeres
 */
public class LwBookLoadController extends RawBookLoadController {

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookLoadController}.
     * @param sectionHandlingService he {@link SectionHandlingService} that will handle the section changing
     */
    public LwBookLoadController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected String doLoad(final Model model, final HttpServletRequest request, final SavedGameContainer savedGameContainer) {
        addJsResource(model, "lw");
        addCssResource(model, "lw");
        return super.doLoad(model, request, savedGameContainer);
    }

    @Override
    public LwCharacterPageData getCharacterPageData(final Character character) {
        return (LwCharacterPageData) getBeanFactory().getBean(getInfo().getCharacterPageDataBeanId(), character, getInfo().getCharacterHandler());
    }

    @Override
    protected Paragraph getDummyParagraph() {
        final Paragraph dummy = getBeanFactory().getBean(Paragraph.class);
        dummy.setData(getBeanFactory().getBean("lwParagraphData", LwParagraphData.class));
        return dummy;
    }

}
