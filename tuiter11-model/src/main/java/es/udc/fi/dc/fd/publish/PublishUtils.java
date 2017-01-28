package es.udc.fi.dc.fd.publish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.block.BlockRepository;
import es.udc.fi.dc.fd.follow.FollowRepository;

/**
 * The Class PublishUtils.
 */
@Component
public class PublishUtils {

    /** The block repository. */
    @Autowired
    private BlockRepository blockRepository;

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The follow repository. */
    @Autowired
    private FollowRepository followRepository;

    /**
     * Create the default publish utils.
     */
    public PublishUtils() {

    }

    /**
     * Granted visibility.
     *
     * @param emailVisitor
     *            the email of the user who visits a conversation
     * @param emailProfile
     *            the email of the user who posts the tuit
     * @return true, if the visitor is authorized to see the tuit
     */
    public boolean grantedVisibility(String emailVisitor, String emailProfile) {
        boolean canIsee = true;
        boolean amIblocked = false;

        canIsee = canSeeProfile(emailVisitor, emailProfile);
        amIblocked = blockRepository.isBlockedByBlocker(emailVisitor,
                emailProfile);

        return (canIsee && !amIblocked);
    }

    /**
     * Return true if the user whith visitorEmail can see the ownerEmail
     * profile.
     *
     * @param visitorEmail
     *            the visitor email
     * @param ownerEmail
     *            the owner email
     * @return true, if successful
     */
    public boolean canSeeProfile(String visitorEmail, String ownerEmail) {
        Account myAccount;
        Account accToSee;

        myAccount = accountRepository.findByEmail(visitorEmail);
        accToSee = accountRepository.findByEmail(ownerEmail);

        if (myAccount.equals(accToSee)) {
            return true;
        }

        boolean heFollowMe = followRepository.isFollowedByFollower(
                myAccount.getId(), accToSee.getId());

        return !((accToSee.getPrivateAccount() != null) && (!heFollowMe));
    }
}
