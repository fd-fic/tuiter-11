package es.udc.fi.dc.fd.config;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import javax.inject.Inject;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * The Class WebSecurityConfigurationAware.
 */
public abstract class WebSecurityConfigurationAware extends
        WebAppConfigurationAware {

    /** The spring security filter chain. */
    @Inject
    private FilterChainProxy springSecurityFilterChain;

    /** The user details service. */
    @Autowired
    @Qualifier("userService")
    protected UserDetailsService userDetailsService;

    /*
     * (non-Javadoc)
     * 
     * @see es.udc.fi.dc.fd.config.WebAppConfigurationAware#before()
     */
    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(this.wac).addFilters(
                this.springSecurityFilterChain).build();
    }

    /**
     * Gets the principal.
     *
     * @param username
     *            the username
     * @return the principal
     */
    protected UsernamePasswordAuthenticationToken getPrincipal(String username) {

        UserDetails user = this.userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());

        return authentication;
    }

    /**
     * Gets the default session.
     *
     * @return the default session
     */
    protected MockHttpSession getDefaultSession() {
        UsernamePasswordAuthenticationToken principal = this
                .getPrincipal("user");
        SecurityContextHolder.getContext().setAuthentication(principal);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
        return session;
    }

}
