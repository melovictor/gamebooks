package hu.zagor.gamebooks.mvc.authorization.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Bean that contains and validates incoming new authorization names and codes.
 * @author Tamas_Szekeres
 */
public class AuthorizationCode {
    @NotNull @Pattern(regexp = "[a-z]+") private String type;
    @NotNull @Pattern(regexp = "\\{[0-9A-F]{8}\\-[0-9A-F]{4}\\-[0-9A-F]{4}\\-[0-9A-F]{4}\\-[0-9A-F]{12}\\}") private String code;

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

}
