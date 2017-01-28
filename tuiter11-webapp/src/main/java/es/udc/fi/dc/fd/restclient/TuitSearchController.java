package es.udc.fi.dc.fd.restclient;

import java.security.Principal;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.tuit.TuitList;


/**
 * The Class TuitSearchController.
 */
@Controller
public class TuitSearchController {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Gets the account.
     *
     * @param principal
     *            the principal
     * @return the account
     */
    @ModelAttribute("myAccount")
    public Account getAccount(Principal principal) {
        Account acc = null;
        if (principal != null) {
            acc = accountRepository.findByEmail(principal.getName());
        }
        return acc;
    }

    /**
     * Search tuits.
     *
     * @param webRequest
     *            the web request
     * @return the list of tuits
     */
    @RequestMapping(value = "/searchTuits", method = RequestMethod.GET)
    public ModelAndView searchTuits(final WebRequest webRequest) {
        ModelAndView mv = new ModelAndView();
        String keywords = webRequest.getParameter("keywords");
        if (keywords == null || keywords.equals("")) {
            mv.setViewName("redirect:/");
            return mv;
        }

        // Obtenemos la url del fichero .properties
        Properties mispropiedades = new UrlProperties().getProperties();
        String url = mispropiedades.getProperty(UrlProperties.REST_SERVICE_URL);

        RestTemplate restTemplate = new RestTemplate();

        TuitList tuits = restTemplate.getForObject(url, TuitList.class,
                keywords);

        mv.addObject("tuits", tuits.getTuits());
        mv.setViewName("search/searchtuits");

        return mv;
    }

}
