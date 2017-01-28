package es.udc.fi.dc.fd.favorite;


/**
 * The Class FavoriteDto.
 */
public class FavoriteDto implements java.io.Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private String id;

    /** The tuit id. */
    private String tuitId;

    /** The owner id. */
    private Long ownerId;

    /**
     * Create a empty Favoritedto.
     */
    protected FavoriteDto() {

    }

    /**
     * Instantiates a new favorite dto.
     *
     * @param tuitId
     *            the tuit id
     * @param ownerId
     *            the owner id
     */
    public FavoriteDto(String tuitId, Long ownerId) {
        super();
        this.tuitId = tuitId;
        this.ownerId = ownerId;
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
     * Gets the owner id.
     *
     * @return the owner id
     */
    public Long getOwnerId() {
        return ownerId;
    }

    /**
     * Sets the owner id.
     *
     * @param ownerId
     *            the new owner id
     */
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
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
        FavoriteDto other = (FavoriteDto) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (ownerId == null) {
            if (other.ownerId != null) {
                return false;
            }
        } else if (!ownerId.equals(other.ownerId)) {
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FavoriteDto [id=" + id + ", tuitId=" + tuitId + ", ownerId="
                + ownerId + "]";
    }

}