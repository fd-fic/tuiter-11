package es.udc.fi.dc.fd.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Throwables;


/**
 * General error handler for the application.
 */
@ControllerAdvice
class ExceptionHandler {

    /**
     * Create the default Exception Handler.
     */
    public ExceptionHandler() {

    }

    /**
     * Handle exceptions thrown by handlers.
     *
     * @param exception
     *            the exception
     * @param request
     *            the request
     * @return the model and view
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(
            value = Exception.class)
    public ModelAndView exception(Exception exception, WebRequest request) {
        ModelAndView modelAndView = new ModelAndView("error/general");
        modelAndView.addObject("errorMessage",
                Throwables.getRootCause(exception));
        return modelAndView;
    }
}