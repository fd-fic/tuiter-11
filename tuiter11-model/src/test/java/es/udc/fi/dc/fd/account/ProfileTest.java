package es.udc.fi.dc.fd.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.config.WebAppConfigurationAware;
import es.udc.fi.dc.fd.follow.FollowRepository;
import es.udc.fi.dc.fd.publish.PublishUtils;

/**
 * The Class ProfileTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProfileTest extends WebAppConfigurationAware {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The follow repository. */
    @Autowired
    private FollowRepository followRepository;

    /** The publish utils. */
    @Autowired
    private PublishUtils publishUtils;

    /**
     * Creates the user.
     *
     * @param email
     *            the email
     * @param nick
     *            the nick
     * @param passwd
     *            the passwd
     * @return the account
     */
    private Account createUser(String email, String nick, String passwd) {
        Account user = new Account(email, nick, passwd, "ROLE_USER");
        accountRepository.save(user);
        return user;
    }

    /**
     * Show profile success.
     */
    @Test
    public void showProfileSuccess() {

        Account user = createUser("a@a", "a", "1234");
        Account userFound = accountRepository.findByEmail("a@a");

        assertEquals(user.getEmail(), userFound.getEmail());
        assertEquals(user.getNickName(), userFound.getNickName());
        assertEquals(user.getId(), userFound.getId());

        userFound = accountRepository.findByNick("a");
        assertEquals(user.getEmail(), userFound.getEmail());
        assertEquals(user.getNickName(), userFound.getNickName());
        assertEquals(user.getId(), userFound.getId());

    }

    /**
     * Edits the user success.
     */
    @Test
    public void editUserSuccess() {
        Account user = createUser("b@b", "b", "1234");
        user.setEmail("c@c");
        accountRepository.update(user);
        Account userFound = accountRepository.findByEmail(user.getEmail());
        assertEquals(user.getEmail(), userFound.getEmail());
        assertEquals(user.getNickName(), userFound.getNickName());
        assertEquals(user.getId(), userFound.getId());

    }

    /**
     * Delete user.
     */
    @Test
    public void deleteUser() {
        String email = "b@b";
        Account user = createUser(email, "b", "1234");
        Account userFound = accountRepository.findByEmail(email);
        assertEquals(user, userFound);

        // Borramos la cuenta
        accountRepository.delete(user);

        boolean exists = true;
        try {
            userFound = accountRepository.findByEmail(email);
            exists = true;
        } catch (UsernameNotFoundException e) {
            exists = false;
        } finally {
            assertFalse(exists);
        }
    }

    /**
     * Show profile un success.
     */
    @Test(expected = UsernameNotFoundException.class)
    public void showProfileUnSuccess() {

        accountRepository.findByEmail("b@a");
    }

    /**
     * Make private account.
     */
    @Test
    public void makePrivateAccount() {

        String userMail = "a@a";
        Account user = createUser(userMail, "a", "1234");

        // Miramos por defecto crea la cuenta publica
        Account foundAcc = accountRepository.findByEmail(userMail);
        assertEquals(foundAcc.getPrivateAccount(), null);

        user.setPrivateAccount(1);

        foundAcc = accountRepository.findByEmail(userMail);
        assertTrue(foundAcc != null);
    }

    /**
     * Can see profile.
     */
    @Test
    public void canSeeProfile() {

        Account user1 = createUser("user1@example", "user1", "1234");
        Account user2 = createUser("user2@example", "user2", "1234");

        // User1 ve el profile de user2 que es publico
        assertTrue(publishUtils.canSeeProfile(user1.getEmail(),
                user2.getEmail()));

        // User1 no consigue ver el perfil de user2 porque es privado
        user2.setPrivateAccount(1);
        assertTrue(!publishUtils.canSeeProfile(user1.getEmail(),
                user2.getEmail()));

        followRepository.followUser(user1, user2);

        assertTrue(!publishUtils.canSeeProfile(user1.getEmail(),
                user2.getEmail()));

        // User1 ahora puede ver el perfil de user2 poque este le sigue
        followRepository.followUser(user2, user1);

        assertTrue(publishUtils.canSeeProfile(user1.getEmail(),
                user2.getEmail()));

        // user2 puede ver su propio perfil
        assertTrue(publishUtils.canSeeProfile(user2.getEmail(),
                user2.getEmail()));
    }
}