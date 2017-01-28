package es.udc.fi.dc.fd.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * The Class UserServiceTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    /** The user service. */
    @InjectMocks
    private UserService userService = new UserService();

    /** The account repository mock. */
    @Mock
    private AccountRepository accountRepositoryMock;

    /** The thrown. */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Should initialize with four demo users.
     */
    @Test
    public void shouldInitializeWithFourDemoUsers() {
        // act
        userService.initialize();
        // assert
        verify(accountRepositoryMock, times(5)).save(any(Account.class));
    }

    /**
     * Should throw exception when user not found by nick.
     */
    @Test
    public void shouldThrowExceptionWhenUserNotFoundByNick() {
        // arrange
        thrown.expect(UsernameNotFoundException.class);
        thrown.expectMessage("user not found");

        when(accountRepositoryMock.findByEmail("userNick")).thenThrow(
                new UsernameNotFoundException("user not found"));

        when(accountRepositoryMock.findByNick("userNick")).thenThrow(
                new UsernameNotFoundException("user not found"));

        // act
        userService.loadUserByUsername("userNick");
    }

    /**
     * Should throw exception when user not found.
     */
    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        // arrange
        thrown.expect(UsernameNotFoundException.class);
        thrown.expectMessage("user not found");

        when(accountRepositoryMock.findByEmail("user@example.com")).thenThrow(
                new UsernameNotFoundException("user not found"));

        when(accountRepositoryMock.findByNick("user@example.com")).thenThrow(
                new UsernameNotFoundException("user not found"));

        // act
        userService.loadUserByUsername("user@example.com");
    }

    /**
     * Should return user details.
     */
    @Test
    public void shouldReturnUserDetails() {
        // arrange
        Account demoUser = new Account("user@example.com", "user", "demo",
                "ROLE_USER");
        when(accountRepositoryMock.findByEmail("user@example.com")).thenReturn(
                demoUser);
        when(accountRepositoryMock.findByEmail("user")).thenThrow(
                new UsernameNotFoundException("user not found"));

        when(accountRepositoryMock.findByNick("user")).thenReturn(demoUser);

        when(accountRepositoryMock.findByNick("user@example.com")).thenThrow(
                new UsernameNotFoundException("user not found"));

        // act
        UserDetails userDetails = userService
                .loadUserByUsername("user@example.com");
        UserDetails userDetails2 = userService.loadUserByUsername("user");

        // assert
        assertThat(demoUser.getEmail()).isEqualTo(userDetails.getUsername());
        assertThat(demoUser.getPassword()).isEqualTo(userDetails.getPassword());
        assertThat(hasAuthority(userDetails, demoUser.getRole()));
        assertThat(demoUser.getEmail()).isEqualTo(userDetails2.getUsername());
        assertThat(demoUser.getPassword())
                .isEqualTo(userDetails2.getPassword());
        assertThat(hasAuthority(userDetails2, demoUser.getRole()));

    }

    /**
     * Checks for authority.
     *
     * @param userDetails
     *            the user details
     * @param role
     *            the role
     * @return true, if successful
     */
    private boolean hasAuthority(UserDetails userDetails, String role) {
        Collection<? extends GrantedAuthority> authorities = userDetails
                .getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
