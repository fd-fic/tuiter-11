package es.udc.fi.dc.fd.conversations;

import org.hibernate.validator.constraints.NotBlank;


/**
 * The Class ResponseForm.
 */
public class ResponseForm {

    /** The Constant NOT_BLANK_MESSAGE. */
    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    /** The tuit. */
    @NotBlank(message = ResponseForm.NOT_BLANK_MESSAGE)
    private String tuit;

    /** The parent tuit. */
    private String parentTuit;

    /**
     * Create the default Response Form.
     */
    public ResponseForm() {

    }

    /**
     * Gets the tuit.
     *
     * @return the tuit
     */
    public String getTuit() {
        return tuit;
    }

    /**
     * Sets the tuit.
     *
     * @param tuit
     *            the new tuit
     */
    public void setTuit(String tuit) {
        this.tuit = tuit;
    }

    /**
     * Gets the parent tuit.
     *
     * @return the parent tuit
     */
    public String getParentTuit() {
        return parentTuit;
    }

    /**
     * Sets the parent tuit.
     *
     * @param parentTuit
     *            the new parent tuit
     */
    public void setParentTuit(String parentTuit) {
        this.parentTuit = parentTuit;
    }

}
