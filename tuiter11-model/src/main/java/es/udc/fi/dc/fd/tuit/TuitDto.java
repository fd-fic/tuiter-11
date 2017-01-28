package es.udc.fi.dc.fd.tuit;

import java.util.Calendar;

import org.springframework.data.annotation.Id;


/**
 * The Class TuitDto.
 */
public class TuitDto implements java.io.Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant FIND_BY_USER. */
    public static final String FIND_BY_USER = "Tuit.findByUserId";

    /** The id. */
    @Id
    private String id;

    /** The fecha ms. */
    private Long fechaMs;

    /** The tuit. */
    private String tuit;

    /** The account id. */
    private Long accountId;

    /** The parent tuit id. */
    private String parentTuitId;

    /** The is offensive. */
    private int isOffensive;

    /**
     * Create the default object.
     */
    protected TuitDto() {

    }

    /**
     * Instantiates a new tuit dto.
     *
     * @param fechaMs
     *            the fecha ms
     * @param tuit
     *            the tuit
     * @param acc
     *            the acc
     * @param parentTuitId
     *            the parent tuit id
     * @param isOffensive
     *            the is offensive
     */
    public TuitDto(Long fechaMs, String tuit, Long acc, String parentTuitId,
            int isOffensive) {
        this.fechaMs = fechaMs;
        this.tuit = tuit;
        this.accountId = acc;
        this.parentTuitId = parentTuitId;
        this.isOffensive = isOffensive;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(String id) {
        this.id = id;
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
     * Gets the fecha ms.
     *
     * @return the fecha ms
     */
    public Long getFechaMs() {
        return fechaMs;
    }

    /**
     * Sets the fecha ms.
     *
     * @param fechaMs
     *            the new fecha ms
     */
    public void setFechaMs(Long fechaMs) {
        this.fechaMs = fechaMs;
    }

    /**
     * Gets the fecha.
     *
     * @return the fecha
     */
    public Calendar getFecha() {
        Calendar f = Calendar.getInstance();
        f.setTimeInMillis(fechaMs);
        return f;
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
     * Gets the account id.
     *
     * @return the account id
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * Sets the account id.
     *
     * @param acc
     *            the new account id
     */
    public void setAccountId(Long acc) {
        this.accountId = acc;
    }

    /**
     * Gets the parent tuit id.
     *
     * @return the parent tuit id
     */
    public String getParentTuitId() {
        return parentTuitId;
    }

    /**
     * Sets the parent tuit id.
     *
     * @param parentTuitId
     *            the new parent tuit id
     */
    public void setParentTuitId(String parentTuitId) {
        this.parentTuitId = parentTuitId;
    }

    /**
     * Gets the checks if is offensive.
     *
     * @return the checks if is offensive
     */
    public int getIsOffensive() {
        return isOffensive;
    }

    /**
     * Sets the checks if is offensive.
     *
     * @param isOffensive
     *            the new checks if is offensive
     */
    public void setIsOffensive(int isOffensive) {
        this.isOffensive = isOffensive;
    }

}
