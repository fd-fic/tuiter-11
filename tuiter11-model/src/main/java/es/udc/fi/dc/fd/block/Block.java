package es.udc.fi.dc.fd.block;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import es.udc.fi.dc.fd.account.Account;


/**
 * The Class Block.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "block")
@NamedQueries({
        @NamedQuery(
                name = Block.FIND_BLOCKED,
                query = "select a.blocked from Block a where a.blocker.email = :email"),
        @NamedQuery(
                name = Block.FIND_BLOCKERS,
                query = "select a.blocker from Block a where a.blocked.email = :email"),
        @NamedQuery(
                name = Block.FIND_BLOCK,
                query = "select a from Block a where a.blocker.email = :blokerMail and a.blocked.email = :blokedMail") })
public class Block implements java.io.Serializable {

    /** The Constant FIND_BLOCKED. */
    public static final String FIND_BLOCKED = "Block.findBlockedByEmail";

    /** The Constant FIND_BLOCKERS. */
    public static final String FIND_BLOCKERS = "Block.findBlockersByEmail";

    /** The Constant FIND_BLOCK. */
    public static final String FIND_BLOCK = "Block.findBlockByEmails";

    /** The id. */
    @Id
    @GeneratedValue
    private Long id;

    /** The blocker. */
    @ManyToOne
    @JoinColumn(name = "blocker")
    private Account blocker;

    /** The blocked. */
    @ManyToOne
    @JoinColumn(name = "blocked")
    private Account blocked;

    /**
     * Create de default object.
     */
    public Block() {
    }

    /**
     * Instantiates a new block.
     *
     * @param blocker
     *            the blocker
     * @param blocked
     *            the blocked
     */
    public Block(Account blocker, Account blocked) {
        super();
        this.blocker = blocker;
        this.blocked = blocked;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the blocker.
     *
     * @return the blocker
     */
    public Account getBlocker() {
        return blocker;
    }

    /**
     * Sets the blocker.
     *
     * @param blocker
     *            the new blocker
     */
    public void setBlocker(Account blocker) {
        this.blocker = blocker;
    }

    /**
     * Gets the blocked.
     *
     * @return the blocked
     */
    public Account getBlocked() {
        return blocked;
    }

    /**
     * Sets the blocked.
     *
     * @param blocked
     *            the new blocked
     */
    public void setBlocked(Account blocked) {
        this.blocked = blocked;
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
        result = prime * result + ((blocked == null) ? 0 : blocked.hashCode());
        result = prime * result + ((blocker == null) ? 0 : blocker.hashCode());
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
        Block other = (Block) obj;
        if (blocked == null) {
            if (other.blocked != null) {
                return false;
            }
        } else if (!blocked.equals(other.blocked)) {
            return false;
        }
        if (blocker == null) {
            if (other.blocker != null) {
                return false;
            }
        } else if (!blocker.equals(other.blocker)) {
            return false;
        }
        return true;
    }

}
