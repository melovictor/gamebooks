package hu.zagor.gamebooks.ff.sor.tcok.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.Character;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.ff.sor.tcok.mvc.books.section.domain.DartThrowingResult;
import hu.zagor.gamebooks.ff.sor.tcok.mvc.books.section.service.DartThrowingService;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_CROWN_OF_KINGS)
public class Sor4BookSectionController extends FfBookSectionController {
    @Autowired private DartThrowingService dartThrowingService;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Sor4BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void handleAfterFight(final HttpSessionWrapper wrapper, final String enemyId) {
        super.handleAfterFight(wrapper, enemyId);
        final Character character = wrapper.getCharacter();
        if (character.getCommandView() == null) {
            final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
            itemHandler.removeItem(character, "4008", 1);
        }
    }

    /**
     * Handler for the Ten-Up game handling.
     * @param request the {@link HttpServletRequest} object
     * @return the new data for the system
     */
    @RequestMapping("throwNextDart")
    @ResponseBody
    public DartThrowingResult handleDartThrowing(final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        return dartThrowingService.throwDart(wrapper, getInfo());
    }

}
