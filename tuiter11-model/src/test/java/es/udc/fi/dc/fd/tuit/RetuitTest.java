package es.udc.fi.dc.fd.tuit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.account.UserService;
import es.udc.fi.dc.fd.config.WebAppConfigurationAware;
import es.udc.fi.dc.fd.retuit.Retuit;
import es.udc.fi.dc.fd.retuit.RetuitDto;
import es.udc.fi.dc.fd.retuit.RetuitRepository;
import es.udc.fi.dc.fd.util.InstanceNotFoundException;


/**
 * The Class RetuitTest.
 */
@Transactional
public class RetuitTest extends WebAppConfigurationAware {

    /** The user service. */
    @InjectMocks
    private UserService userService = new UserService();

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /** The retuit repository. */
    @Autowired
    private RetuitRepository retuitRepository;

    /** The thrown. */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /** The mongo template. */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Clean mongo db.
     */
    public void cleanMongoDB() {
        mongoTemplate.dropCollection(TuitDto.class);
        mongoTemplate.dropCollection(RetuitDto.class);
    }

    /**
     * Creates the user.
     *
     * @param email
     *            the email
     * @param nick
     *            the nick
     * @return the account
     */
    private Account createUser(String email, String nick) {
        Account demoUser = new Account(email, nick, "demo", "ROLE_USER");

        accountRepository.save(demoUser);
        return demoUser;
    }

    /**
     * Creates the tuit.
     *
     * @param message
     *            the message
     * @param account
     *            the account
     * @return the tuit
     */
    private Tuit createTuit(String message, Account account) {
        Calendar fecha = Calendar.getInstance();
        fecha.set(Calendar.MILLISECOND, 0);
        Tuit tuit = new Tuit(fecha.getTimeInMillis(), message, account);
        tuitRepository.save(tuit);
        return tuit;
    }

    /**
     * Creates the retuit.
     *
     * @param tuit
     *            the tuit
     * @param retuiter
     *            the retuiter
     * @return the retuit
     */
    private Retuit createRetuit(Tuit tuit, Account retuiter) {
        Calendar fecha = Calendar.getInstance();
        fecha.set(Calendar.MILLISECOND, 0);
        Retuit retuit = new Retuit(fecha.getTimeInMillis(), tuit, retuiter);
        retuitRepository.save(retuit);
        return retuit;
    }

    /**
     * Creates the new retuit and find it by id.
     */
    @Test
    public void createNewRetuitAndFindItById() {
        cleanMongoDB();

        Account cuentaPepe = createUser("pepe@example.com", "pepe");
        Account cuentaManolo = createUser("manolo@example.com", "manolo");

        Tuit tuitPepe = createTuit("tuit de prueba", cuentaPepe);

        Retuit manoloRetuit = createRetuit(tuitPepe, cuentaManolo);

        Retuit foundRetuit = retuitRepository.findByTuitAndEmail(
                tuitPepe.getId(), cuentaManolo.getEmail());

        Tuit retuitedTuit = tuitRepository.findById(foundRetuit.getTuit()
                .getId());
        assertEquals(tuitPepe, retuitedTuit);

        assertTrue(retuitRepository.wasRetuitedBy(tuitPepe.getId(),
                cuentaManolo.getEmail()));
        List<Retuit> foundRetuits = retuitRepository
                .findRetuitedBy(cuentaManolo.getEmail());

        assertTrue(foundRetuits.contains(manoloRetuit));

        cleanMongoDB();
    }

    /**
     * Creates the and delete tuit.
     */
    @Test
    public void createAndDeleteTuit() {
        cleanMongoDB();

        Account cuentaPepe = createUser("pepe@example.com", "pepe");
        Account cuentaManolo = createUser("manolo@example.com", "manolo");

        Tuit tuitPepe = createTuit("tuit de prueba", cuentaPepe);

        createRetuit(tuitPepe, cuentaManolo);

        Retuit foundRetuit = retuitRepository.findByTuitAndEmail(
                tuitPepe.getId(), cuentaManolo.getEmail());

        retuitRepository.deleteRetuit(foundRetuit);

        boolean exception = false;

        try {
            retuitRepository.findByTuitAndEmail(tuitPepe.getId(),
                    cuentaManolo.getEmail());
        } catch (InstanceNotFoundException e) {
            exception = true;
        }
        assertTrue(exception);

        cleanMongoDB();
    }

    /**
     * Test find retuited by.
     */
    @Test
    public void TestFindRetuitedBy() {
        cleanMongoDB();

        Account user1 = createUser("user1@example.com", "user1");
        Account user2 = createUser("user2@example.com", "user2");
        Account user3 = createUser("user3@example.com", "user3");
        Account user4 = createUser("user4@example.com", "user4");

        Tuit tuitU11 = createTuit("tuit de prueba 1 user1", user1);
        createTuit("tuit de prueba 2 user1", user1);

        Tuit tuitU21 = createTuit("tuit de prueba 1 user2", user2);
        createTuit("tuit de prueba 2 user2", user2);

        Tuit tuitU31 = createTuit("tuit de prueba 1 user3", user3);
        createTuit("tuit de prueba 2 user3", user3);

        Tuit tuitU41 = createTuit("tuit de prueba 1 user4", user4);
        createTuit("tuit de prueba 2 user4", user4);

        // user1 retuitea el primer tuit de user2 y user3
        Retuit Retuit_u2_t1 = createRetuit(tuitU21, user1);
        Retuit Retuit_u3_t1 = createRetuit(tuitU31, user1);

        List<Retuit> retuits;
        retuits = retuitRepository.findRetuitedBy(user1.getEmail());
        assertEquals(retuits.size(), 2);
        assertTrue(retuits.contains(Retuit_u2_t1));
        assertTrue(retuits.contains(Retuit_u3_t1));

        List<Tuit> tuits;
        tuits = retuitRepository.findTuitsRetuitedBy(user1.getEmail());
        assertEquals(tuits.size(), 2);
        assertTrue(tuits.contains(tuitU21));
        assertTrue(tuits.contains(tuitU31));

        // user3 retuitea a user4 Deberámos obtener el mismo resultado

        createRetuit(tuitU41, user3);

        retuits = retuitRepository.findRetuitedBy(user1.getEmail());
        assertEquals(retuits.size(), 2);
        assertTrue(retuits.contains(Retuit_u2_t1));
        assertTrue(retuits.contains(Retuit_u3_t1));

        tuits = retuitRepository.findTuitsRetuitedBy(user1.getEmail());
        assertEquals(tuits.size(), 2);
        assertTrue(tuits.contains(tuitU21));
        assertTrue(tuits.contains(tuitU31));

        // user2 retuitea a user1 Deberámos obtener el mismo resultado

        createRetuit(tuitU11, user2);

        retuits = retuitRepository.findRetuitedBy(user1.getEmail());
        assertEquals(retuits.size(), 2);
        assertTrue(retuits.contains(Retuit_u2_t1));
        assertTrue(retuits.contains(Retuit_u3_t1));

        tuits = retuitRepository.findTuitsRetuitedBy(user1.getEmail());
        assertEquals(tuits.size(), 2);
        assertTrue(tuits.contains(tuitU21));
        assertTrue(tuits.contains(tuitU31));

        cleanMongoDB();
    }
}
