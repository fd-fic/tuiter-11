package es.udc.fi.dc.fd.conversations;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Calendar;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockHttpSession;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.config.WebSecurityConfigurationAware;
import es.udc.fi.dc.fd.retuit.RetuitDto;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitDto;
import es.udc.fi.dc.fd.tuit.TuitRepository;

/**
 * The Class ConversationsControllerTest.
 */
public class ConversationsControllerTest extends WebSecurityConfigurationAware {

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

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
     * Conversation get test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void conversationGetTest() throws Exception {

        Account acc = accountRepository.findByEmail("user");
        Calendar fecha = Calendar.getInstance();
        fecha.set(Calendar.MILLISECOND, 0);
        Tuit tuit = new Tuit(fecha.getTimeInMillis(), "Tuit padre", acc);
        tuit = tuitRepository.save(tuit);

        MockHttpSession session = getDefaultSession();

        mockMvc.perform(
                get("/" + tuit.getId() + "/conversations").session(session))
                .andExpect(view().name("conversations/conversation"))
                .andExpect(model().attributeExists("responseForm"))
                .andExpect(model().attributeExists("tuit"))
                .andExpect(
                        content().string(containsString("<p>Tuit padre</p>")));

    }

    /**
     * Conversation tuit empty message test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void conversationTuitEmptyMessageTest() throws Exception {

        Account acc = accountRepository.findByEmail("user");
        Calendar fecha = Calendar.getInstance();
        fecha.set(Calendar.MILLISECOND, 0);
        Tuit tuit = new Tuit(fecha.getTimeInMillis(), "Tuit padre", acc);
        tuit = tuitRepository.save(tuit);

        MockHttpSession session = getDefaultSession();

        mockMvc.perform(
                post("/" + tuit.getId() + "/processResponse").session(session)
                        .param("tuit", ""))
                .andExpect(
                        view().name(
                                "redirect:/" + tuit.getId() + "/conversations"))
                .andExpect(model().attributeExists("responseForm"));

        cleanMongoDB();
    }

    /**
     * Conversation post tuits test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void conversationPostTuitsTest() throws Exception {
        Account acc = accountRepository.findByEmail("user");
        Calendar fecha = Calendar.getInstance();
        fecha.set(Calendar.MILLISECOND, 0);
        Tuit tuit = new Tuit(fecha.getTimeInMillis(), "Tuit padre", acc);
        tuit = tuitRepository.save(tuit);

        MockHttpSession session = getDefaultSession();

        mockMvc.perform(
                post("/" + tuit.getId() + "/processResponse").session(session)
                        .param("tuit", "Tuit respuesta"))
                .andExpect(
                        view().name(
                                "redirect:/" + tuit.getId() + "/conversations"))
                .andExpect(model().attributeExists("responseForm"));

        Calendar fecha2 = Calendar.getInstance();
        fecha2.set(Calendar.MILLISECOND, 0);
        Tuit tuit2 = new Tuit(fecha.getTimeInMillis(), "Tuit respuesta", acc);
        tuitRepository.saveWithParent(tuit2, tuit.getId(), 0);

        mockMvc.perform(
                get("/" + tuit.getId() + "/conversations").session(session))
                .andExpect(
                        content()
                                .string(allOf(
                                        containsString("<p>Tuit padre</p>"),
                                        containsString("<p>Tuit respuesta</p>"))));

        mockMvc.perform(
                post("/" + tuit.getId() + "/processResponse").session(session)
                        .param("tuit", "puta"))
                .andExpect(
                        view().name(
                                "redirect:/" + tuit.getId() + "/conversations"))
                .andExpect(model().attributeExists("responseForm"));

        Calendar fecha3 = Calendar.getInstance();
        fecha3.set(Calendar.MILLISECOND, 0);
        Tuit tuit3 = new Tuit(fecha.getTimeInMillis(), "puta", acc);
        tuitRepository.saveWithParent(tuit3, tuit.getId(), 1);

        mockMvc.perform(
                get("/" + tuit.getId() + "/conversations").session(session))
                .andExpect(
                        content()
                                .string(allOf(
                                        containsString("<p>Tuit padre</p>"),
                                        containsString("<p>Tuit respuesta</p>"),
                                        containsString("<p>puta</p>"))));

        cleanMongoDB();
    }
}
