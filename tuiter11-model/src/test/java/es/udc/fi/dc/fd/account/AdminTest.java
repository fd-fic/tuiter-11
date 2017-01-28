package es.udc.fi.dc.fd.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.config.WebAppConfigurationAware;
import es.udc.fi.dc.fd.retuit.RetuitDto;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitDto;
import es.udc.fi.dc.fd.tuit.TuitRepository;


/**
 * The Class AdminTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdminTest extends WebAppConfigurationAware {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /** The mongo template. */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Creates the user.
     *
     * @param email
     *            the email
     * @param nick
     *            the nick
     * @param role
     *            the role
     * @return the account
     */
    private Account createUser(String email, String nick, String role) {
        Account user = new Account(email, nick, "pass", role);
        accountRepository.save(user);
        return user;
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
     * Creates the offensive tuit.
     *
     * @param message
     *            the message
     * @param account
     *            the account
     * @return the tuit
     */
    private Tuit createOffensiveTuit(String message, Account account) {
        Calendar fecha = Calendar.getInstance();
        fecha.set(Calendar.MILLISECOND, 0);
        Tuit tuit = new Tuit(fecha.getTimeInMillis(), message, account);
        tuitRepository.saveOffensiveTuit(tuit);
        return tuit;
    }

    /**
     * Clean mongo db.
     */
    public void cleanMongoDB() {
        mongoTemplate.dropCollection(TuitDto.class);
        mongoTemplate.dropCollection(RetuitDto.class);
    }

    /**
     * Accounts not confirmed test.
     */
    @Test
    public void accountsNotConfirmedTest() {
        Account user1 = createUser("user1@user", "user1", "ROLE_USER");
        Account user2 = createUser("user2@user", "user2", "ROLE_USER");
        Account user3 = createUser("user3@user", "user3", "ROLE_USER");

        List<Account> accounts = accountRepository.getNotConfirmedAccounts();

        assertEquals(3, accounts.size());
        assertTrue(accounts.contains(user1));
        assertTrue(accounts.contains(user2));
        assertTrue(accounts.contains(user3));

        accountRepository.confirmUser(user2);

        accounts = accountRepository.getNotConfirmedAccounts();

        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(user1));
        assertTrue(accounts.contains(user3));

        accountRepository.delete(user1);

        accounts = accountRepository.getNotConfirmedAccounts();

        assertEquals(1, accounts.size());
        assertTrue(accounts.contains(user3));
    }

    /**
     * Offensive tuits tests.
     */
    @Test
    public void offensiveTuitsTests() {

        cleanMongoDB();

        // Creamos los usuarios
        Account pepe = createUser("pepe@user", "pepe", "ROLE_USER");
        Account manolo = createUser("manolo@user", "manolo", "ROLE_USER");

        // Creamos los tuits normales para cada usuario
        createTuit("Tuit inofensivo", pepe);
        createTuit("Tuit normal", manolo);
        createTuit("Otro tuit normal", manolo);

        // Obtenemos los tuits ofensivos, que deberian ser 0
        List<Tuit> tuitsOfensivos = tuitRepository.getOffensiveTuits();

        assertEquals(0, tuitsOfensivos.size());

        // Creamos tuits ofensivos para cada usuario
        Tuit tuitPepeOfensivo = createOffensiveTuit("Tu puta madre!", pepe);
        Tuit tuitManoloOfensivo = createOffensiveTuit("hijoputa", manolo);

        // Comprobamos que se recuperan bien esos tuits
        tuitsOfensivos = tuitRepository.getOffensiveTuits();

        assertEquals(2, tuitsOfensivos.size());
        assertEquals(tuitManoloOfensivo, tuitsOfensivos.get(1));
        assertEquals(tuitPepeOfensivo, tuitsOfensivos.get(0));

        cleanMongoDB();
    }
}
