package hu.zagor.gamebooks.mvc.authorization.controller;

import hu.zagor.gamebooks.mvc.authorization.domain.AuthorizationCode;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for updating the authorization codes.
 * @author Tamas_Szekeres
 */
@Controller
public class AuthorizationCodeUpdateController {
    @Resource(name = "authorizationCodeContainer") private Map<String, String> authorizationCodeContainer;

    /**
     * Update a specific authorization code.
     * @param form the form containing the incoming values
     * @param bindingResult {@link BindingResult} bean with the validation result
     */
    @RequestMapping(value = "authCode")
    @ResponseBody
    public void updateAuthorizationCode(@Valid final AuthorizationCode form, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException();
        }

        authorizationCodeContainer.put(form.getType(), form.getCode());
    }

}
