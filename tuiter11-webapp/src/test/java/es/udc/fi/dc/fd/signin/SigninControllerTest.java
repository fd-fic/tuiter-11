package es.udc.fi.dc.fd.signin;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;

import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.account.UserService;
import es.udc.fi.dc.fd.config.WebSecurityConfigurationAware;


/**
 * The Class SigninControllerTest.
 */
public class SigninControllerTest extends WebSecurityConfigurationAware {

    /** The spring security filter chain. */
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    /** The user service. */
    @Autowired
    private UserService userService;

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Displays signin form.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void displaysSigninForm() throws Exception {
        mockMvc.perform(get("/signin"))
                .andExpect(
                        content()
                                .string(allOf(
                                        containsString("<title>Sign In</title>"),
                                        containsString("<legend>Please Sign In</legend>"))));

    }

    /**
     * Sign in successful.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void signInSuccessful() throws Exception {

        MockHttpSession session = getDefaultSession();

        mockMvc.perform(post("/j_spring_security_check").session(session))
                .andExpect(status().isMovedTemporarily());

    }
}
