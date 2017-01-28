package es.udc.fi.dc.fd.favorite;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.tuit.Tuit;


/**
 * The Class Favorite.
 */
public class Favorite implements java.io.Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private String id;

    /** The tuit. */
    private Tuit tuit;

    /** The owner. */
    private Account owner;

    /**
     * Create an empty constructor.
     */
    protected Favorite() {

    }

    /**
     * Instantiates a new favorite.
     *
     * @param tuit
     *            the tuit
     * @param owner
     *            the owner
     */
    public Favorite(Tuit tuit, Account owner) {
        super();
        this.tuit = tuit;
        this.owner = owner;

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
     * Gets the owner.
     *
     * @return the owner
     */
    public Account getOwner() {
        return owner;
    }

    /**
     * Sets the owner.
     *
     * @param owner
     *            the new owner
     */
    public void setOwner(Account owner) {
        this.owner = owner;
    }

    /**
     * Gets the tuit.
     *
     * @return the tuit
     */
    public Tuit getTuit() {
        return tuit;
    }

    /**
     * Sets the tuit.
     *
     * @param tuit
     *            the new tuit
     */
    public void setTuit(Tuit tuit) {
        this.tuit = tuit;
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
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((tuit == null) ? 0 : tuit.hashCode());
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
        Favorite other = (Favorite) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (owner == null) {
            if (other.owner != null) {
                return false;
            }
        } else if (!owner.equals(other.owner)) {
            return false;
        }
        if (tuit == null) {
            if (other.tuit != null) {
                return false;
            }
        } else if (!tuit.equals(other.tuit)) {
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
        return "Favorite [id=" + id + ", tuit=" + tuit + ", owner=" + owner
                + "]";
    }

}