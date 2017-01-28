package es.udc.fi.dc.fd.account;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.block.Block;
import es.udc.fi.dc.fd.block.BlockRepository;
import es.udc.fi.dc.fd.config.WebAppConfigurationAware;
import es.udc.fi.dc.fd.follow.FollowRepository;
import es.udc.fi.dc.fd.publish.Publish;
import es.udc.fi.dc.fd.publish.PublishService;
import es.udc.fi.dc.fd.retuit.RetuitDto;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitDto;
import es.udc.fi.dc.fd.tuit.TuitRepository;


/**
 * The Class PublicationsTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PublicationsTest extends WebAppConfigurationAware {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /** The publish service. */
    @Autowired
    private PublishService publishService;

    /** The follow repository. */
    @Autowired
    private FollowRepository followRepository;

    /** The block repository. */
    @Autowired
    private BlockRepository blockRepository;

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
     * Creates the account.
     *
     * @param email
     *            the email
     * @param nick
     *            the nick
     * @param passw
     *            the passw
     * @param role
     *            the role
     * @return the account
     */
    private Account createAccount(String email, String nick, String passw,
            String role) {
        Account account = new Account(email, nick, passw, role);
        accountRepository.save(account);
        return account;
    }

    /**
     * Test show my tuits.
     */
    @Test
    public void TestShowMyTuits() {

        cleanMongoDB();
        // Crear user1 y user2
        Account user1 = createAccount("user1@example", "user1", "1234",
                "ROLE_USER");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DAY_OF_YEAR, 1);
        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DAY_OF_YEAR, -1);
        Calendar c4 = Calendar.getInstance();
        c4.add(Calendar.DAY_OF_YEAR, 2);

        Tuit tuit1 = new Tuit(c1.getTimeInMillis(), "Tuit1", user1);
        tuitRepository.save(tuit1);
        Tuit tuit2 = new Tuit(c2.getTimeInMillis(), "Tuit2", user1);
        tuitRepository.save(tuit2);
        Tuit tuit3 = new Tuit(c3.getTimeInMillis(), "Tuit3", user1);
        tuitRepository.save(tuit3);
        Tuit tuit4 = new Tuit(c4.getTimeInMillis(), "Tuit4", user1);
        tuitRepository.save(tuit4);

        List<? extends Publish> myPublications = publishService
                .getUserPublications(user1.getEmail());

        assertEquals(myPublications.size(), 4);
        assertEquals(myPublications.get(0), tuit4);
        assertEquals(myPublications.get(1), tuit2);
        assertEquals(myPublications.get(2), tuit1);
        assertEquals(myPublications.get(3), tuit3);

        cleanMongoDB();
    }

    /**
     * Test my time line.
     */
    @Test
    public void TestMyTimeLine() {
        // Crear user1 y user2
        Account user1 = createAccount("user1@example", "user1", "1234",
                "ROLE_USER");
        Account user2 = createAccount("user2@example", "user2", "1234",
                "ROLE_USER");

        Account user3 = createAccount("user3@example", "user3", "1234",
                "ROLE_USER");

        followRepository.followUser(user1, user2);
        followRepository.followUser(user1, user3);

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DAY_OF_YEAR, 1);
        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DAY_OF_YEAR, -1);
        Calendar c4 = Calendar.getInstance();
        c4.add(Calendar.DAY_OF_YEAR, 2);

        Tuit tuit1 = new Tuit(c1.getTimeInMillis(), "Tuit1 - user1", user1);
        tuitRepository.save(tuit1);
        Tuit tuit2 = new Tuit(c2.getTimeInMillis(), "Tuit2 - user2", user2);
        tuitRepository.save(tuit2);
        Tuit tuit3 = new Tuit(c3.getTimeInMillis(), "Tuit3 - user2", user2);
        tuitRepository.save(tuit3);
        Tuit tuit4 = new Tuit(c4.getTimeInMillis(), "Tuit4 - user3", user3);
        tuitRepository.save(tuit4);

        // Comprobamos la lista de mis publicaciones
        assertEquals(publishService.getUserPublications(user1.getEmail())
                .size(), 1);
        assertEquals(publishService.getUserPublications(user1.getEmail())
                .get(0), tuit1);

        // Comprobamos el timeLine
        List<? extends Publish> timeLine = publishService
                .getTimeLinePublications(user1.getEmail());

        assertEquals(timeLine.size(), 4);
        assertEquals(timeLine.get(0), tuit4);
        assertEquals(timeLine.get(1), tuit2);
        assertEquals(timeLine.get(2), tuit1);
        assertEquals(timeLine.get(3), tuit3);

    }

    /**
     * Test my time line whith private acounts.
     */
    @Test
    public void TestMyTimeLineWhithPrivateAcounts() {

        // Crear user1 y user2
        Account user1 = createAccount("user1@example", "user1", "1234",
                "ROLE_USER");
        Account user2 = createAccount("user2@example", "user2", "1234",
                "ROLE_USER");

        Account user3 = createAccount("user3@example", "user3", "1234",
                "ROLE_USER");

        followRepository.followUser(user1, user2);
        followRepository.followUser(user1, user3);

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DAY_OF_YEAR, 1);
        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DAY_OF_YEAR, -1);
        Calendar c4 = Calendar.getInstance();
        c4.add(Calendar.DAY_OF_YEAR, 2);

        Tuit tuit1 = new Tuit(c1.getTimeInMillis(), "Tuit1 - user1", user1);
        tuitRepository.save(tuit1);
        Tuit tuit2 = new Tuit(c2.getTimeInMillis(), "Tuit2 - user2", user2);
        tuitRepository.save(tuit2);
        Tuit tuit3 = new Tuit(c3.getTimeInMillis(), "Tuit3 - user2", user2);
        tuitRepository.save(tuit3);
        Tuit tuit4 = new Tuit(c4.getTimeInMillis(), "Tuit4 - user3", user3);
        tuitRepository.save(tuit4);

        // Declaramos la cuenta 3 como privada
        user3.setPrivateAccount(1);
        accountRepository.updateWithoutPass(user3);

        List<? extends Publish> timeLine = publishService
                .getTimeLinePublications(user1.getEmail());

        assertEquals(timeLine.size(), 3);
        assertEquals(timeLine.get(0), tuit2);
        assertEquals(timeLine.get(1), tuit1);
        assertEquals(timeLine.get(2), tuit3);

        user3.setPrivateAccount(null);
        accountRepository.updateWithoutPass(user3);

        // La desdeclaramos para ver que nos deja ver de nuevo sus tuits
        timeLine = publishService.getTimeLinePublications(user1.getEmail());

        assertEquals(timeLine.size(), 4);
        assertEquals(timeLine.get(0), tuit4);
        assertEquals(timeLine.get(1), tuit2);
        assertEquals(timeLine.get(2), tuit1);
        assertEquals(timeLine.get(3), tuit3);

        // Ahora la ponemos privada, y hacemos que user3 sigua a user1, el
        // resultado debería de ser el mismo
        user3.setPrivateAccount(1);
        accountRepository.updateWithoutPass(user3);
        followRepository.followUser(user3, user1);

        timeLine = publishService.getTimeLinePublications(user1.getEmail());

        assertEquals(timeLine.size(), 4);
        assertEquals(timeLine.get(0), tuit4);
        assertEquals(timeLine.get(1), tuit2);
        assertEquals(timeLine.get(2), tuit1);
        assertEquals(timeLine.get(3), tuit3);

    }

    /**
     * Test my time line whith blocks.
     */
    @Test
    public void TestMyTimeLineWhithBlocks() {
        // Crear user1 y user2
        Account user1 = createAccount("user1@example", "user1", "1234",
                "ROLE_USER");
        Account user2 = createAccount("user2@example", "user2", "1234",
                "ROLE_USER");

        Account user3 = createAccount("user3@example", "user3", "1234",
                "ROLE_USER");

        followRepository.followUser(user1, user2);
        followRepository.followUser(user1, user3);

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DAY_OF_YEAR, 1);
        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DAY_OF_YEAR, -1);
        Calendar c4 = Calendar.getInstance();
        c4.add(Calendar.DAY_OF_YEAR, 2);

        Tuit tuit1 = new Tuit(c1.getTimeInMillis(), "Tuit1 - user1", user1);
        tuitRepository.save(tuit1);
        Tuit tuit2 = new Tuit(c2.getTimeInMillis(), "Tuit2 - user2", user2);
        tuitRepository.save(tuit2);
        Tuit tuit3 = new Tuit(c3.getTimeInMillis(), "Tuit3 - user2", user2);
        tuitRepository.save(tuit3);
        Tuit tuit4 = new Tuit(c4.getTimeInMillis(), "Tuit4 - user3", user3);
        tuitRepository.save(tuit4);

        Block block = blockRepository.blockUser(user3, user1);

        // Comprobamos el timeLine
        List<? extends Publish> timeLine = publishService
                .getTimeLinePublications(user1.getEmail());

        assertEquals(timeLine.size(), 3);
        assertEquals(timeLine.get(0), tuit2);
        assertEquals(timeLine.get(1), tuit1);
        assertEquals(timeLine.get(2), tuit3);

        user3.setPrivateAccount(null);
        accountRepository.updateWithoutPass(user3);

        blockRepository.deleteBlock(block);

        // TODO que pasa en este caso? Hay que recuperar los tuits o no?
        // assertEquals(timeLine.size(), 4);
        // assertEquals(timeLine.get(0), tuit4);
        // assertEquals(timeLine.get(1), tuit2);
        // assertEquals(timeLine.get(2), tuit1);
        // assertEquals(timeLine.get(3), tuit3);

    }

}
