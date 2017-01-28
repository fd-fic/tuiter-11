package es.udc.fi.dc.fd.error;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


/**
 * The Class UsernameNotFoundExceptionHandler.
 */
@ControllerAdvice
public class UsernameNotFoundExceptionHandler {

    /**
     * Handle username not found.
     *
     * @param ex
     *            the ex
     * @return the model and view
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView handleUsernameNotFound(UsernameNotFoundException ex) {
        return errorModelAndView(ex);
    }

    /**
     * Get the users details for the 'personal' page.
     *
     * @param ex
     *            the ex
     * @return the model and view
     */
    private ModelAndView errorModelAndView(Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/userNotFoundError");
        modelAndView.addObject("notFoundUser", ex.getMessage());

        return modelAndView;
    }
}
