package es.udc.fi.dc.fd.block;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.follow.Follow;
import es.udc.fi.dc.fd.follow.FollowRepository;
import es.udc.fi.dc.fd.util.InstanceNotFoundException;


/**
 * The Class BlockRepository.
 */
@Repository
@Transactional
public class BlockRepository {

    /** The entity manager. */
    @PersistenceContext
    private EntityManager entityManager;

    /** The follow repository. */
    @Autowired
    private FollowRepository followRepository;

    /**
     * Instantiates a new block repository.
     */
    public BlockRepository() {

    }

    /**
     * The user blocker block the user blocked, if any of them are following the
     * other, the follow object will be deleted.
     *
     * @param blocker
     *            the blocker
     * @param blocked
     *            the blocked
     * @return the block
     */
    @Transactional(noRollbackFor = { InstanceNotFoundException.class })
    public Block blockUser(Account blocker, Account blocked) {

        Block block = new Block(blocker, blocked);
        entityManager.persist(block);

        // una vez bloqueados,en caso de que se sigan, dejaran de seguirse
        try {
            Follow followMe = followRepository.findUserFollowed(
                    blocked.getId(), blocker.getId());
            followRepository.removeFollowed(followMe);
        } catch (InstanceNotFoundException e) {
            // Si lo que no se ha encontrado no es el objeto follow dejamos
            // correr la excepción
            if (e.getNotFoundClass() != Follow.class) {
                throw e;
            }
        }

        try {
            Follow myFollow = followRepository.findUserFollowed(
                    blocker.getId(), blocked.getId());
            followRepository.removeFollowed(myFollow);
        } catch (InstanceNotFoundException e) {
            // Si lo que no se ha encontrado no es el objeto follow dejamos
            // correr la excepción
            if (e.getNotFoundClass() != Follow.class) {
                throw e;
            }
        }
        return block;
    }

    /**
     * Delete block.
     *
     * @param block
     *            the block
     */
    @Transactional
    public void deleteBlock(Block block) {
        entityManager.remove(entityManager.contains(block) ? block
                : entityManager.merge(block));
    }

    /**
     * Find block by emails.
     *
     * @param blokerMail
     *            the bloker mail
     * @param blokedMail
     *            the bloked mail
     * @return the block
     */
    public Block findBlockByEmails(String blokerMail, String blokedMail) {
        try {
            return entityManager
                    .createNamedQuery(Block.FIND_BLOCK, Block.class)
                    .setParameter("blokerMail", blokerMail)
                    .setParameter("blokedMail", blokedMail).getSingleResult();
        } catch (NoResultException e) {
            throw new InstanceNotFoundException("User whith mail: "
                    + blokedMail + "aren't blocked by the user with ID: "
                    + blokerMail, Block.class, e);
        }
    }

    /**
     * Checks if is blocked by blocker.
     *
     * @param blokedMail
     *            the bloked mail
     * @param blokerMail
     *            the bloker mail
     * @return the boolean
     */
    public Boolean isBlockedByBlocker(String blokedMail, String blokerMail) {
        return !entityManager.createNamedQuery(Block.FIND_BLOCK, Block.class)
                .setParameter("blokerMail", blokerMail)
                .setParameter("blokedMail", blokedMail).getResultList()
                .isEmpty();
    }

    /**
     * Find blocked by me.
     *
     * @param email
     *            the email
     * @return the list
     */
    public List<Account> findBlockedByMe(String email) {
        return entityManager
                .createNamedQuery(Block.FIND_BLOCKED, Account.class)
                .setParameter("email", email).getResultList();
    }

    /**
     * Find who block me.
     *
     * @param myEmail
     *            the my email
     * @return the list
     */
    public List<Account> findWhoBlockMe(String myEmail) {
        return entityManager
                .createNamedQuery(Block.FIND_BLOCKERS, Account.class)
                .setParameter("email", myEmail).getResultList();
    }
}
