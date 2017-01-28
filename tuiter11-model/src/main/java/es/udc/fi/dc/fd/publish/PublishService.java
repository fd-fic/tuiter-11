package es.udc.fi.dc.fd.publish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.follow.FollowRepository;
import es.udc.fi.dc.fd.retuit.RetuitRepository;
import es.udc.fi.dc.fd.tuit.TuitRepository;

/**
 * The Class PublishService.
 */
@Repository
public class PublishService {

    /** The follow repository. */
    @Autowired
    private FollowRepository followRepository;

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
     * Create the default Publish Service.
     */
    public PublishService() {

    }

    /**
     * It returns Tuits, Retuits and Responses that we can see in the
     * Application Home when a user has logged in.
     *
     * @param email
     *            The user email you want to see the timeline.
     * @return the time line publications
     */
    public List<Publish> getTimeLinePublications(String email) {

        List<Account> followedAccs = followRepository.findFollowedBy(email);

        // Añadimos nuestra propia cuenta para que aparezcan tambien
        // nuestros tuits
        Account myAccount = accountRepository.findByEmail(email);
        followedAccs.add(myAccount);

        List<Publish> publications = new ArrayList<Publish>();

        for (Account a : followedAccs) {
            boolean esPrivada = (a.getPrivateAccount() != null);
            boolean meSigue = followRepository.findFollowedBy(a.getEmail())
                    .contains(myAccount);
            boolean esMiCuenta = (a.getEmail().equals(myAccount.getEmail()));
            boolean denied = (esPrivada) && (!meSigue);

            if (!denied || esMiCuenta) {
                // Añadimos las publicaciones
                publications
                        .addAll(tuitRepository.findByUserEmail(a.getEmail()));
                publications.addAll(retuitRepository.findRetuitedBy(a
                        .getEmail()));
            }
        }

        // Ordenamos por fecha las publicaciones
        Collections.sort(publications, new PublishComparator());

        return publications;
    }

    /**
     * It returns the list of Tuits, retuit and Answers can be viewed in a
     * user's profile.
     *
     * @param email
     *            The user email you want to see the profile.
     * @return the user publications
     */
    public List<Publish> getUserPublications(String email) {

        List<Publish> publications = new ArrayList<Publish>();

        publications.addAll(tuitRepository.findByUserEmail(email));
        publications.addAll(retuitRepository.findRetuitedBy(email));

        // Ordenamos por fecha las publicaciones
        Collections.sort(publications, new PublishComparator());

        return publications;
    }

    /**
     * The Class PublishComparator.
     */
    static class PublishComparator implements Comparator<Publish>,
            java.io.Serializable {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(Publish pub1, Publish pub2) {
            return pub2.getPublishedDate().compareTo(pub1.getPublishedDate());
        }

    }
}
