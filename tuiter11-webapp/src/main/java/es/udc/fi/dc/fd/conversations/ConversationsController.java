package es.udc.fi.dc.fd.conversations;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.favorite.FavoriteRepository;
import es.udc.fi.dc.fd.retuit.RetuitRepository;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitRepository;
import es.udc.fi.dc.fd.util.DictionaryLoader;


/**
 * The Class ConversationsController.
 */
@Controller
@Secured("ROLE_USER")
public class ConversationsController {

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /** The retuit repository. */
    @Autowired
    private RetuitRepository retuitRepository;

    /** The favorite repository. */
    @Autowired
    private FavoriteRepository favoriteRepository;

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The bad words. */
    private static Set<String> badWords = DictionaryLoader.getDictionary();

    /**
     * Create the default Conversations Controller.
     */
    public ConversationsController() {

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
     * Response form.
     *
     * @return the response form
     */
    @ModelAttribute("responseForm")
    public ResponseForm responseForm() {
        return new ResponseForm();
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

    /**
     * Gets the parents.
     *
     * @param tuitId
     *            the tuit id
     * @param principal
     *            the principal
     * @return the parents
     */
    @ModelAttribute("parents")
    public List<Tuit> getParents(@PathVariable("tuitId") String tuitId,
            Principal principal) {
        List<Tuit> parents = tuitRepository.getParents(tuitId,
                principal.getName());
        return parents;
    }

    /**
     * Gets the responses.
     *
     * @param tuitId
     *            the tuit id
     * @param principal
     *            the principal
     * @return the responses
     */
    @ModelAttribute("responses")
    public List<Tuit> getResponses(@PathVariable("tuitId") String tuitId,
            Principal principal) {
        List<Tuit> tuits = tuitRepository.getResponses(tuitId,
                principal.getName());
        return tuits;
    }

    /**
     * Process response.
     *
     * @param responseForm
     *            the response form
     * @param parentTuit
     *            the parent tuit
     * @param principal
     *            the principal
     * @return the string
     */
    @RequestMapping(value = "/{tuitId}/processResponse",
            method = RequestMethod.POST)
    public String processResponse(
            @ModelAttribute("responseForm") ResponseForm responseForm,
            @PathVariable("tuitId") String parentTuit, Principal principal) {

        String tuitMessage = responseForm.getTuit();
        if (!tuitMessage.isEmpty()) {
            Account acc = accountRepository.findByEmail(principal.getName());
            Tuit tuit = new Tuit(Long.valueOf(Calendar.getInstance()
                    .getTimeInMillis()), tuitMessage, acc);
            String[] messageSplitted = tuitMessage.split(" ");
            for (String word : messageSplitted) {
                if (badWords.contains(word)) {
                    tuitRepository.saveWithParent(tuit, parentTuit, 1);
                    return "redirect:/" + parentTuit + "/conversations";
                }
            }
            tuitRepository.saveWithParent(tuit, parentTuit, 0);
        }
        return "redirect:/" + parentTuit + "/conversations";
    }

    /**
     * Show conversation.
     *
     * @param tuitId
     *            the tuit id
     * @return the model and view
     */
    @RequestMapping(value = "/{tuitId}/conversations",
            method = RequestMethod.GET)
    public ModelAndView showConversation(@PathVariable("tuitId") String tuitId) {

        Tuit tuit = tuitRepository.findById(tuitId);
        ModelAndView mv = new ModelAndView();
        mv.addObject("tuit", tuit);

        mv.setViewName("conversations/conversation");
        return mv;
    }

}
