package es.udc.fi.dc.fd.restservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.dc.fd.tuit.CustomTuit;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitList;
import es.udc.fi.dc.fd.tuit.TuitRepository;


/**
 * The Class RestSearchController.
 */
@RestController
public class RestSearchController {

    /**
     * Instantiates a new rest search controller.
     */
    public RestSearchController() {
    }

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /**
     * Gets the tuits.
     *
     * @param keywords
     *            the keywords
     * @return the tuits
     */
    @RequestMapping(value = "/")
    @ResponseBody
    public TuitList getTuits(@RequestParam(value = "keywords") String keywords) {

        List<Tuit> tuitsObtained = tuitRepository.findByKeyWords(keywords);
        List<CustomTuit> tuitsToShow = new ArrayList<CustomTuit>();
        for (Tuit t : tuitsObtained) {
            tuitsToShow.add(new CustomTuit(t.getAcc().getNickName(), t
                    .getFecha(), t.getTuit()));
        }
        return new TuitList(tuitsToShow);
    }
}
