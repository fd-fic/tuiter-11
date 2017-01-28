package es.udc.fi.dc.fd.conversations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.block.Block;
import es.udc.fi.dc.fd.block.BlockRepository;
import es.udc.fi.dc.fd.config.WebAppConfigurationAware;
import es.udc.fi.dc.fd.favorite.Favorite;
import es.udc.fi.dc.fd.favorite.FavoriteRepository;
import es.udc.fi.dc.fd.follow.FollowRepository;
import es.udc.fi.dc.fd.retuit.Retuit;
import es.udc.fi.dc.fd.retuit.RetuitDto;
import es.udc.fi.dc.fd.retuit.RetuitRepository;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitDto;
import es.udc.fi.dc.fd.tuit.TuitRepository;


/**
 * The Class ConversationsTest.
 */
@Transactional
public class ConversationsTest extends WebAppConfigurationAware {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /** The block repository. */
    @Autowired
    private BlockRepository blockRepository;

    /** The retuit repository. */
    @Autowired
    private RetuitRepository retuitRepository;

    /** The favorite repository. */
    @Autowired
    private FavoriteRepository favoriteRepository;

    /** The follow repository. */
    @Autowired
    private FollowRepository followRepository;

    /** The mongo template. */
    @Autowired
    private MongoTemplate mongoTemplate;

    /** The cuenta pepe. */
    Account cuentaPepe = null;

    /** The cuenta manolo. */
    Account cuentaManolo = null;

    /** The cuenta jose. */
    Account cuentaJose = null;

    /** The cuenta juan. */
    Account cuentaJuan = null;

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
     * @param user
     *            the user
     * @param parent
     *            the parent
     * @param fecha
     *            the fecha
     * @return the tuit
     */
    private Tuit createTuit(String message, Account user, String parent,
            Calendar fecha) {
        // Calendar fecha = Calendar.getInstance();
        Tuit tuit = new Tuit(fecha.getTimeInMillis(), message, user);
        tuit = tuitRepository.saveWithParent(tuit, parent, 0);
        return tuit;
    }

    /**
     * Initialize users.
     */
    @Before
    public void initializeUsers() {
        cuentaPepe = createUser("pepe@gmail.com", "pepe");
        cuentaManolo = createUser("manolo@gmail.com", "manolo");
        cuentaJose = createUser("jose@gmail.com", "jose");
        cuentaJuan = createUser("juan@gmail.com", "juan");
    }

    /**
     * Replies test.
     */
    @Test
    public void repliesTest() {
        cleanMongoDB();
        Calendar fecha = Calendar.getInstance();
        // Creamos un tuit para Pepe
        Tuit tuitPepe = createTuit("Este es un tuit de prueba", cuentaPepe,
                null, fecha);
        // Creamos un tuit para Manolo
        fecha.add(Calendar.MINUTE, 1);
        Tuit tuitManolo = createTuit("Yo respondo a Pepe", cuentaManolo,
                tuitPepe.getId().toString(), fecha);

        fecha.add(Calendar.MINUTE, 2);
        // Creamos un tuit para Jose
        Tuit tuitJose = createTuit("Yo respondo a Manolo", cuentaJose,
                tuitManolo.getId().toString(), fecha);

        fecha.add(Calendar.MINUTE, 3);
        // Creamos un tuit para Juan
        Tuit tuitJuan = createTuit("Yo respondo a Pepe", cuentaJuan, tuitPepe
                .getId().toString(), fecha);

        // Accedemos al tuit de Pepe para ver sus respuestas
        List<Tuit> responsesTuitPepe = new ArrayList<Tuit>();
        responsesTuitPepe = tuitRepository.getResponses(tuitPepe.getId(),
                cuentaPepe.getEmail());

        assertEquals(3, responsesTuitPepe.size());
        assertEquals(tuitManolo, responsesTuitPepe.get(0));
        assertEquals(tuitJose, responsesTuitPepe.get(1));
        assertEquals(tuitJuan, responsesTuitPepe.get(2));

        // Accedemos al tuit de Juan para ver sus respuestas
        List<Tuit> responsesTuitJuan = new ArrayList<Tuit>();
        responsesTuitJuan = tuitRepository.getResponses(tuitJuan.getId(),
                cuentaJuan.getEmail());
        // Accedemos al tuit de Jose para ver sus respuestas
        List<Tuit> responsesTuitJose = new ArrayList<Tuit>();
        responsesTuitJose = tuitRepository.getResponses(tuitJose.getId(),
                cuentaJose.getEmail());

        assertEquals(0, responsesTuitJuan.size());
        assertEquals(0, responsesTuitJose.size());

        // Accedemos al tuit de Manolo para ver sus respuestas
        List<Tuit> responsesTuitManolo = new ArrayList<Tuit>();
        responsesTuitManolo = tuitRepository.getResponses(tuitManolo.getId(),
                cuentaManolo.getEmail());

        assertEquals(1, responsesTuitManolo.size());
        assertEquals(tuitJose, responsesTuitManolo.get(0));

        cleanMongoDB();
    }

    /**
     * Parents test.
     */
    @Test
    public void parentsTest() {
        cleanMongoDB();

        Calendar fecha = Calendar.getInstance();
        // Creamos un tuit para Pepe
        Tuit tuitPepe = createTuit("Este es un tuit de prueba", cuentaPepe,
                null, fecha);
        // Creamos un tuit para Manolo
        fecha.add(Calendar.MINUTE, 1);
        Tuit tuitManolo = createTuit("Yo respondo a Pepe", cuentaManolo,
                tuitPepe.getId().toString(), fecha);

        fecha.add(Calendar.MINUTE, 2);
        // Creamos un tuit para Jose
        Tuit tuitJose = createTuit("Yo respondo a Manolo", cuentaJose,
                tuitManolo.getId().toString(), fecha);

        fecha.add(Calendar.MINUTE, 3);
        // Creamos un tuit para Juan
        Tuit tuitJuan = createTuit("Yo respondo a Pepe", cuentaJuan, tuitPepe
                .getId().toString(), fecha);

        List<Tuit> parentsTuitJose = tuitRepository.getParents(
                tuitJose.getId(), cuentaJose.getEmail());
        List<Tuit> parentsTuitJuan = tuitRepository.getParents(
                tuitJuan.getId(), cuentaJuan.getEmail());

        assertEquals(2, parentsTuitJose.size());
        assertEquals(1, parentsTuitJuan.size());

        assertEquals(tuitPepe, parentsTuitJose.get(0));
        assertEquals(tuitManolo, parentsTuitJose.get(1));
        assertEquals(tuitPepe, parentsTuitJuan.get(0));

        cleanMongoDB();
    }

    /**
     * Visibility conversations test.
     */
    @Test
    public void visibilityConversationsTest() {

        cleanMongoDB();

        Calendar fecha = Calendar.getInstance();
        // Creamos un tuit para Pepe
        Tuit tuitPepe = createTuit("Este es un tuit de prueba", cuentaPepe,
                null, fecha);
        // Creamos un tuit para Manolo
        fecha.add(Calendar.MINUTE, 1);
        Tuit tuitManolo = createTuit("Yo respondo a Pepe", cuentaManolo,
                tuitPepe.getId().toString(), fecha);

        fecha.add(Calendar.MINUTE, 2);
        // Creamos un tuit para Jose
        Tuit tuitJose = createTuit("Yo respondo a Manolo", cuentaJose,
                tuitManolo.getId().toString(), fecha);

        fecha.add(Calendar.MINUTE, 3);
        // Creamos un tuit para Juan
        Tuit tuitJuan = createTuit("Yo respondo a Pepe", cuentaJuan, tuitPepe
                .getId().toString(), fecha);

        // Manolo pone su cuenta como privada
        cuentaManolo.setPrivateAccount(1);
        accountRepository.updateWithoutPass(cuentaManolo);
        // Pepe bloquea a Juan
        Block blockPepeJuan = blockRepository.blockUser(cuentaPepe, cuentaJuan);

        List<Tuit> parentsTuitJuan = tuitRepository.getParents(
                tuitJuan.getId(), cuentaJuan.getEmail());

        assertEquals(0, parentsTuitJuan.size());

        // Ahora Pepe desbloquea a Juan
        blockRepository.deleteBlock(blockPepeJuan);

        parentsTuitJuan = tuitRepository.getParents(tuitJuan.getId(),
                cuentaJuan.getEmail());
        assertEquals(1, parentsTuitJuan.size());
        assertEquals(tuitPepe, parentsTuitJuan.get(0));

        // Buscamos los padres del tuit de Jose
        List<Tuit> parentsTuitJose = tuitRepository.getParents(
                tuitJose.getId(), cuentaPepe.getEmail());

        // Nos aseguramos de que el tuit encontrado es de Pepe
        assertEquals(1, parentsTuitJose.size());
        assertTrue(tuitPepe.equals(parentsTuitJose.get(0)));

        // Manolo vuelve a poner su cuenta como publica
        cuentaManolo.setPrivateAccount(null);
        accountRepository.updateWithoutPass(cuentaManolo);

        // Volvemos a buscar los tuits padres del tuit de Jose
        parentsTuitJose = tuitRepository.getParents(tuitJose.getId(),
                cuentaPepe.getEmail());

        // Nos aseguramos de que los tuits encontrados son de Pepe y Manolo
        assertEquals(2, parentsTuitJose.size());
        assertEquals(tuitPepe, parentsTuitJose.get(0));
        assertEquals(tuitManolo, parentsTuitJose.get(1));

        cleanMongoDB();
    }

    /**
     * Visibility conversations test2.
     */
    @Test
    public void visibilityConversationsTest2() {

        cleanMongoDB();
        Calendar fecha = Calendar.getInstance();
        // Creamos un tuit para Pepe
        Tuit tuitPepe = createTuit("Este es un tuit de prueba", cuentaPepe,
                null, fecha);
        // Creamos un tuit para Manolo
        fecha.add(Calendar.MINUTE, 1);
        Tuit tuitManolo = createTuit("Soy Manolo y me quedo yo solo",
                cuentaManolo, null, fecha);

        fecha.add(Calendar.MINUTE, 2);
        // Creamos un tuit para Jose
        Tuit tuitJose = createTuit("Yo respondo a Pepe", cuentaJose, tuitPepe
                .getId().toString(), fecha);

        fecha.add(Calendar.MINUTE, 3);
        // Creamos un tuit para Juan
        Tuit tuitJuan = createTuit("Yo respondo a Manolo", cuentaJuan,
                tuitManolo.getId().toString(), fecha);

        // Manolo accede al tuit de Pepe y mostramos sus respuestas
        List<Tuit> responsesTuitPepe = tuitRepository.getResponses(
                tuitPepe.getId(), cuentaManolo.getEmail());

        assertEquals(1, responsesTuitPepe.size());
        assertTrue(tuitJose.equals(responsesTuitPepe.get(0)));

        // Pepe accede al tuit de Manolo y mostramos sus respuestas
        List<Tuit> responsesTuitManolo = tuitRepository.getResponses(
                tuitManolo.getId(), cuentaPepe.getEmail());

        assertEquals(1, responsesTuitManolo.size());
        assertEquals(tuitJuan, responsesTuitManolo.get(0));

        // Juan va a retuitear el tuit de Jose
        Calendar fechaActual = Calendar.getInstance();
        Retuit retuit = new Retuit(fechaActual.getTimeInMillis(), tuitJose,
                cuentaJuan);
        retuitRepository.save(retuit);

        // Juan accede al tuit de Pepe y mostramos sus respuestas
        responsesTuitPepe = tuitRepository.getResponses(tuitPepe.getId(),
                cuentaJuan.getEmail());

        assertEquals(1, responsesTuitPepe.size());
        assertEquals(tuitJose, responsesTuitPepe.get(0));

        // Jose pone su cuenta como publica
        cuentaJose.setPrivateAccount(1);
        accountRepository.updateWithoutPass(cuentaJose);

        // Juan accede al tuit de Pepe y mostramos sus respuestas
        responsesTuitPepe = tuitRepository.getResponses(tuitPepe.getId(),
                cuentaJuan.getEmail());

        // Juan ya no puede ver el tuit de Jose porque ha puesto su cuenta como
        // privada
        assertEquals(0, responsesTuitPepe.size());

        // Juan sigue ahora a Jose
        followRepository.followUser(cuentaJuan, cuentaJose);

        // Jose sigue ahora a Juan
        followRepository.followUser(cuentaJose, cuentaJuan);

        // Comprobamos que Juan ya puede ver el tuit de Jose
        responsesTuitPepe = tuitRepository.getResponses(tuitPepe.getId(),
                cuentaJuan.getEmail());

        assertEquals(1, responsesTuitPepe.size());
        assertEquals(tuitJose, responsesTuitPepe.get(0));

        cleanMongoDB();
    }

    /**
     * Visibility conversations test3.
     */
    @Test
    public void visibilityConversationsTest3() {

        cleanMongoDB();

        Calendar fecha = Calendar.getInstance();
        // Creamos un tuit para Pepe
        Tuit tuitPepe = createTuit("Este es un tuit de prueba", cuentaPepe,
                null, fecha);

        fecha.add(Calendar.MINUTE, 1);
        // Creamos un tuit para Manolo, que contesta al primero de Pepe
        Tuit tuitManolo = createTuit("Contesto a Pepe!", cuentaManolo,
                tuitPepe.getId(), fecha);

        fecha.add(Calendar.MINUTE, 1);
        // Creamos otro tuit para Pepe, la contestacion al de Manolo
        Tuit tuitPepeContestaManolo = createTuit(
                "Pues yo te contesto a Manolo!", cuentaPepe,
                tuitManolo.getId(), fecha);

        // Pepe marca como favorito el tuit de Manolo
        Favorite favoritoPepe = new Favorite(tuitManolo, cuentaPepe);
        favoriteRepository.save(favoritoPepe);

        List<Tuit> parentsTuitManolo = tuitRepository.getParents(
                tuitManolo.getId(), cuentaPepe.getEmail());
        List<Tuit> responsesTuitManolo = tuitRepository.getResponses(
                tuitManolo.getId(), cuentaPepe.getEmail());

        assertEquals(1, parentsTuitManolo.size());
        assertEquals(1, responsesTuitManolo.size());

        assertTrue(tuitPepe.equals(parentsTuitManolo.get(0)));
        assertTrue(tuitPepeContestaManolo.equals(responsesTuitManolo.get(0)));

        // Ahora, Manolo pondrá su cuenta como privada
        cuentaManolo.setPrivateAccount(1);
        accountRepository.updateWithoutPass(cuentaManolo);

        List<Tuit> responsesTuitPepe = tuitRepository.getResponses(
                tuitPepe.getId(), cuentaPepe.getEmail());

        assertEquals(1, responsesTuitPepe.size());
        assertTrue(tuitPepeContestaManolo.equals(responsesTuitPepe.get(0)));

        // Manolo pone su cuenta publica otra vez
        cuentaManolo.setPrivateAccount(null);
        accountRepository.updateWithoutPass(cuentaManolo);

        responsesTuitPepe = tuitRepository.getResponses(tuitPepe.getId(),
                cuentaManolo.getEmail());

        assertEquals(2, responsesTuitPepe.size());
        assertTrue(tuitManolo.equals(responsesTuitPepe.get(0)));
        assertTrue(tuitPepeContestaManolo.equals(responsesTuitPepe.get(1)));

        cleanMongoDB();
    }
}
