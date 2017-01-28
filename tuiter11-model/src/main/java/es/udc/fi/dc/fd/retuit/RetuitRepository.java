package es.udc.fi.dc.fd.retuit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.publish.PublishUtils;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitRepository;
import es.udc.fi.dc.fd.util.InstanceNotFoundException;


/**
 * The Class RetuitRepository.
 */
@Repository
@Transactional(readOnly = true)
@ComponentScan("es.udc.fi.dc.fd")
public class RetuitRepository {

    /** The mongo template. */
    @Autowired
    private MongoTemplate mongoTemplate;

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /** The publish utils. */
    @Autowired
    private PublishUtils publishUtils;

    /**
     * Create the default Repository.
     */
    public RetuitRepository() {

    }

    /**
     * Save.
     *
     * @param retuit
     *            the retuit
     * @return the retuit
     */
    @Transactional
    public Retuit save(Retuit retuit) {

        RetuitDto retuitDto = new RetuitDto(retuit.getFechaMs(), retuit
                .getTuit().getId(), retuit.getRetuiter().getId());
        mongoTemplate.insert(retuitDto);

        retuit.setId(retuitDto.getId());

        return retuit;
    }

    /**
     * Delete retuit.
     *
     * @param retuit
     *            the retuit
     */
    @Transactional
    public void deleteRetuit(Retuit retuit) {

        RetuitDto retuitDto = mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(retuit.getId())),
                RetuitDto.class);

        mongoTemplate.remove(retuitDto);
    }

    /**
     * Return all Retuits made by the user whith that email.
     *
     * @param email
     *            the email
     * @return the list
     */
    public List<Retuit> findRetuitedBy(String email) {

        Account acc = accountRepository.findByEmail(email);

        List<RetuitDto> retuitsDtos = mongoTemplate.find(
                Query.query(Criteria.where("retuiterId").is(acc.getId())),
                RetuitDto.class);

        List<Retuit> obtainedRetuits = retuitDtosToRetuitsConv(retuitsDtos);
        List<Retuit> result = new ArrayList<Retuit>();
        for (Retuit r : obtainedRetuits) {
            if (publishUtils.grantedVisibility(email, r.getTuit().getAcc()
                    .getEmail())) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Return all Tuits retuited by the user whith that email.
     *
     * @param email
     *            the email
     * @return the list
     */
    public List<Tuit> findTuitsRetuitedBy(String email) {

        List<Tuit> tuits = new ArrayList<Tuit>();
        for (Retuit r : findRetuitedBy(email)) {
            tuits.add(r.getTuit());
        }

        return tuits;
    }

    /**
     * Return a Retuit, made by the user whit email and whit id tuitId.
     *
     * @param tuitId
     *            the tuit id
     * @param email
     *            the email
     * @return the retuit
     */
    public Retuit findByTuitAndEmail(String tuitId, String email) {

        Account acc = accountRepository.findByEmail(email);
        RetuitDto retuitDto = mongoTemplate.findOne(
                Query.query(Criteria.where("tuitId").is(tuitId)
                        .and("retuiterId").is(acc.getId())), RetuitDto.class);

        if (retuitDto == null) {
            throw new InstanceNotFoundException(
                    "Unable to find the retuit for the tuit with id: " + tuitId
                            + " and the email: " + email, Retuit.class);
        }
        return retuitDtoToRetuitConv(retuitDto);
    }

    /**
     * Return true if the user whit email made a retuit to the retuit whith
     * tuitId.
     *
     * @param tuitId
     *            the tuit id
     * @param email
     *            the email
     * @return true, if successful
     */
    public boolean wasRetuitedBy(String tuitId, String email) {

        Account acc = accountRepository.findByEmail(email);
        return mongoTemplate.count(
                Query.query(Criteria.where("tuitId").is(tuitId)
                        .and("retuiterId").is(acc.getId())), RetuitDto.class) > 0;
    }

    /**
     * Find retuits by tuit.
     *
     * @param tuitId
     *            the tuit id
     * @return the list
     */
    public List<Retuit> findRetuitsByTuit(String tuitId) {
        tuitRepository.findById(tuitId);

        List<RetuitDto> retuits = mongoTemplate.find(
                Query.query(Criteria.where("tuitId").is(tuitId)),
                RetuitDto.class);

        // Si no hay retuits devolvemos una lista vacía
        if (retuits == null) {
            return new ArrayList<Retuit>();
        }

        return retuitDtosToRetuitsConv(retuits);

    }

    /**
     * Retuit dtos to retuits conv.
     *
     * @param retuitsDtos
     *            the retuits dtos
     * @return the list
     */
    private List<Retuit> retuitDtosToRetuitsConv(List<RetuitDto> retuitsDtos) {
        List<Retuit> result = new ArrayList<>();
        for (RetuitDto rDto : retuitsDtos) {
            result.add(retuitDtoToRetuitConv(rDto));
        }
        return result;
    }

    /**
     * Retuit dto to retuit conv.
     *
     * @param retuitDto
     *            the retuit dto
     * @return the retuit
     */
    private Retuit retuitDtoToRetuitConv(RetuitDto retuitDto) {

        Retuit retuit = new Retuit(retuitDto.getFechaMs(),
                tuitRepository.findById(retuitDto.getTuitId()),
                accountRepository.findById(retuitDto.getRetuiterId()));
        retuit.setId(retuitDto.getId());
        return retuit;
    }
}
