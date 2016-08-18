package hu.zagor.gamebooks.ff.mvc.book.load.controller;

import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.content.SorParagraphData;
import hu.zagor.gamebooks.mvc.book.load.controller.GenericBookLoadController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;

/**
 * Generic load controller for Sorcery books.
 * @author Tamas_Szekeres
 */
public class SorBookLoadController extends FfBookLoadController {

    /**
     * Basic constructor that expects the spring id of the book's bean and passes it down to the {@link GenericBookLoadController}.
     * @param sectionHandlingService he {@link SectionHandlingService} that will handle the section changing
     */
    public SorBookLoadController(final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected Paragraph getDummyParagraph() {
        final Paragraph dummy = getBeanFactory().getBean(Paragraph.class);
        dummy.setData(getBeanFactory().getBean("sorParagraphData", SorParagraphData.class));
        return dummy;
    }
}
