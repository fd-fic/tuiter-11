package es.udc.fi.dc.fd.signup;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.account.UserService;
import es.udc.fi.dc.fd.support.web.MessageHelper;


/**
 * The Class SignupController.
 */
@Controller
public class SignupController {

    /** The Constant SIGNUP_VIEW_NAME. */
    private static final String SIGNUP_VIEW_NAME = "signup/signup";

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The user service. */
    @Autowired
    private UserService userService;

    /**
     * Create the default Controller.
     */
    public SignupController() {

    }

    /**
     * Signup.
     *
     * @param model
     *            the model
     * @return the string
     */
    @RequestMapping(value = "signup")
    public String signup(Model model) {
        model.addAttribute(new SignupForm());
        return SIGNUP_VIEW_NAME;
    }

    /**
     * Signup.
     *
     * @param signupForm
     *            the signup form
     * @param errors
     *            the errors
     * @param ra
     *            the ra
     * @return the string
     */
    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String signup(@Valid @ModelAttribute SignupForm signupForm,
            Errors errors, RedirectAttributes ra) {

        boolean isUniqueEmail, isUniqueNick;

        if (errors.hasErrors()) {
            return SIGNUP_VIEW_NAME;
        } else {
            try {
                accountRepository.findByEmail(signupForm.getEmail());
                isUniqueEmail = false;
            } catch (UsernameNotFoundException e) {
                isUniqueEmail = true;

            }
            try {
                accountRepository.findByNick(signupForm.getNickName());
                isUniqueNick = false;
            } catch (UsernameNotFoundException e) {
                isUniqueNick = true;
            }
        }
        if (isUniqueEmail && isUniqueNick) {
            Account account = accountRepository
                    .save(signupForm.createAccount());
            userService.signin(account);
            // see /WEB-INF/i18n/messages.properties and
            // /WEB-INF/views/homeSignedIn.html
            MessageHelper.addSuccessAttribute(ra, "signup.success");
            return "redirect:/";
        } else if (isUniqueEmail) {
            return "redirect:signup?nickError=1";
        } else if (isUniqueNick) {
            return "redirect:signup?userError=1";
        } else {
            return "redirect:signup?userError=1&nickError=1";
        }
    }
}
