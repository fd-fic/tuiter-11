package es.udc.fi.dc.fd.retuit;

import java.security.Principal;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitRepository;


/**
 * The Class RetuitController.
 */
@Controller
@Secured("ROLE_USER")
public class RetuitController {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /** The retuit repository. */
    @Autowired
    private RetuitRepository retuitRepository;

    /**
     * Create the default controller.
     */
    public RetuitController() {

    }

    /**
     * Retuit.
     *
     * @param tuitId
     *            the tuit id
     * @param principal
     *            the principal
     * @param request
     *            the request
     * @return the string
     */
    @RequestMapping(value = "/{tuitId}/retuit", method = RequestMethod.GET)
    public String retuit(@PathVariable("tuitId") String tuitId,
            Principal principal, HttpServletRequest request) {

        Account myAccount = accountRepository.findByEmail(principal.getName());

        Tuit tuit = tuitRepository.findById(tuitId);
        Retuit retuit = new Retuit(Calendar.getInstance().getTimeInMillis(),
                tuit, myAccount);
        retuitRepository.save(retuit);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    /**
     * Unretuit.
     *
     * @param tuitId
     *            the tuit id
     * @param principal
     *            the principal
     * @param request
     *            the request
     * @return the string
     */
    @RequestMapping(value = "/{tuitId}/unretuit", method = RequestMethod.GET)
    public String unretuit(@PathVariable("tuitId") String tuitId,
            Principal principal, HttpServletRequest request) {

        Tuit tuit = tuitRepository.findById(tuitId);
        if (tuit != null) {
            Retuit foundRetuit = retuitRepository.findByTuitAndEmail(
                    tuit.getId(), principal.getName());

            retuitRepository.deleteRetuit(foundRetuit);
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
