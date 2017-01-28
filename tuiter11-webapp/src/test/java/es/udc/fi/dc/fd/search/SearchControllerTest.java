package es.udc.fi.dc.fd.search;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import es.udc.fi.dc.fd.config.WebSecurityConfigurationAware;


/**
 * The Class SearchControllerTest.
 */
public class SearchControllerTest extends WebSecurityConfigurationAware {

    /**
     * Search method non existent keywords test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void searchMethodNonExistentKeywordsTest() throws Exception {

        MockHttpSession session = getDefaultSession();

        mockMvc.perform(get("/search").session(session)).andExpect(
                view().name("redirect:/"));

    }

    /**
     * Search method non existent user test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void searchMethodNonExistentUserTest() throws Exception {

        MockHttpSession session = getDefaultSession();

        mockMvc.perform(get("/search?keyword=aaa").session(session))
                .andExpect(view().name("error/resultsNotFound"))
                .andExpect(model().attributeExists("keyword"));

    }

    /**
     * Search method existent user test.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void searchMethodExistentUserTest() throws Exception {

        MockHttpSession session = getDefaultSession();

        mockMvc.perform(get("/search?keyword=us").session(session))
                .andExpect(view().name("search/search"))
                .andExpect(model().attributeExists("accounts"))
                .andExpect(
                        content().string(
                                containsString("<h2>Search results: </h2>")));

    }

}
