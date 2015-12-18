package hu.zagor.gamebooks.ff.sor.tsh.mvc.books.section.controller;

import hu.zagor.gamebooks.PageAddresses;
import hu.zagor.gamebooks.character.handler.item.FfCharacterItemHandler;
import hu.zagor.gamebooks.character.item.FfItem;
import hu.zagor.gamebooks.controller.session.HttpSessionWrapper;
import hu.zagor.gamebooks.ff.character.SorCharacter;
import hu.zagor.gamebooks.ff.mvc.book.section.controller.FfBookSectionController;
import hu.zagor.gamebooks.mvc.book.section.service.SectionHandlingService;
import hu.zagor.gamebooks.support.bookids.english.Sorcery;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling the section changes in the given book.
 * @author Tamas_Szekeres
 */
@Controller
@RequestMapping(value = PageAddresses.BOOK_PAGE + "/" + Sorcery.THE_SHAMUTANTI_HILLS)
public class Sor1BookSectionController extends FfBookSectionController {
    @Resource(name = "sorSwordItems") private Set<String> swordItemIds;

    /**
     * Constructor expecting the {@link SectionHandlingService} bean.
     * @param sectionHandlingService the {@link SectionHandlingService} bean
     */
    @Autowired
    public Sor1BookSectionController(@Qualifier("ffSectionHandlingService") final SectionHandlingService sectionHandlingService) {
        super(sectionHandlingService);
    }

    @Override
    public String handleFight(final Model model, final HttpServletRequest request, @RequestParam("id") final String enemyId, @RequestParam("hit") final Boolean luckOnHit,
        @RequestParam("def") final Boolean luckOnDefense, @RequestParam("oth") final Boolean luckOnOther) {
        setUpRagnarsBracelet(request);
        return super.handleFight(model, request, enemyId, luckOnHit, luckOnDefense, luckOnOther);
    }

    private void setUpRagnarsBracelet(final HttpServletRequest request) {
        final HttpSessionWrapper wrapper = getWrapper(request);
        final SorCharacter character = (SorCharacter) wrapper.getCharacter();
        final FfCharacterItemHandler itemHandler = getInfo().getCharacterHandler().getItemHandler();
        final FfItem item = (FfItem) itemHandler.getItem(character, "3020");

        if (item != null) {
            final FfItem equippedWeapon = itemHandler.getEquippedWeapon(character);
            if (isSword(equippedWeapon)) {
                item.setAttackStrength(2);
            } else {
                item.setAttackStrength(0);
            }
        }
    }

    private boolean isSword(final FfItem equippedWeapon) {
        return swordItemIds.contains(equippedWeapon.getId());
    }
}
