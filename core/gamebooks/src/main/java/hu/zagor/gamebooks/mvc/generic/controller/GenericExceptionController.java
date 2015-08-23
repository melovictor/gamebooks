package hu.zagor.gamebooks.mvc.generic.controller;

import hu.zagor.gamebooks.mvc.login.domain.LoginData;

import org.springframework.beans.InvalidPropertyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for some specific use cases.
 * @author Tamas_Szekeres
 */
@ControllerAdvice
public class GenericExceptionController {

    /**
     * Method to handle some cases of the {@link InvalidPropertyException}.
     * @param exception the exception in question
     * @return the name of the view to go to
     */
    @ExceptionHandler(InvalidPropertyException.class)
    public String invalidPropertyException(final InvalidPropertyException exception) {
        String newView;
        if (exception.getBeanClass() == LoginData.class) {
            newView = "redirect:login";
        } else {
            throw exception;
        }
        return newView;
    }

}
