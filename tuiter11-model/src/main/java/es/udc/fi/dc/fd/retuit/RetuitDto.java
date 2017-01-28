package es.udc.fi.dc.fd.retuit;


/**
 * The Class RetuitDto.
 */
public class RetuitDto implements java.io.Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private String id;

    /** The fecha ms. */
    private Long fechaMs;

    /** The tuit id. */
    private String tuitId;

    /** The retuiter id. */
    private Long retuiterId;

    /**
     * Create a empty RetuitDto.
     */
    protected RetuitDto() {

    }

    /**
     * Instantiates a new retuit dto.
     *
     * @param fechaMs
     *            the fecha ms
     * @param tuitId
     *            the tuit id
     * @param retuiterId
     *            the retuiter id
     */
    public RetuitDto(Long fechaMs, String tuitId, Long retuiterId) {
        super();
        this.fechaMs = fechaMs;
        this.tuitId = tuitId;
        this.retuiterId = retuiterId;
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
     * Gets the retuiter id.
     *
     * @return the retuiter id
     */
    public Long getRetuiterId() {
        return retuiterId;
    }

    /**
     * Sets the retuiter id.
     *
     * @param retuiterId
     *            the new retuiter id
     */
    public void setRetuiterId(Long retuiterId) {
        this.retuiterId = retuiterId;
    }

    /**
     * Gets the tuit id.
     *
     * @return the tuit id
     */
    public String getTuitId() {
        return tuitId;
    }

    /**
     * Sets the tuit id.
     *
     * @param tuitId
     *            the new tuit id
     */
    public void setTuitId(String tuitId) {
        this.tuitId = tuitId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RetuitDto [id=" + id + ", fechaMs=" + fechaMs + ", tuitId="
                + tuitId + ", retuiterId=" + retuiterId + "]";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((fechaMs == null) ? 0 : fechaMs.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((retuiterId == null) ? 0 : retuiterId.hashCode());
        result = prime * result + ((tuitId == null) ? 0 : tuitId.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RetuitDto other = (RetuitDto) obj;
        if (fechaMs == null) {
            if (other.fechaMs != null) {
                return false;
            }
        } else if (!fechaMs.equals(other.fechaMs)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (retuiterId == null) {
            if (other.retuiterId != null) {
                return false;
            }
        } else if (!retuiterId.equals(other.retuiterId)) {
            return false;
        }
        if (tuitId == null) {
            if (other.tuitId != null) {
                return false;
            }
        } else if (!tuitId.equals(other.tuitId)) {
            return false;
        }
        return true;
    }

}