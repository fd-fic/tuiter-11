package es.udc.fi.dc.fd.follow;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.util.InstanceNotFoundException;


/**
 * The Class FollowRepository.
 */
@Repository
@Transactional(readOnly = true)
public class FollowRepository {

    /** The entity manager. */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create the default Follow Repository.
     */
    public FollowRepository() {

    }

    /**
     * Follow user.
     *
     * @param follower
     *            the follower
     * @param followed
     *            the followed
     * @return the follow
     */
    @Transactional
    public Follow followUser(Account follower, Account followed) {
        Follow follow = new Follow(follower, followed);
        entityManager.persist(follow);
        return follow;
    }

    /**
     * Find followed by.
     *
     * @param email
     *            the email
     * @return the list
     */
    public List<Account> findFollowedBy(String email) {

        return entityManager
                .createNamedQuery(Follow.FIND_FOLLOWED, Account.class)
                .setParameter("email", email).getResultList();
    }

    /**
     * Find who follow me.
     *
     * @param myEmail
     *            the my email
     * @return the list
     */
    public List<Account> findWhoFollowMe(String myEmail) {
        return entityManager
                .createNamedQuery(Follow.FIND_FOLLOWERS, Account.class)
                .setParameter("email", myEmail).getResultList();

    }

    /**
     * Find user followed.
     *
     * @param idUser
     *            the id user
     * @param userFollowedId
     *            the user followed id
     * @return the follow
     */
    @Transactional(noRollbackFor = { NoResultException.class,
            InstanceNotFoundException.class })
    public Follow findUserFollowed(Long idUser, Long userFollowedId) {
        try {
            Follow a = entityManager
                    .createNamedQuery(Follow.FIND_USER_FOLLOWED, Follow.class)
                    .setParameter("idUser", idUser)
                    .setParameter("userFollowedId", userFollowedId)
                    .getSingleResult();
            return a;
        } catch (NoResultException e) {
            throw new InstanceNotFoundException(
                    "User whith ID: " + idUser
                            + "aren't following to the user with ID: "
                            + userFollowedId, Follow.class, e);
        }
    }

    /**
     * Checks if is followed by follower.
     *
     * @param followedId
     *            the followed id
     * @param followerId
     *            the follower id
     * @return true, if is followed by follower
     */
    public boolean isFollowedByFollower(Long followedId, Long followerId) {
        return !entityManager
                .createNamedQuery(Follow.FIND_USER_FOLLOWED, Follow.class)
                .setParameter("idUser", followerId)
                .setParameter("userFollowedId", followedId).getResultList()
                .isEmpty();
    }

    /**
     * Removes the followed.
     *
     * @param follow
     *            the follow
     */
    @Transactional
    public void removeFollowed(Follow follow) {
        entityManager.remove(entityManager.contains(follow) ? follow
                : entityManager.merge(follow));

    }

}
