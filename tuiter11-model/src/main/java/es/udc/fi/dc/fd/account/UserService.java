package es.udc.fi.dc.fd.account;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * The Class UserService.
 */
public class UserService implements UserDetailsService {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Create a default User Service.
     */
    public UserService() {

    }

    /**
     * Initialize.
     */
    @PostConstruct
    protected void initialize() {
        Account a = accountRepository.save(new Account("user", "userN", "demo",
                "ROLE_USER"));
        accountRepository.confirmUser(a);
        a = accountRepository.save(new Account("user2", "userN2", "demo2",
                "ROLE_USER"));
        accountRepository.confirmUser(a);
        a = accountRepository.save(new Account("user@example.com", "userNick",
                "user", "ROLE_USER"));
        accountRepository.confirmUser(a);
        a = accountRepository.save(new Account("user3", "userN3", "demo3",
                "ROLE_USER"));
        accountRepository.confirmUser(a);
        a = accountRepository.save(new Account("admin", "admin", "admin",
                "ROLE_ADMIN"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.core.userdetails.UserDetailsService#
     * loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Account account;

        try {
            account = accountRepository.findByEmail(username);
            return createUser(account);
        } catch (UsernameNotFoundException e) {
            try {
                account = accountRepository.findByNick(username);
                return createUser(account);
            } catch (UsernameNotFoundException e2) {
                throw new UsernameNotFoundException("user not found", e2);
            }
        }
    }

    /**
     * Signin.
     *
     * @param account
     *            the account
     */
    public void signin(Account account) {
        SecurityContextHolder.getContext().setAuthentication(
                authenticate(account));
    }

    /**
     * Authenticate.
     *
     * @param account
     *            the account
     * @return the authentication
     */
    private Authentication authenticate(Account account) {
        return new UsernamePasswordAuthenticationToken(createUser(account),
                null, Collections.singleton(createAuthority(account)));
    }

    /**
     * Creates the user.
     *
     * @param account
     *            the account
     * @return the user
     */
    private User createUser(Account account) {
        return new User(account.getEmail(), account.getPassword(),
                Collections.singleton(createAuthority(account)));
    }

    /**
     * Creates the authority.
     *
     * @param account
     *            the account
     * @return the granted authority
     */
    private GrantedAuthority createAuthority(Account account) {
        return new SimpleGrantedAuthority(account.getRole());
    }

}
