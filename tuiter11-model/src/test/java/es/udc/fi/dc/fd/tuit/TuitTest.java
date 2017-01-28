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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.account.UserService;
import es.udc.fi.dc.fd.config.WebAppConfigurationAware;
import es.udc.fi.dc.fd.favorite.Favorite;
import es.udc.fi.dc.fd.favorite.FavoriteRepository;
import es.udc.fi.dc.fd.retuit.Retuit;
import es.udc.fi.dc.fd.retuit.RetuitDto;
import es.udc.fi.dc.fd.retuit.RetuitRepository;
import es.udc.fi.dc.fd.util.InstanceNotFoundException;


/**
 * The Class TuitTest.
 */
@Transactional
public class TuitTest extends WebAppConfigurationAware {

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

    /** The favorite repository. */
    @Autowired
    private FavoriteRepository favoriteRepository;

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
     * Creates the response.
     *
     * @param message
     *            the message
     * @param account
     *            the account
     * @param parentId
     *            the parent id
     * @return the tuit
     */
    private Tuit createResponse(String message, Account account, String parentId) {
        Calendar fecha = Calendar.getInstance();
        fecha.set(Calendar.MILLISECOND, 0);
        Tuit tuit = new Tuit(fecha.getTimeInMillis(), message, account);
        tuitRepository.saveWithParent(tuit, parentId, 0);
        return tuit;

    }

    /**
     * Creates the favorite.
     *
     * @param tuit
     *            the tuit
     * @param favAccount
     *            the fav account
     * @return the favorite
     */
    private Favorite createFavorite(Tuit tuit, Account favAccount) {
        Favorite favorite = new Favorite(tuit, favAccount);
        favoriteRepository.save(favorite);
        return favorite;
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
     * Creates the new tuit and find it by id.
     */
    @Test
    public void createNewTuitAndFindItById() {
        cleanMongoDB();

        Account cuentaPepe = createUser("pepe@example.com", "pepe");
        Tuit tuitPepe = createTuit("tuit de prueba", cuentaPepe);

        Tuit findedTuit = tuitRepository.findById(tuitPepe.getId());

        assertEquals(tuitPepe, findedTuit);

        cleanMongoDB();
    }

    /**
     * Creates the new tuit and find it.
     */
    @Test
    public void createNewTuitAndFindIt() {
        cleanMongoDB();

        Account cuentaPepe = createUser("pepe@example.com", "pepe");
        Account cuentaManolo = createUser("manolo@example.com", "manolo");

        Tuit tuitPepe = createTuit("tuit de prueba", cuentaPepe);
        Tuit tuitManolo = createTuit("tuit de prueba 2", cuentaManolo);
        Tuit tuitManolo2 = createTuit("tuit de prueba 3", cuentaManolo);

        List<Tuit> tuitsPepe = tuitRepository.findByUserEmail(cuentaPepe
                .getEmail());
        List<Tuit> tuitsManolo = tuitRepository.findByUserEmail(cuentaManolo
                .getEmail());

        assertTrue(tuitsPepe.size() == 1);
        assertEquals(
                tuitPepe.toString().compareTo(tuitsPepe.get(0).toString()), 0);

        assertTrue(tuitsManolo.size() == 2);
        assertEquals(
                tuitManolo.toString().compareTo(tuitsManolo.get(0).toString()),
                0);
        assertEquals(
                tuitManolo2.toString().compareTo(tuitsManolo.get(1).toString()),
                0);

        cleanMongoDB();
    }

    /**
     * Find tuits by keywords test.
     */
    @Test
    public void findTuitsByKeywordsTest() {
        cleanMongoDB();

        Account cuentaManolo = createUser("manolo@example.com", "manolo");

        String keywords = "tuit";
        Tuit tuitManolo = createTuit(keywords + " uno", cuentaManolo);
        Tuit tuitManolo2 = createTuit(keywords + " dos", cuentaManolo);

        List<Tuit> tuitsTuit = tuitRepository.findByKeyWords(keywords);
        List<Tuit> tuitsUno = tuitRepository.findByKeyWords("uno");

        assertTrue(tuitsTuit.size() == 2);
        assertTrue(tuitsUno.size() == 1);
        assertEquals(tuitsTuit.get(0), tuitManolo);
        assertEquals(tuitsTuit.get(1), tuitManolo2);
        assertEquals(tuitsUno.get(0), tuitManolo);

        cleanMongoDB();
    }

    /**
     * Find non existent tuit.
     */
    @Test(expected = InstanceNotFoundException.class)
    public void findNonExistentTuit() {
        tuitRepository.findById("webrqwkr4bw5lqjvhr");
    }

    /**
     * Find non existent tuits.
     */
    @Test(expected = UsernameNotFoundException.class)
    public void findNonExistentTuits() {
        tuitRepository.findByUserEmail("cuentaFalsa@example.com");
    }

    /**
     * Find existent accounts.
     */
    @Test
    public void findExistentAccounts() {
        Account cuentaPepe = createUser("pepe@example.com", "NickPepe");
        Account cuentaManolo = createUser("manolo@example.com", "manolo");
        Account cuentaPedro = createUser("pedro@example.com", "pedroOfficial");
        Account cuentaJuan = createUser("juan@example.com", "offJuan");

        List<Account> accountsFound = accountRepository.findAccounts("off");
        assertEquals(accountsFound.size(), 2);
        assertEquals(cuentaPedro.toString(), accountsFound.get(0).toString());
        assertEquals(cuentaJuan.toString(), accountsFound.get(1).toString());

        List<Account> accountsFound2 = accountRepository
                .findAccounts("nickpepe");
        List<Account> accountsFound3 = accountRepository
                .findAccounts("NickPepe");
        assertEquals(accountsFound2.size(), 1);
        assertEquals(accountsFound3.size(), 1);
        assertEquals(cuentaPepe.toString(), accountsFound2.get(0).toString());
        assertEquals(cuentaPepe.toString(), accountsFound3.get(0).toString());

        List<Account> accountsFound4 = accountRepository.findAccounts("Pe");
        assertEquals(accountsFound4.size(), 2);
        assertEquals(cuentaPepe.toString(), accountsFound4.get(0).toString());
        assertEquals(cuentaPedro.toString(), accountsFound4.get(1).toString());

        List<Account> accountsFound5 = accountRepository.findAccounts("an");
        assertEquals(accountsFound5.size(), 2);
        assertEquals(cuentaManolo.toString(), accountsFound5.get(0).toString());
        assertEquals(cuentaJuan.toString(), accountsFound5.get(1).toString());
    }

    /**
     * Find non existent accounts.
     */
    @Test
    public void findNonExistentAccounts() {
        createUser("pepe@example.com", "NickPepe");
        createUser("manolo@example.com", "manolo");
        createUser("pedro@example.com", "pedroOfficial");
        createUser("juan@example.com", "offJuan");

        List<Account> accountsFound = accountRepository
                .findAccounts("nickpepefalso");
        List<Account> accountsFound2 = accountRepository
                .findAccounts("offerfretg");
        List<Account> accountsFound3 = accountRepository
                .findAccounts("manoloOfficial");
        List<Account> accountsFound4 = accountRepository
                .findAccounts("Juanpedro");

        assertEquals(accountsFound.size(), 0);
        assertEquals(accountsFound2.size(), 0);
        assertEquals(accountsFound3.size(), 0);
        assertEquals(accountsFound4.size(), 0);
    }

    /**
     * Removes the a simple tuit test.
     */
    @Test
    public void removeASimpleTuitTest() {
        cleanMongoDB();

        Account cuentaPepe = createUser("pepe@example.com", "NickPepe");

        Tuit tuitPepe = createTuit("tuit de prueba", cuentaPepe);

        Tuit findedTuit = tuitRepository.findById(tuitPepe.getId());
        String idPepe = tuitPepe.getId();

        assertEquals(tuitPepe, findedTuit);

        tuitRepository.borrarTuit(findedTuit);

        boolean exception = false;
        try {
            tuitRepository.findById(idPepe);
        } catch (InstanceNotFoundException e) {
            assertEquals(Tuit.class, e.getNotFoundClass());
            exception = true;
        }
        assertTrue(exception);

        cleanMongoDB();

    }

    /**
     * Removes the a tuit with favorites test.
     */
    @Test
    public void removeATuitWithFavoritesTest() {
        cleanMongoDB();

        Account cuentaPepe = createUser("pepe@example.com", "pepe");
        Account cuentaManolo = createUser("manolo@example.com", "manolo");
        Account cuentaPedro = createUser("pedro@example.com", "pedroOfficial");

        Tuit tuitPepe = createTuit("tuit de prueba", cuentaPepe);

        createFavorite(tuitPepe, cuentaManolo);
        createFavorite(tuitPepe, cuentaPedro);

        tuitRepository.borrarTuit(tuitPepe);

        boolean exception = false;
        try {
            tuitRepository.findById(tuitPepe.getId());
        } catch (InstanceNotFoundException e) {
            assertEquals(Tuit.class, e.getNotFoundClass());
            exception = true;
        }
        assertTrue(exception);

        exception = false;
        try {
            favoriteRepository.findFavoritesByTuit(tuitPepe.getId());
        } catch (InstanceNotFoundException e) {
            assertEquals(Tuit.class, e.getNotFoundClass());
            exception = true;
        }
        assertTrue(exception);

        cleanMongoDB();
    }

    /**
     * Removes the tuit with retuits test.
     */
    @Test
    public void removeTuitWithRetuitsTest() {

        Account cuentaPepe = createUser("pepe@example.com", "pepe");
        Account cuentaManolo = createUser("manolo@example.com", "manolo");
        Account cuentaPedro = createUser("pedro@example.com", "pedroOfficial");

        Tuit tuitPepe = createTuit("tuit de prueba", cuentaPepe);

        createRetuit(tuitPepe, cuentaManolo);
        createRetuit(tuitPepe, cuentaPedro);

        tuitRepository.borrarTuit(tuitPepe);

        boolean exception = false;
        try {
            tuitRepository.findById(tuitPepe.getId());
        } catch (InstanceNotFoundException e) {
            assertEquals(Tuit.class, e.getNotFoundClass());
            exception = true;
        }
        assertTrue(exception);

        exception = false;
        try {
            retuitRepository.findRetuitsByTuit(tuitPepe.getId());
        } catch (InstanceNotFoundException e) {
            assertEquals(Tuit.class, e.getNotFoundClass());
            exception = true;
        }
        assertTrue(exception);

    }

    /**
     * Removes the tuit with responses.
     */
    @Test
    public void removeTuitWithResponses() {

        Account cuentaPepe = createUser("pepe@example.com", "pepe");
        Account cuentaManolo = createUser("manolo@example.com", "manolo");
        Account cuentaPedro = createUser("pedro@example.com", "pedroOfficial");

        Tuit tuitPepe = createTuit("tuit de prueba", cuentaPepe);
        createResponse("Hola pepe", cuentaManolo, tuitPepe.getId());
        createResponse("que dices pepe", cuentaPedro, tuitPepe.getId());

        tuitRepository.borrarTuit(tuitPepe);

        boolean exception = false;
        try {
            tuitRepository.findById(tuitPepe.getId());
        } catch (InstanceNotFoundException e) {
            assertEquals(Tuit.class, e.getNotFoundClass());
            exception = true;
        }
        assertTrue(exception);

        List<Tuit> foundResponses = tuitRepository.getResponses(
                tuitPepe.getId(), cuentaPepe.getEmail());

        assertEquals(0, foundResponses.size());

    }

    /**
     * Removes the complex tuit.
     */
    @Test
    public void removeComplexTuit() {
        cleanMongoDB();

        Account cuentaPepe = createUser("pepe@example.com", "pepe");
        Account cuentaManolo = createUser("manolo@example.com", "manolo");
        Account cuentaPedro = createUser("pedro@example.com", "pedroOfficial");

        Tuit tuitPepe = createTuit("tuit de prueba", cuentaPepe);

        createFavorite(tuitPepe, cuentaManolo);
        createFavorite(tuitPepe, cuentaPedro);

        createRetuit(tuitPepe, cuentaManolo);
        createRetuit(tuitPepe, cuentaPedro);

        createResponse("Hola pepe", cuentaManolo, tuitPepe.getId());
        createResponse("que dices pepe", cuentaPedro, tuitPepe.getId());

        tuitRepository.borrarTuit(tuitPepe);

        boolean exception = false;

        try {
            tuitRepository.findById(tuitPepe.getId());
        } catch (InstanceNotFoundException e) {
            assertEquals(Tuit.class, e.getNotFoundClass());
            exception = true;
        }
        assertTrue(exception);

        exception = false;
        try {
            favoriteRepository.findFavoritesByTuit(tuitPepe.getId());
        } catch (InstanceNotFoundException e) {
            assertEquals(Tuit.class, e.getNotFoundClass());
            exception = true;
        }

        assertTrue(exception);

        List<Tuit> foundResponses = tuitRepository.getResponses(
                tuitPepe.getId(), cuentaPepe.getEmail());

        assertEquals(0, foundResponses.size());

        exception = false;
        try {
            retuitRepository.findRetuitsByTuit(tuitPepe.getId());
        } catch (InstanceNotFoundException e) {
            assertEquals(Tuit.class, e.getNotFoundClass());
            exception = true;
        }

        assertTrue(exception);

        cleanMongoDB();
    }
}
