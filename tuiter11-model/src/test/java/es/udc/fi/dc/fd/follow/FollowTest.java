package es.udc.fi.dc.fd.follow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.config.WebAppConfigurationAware;


/**
 * The Class FollowTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FollowTest extends WebAppConfigurationAware {

    /** The follow repository. */
    @Autowired
    public FollowRepository followRepository;

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

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
     * Follow unique user test.
     */
    @Test
    public void followUniqueUserTest() {
        Account follower = createAccount("samu@gmail.com", "samu8", "1234",
                "ROLE_USER");
        Account followed = createAccount("iago@gmail.com", "iago5", "1234",
                "ROLE_USER");

        // followRepository.addFollowed(follower, followed);

        Account fr = accountRepository.findByEmail(follower.getEmail());
        Account fd = accountRepository.findByEmail(followed.getEmail());

        followRepository.followUser(fr, fd);

        List<Account> followedList = followRepository.findFollowedBy(fr
                .getEmail());

        assertEquals(followedList.get(0), fd);

        List<Account> followerList = followRepository.findWhoFollowMe(fd
                .getEmail());

        assertEquals(followerList.get(0), fr);
    }

    /**
     * Follow to multiple users test.
     */
    @Test
    public void followToMultipleUsersTest() {
        Account follower = createAccount("samu@gmail.com", "samu8", "1234",
                "ROLE_USER");
        Account followed1 = createAccount("iago@gmail.com", "iago5", "1234",
                "ROLE_USER");
        Account followed2 = createAccount("rene@gmail.com", "rene1", "1234",
                "ROLE_USER");
        Account followed3 = createAccount("manolo@gmail.com", "manu2", "1234",
                "ROLE_USER");

        // Account follower, Account followed
        followRepository.followUser(follower, followed1);
        followRepository.followUser(follower, followed2);
        followRepository.followUser(follower, followed3);

        List<Account> followedList = followRepository.findFollowedBy(follower
                .getEmail());

        assertEquals(followedList.get(0), followed1);
        assertEquals(followedList.get(1), followed2);
        assertEquals(followedList.get(2), followed3);
    }

    /**
     * Be followed by multiple users test.
     */
    @Test
    public void beFollowedByMultipleUsersTest() {
        Account followed = createAccount("samu@gmail.com", "samu8", "1234",
                "ROLE_USER");
        Account follower1 = createAccount("iago@gmail.com", "iago5", "1234",
                "ROLE_USER");
        Account follower2 = createAccount("rene@gmail.com", "rene1", "1234",
                "ROLE_USER");
        Account follower3 = createAccount("manolo@gmail.com", "manu2", "1234",
                "ROLE_USER");

        // Account follower, Account followed
        followRepository.followUser(follower1, followed);
        followRepository.followUser(follower2, followed);
        followRepository.followUser(follower3, followed);

        List<Account> followedList = followRepository.findWhoFollowMe(followed
                .getEmail());

        assertEquals(followedList.get(0), follower1);
        assertEquals(followedList.get(1), follower2);
        assertEquals(followedList.get(2), follower3);
    }

    /**
     * Mutually follow users test.
     */
    @Test
    public void mutuallyFollowUsersTest() {
        Account user1 = createAccount("samu@gmail.com", "samu8", "1234",
                "ROLE_USER");
        Account user2 = createAccount("iago@gmail.com", "iago5", "1234",
                "ROLE_USER");
        Account user3 = createAccount("rene@gmail.com", "rene1", "1234",
                "ROLE_USER");

        followRepository.followUser(user1, user2);
        followRepository.followUser(user1, user3);
        followRepository.followUser(user2, user1);
        followRepository.followUser(user2, user3);
        followRepository.followUser(user3, user1);
        followRepository.followUser(user3, user2);

        List<Account> followedList1 = followRepository.findFollowedBy(user1
                .getEmail());
        List<Account> followedList2 = followRepository.findFollowedBy(user2
                .getEmail());
        List<Account> followedList3 = followRepository.findFollowedBy(user3
                .getEmail());

        assertEquals(followedList1.size(), 2);
        assertEquals(followedList2.size(), 2);
        assertEquals(followedList3.size(), 2);

        assertTrue(followedList1.contains(user2));
        assertTrue(followedList1.contains(user3));
        assertTrue(followedList2.contains(user1));
        assertTrue(followedList2.contains(user3));
        assertTrue(followedList3.contains(user1));
        assertTrue(followedList3.contains(user2));

        List<Account> followerList1 = followRepository.findFollowedBy(user1
                .getEmail());
        List<Account> followerList2 = followRepository.findFollowedBy(user2
                .getEmail());
        List<Account> followerList3 = followRepository.findFollowedBy(user3
                .getEmail());

        assertEquals(followedList1.size(), 2);
        assertEquals(followedList2.size(), 2);
        assertEquals(followedList3.size(), 2);

        assertTrue(followerList1.contains(user2));
        assertTrue(followerList1.contains(user3));
        assertTrue(followerList2.contains(user1));
        assertTrue(followerList2.contains(user3));
        assertTrue(followerList3.contains(user1));
        assertTrue(followerList3.contains(user2));
    }
}
