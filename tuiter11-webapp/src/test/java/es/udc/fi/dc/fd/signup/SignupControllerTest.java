package es.udc.fi.dc.fd.signup;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import es.udc.fi.dc.fd.config.WebSecurityConfigurationAware;


/**
 * The Class SignupControllerTest.
 */
public class SignupControllerTest extends WebSecurityConfigurationAware {

    /**
     * Displays signup form.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void displaysSignupForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(model().attributeExists("signupForm"))
                .andExpect(view().name("signup/signup"))
                .andExpect(
                        content()
                                .string(allOf(
                                        containsString("<title>Signup</title>"),
                                        containsString("<legend>Please Sign Up</legend>"))));
    }

    /**
     * Sign up successful.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void signUpSuccessful() throws Exception {
        mockMvc.perform(
                post("/signup").param("email", "userTest@example.com")
                        .param("nickName", "demo").param("password", "dferf"))
                .andExpect(view().name("redirect:/"));

        UsernamePasswordAuthenticationToken principal = getPrincipal("userTest@example.com");
        SecurityContextHolder.getContext().setAuthentication(principal);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        mockMvc.perform(get("/").session(session))
                .andExpect(model().attributeExists("tuitForm"))
                .andExpect(
                        content().string(
                                containsString("<h1>Welcome to tuiter11</h1>")));

    }

    /**
     * Sign up existent nick.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void signUpExistentNick() throws Exception {
        mockMvc.perform(
                post("/signup").param("email", "user@prueba.com")
                        .param("nickName", "userN").param("password", "dferf"))
                .andExpect(view().name("redirect:signup?nickError=1"));
    }

    /**
     * Sign up existent email.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void signUpExistentEmail() throws Exception {
        mockMvc.perform(
                post("/signup").param("email", "user@example.com")
                        .param("nickName", "usuario")
                        .param("password", "dferf")).andExpect(
                view().name("redirect:signup?userError=1"));
    }

    /**
     * Sign up existent nick and email.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void signUpExistentNickAndEmail() throws Exception {
        mockMvc.perform(
                post("/signup").param("email", "user@example.com")
                        .param("nickName", "userN").param("password", "dferf"))
                .andExpect(
                        view().name("redirect:signup?userError=1&nickError=1"));
    }

}