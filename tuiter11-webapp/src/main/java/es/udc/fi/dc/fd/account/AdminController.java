package es.udc.fi.dc.fd.account;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitRepository;


/**
 * The Class AdminController.
 */
@Controller
@Secured("ROLE_ADMIN")
class AdminController {

    /** The Constant ADMINPANEL_VIEW. */
    private static final String ADMINPANEL_VIEW = "/admin/adminpanel";

    /** The Constant ADMIN_NEW_USERS_VIEW. */
    private static final String ADMIN_NEW_USERS_VIEW = "/admin/newusers";

    /** The Constant ADMIN_OFFENSIVE_TUITS. */
    private static final String ADMIN_OFFENSIVE_TUITS = "/admin/offensiveTuits";

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /**
     * Create an empty controller.
     */
    public AdminController() {

    }

    // ------------------ MODEL ATRIBUTES --------------------------

    /**
     * Gets the my account.
     *
     * @param principal
     *            the principal
     * @return the my account
     */
    @ModelAttribute("myAccount")
    public Account getMyAccount(Principal principal) {
        return accountRepository.findByEmail(principal.getName());
    }

    // ------------------ REQUEST MAPPINGS -------------------------

    /**
     * Removes the tuit.
     *
     * @param receivedTuitId
     *            the received tuit id
     * @param request
     *            the request
     * @return the string
     */
    @RequestMapping(value = "/{tuitId}/removetuit", method = RequestMethod.GET)
    public String removeTuit(@PathVariable("tuitId") String receivedTuitId,
            HttpServletRequest request) {

        Tuit tuit = tuitRepository.findById(receivedTuitId);

        tuitRepository.borrarTuit(tuit);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    /**
     * Allow the users to see the nickName profile if he's not bloked.
     *
     * @return the admin panel
     */
    @RequestMapping(value = "/adminpanel", method = RequestMethod.GET)
    public String getAdminPanel() {

        return ADMINPANEL_VIEW;
    }

    /**
     * Gets the admin new users.
     *
     * @param model
     *            the model
     * @return the admin new users
     */
    @RequestMapping(value = "/admin/newusers", method = RequestMethod.GET)
    public String getAdminNewUsers(Model model) {

        model.addAttribute("notAllowedAccs",
                accountRepository.getNotConfirmedAccounts());
        return ADMIN_NEW_USERS_VIEW;
    }

    /**
     * See offensive tuits.
     *
     * @param model
     *            the model
     * @return the string
     */
    @RequestMapping(value = "/admin/offensiveTuits", method = RequestMethod.GET)
    public String seeOffensiveTuits(Model model) {

        model.addAttribute("offensiveTuits", tuitRepository.getOffensiveTuits());
        return ADMIN_OFFENSIVE_TUITS;
    }

    /**
     * Confirm user.
     *
     * @param receivedNickName
     *            the received nick name
     * @param model
     *            the model
     * @param principal
     *            the principal
     * @return the string
     */
    @RequestMapping(value = "/admin/newusers/{nickName}/confirmuser",
            method = RequestMethod.GET)
    public String confirmUser(
            @PathVariable("nickName") String receivedNickName, Model model,
            Principal principal) {

        Account account = accountRepository.findByNick(receivedNickName);

        // Cuenta no encontrada
        if (account == null) {
            model.addAttribute("notFoundUser", receivedNickName);
            return "error/userNotFoundError";
        }
        accountRepository.confirmUser(account);
        return "redirect:" + ADMIN_NEW_USERS_VIEW;
    }

    /**
     * Deny user.
     *
     * @param receivedNickName
     *            the received nick name
     * @param model
     *            the model
     * @param principal
     *            the principal
     * @return the string
     */
    @RequestMapping(value = "/admin/newusers/{nickName}/denyuser",
            method = RequestMethod.GET)
    public String denyUser(@PathVariable("nickName") String receivedNickName,
            Model model, Principal principal) {

        Account account = accountRepository.findByNick(receivedNickName);

        accountRepository.delete(account);
        return "redirect:" + ADMIN_NEW_USERS_VIEW;
    }
}
