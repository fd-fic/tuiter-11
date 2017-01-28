package es.udc.fi.dc.fd.favorite;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitRepository;


/**
 * The Class FavoriteController.
 */
@Controller
@Secured("ROLE_USER")
public class FavoriteController {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /** The fav repository. */
    @Autowired
    private FavoriteRepository favRepository;

    /**
     * Create the default controller.
     */
    public FavoriteController() {

    }

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
     * Favorite.
     *
     * @param tuitId
     *            the tuit id
     * @param principal
     *            the principal
     * @param request
     *            the request
     * @return the string
     */
    @RequestMapping(value = "/{tuitId}/favorite", method = RequestMethod.GET)
    public String favorite(@PathVariable("tuitId") String tuitId,
            Principal principal, HttpServletRequest request) {

        Account myAccount = accountRepository.findByEmail(principal.getName());
        Tuit tuit = tuitRepository.findById(tuitId);

        Favorite favorite = new Favorite(tuit, myAccount);
        favRepository.save(favorite);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    /**
     * Removes the favorite.
     *
     * @param tuitId
     *            the tuit id
     * @param principal
     *            the principal
     * @param request
     *            the request
     * @return the string
     */
    @RequestMapping(value = "/{tuitId}/removefavorite",
            method = RequestMethod.GET)
    public String removeFavorite(@PathVariable("tuitId") String tuitId,
            Principal principal, HttpServletRequest request) {

        Tuit tuit = tuitRepository.findById(tuitId);

        Favorite foundFavorite = favRepository.findByTuitAndEmail(tuit.getId(),
                principal.getName());
        favRepository.deleteFavorite(foundFavorite);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
