package hu.zagor.gamebooks.mvc.book.exception.controller;

import hu.zagor.gamebooks.exception.InvalidStepChoiceException;
import hu.zagor.gamebooks.support.logging.LogInject;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Class for handling the exceptions for the different book rules.
 * @author Tamas_Szekeres
 */
public class GenericBookExceptionController {

    private static final String IS_ERROR_PAGE = "isErrorPage";
    private static final String PROBLEM_CODE = "problemCode";
    private static final String BOOK_ERROR_VIEW = "bookError";
    private static final String BOOK_ERROR_HANDLER_WARNING = "Book error handler kicked in!";
    @LogInject
    private Logger logger;

    /**
     * Handles whatever other unexpected exception occurs.
     * @param throwable the throwable that occurred
     * @return the error page
     */
    @ExceptionHandler(Throwable.class)
    public ModelAndView errorHandler(final Throwable throwable) {
        final ModelAndView mav = new ModelAndView(BOOK_ERROR_VIEW);
        final Map<String, Object> model = mav.getModel();
        model.put(PROBLEM_CODE, "page.book.error.unexpected");
        model.put(IS_ERROR_PAGE, true);
        logger.warn(BOOK_ERROR_HANDLER_WARNING, throwable);
        return mav;
    }

    /**
     * Handles {@link InvalidStepChoiceException} exceptions.
     * @param throwable the throwable that occurred
     * @return the error page
     */
    @ExceptionHandler(InvalidStepChoiceException.class)
    public ModelAndView errorHandler(final InvalidStepChoiceException throwable) {
        final ModelAndView mav = new ModelAndView(BOOK_ERROR_VIEW);
        final Map<String, Object> model = mav.getModel();
        model.put(PROBLEM_CODE, "page.book.error.invalidstep");
        model.put(IS_ERROR_PAGE, true);
        logger.warn(BOOK_ERROR_HANDLER_WARNING, throwable);
        return mav;
    }

}
