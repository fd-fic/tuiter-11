package es.udc.fi.dc.fd.home;

import org.hibernate.validator.constraints.NotBlank;


/**
 * The Class TuitForm.
 */
public class TuitForm {

    /** The Constant NOT_BLANK_MESSAGE. */
    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    /** The tuit. */
    @NotBlank(message = TuitForm.NOT_BLANK_MESSAGE)
    private String tuit;

    /**
     * Create the default Tuit Form.
     */
    public TuitForm() {

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

}
