package es.udc.fi.dc.fd.block;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.config.WebAppConfigurationAware;
import es.udc.fi.dc.fd.util.InstanceNotFoundException;


/**
 * The Class UnBlockTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UnBlockTest extends WebAppConfigurationAware {

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
     * Un block user test.
     */
    @Test
    public void unBlockUserTest() {
        Account blocker = createAccount("samu@gmail.com", "samu8", "1234",
                "ROLE_USER");
        Account blocked = createAccount("iago@gmail.com", "iago5", "1234",
                "ROLE_USER");

        Account fr = accountRepository.findByEmail(blocker.getEmail());
        Account fd = accountRepository.findByEmail(blocked.getEmail());

        blockRepository.blockUser(fr, fd);

        Block rescatedBlock = blockRepository.findBlockByEmails(
                blocker.getEmail(), blocked.getEmail());

        assertEquals(rescatedBlock.getBlocker(), blocker);
        assertEquals(rescatedBlock.getBlocked(), blocked);

        // Now delete the block
        blockRepository.deleteBlock(rescatedBlock);

        boolean exception = false;
        try {
            blockRepository.findBlockByEmails(blocker.getEmail(),
                    blocked.getEmail());
        } catch (InstanceNotFoundException e) {
            exception = true;
        }
        assertTrue(exception);
    }

}
