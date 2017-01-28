package es.udc.fi.dc.fd.home;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockHttpSession;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.config.WebSecurityConfigurationAware;
import es.udc.fi.dc.fd.retuit.RetuitDto;
import es.udc.fi.dc.fd.tuit.TuitDto;

/**
 * The Class HomeSignedInControllerTest.
 */
public class HomeSignedInControllerTest extends WebSecurityConfigurationAware {

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The mongo template. */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Clean mongo db.
     */
    public void cleanMongoDB() {
        mongoTemplate.dropCollection(TuitDto.class);
        mongoTemplate.dropCollection(RetuitDto.class);
    }

    /**
     * Profile view test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void profileViewTest() throws Exception {

        Account acc = accountRepository.findByEmail("user");

        MockHttpSession session = getDefaultSession();

        // Comprobamos que al hacer una peticion get a /profile nos redirige a
        // /usuario
        // y comprobamos su contenido
        mockMvc.perform(get("/profile").session(session))
                .andExpect(view().name("redirect:/" + acc.getNickName()))
                .andExpect(model().attributeExists("myAccount"))
                .andExpect(model().attributeExists("myRetuitedTuits"))
                .andExpect(model().attributeExists("myFavoriteTuits"));

        mockMvc.perform(get("/" + acc.getNickName()).session(session))
                .andExpect(view().name("/profile/profile"))
                .andExpect(model().attributeExists("myAccount"))
                .andExpect(model().attributeExists("publications"))
                .andExpect(model().attributeExists("followeds"))
                .andExpect(model().attributeExists("followers"))
                .andExpect(
                        content()
                                .string(allOf(
                                        containsString("<th>E-mail</th>"),
                                        containsString("<td>user</td>"),
                                        containsString("<th>Nick</th>"),
                                        containsString("<td>userN</td>"),
                                        containsString("<a href=\"/profile/edit\" class=\"btn btn-default\">Edit Profile</a>"))));

    }

    /**
     * Edits the profile view test.
     *
     * @throws Exception
     *             the exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void editProfileViewTest() throws Exception {

        MockHttpSession session = getDefaultSession();

        // Hacemos GET a /profile/edit para comprobar que la informacion que se
        // "pinta"
        // es la correcta y que existe el model attribute profileForm
        mockMvc.perform(get("/profile/edit").session(session))
                .andExpect(view().name("/profile/edit"))
                .andExpect(model().attributeExists("profileForm"))
                .andExpect(
                        content()
                                .string(allOf(
                                        containsString("<legend>Edit Profile</legend>"),
                                        containsString("<label for=\"email\" class=\"col-lg-2 control-label\">New Email</label>"),
                                        containsString("<label for=\"nickName\" class=\"col-lg-2 control-label\">New Nick</label>"),
                                        containsString("<label for=\"password\" class=\"col-lg-2 control-label\"> New Password</label>"),
                                        containsString("<label for=\"olPassword\" class=\"col-lg-2 control-label\"> Old Password</label>"),
                                        containsString("<label>Private account</label>"),
                                        containsString("<input type=\"checkbox\" name=\"active\" />"),
                                        containsString("<button type=\"submit\" class=\"btn btn-default\">Save Changes</button>"))));

    }

    /**
     * Edits the profile test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void editProfileTest() throws Exception {

        Account acc = accountRepository.findByEmail("user");
        acc.setPrivateAccount(1);
        accountRepository.update(acc);

        MockHttpSession session = getDefaultSession();

        mockMvc.perform(get("/profile/edit").session(session))
                .andExpect(view().name("/profile/edit"))
                .andExpect(
                        content()
                                .string(containsString("<input type=\"checkbox\" name=\"active\" checked=\"checked\" />")));

    }

    /**
     * Post tuit test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void postTuitTest() throws Exception {

        MockHttpSession session = getDefaultSession();

        mockMvc.perform(post("/processTuit").session(session).param("tuit", ""))
                .andExpect(view().name("redirect:/"))
                .andExpect(model().attributeExists("tuitForm"));

        mockMvc.perform(
                post("/processTuit").session(session).param("tuit", "puta"))
                .andExpect(view().name("redirect:/"))
                .andExpect(model().attributeExists("tuitForm"));

        mockMvc.perform(get("/").session(session))
                .andExpect(
                        content()
                                .string(allOf(
                                        containsString("<a id=\"tuitUser\" class=\"text-right\" href=\"/userN\">@userN</a>"),
                                        containsString("<p>puta</p>"),
                                        containsString("<div class=\"tuitOpt\">"))));
        cleanMongoDB();
    }
}
