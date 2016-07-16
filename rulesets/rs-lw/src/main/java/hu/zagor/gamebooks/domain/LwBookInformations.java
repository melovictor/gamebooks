package hu.zagor.gamebooks.domain;

public class LwBookInformations extends BookInformations {

    public LwBookInformations(final long id) {
        super(id);
        setCharacterBeanId("lwCharacter");
        setCharacterPageDataBeanId("lwCharacterPageData");
    }

}
