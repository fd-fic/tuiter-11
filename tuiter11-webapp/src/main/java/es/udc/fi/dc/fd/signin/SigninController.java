package es.udc.fi.dc.fd.signin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * The Class SigninController.
 */
@Controller
public class SigninController {

    /**
     * Create the default Controller.
     */
    public SigninController() {

    }

    /**
     * Signin.
     *
     * @return the string
     */
    @RequestMapping(value = "signin")
    public String signin() {
        return "signin/signin";
    }

}
