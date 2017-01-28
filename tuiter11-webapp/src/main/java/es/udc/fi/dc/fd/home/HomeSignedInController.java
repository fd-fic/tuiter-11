package es.udc.fi.dc.fd.home;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.account.UserService;
import es.udc.fi.dc.fd.favorite.FavoriteRepository;
import es.udc.fi.dc.fd.retuit.RetuitRepository;
import es.udc.fi.dc.fd.support.web.MessageHelper;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitRepository;
import es.udc.fi.dc.fd.util.DictionaryLoader;


/**
 * The Class HomeSignedInController.
 */
@Controller
@Secured("ROLE_USER")
public class HomeSignedInController {

    /** The Constant PROFILE_EDIT_VIEW. */
    private static final String PROFILE_EDIT_VIEW = "/profile/edit";

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /** The retuit repository. */
    @Autowired
    private RetuitRepository retuitRepository;

    /** The favorite repository. */
    @Autowired
    private FavoriteRepository favoriteRepository;

    /** The user service. */
    @Autowired
    private UserService userService;

    /** The password encoder. */
    @Inject
    private PasswordEncoder passwordEncoder;

    /** The bad words. */
    private static Set<String> badWords = DictionaryLoader.getDictionary();

    /**
     * Create the default controller.
     */
    public HomeSignedInController() {

    }

    // ------------------ MODEL ATRIBUTES --------------------------

    /**
     * Gets the account.
     *
     * @param principal
     *            the principal
     * @return the account
     */
    @ModelAttribute("myAccount")
    public Account getAccount(Principal principal) {
        return accountRepository.findByEmail(principal.getName());
    }

    /**
     * Gets the my retuits.
     *
     * @param principal
     *            the principal
     * @return the my retuits
     */
    @ModelAttribute("myRetuitedTuits")
    public List<Tuit> getMyRetuits(Principal principal) {
        if (principal == null) {
            return null;
        }
        return retuitRepository.findTuitsRetuitedBy(principal.getName());

    }

    /**
     * Gets the my favorite tuits.
     *
     * @param principal
     *            the principal
     * @return the my favorite tuits
     */
    @ModelAttribute("myFavoriteTuits")
    public List<Tuit> getMyFavoriteTuits(Principal principal) {
        if (principal == null) {
            return null;
        }
        List<Tuit> tuits = favoriteRepository.findFavoriteTuitsOf(principal
                .getName());
        return tuits;
    }

    // ------------------ REQUEST MAPPINGS -------------------------

    /**
     * Show profile.
     *
     * @param principal
     *            the principal
     * @return the string
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String showProfile(Principal principal) {
        return "redirect:/"
                + accountRepository.findByEmail(principal.getName())
                        .getNickName();
    }

    /**
     * Edits the profile.
     *
     * @param model
     *            the model
     * @return the string
     */
    @RequestMapping(value = "/profile/edit", method = RequestMethod.GET)
    public String editProfile(Model model) {
        model.addAttribute(new ProfileForm());
        return "/profile/edit";
    }

    /**
     * Edits the profile.
     *
     * @param profileForm
     *            the profile form
     * @param principal
     *            the principal
     * @param webRequest
     *            the web request
     * @param ra
     *            the ra
     * @param errors
     *            the errors
     * @return the string
     */
    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
    public String editProfile(@ModelAttribute ProfileForm profileForm,
            Principal principal, final WebRequest webRequest,
            RedirectAttributes ra, Errors errors) {

        // Obtenemos los datos de la cuenta que está logueada
        Account accSession = accountRepository.findByEmail(principal.getName());

        String resultErrors = validateFields(accSession, errors, profileForm);

        // Si hay errores salimos los devolvemos
        if (resultErrors != null) {
            return resultErrors;
        }

        // Comprobamos el estado del checkbox y actualizamos la privacidad
        if (webRequest.getParameter("active") == null) {
            accSession.setPrivateAccount(null);
        } else {
            accSession.setPrivateAccount(1);
        }

        // Si las contraseñas no son vacías las actualiza
        if (!((profileForm.getOldPassword().isEmpty()) && (profileForm
                .getPassword().isEmpty()))) {
            accountRepository.update(profileForm.updateAccount(accSession));
        } else {
            userService.signin(accountRepository.updateWithoutPass(accSession));
        }
        userService.signin(accSession);
        MessageHelper.addSuccessAttribute(ra, "update.success");

        return "redirect:/";
    }

    /**
     * Return the page error, or null if has no errors.
     *
     * @param myAccount the my account
     * @param errors the errors
     * @param profileForm the profile form
     * @return the string
     */
    private String validateFields(Account myAccount, Errors errors,
            ProfileForm profileForm) {

        if (errors.hasErrors()) {
            return PROFILE_EDIT_VIEW;
        }

        boolean existsEmail, existsNickName, validEmail, validNick, emptyPasswords;
        try {
            accountRepository.findByEmail(profileForm.getEmail());
            existsEmail = true;
        } catch (UsernameNotFoundException e) {
            existsEmail = false;
        }
        validEmail = (myAccount.getEmail().equals(profileForm.getEmail()))
                || !existsEmail;

        try {
            accountRepository.findByNick(profileForm.getNickName());
            existsNickName = true;
        } catch (UsernameNotFoundException e) {
            existsNickName = false;
        }

        validNick = (myAccount.getNickName().equals(profileForm.getNickName()))
                || !existsNickName;

        emptyPasswords = ((profileForm.getOldPassword().isEmpty()) && (profileForm
                .getPassword().isEmpty()));
        // comparamos que no esten en base de datos si se introducen nuevos
        // datos sobre nick o email
        if (!emptyPasswords
                && !isCorrectPass(myAccount, profileForm.getOldPassword())) {
            return "redirect:edit?passError=1";
        }
        if (!validNick && !validEmail) {
            return "redirect:edit?userError=1&nickError=1";
        }
        if (!validNick) {
            return "redirect:edit?nickError=1";
        }
        if (!validEmail) {
            return "redirect:edit?userError=1";
        }
        return null;
    }

    /**
     * Checks if is correct pass.
     *
     * @param acc the acc
     * @param pass the pass
     * @return true, if is correct pass
     */
    private boolean isCorrectPass(Account acc, String pass) {
        return passwordEncoder.matches(pass, acc.getPassword());
    }

    /**
     * Show tuits.
     *
     * @return the string
     */
    @RequestMapping(value = "/favorites", method = RequestMethod.GET)
    public String showTuits() {
        return "home/favorites";
    }

    /**
     * Process tuit.
     *
     * @param tuitForm
     *            the tuit form
     * @param principal
     *            the principal
     * @return the string
     */
    @RequestMapping(value = "processTuit", method = RequestMethod.POST)
    public String processTuit(@ModelAttribute("tuitForm") TuitForm tuitForm,
            Principal principal) {
        String tuitMessage = tuitForm.getTuit();
        if (!tuitMessage.isEmpty()) {
            Account acc = accountRepository.findByEmail(principal.getName());
            Tuit tuit = new Tuit(Long.valueOf(Calendar.getInstance()
                    .getTimeInMillis()), tuitMessage, acc);
            String[] messageSplitted = tuitMessage.split(" ");
            for (String word : messageSplitted) {
                if (badWords.contains(word)) {
                    tuitRepository.saveOffensiveTuit(tuit);
                    return "redirect:/";
                }
            }
            tuitRepository.save(tuit);
        }
        return "redirect:/";
    }
}
