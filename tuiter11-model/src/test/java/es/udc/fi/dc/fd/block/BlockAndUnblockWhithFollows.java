package es.udc.fi.dc.fd.block;

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

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.config.WebAppConfigurationAware;
import es.udc.fi.dc.fd.follow.FollowRepository;
import es.udc.fi.dc.fd.publish.Publish;
import es.udc.fi.dc.fd.publish.PublishService;
import es.udc.fi.dc.fd.retuit.RetuitDto;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitDto;
import es.udc.fi.dc.fd.tuit.TuitRepository;


/**
 * The Class BlockAndUnblockWhithFollows.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BlockAndUnblockWhithFollows extends WebAppConfigurationAware {

    /** The block repository. */
    @Autowired
    public BlockRepository blockRepository;

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The follow repository. */
    @Autowired
    private FollowRepository followRepository;

    /** The publish service. */
    @Autowired
    private PublishService publishService;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

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
     * Block follower.
     */
    @Test
    public void BlockFollower() {

        cleanMongoDB();

        // Crear user1 y user2
        Account user1 = createAccount("user1@example", "user1", "1234",
                "ROLE_USER");
        Account user2 = createAccount("user2@example", "user2", "1234",
                "ROLE_USER");

        // User1 sigue a user2
        followRepository.followUser(user1, user2);
        // User 2 tuitea algo
        Tuit tuit = new Tuit(Calendar.getInstance().getTimeInMillis(),
                "Tuit de prueba", user2);
        tuitRepository.save(tuit);
        // User 1 obtiene su timeline con el tuit de user 2
        List<? extends Publish> user1TimeLine = publishService
                .getTimeLinePublications(user1.getEmail());

        assertEquals(1, user1TimeLine.size());
        assertTrue(user1TimeLine.contains(tuit));

        // User2 bloquea a user1
        Block block = blockRepository.blockUser(user2, user1);
        // Se comprueba el bloqueo
        blockRepository.findBlockByEmails(user2.getEmail(), user1.getEmail());
        // User1 obtiene su timeLine (vacío)
        user1TimeLine = publishService
                .getTimeLinePublications(user1.getEmail());

        assertTrue(user1TimeLine.isEmpty());

        // User 2 desbloquea a user1
        blockRepository.deleteBlock(block);
        // TODO Que debería pasar ahora cuando user1 ve su timeline

        cleanMongoDB();

    }
}
