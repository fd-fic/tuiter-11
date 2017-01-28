package es.udc.fi.dc.fd.search;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;


/**
 * The Class SearchController.
 */
@Controller
@Secured("ROLE_USER")
public class SearchController {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Create the default controller.
     */
    public SearchController() {

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
     * This method processes the input form to search for accounts.
     *
     * @param webRequest
     *            contains the keyword to search the users
     * @param principal
     *            the principal
     * @return the model and view
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search(final WebRequest webRequest, Principal principal) {
        ModelAndView mv = new ModelAndView();
        String keyword = webRequest.getParameter("keyword");
        if (keyword == null || keyword.equals("")) {
            mv.setViewName("redirect:/");
            return mv;
        }
        List<Account> accounts = accountRepository.findAccounts(keyword);
        if (accounts.size() == 0) {
            mv.addObject("keyword", keyword);
            mv.setViewName("error/resultsNotFound");
            return mv;
        }
        mv.addObject("accounts", accounts);
        mv.setViewName("search/search");
        return mv;
    }

}
