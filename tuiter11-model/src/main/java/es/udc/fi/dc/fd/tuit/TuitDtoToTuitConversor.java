package es.udc.fi.dc.fd.tuit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;


/**
 * The Class TuitDtoToTuitConversor.
 */
@Component
public class TuitDtoToTuitConversor {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Create the default Conversor.
     */
    public TuitDtoToTuitConversor() {

    }

    /**
     * Tuit dto to tuit.
     *
     * @param tuitDto
     *            the tuit dto
     * @return the tuit
     */
    public Tuit tuitDtoToTuit(TuitDto tuitDto) {
        Account relatedAcc = accountRepository.findById(tuitDto.getAccountId());
        Tuit tuit = new Tuit(tuitDto.getFechaMs(), tuitDto.getTuit(),
                relatedAcc);
        tuit.setId(tuitDto.getId());
        return tuit;
    }

    /**
     * Tuit dtos to tuits.
     *
     * @param tuitsDtos
     *            the tuits dtos
     * @return the list
     */
    public List<Tuit> tuitDtosToTuits(List<TuitDto> tuitsDtos) {

        List<Tuit> tuits = new ArrayList<Tuit>();
        for (TuitDto tuitDto : tuitsDtos) {
            tuits.add(tuitDtoToTuit(tuitDto));
        }
        return tuits;
    }
}
