package es.udc.fi.dc.fd.tuit;

import java.util.Calendar;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.publish.Publish;


/**
 * The Class Tuit.
 */
@SuppressWarnings("serial")
public class Tuit implements java.io.Serializable, Publish {

    /** The Constant FIND_BY_USER. */
    public static final String FIND_BY_USER = "Tuit.findByUserId";

    /** The id. */
    private String id;

    /** The fecha ms. */
    private Long fechaMs;

    /** The tuit. */
    private String tuit;

    /** The acc. */
    private Account acc;

    /**
     * Create the default object.
     */
    public Tuit() {

    }

    /**
     * Instantiates a new tuit.
     *
     * @param fecha
     *            the fecha
     * @param tuit
     *            the tuit
     * @param acc
     *            the acc
     */
    public Tuit(Long fecha, String tuit, Account acc) {
        this.fechaMs = fecha;
        this.tuit = tuit;
        this.acc = acc;
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
     * Gets the acc.
     *
     * @return the acc
     */
    public Account getAcc() {
        return acc;
    }

    /**
     * Sets the acc.
     *
     * @param acc
     *            the new acc
     */
    public void setAcc(Account acc) {
        this.acc = acc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Tuit [id=" + id + ", fechaMs=" + fechaMs + ", tuit=" + tuit
                + ", acc=" + acc + "]";
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
        result = prime * result + ((acc == null) ? 0 : acc.hashCode());
        result = prime * result + ((fechaMs == null) ? 0 : fechaMs.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Tuit other = (Tuit) obj;
        if (acc == null) {
            if (other.acc != null) {
                return false;
            }
        } else if (!acc.equals(other.acc)) {
            return false;
        }
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
     * @see es.udc.fi.dc.fd.account.Publish#getPublishedDate()
     */
    @Override
    public Calendar getPublishedDate() {
        return getFecha();
    }

}