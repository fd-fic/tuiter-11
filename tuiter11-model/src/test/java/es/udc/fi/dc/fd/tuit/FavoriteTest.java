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
import es.udc.fi.dc.fd.favorite.Favorite;
import es.udc.fi.dc.fd.favorite.FavoriteDto;
import es.udc.fi.dc.fd.favorite.FavoriteRepository;
import es.udc.fi.dc.fd.util.InstanceNotFoundException;


/**
 * The Class FavoriteTest.
 */
@Transactional
public class FavoriteTest extends WebAppConfigurationAware {

    /** The user service. */
    @InjectMocks
    private UserService userService = new UserService();

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

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
        mongoTemplate.dropCollection(FavoriteDto.class);
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
     * Creates the new favorite and find it by id.
     */
    @Test
    public void createNewFavoriteAndFindItById() {
        cleanMongoDB();

        Account cuentaPepe = createUser("pepe@example.com", "pepe");
        Account cuentaManolo = createUser("manolo@example.com", "manolo");

        Tuit tuitPepe = createTuit("tuit de prueba", cuentaPepe);

        Favorite manoloFavorite = createFavorite(tuitPepe, cuentaManolo);

        Favorite foundFavorite = favoriteRepository.findByTuitAndEmail(
                tuitPepe.getId(), cuentaManolo.getEmail());

        Tuit favoriteTuit = tuitRepository.findById(foundFavorite.getTuit()
                .getId());
        assertEquals(tuitPepe, favoriteTuit);

        assertTrue(favoriteRepository.wasMarkedAsFavorite(tuitPepe.getId(),
                cuentaManolo.getEmail()));
        List<Favorite> foundFavorites = favoriteRepository
                .findFavoritesBy(cuentaManolo.getEmail());

        assertTrue(foundFavorites.contains(manoloFavorite));

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

        createFavorite(tuitPepe, cuentaManolo);

        Favorite foundFavorite = favoriteRepository.findByTuitAndEmail(
                tuitPepe.getId(), cuentaManolo.getEmail());

        favoriteRepository.deleteFavorite(foundFavorite);

        // Comprobamos que efectivamente ya no está marcado como favorito
        boolean exception = false;
        try {
            favoriteRepository.findByTuitAndEmail(tuitPepe.getId(),
                    cuentaManolo.getEmail());
        } catch (InstanceNotFoundException e) {
            exception = true;
        }

        assertTrue(exception);

        cleanMongoDB();
    }

    /**
     * Test find favoriteed by.
     */
    @Test
    public void TestFindFavoriteedBy() {
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
        Favorite Favorite_u2_t1 = createFavorite(tuitU21, user1);
        Favorite Favorite_u3_t1 = createFavorite(tuitU31, user1);

        List<Favorite> favorites;
        favorites = favoriteRepository.findFavoritesBy(user1.getEmail());
        assertEquals(favorites.size(), 2);
        assertTrue(favorites.contains(Favorite_u2_t1));
        assertTrue(favorites.contains(Favorite_u3_t1));

        List<Tuit> tuits;
        tuits = favoriteRepository.findFavoriteTuitsOf(user1.getEmail());
        assertEquals(tuits.size(), 2);
        assertTrue(tuits.contains(tuitU21));
        assertTrue(tuits.contains(tuitU31));

        // user3 favoriteea a user4 Deberámos obtener el mismo resultado

        createFavorite(tuitU41, user3);

        favorites = favoriteRepository.findFavoritesBy(user1.getEmail());
        assertEquals(favorites.size(), 2);
        assertTrue(favorites.contains(Favorite_u2_t1));
        assertTrue(favorites.contains(Favorite_u3_t1));

        tuits = favoriteRepository.findFavoriteTuitsOf(user1.getEmail());
        assertEquals(tuits.size(), 2);
        assertTrue(tuits.contains(tuitU21));
        assertTrue(tuits.contains(tuitU31));

        // user2 hace fav a user1 Deberámos obtener el mismo resultado

        createFavorite(tuitU11, user2);

        favorites = favoriteRepository.findFavoritesBy(user1.getEmail());
        assertEquals(favorites.size(), 2);
        assertTrue(favorites.contains(Favorite_u2_t1));
        assertTrue(favorites.contains(Favorite_u3_t1));

        tuits = favoriteRepository.findFavoriteTuitsOf(user1.getEmail());
        assertEquals(tuits.size(), 2);
        assertTrue(tuits.contains(tuitU21));
        assertTrue(tuits.contains(tuitU31));

        cleanMongoDB();
    }
}
