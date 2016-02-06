package hu.zagor.gamebooks.ff.ff.trok.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.content.Paragraph;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.FfCharacter;
import hu.zagor.gamebooks.ff.ff.trok.character.Ff15Character;
import hu.zagor.gamebooks.ff.ff.trok.character.domain.Ff15ShipAttributes;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.FightingFantasy;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + FightingFantasy.THE_RINGS_OF_KETHER)
public class Ff15BookSectionController extends FfBookSectionController {
    @Autowired private HttpServletRequest request;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Ff15BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    protected void prepareFight(final HttpSessionWrapper wrapper) {
        super.prepareFight(wrapper);
        final FfCharacter character = (FfCharacter) wrapper.getCharacter();
        getInfo().getCharacterHandler().getInteractionHandler().setFightCommand(character, "missile", String.valueOf("true".equals(request.getParameter("missile"))));
    }

    @Override
    protected void handleCustomSectionsPre(final Model model, final HttpSessionWrapper wrapper, final boolean changedSection) {
        final Paragraph paragraph = wrapper.getParagraph();
        final Ff15Character character = (Ff15Character) wrapper.getCharacter();
        final Ff15ShipAttributes shipAttributes = character.getShipAttributes();

        if ("48".equals(paragraph.getId())) {
            shipAttributes.setShield(shipAttributes.getShield() - 1);
            if (shipAttributes.getShield() <= 0) {
                paragraph.getData().getChoices().clear();
            }
        } else if ("351".equals(paragraph.getId())) {
            shipAttributes.setShield(shipAttributes.getShield() - 2);
            if (shipAttributes.getShield() <= 0) {
                paragraph.getData().getChoices().clear();
            }
        } else if ("87b".equals(paragraph.getId())) {
            shipAttributes.setShield(shipAttributes.getShield());
            shipAttributes.setWeaponStrength(shipAttributes.getWeaponStrength());
        }
        super.handleCustomSectionsPre(model, wrapper, changedSection);
    }

}
