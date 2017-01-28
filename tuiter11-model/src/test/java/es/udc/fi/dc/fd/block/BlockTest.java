package es.udc.fi.dc.fd.block;

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
 * The Class BlockTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BlockTest extends WebAppConfigurationAware {

    /** The block repository. */
    @Autowired
    public BlockRepository blockRepository;

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
     * Block unique user test.
     */
    @Test
    public void blockUniqueUserTest() {
        Account blocker = createAccount("samu@gmail.com", "samu8", "1234",
                "ROLE_USER");
        Account blocked = createAccount("iago@gmail.com", "iago5", "1234",
                "ROLE_USER");

        // blockRepository.addBlocked(blocker, blocked);

        Account fr = accountRepository.findByEmail(blocker.getEmail());
        Account fd = accountRepository.findByEmail(blocked.getEmail());

        blockRepository.blockUser(fr, fd);

        Block rescatedBlock = blockRepository.findBlockByEmails(
                blocker.getEmail(), blocked.getEmail());

        assertEquals(rescatedBlock.getBlocker(), blocker);
        assertEquals(rescatedBlock.getBlocked(), blocked);

        List<Account> blockedList = blockRepository.findBlockedByMe(fr
                .getEmail());

        assertEquals(blockedList.get(0), fd);

        List<Account> blockerList = blockRepository.findWhoBlockMe(fd
                .getEmail());

        assertEquals(blockerList.get(0), fr);
    }

    /**
     * Block to multiple users test.
     */
    @Test
    public void blockToMultipleUsersTest() {
        Account blocker = createAccount("samu@gmail.com", "samu8", "1234",
                "ROLE_USER");
        Account blocked1 = createAccount("iago@gmail.com", "iago5", "1234",
                "ROLE_USER");
        Account blocked2 = createAccount("rene@gmail.com", "rene1", "1234",
                "ROLE_USER");
        Account blocked3 = createAccount("manolo@gmail.com", "manu2", "1234",
                "ROLE_USER");

        blockRepository.blockUser(blocker, blocked1);
        blockRepository.blockUser(blocker, blocked2);
        blockRepository.blockUser(blocker, blocked3);

        List<Account> blockedList = blockRepository.findBlockedByMe(blocker
                .getEmail());

        assertEquals(blockedList.get(0), blocked1);
        assertEquals(blockedList.get(1), blocked2);
        assertEquals(blockedList.get(2), blocked3);
    }

    /**
     * Be blocked by multiple users test.
     */
    @Test
    public void beBlockedByMultipleUsersTest() {
        Account blocked = createAccount("samu@gmail.com", "samu8", "1234",
                "ROLE_USER");
        Account blocker1 = createAccount("iago@gmail.com", "iago5", "1234",
                "ROLE_USER");
        Account blocker2 = createAccount("rene@gmail.com", "rene1", "1234",
                "ROLE_USER");
        Account blocker3 = createAccount("manolo@gmail.com", "manu2", "1234",
                "ROLE_USER");

        // Account blocker, Account blocked
        blockRepository.blockUser(blocker1, blocked);
        blockRepository.blockUser(blocker2, blocked);
        blockRepository.blockUser(blocker3, blocked);

        List<Account> blockedList = blockRepository.findWhoBlockMe(blocked
                .getEmail());

        assertEquals(blockedList.get(0), blocker1);
        assertEquals(blockedList.get(1), blocker2);
        assertEquals(blockedList.get(2), blocker3);
    }

    /**
     * Mutually block users test.
     */
    @Test
    public void mutuallyBlockUsersTest() {
        Account user1 = createAccount("samu@gmail.com", "samu8", "1234",
                "ROLE_USER");
        Account user2 = createAccount("iago@gmail.com", "iago5", "1234",
                "ROLE_USER");
        Account user3 = createAccount("rene@gmail.com", "rene1", "1234",
                "ROLE_USER");

        blockRepository.blockUser(user1, user2);
        blockRepository.blockUser(user1, user3);
        blockRepository.blockUser(user2, user1);
        blockRepository.blockUser(user2, user3);
        blockRepository.blockUser(user3, user1);
        blockRepository.blockUser(user3, user2);

        List<Account> blockedList1 = blockRepository.findBlockedByMe(user1
                .getEmail());
        List<Account> blockedList2 = blockRepository.findBlockedByMe(user2
                .getEmail());
        List<Account> blockedList3 = blockRepository.findBlockedByMe(user3
                .getEmail());

        assertEquals(blockedList1.size(), 2);
        assertEquals(blockedList2.size(), 2);
        assertEquals(blockedList3.size(), 2);

        assertTrue(blockedList1.contains(user2));
        assertTrue(blockedList1.contains(user3));
        assertTrue(blockedList2.contains(user1));
        assertTrue(blockedList2.contains(user3));
        assertTrue(blockedList3.contains(user1));
        assertTrue(blockedList3.contains(user2));

        List<Account> blockerList1 = blockRepository.findBlockedByMe(user1
                .getEmail());
        List<Account> blockerList2 = blockRepository.findBlockedByMe(user2
                .getEmail());
        List<Account> blockerList3 = blockRepository.findBlockedByMe(user3
                .getEmail());

        assertEquals(blockedList1.size(), 2);
        assertEquals(blockedList2.size(), 2);
        assertEquals(blockedList3.size(), 2);

        assertTrue(blockerList1.contains(user2));
        assertTrue(blockerList1.contains(user3));
        assertTrue(blockerList2.contains(user1));
        assertTrue(blockerList2.contains(user3));
        assertTrue(blockerList3.contains(user1));
        assertTrue(blockerList3.contains(user2));
    }
}
