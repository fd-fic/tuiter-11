package es.udc.fi.dc.fd.retuit;

import java.util.Calendar;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.publish.Publish;
import es.udc.fi.dc.fd.tuit.Tuit;


/**
 * The Class Retuit.
 */
public class Retuit implements java.io.Serializable, Publish {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private String id;

    /** The fecha ms. */
    private Long fechaMs;

    /** The tuit. */
    private Tuit tuit;

    /** The retuiter. */
    private Account retuiter;

    /**
     * Create the default object.
     */
    protected Retuit() {

    }

    /**
     * Instantiates a new retuit.
     *
     * @param fechaMs
     *            the fecha ms
     * @param tuit
     *            the tuit
     * @param retuiter
     *            the retuiter
     */
    public Retuit(Long fechaMs, Tuit tuit, Account retuiter) {
        super();
        this.fechaMs = fechaMs;
        this.tuit = tuit;
        this.retuiter = retuiter;

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
     * Gets the retuiter.
     *
     * @return the retuiter
     */
    public Account getRetuiter() {
        return retuiter;
    }

    /**
     * Sets the retuiter.
     *
     * @param retuiter
     *            the new retuiter
     */
    public void setRetuiter(Account retuiter) {
        this.retuiter = retuiter;
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
        int prime = 31;
        int result = 1;
        result = prime * result + ((fechaMs == null) ? 0 : fechaMs.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((retuiter == null) ? 0 : retuiter.hashCode());
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
        Retuit other = (Retuit) obj;
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
        if (retuiter == null) {
            if (other.retuiter != null) {
                return false;
            }
        } else if (!retuiter.equals(other.retuiter)) {
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
        return "Retuit [id=" + id + ", fechaMs=" + fechaMs + ", tuit=" + tuit
                + ", retuiter=" + retuiter + "]";
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.udc.fi.dc.fd.account.Publish#getPublishedDate()
     */
    @Override
    public Calendar getPublishedDate() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(fechaMs);
        return c;
    }

}