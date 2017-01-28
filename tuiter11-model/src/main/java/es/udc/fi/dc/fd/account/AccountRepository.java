package es.udc.fi.dc.fd.account;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.follow.FollowRepository;


/**
 * The Class AccountRepository.
 */
@Repository
@Transactional(readOnly = true)
public class AccountRepository {

    /** The entity manager. */
    @PersistenceContext
    private EntityManager entityManager;

    /** The password encoder. */
    @Inject
    private PasswordEncoder passwordEncoder;

    /** The follow repository. */
    @Autowired
    private FollowRepository followRepository;

    /**
     * Create the default account repository.
     */
    public AccountRepository() {

    }

    /**
     * Save.
     *
     * @param account
     *            the account
     * @return the account
     */
    @Transactional
    public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        // el programa falla y para aqui. por la sig linea.
        entityManager.persist(account);
        return account;
    }

    /**
     * Delete.
     *
     * @param account
     *            the account
     */
    @Transactional
    public void delete(Account account) {
        Account foundAccount = findById(account.getId());
        entityManager.remove(foundAccount);
    }

    /**
     * Update.
     *
     * @param account
     *            the account
     * @return the account
     */
    @Transactional
    public Account update(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        entityManager.merge(account);
        return account;

    }

    /**
     * Update privacity.
     *
     * @param account
     *            the account
     * @return the account
     */
    @Transactional
    public Account updateWithoutPass(Account account) {
        entityManager.merge(account);
        return account;
    }

    /**
     * Find by id.
     *
     * @param accId
     *            the acc id
     * @return the account
     */
    public Account findById(Long accId) {
        try {
            return entityManager
                    .createNamedQuery(Account.FIND_BY_ID, Account.class)
                    .setParameter("accId", accId).getSingleResult();
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("Account with id: " + accId
                    + " does not exists.", e);
        }
    }

    /**
     * Find by email.
     *
     * @param email
     *            the email
     * @return the account
     */
    public Account findByEmail(String email) {
        try {
            Account acc = entityManager
                    .createNamedQuery(Account.FIND_BY_EMAIL, Account.class)
                    .setParameter("email", email).getSingleResult();
            return acc;
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("Account with email: " + email
                    + " does not exists.", e);
        }
    }

    /**
     * Find by nick.
     *
     * @param nick
     *            the nick
     * @return the account
     */
    public Account findByNick(String nick) {
        try {
            Account acc = entityManager
                    .createNamedQuery(Account.FIND_BY_NICK, Account.class)
                    .setParameter("nickName", nick).getSingleResult();
            return acc;
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("user with nick: " + nick
                    + " does not exists.", e);
        }
    }

    /**
     * Find accounts.
     *
     * @param keyword
     *            the keyword
     * @return the list
     */
    public List<Account> findAccounts(String keyword) {
        List<Account> accounts = entityManager
                .createNamedQuery(Account.FIND_ACCOUNTS, Account.class)
                .setParameter("keyword", "%" + keyword.toLowerCase() + "%")
                .getResultList();
        return accounts;
    }

    /**
     * Gets the not confirmed accounts.
     *
     * @return the not confirmed accounts
     */
    public List<Account> getNotConfirmedAccounts() {
        List<Account> accounts = entityManager.createNamedQuery(
                Account.FIND_NOT_CONFIRMED_ACCOUNS, Account.class)
                .getResultList();
        return accounts;
    }

    /**
     * Confirm user.
     *
     * @param acc
     *            the acc
     */
    @Transactional
    public void confirmUser(Account acc) {
        if (acc == null) {
            return;
        }
        acc.setRole("ROLE_USER");
        entityManager.merge(acc);
    }
}
