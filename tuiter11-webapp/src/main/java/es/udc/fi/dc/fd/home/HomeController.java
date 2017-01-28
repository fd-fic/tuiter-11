package es.udc.fi.dc.fd.home;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.favorite.FavoriteRepository;
import es.udc.fi.dc.fd.publish.Publish;
import es.udc.fi.dc.fd.publish.PublishService;
import es.udc.fi.dc.fd.retuit.RetuitRepository;
import es.udc.fi.dc.fd.tuit.Tuit;


/**
 * The Class HomeController.
 */
@Controller
public class HomeController {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The retuit repository. */
    @Autowired
    private RetuitRepository retuitRepository;

    /** The favorite repository. */
    @Autowired
    private FavoriteRepository favoriteRepository;

    /** The publish service. */
    @Autowired
    private PublishService publishService;

    /**
     * Create the default Controller.
     */
    public HomeController() {

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
        if (principal == null) {
            return null;
        }
        return accountRepository.findByEmail(principal.getName());
    }

    /**
     * Home signed in.
     *
     * @return the tuit form
     */
    @ModelAttribute("tuitForm")
    public TuitForm homeSignedIn() {
        return new TuitForm();
    }

    /**
     * Gets the time line publications.
     *
     * @param principal
     *            the principal
     * @return the time line publications
     */
    @ModelAttribute("publications")
    public List<Publish> getTimeLinePublications(Principal principal) {
        if (principal == null) {
            return null;
        }

        List<Publish> result = publishService.getTimeLinePublications(principal
                .getName());
        return result;
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
     * Index.
     *
     * @param principal
     *            the principal
     * @return the string
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Principal principal) {
        if (principal == null) {
            return "home/homeNotSignedIn";
        } else {
            Account acc = accountRepository.findByEmail(principal.getName());
            if (acc.getRole().equals("ROLE_NOT_CONFIRMED")) {
                return "home/homeNotConfirm";
            } else {
                return "home/homeSignedIn";
            }
        }
    }
}
