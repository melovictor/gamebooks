package hu.zagor.gamebooks.mvc.book.controller.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A bean for storing the CSS and JS files that need to be loaded for a specific book.
 * @author Tamas_Szekeres
 */
@Component
@Scope("prototype")
public class StaticResourceDescriptor {

    private final Set<String> css = new HashSet<>();
    private final Set<String> js = new HashSet<>();

    public Set<String> getCss() {
        return css;
    }

    public Set<String> getJs() {
        return js;
    }

}
