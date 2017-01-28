package es.udc.fi.dc.fd.search;

import org.hibernate.validator.constraints.NotBlank;


/**
 * The Class SearchForm.
 */
public class SearchForm {

    /** The Constant NOT_BLANK_MESSAGE. */
    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    /** The search. */
    @NotBlank(message = SearchForm.NOT_BLANK_MESSAGE)
    private String search;

    /**
     * Create the default Form.
     */
    public SearchForm() {

    }

    /**
     * Gets the search.
     *
     * @return the search
     */
    public String getSearch() {
        return search;
    }

    /**
     * Sets the search.
     *
     * @param search
     *            the new search
     */
    public void setSearch(String search) {
        this.search = search;
    }

}
