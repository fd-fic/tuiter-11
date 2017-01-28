package es.udc.fi.dc.fd.follow;

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
 * The Class UnfollowTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UnfollowTest extends WebAppConfigurationAware {

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
     * Unfollow user test.
     */
    @Test
    public void unfollowUserTest() {

        // creamos las cuentas
        Account a1 = createAccount("samu@gmail.com", "samu8", "1234",
                "ROLE_USER");
        Account a2 = createAccount("iago@gmail.com", "iago5", "1234",
                "ROLE_USER");

        // a1 sigue a a2
        followRepository.followUser(a1, a2);

        // busca si se ha guardado correctamente
        Follow followSucces = followRepository.findUserFollowed(a1.getId(),
                a2.getId());

        // a1 deja de seguir a a2
        followRepository.removeFollowed(followSucces);

        // busca insatisfactoriamente
        boolean exception = false;
        try {
            followRepository.findUserFollowed(a1.getId(), a2.getId());
        } catch (InstanceNotFoundException e) {
            exception = true;
        }
        assertTrue(exception);
    }

}
