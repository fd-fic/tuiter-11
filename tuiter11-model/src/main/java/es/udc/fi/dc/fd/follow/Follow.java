package es.udc.fi.dc.fd.follow;

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
 * The Class Follow.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "follow")
@NamedQueries({
        @NamedQuery(
                name = Follow.FIND_FOLLOWED,
                query = "select a.followed from Follow a where a.follower.email = :email"),
        @NamedQuery(
                name = Follow.FIND_FOLLOWERS,
                query = "select a.follower from Follow a where a.followed.email = :email"),
        @NamedQuery(
                name = Follow.FIND_USER_FOLLOWED,
                query = "select a from Follow a where a.follower.id = :idUser and a.followed.id = :userFollowedId") })
public class Follow implements java.io.Serializable {

    /** The Constant FIND_FOLLOWED. */
    public static final String FIND_FOLLOWED = "Follow.findFollowedByEmail";

    /** The Constant FIND_FOLLOWERS. */
    public static final String FIND_FOLLOWERS = "Follow.findFollowersByEmail";

    /** The Constant FIND_USER_FOLLOWED. */
    public static final String FIND_USER_FOLLOWED = "Follow.findUserFollowedById";

    /** The id. */
    @Id
    @GeneratedValue
    private Long id;

    /** The follower. */
    @ManyToOne
    @JoinColumn(name = "follower")
    private Account follower;

    /** The followed. */
    @ManyToOne
    @JoinColumn(name = "followed")
    private Account followed;

    /**
     * Create the default object.
     */
    public Follow() {
    }

    /**
     * Instantiates a new follow.
     *
     * @param follower
     *            the follower
     * @param followed
     *            the followed
     */
    public Follow(Account follower, Account followed) {
        super();
        this.follower = follower;
        this.followed = followed;
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
     * Gets the follower.
     *
     * @return the follower
     */
    public Account getFollower() {
        return follower;
    }

    /**
     * Sets the follower.
     *
     * @param follower
     *            the new follower
     */
    public void setFollower(Account follower) {
        this.follower = follower;
    }

    /**
     * Gets the followed.
     *
     * @return the followed
     */
    public Account getFollowed() {
        return followed;
    }

    /**
     * Sets the followed.
     *
     * @param followed
     *            the new followed
     */
    public void setFollowed(Account followed) {
        this.followed = followed;
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
        result = prime * result
                + ((followed == null) ? 0 : followed.hashCode());
        result = prime * result
                + ((follower == null) ? 0 : follower.hashCode());
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
        Follow other = (Follow) obj;
        if (followed == null) {
            if (other.followed != null) {
                return false;
            }
        } else if (!followed.equals(other.followed)) {
            return false;
        }
        if (follower == null) {
            if (other.follower != null) {
                return false;
            }
        } else if (!follower.equals(other.follower)) {
            return false;
        }
        return true;
    }

}
